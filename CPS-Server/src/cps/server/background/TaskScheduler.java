/**
 * 
 */
package cps.server.background;

import static cps.common.Utilities.debugPrint;
import static cps.common.Utilities.debugPrintln;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.entities.models.WeeklyStatistics;
import cps.server.ServerConfig;
import cps.server.ServerException;
import cps.server.TimeProvider;
import cps.server.database.DatabaseController;

/** Runs background tasks that need to be performed periodically.
 * 1. Ask users who are late to their reserved parking whether they are still interested in the reservation.
 * 2. Remind users who purchased a monthly subscription, that there is 1 week left before their subscription runs out.
 * 3. Generate weekly statistics from daily statistical records. */
public class TaskScheduler extends Thread {
  DatabaseController         db = null;
  private HashSet<LocalDate> checkedWeeks = new HashSet<>();
  private TimeProvider       clock;

  /** Number of milliseconds to wait until the next check. */
  private static final long INTERVAL = 60000; // Once in 1 minute
  // private static final long INTERVAL = 6000; // Once in 6 seconds

  public TaskScheduler(ServerConfig config, TimeProvider clock) throws Exception {
    db = new DatabaseController(config);
    this.clock = clock;
  }

  /* (non-Javadoc)
   * @see java.lang.Thread#run() */
  @Override
  public void run() {

    try {
      while (true) {
        Thread.sleep(INTERVAL);
        runTasks();
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public void runTasks() {
    try {
      /* This function will send messages to users that are late by at least
       * one second */
      warnLateCustomers();

      /* This function will find users that are late more than 30 minutes and
       * have not been authorized to do that and will cancel their one time
       * order */
      cancelLateReservations();

      /* This function will find users that purchased a monthly subscription
       * and have 1 week left until it expires,
       * and will notify them to renew their subscription. */
      warnSubscriptionOwners();

      /* This function will generate weekly reports */
      generateLastWeekReport();
    } catch (ServerException e) {
      e.printStackTrace();
    }    
  }
  
  private void generateLastWeekReport() throws ServerException {
    LocalDate today = clock.now().toLocalDate();
    if (today.getDayOfWeek() == DayOfWeek.SUNDAY && !checkedWeeks.contains(today)) {
      generateWeeklyReport(today.minusDays(7));
      checkedWeeks.add(today);
    }    
  }

  /** Generate last week weekly report for each parking lot and populate it into the database.
   * @param weekStart the date when the week started
   * @throws ServerException the server exception */
  private void generateWeeklyReport(LocalDate weekStart) throws ServerException {
    db.performAction(conn -> {
      debugPrint("Updating last week weekly report ");
      LinkedList<ParkingLot> lots = (LinkedList<ParkingLot>) ParkingLot.findAll(conn);
      for (ParkingLot lot : lots) {
        WeeklyStatistics.createUpdateWeeklyReport(conn, weekStart, lot.getId());
      }

      debugPrintln("...updated weekly reports for %s lots", lots.size());
    });
  }

  /** Find and warn customers who reserved a parking and did not show up yet.
   * @throws ServerException on error */
  private void warnLateCustomers() throws ServerException {
    db.performAction(conn -> {
      debugPrint("Searching late customers ");
      Collection<OnetimeService> items = OnetimeService.findLateCustomers(conn, clock.now(), Duration.ofMinutes(0), false);

      for (OnetimeService entry : items) {
        sendWarning(conn, entry);
        entry.setWarned(true);
        entry.update(conn); // We assume that update always works, if it
                            // doesn't, it throws an SQL exception
      }

      debugPrintln("...warned %s customers", items.size());
    });
  }

  /** Find users that are late more than 30 minutes and cancel their reservation
   * if they did not confirm that they are still interested.
   * @throws ServerException on error */
  private void cancelLateReservations() throws ServerException {
    db.performAction(conn -> {
      debugPrint("Searching customers who forfeited their reservation");
      Collection<OnetimeService> items = OnetimeService.findLateCustomers(conn, clock.now(), Duration.ofMinutes(30), true);

      for (OnetimeService entry : items) {
        entry.setCanceled(true);
        entry.update(conn); // We assume that update always works, if it
                            // doesn't, it throws an SQL exception
      }

      debugPrintln("...canceled %s reservations", items.size());
    });

  }

  private void sendWarning(Connection conn, OnetimeService late) {
    // TODO optional - send an email to email address
    // Email should be sent from this function when the client provides an SMTP server
    System.out.printf("\nSending message to customer %d, to email %s.\n We are waiting for your car %s, at lot %s, at %s\n", late.getCustomerID(),
        late.getEmail(), late.getCarID(), late.getLotID(), late.getPlannedStartTime());
  }

  private void warnSubscriptionOwners() throws ServerException {
    db.performAction(conn -> {
      debugPrint("Searching customers who have 1 week left on their subscription");
      Collection<SubscriptionService> items = SubscriptionService.findExpiringAfter(conn, clock.now(), Duration.ofDays(7), true);

      for (SubscriptionService entry : items) {
        entry.setWarned(true);
        entry.update(conn); // We assume that update always works, if it
                            // doesn't, it throws an SQL exception
      }

      // Note: the same customer can be warned multiple times if they own more than one subscription
      debugPrintln("...sent %s subscription expiration warnings", items.size());
    });

  }
}