/**
 * 
 */
package cps.server.background;

import java.sql.Connection;
import java.util.Collection;

import cps.entities.models.OnetimeService;
import cps.server.ServerConfig;
import cps.server.ServerException;
import cps.server.database.DatabaseController;

import static cps.common.Utilities.debugPrint;
import static cps.common.Utilities.debugPrintln;

/** The Class Reminder.
 * @author Tegra */
public class Reminder extends Thread {
  DatabaseController db = null;

  /** Number of milliseconds to wait until the next check. */
  private static final long INTERVAL = 60000; // Once in 1 minute
  // private static final long INTERVAL = 6000; // Once in 6 seconds

  public Reminder(ServerConfig config) throws Exception {
    db = new DatabaseController(config);
  }

  /* (non-Javadoc)
   * @see java.lang.Thread#run() */
  @Override
  public void run() {

    try {
      while (true) {
        Thread.sleep(INTERVAL);
        // TODO check one time services where customer has to park within 30
        // minutes

        /* This function will send messages to users that are late by at least
         * one second */
        warnLateCustomers();

        /* This function will find users that are late more than 30 minutes and
         * has not been authorized to do that and will cancel their one time
         * order */
        // otpc.deleteTooLate();

      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ServerException e) {
      e.printStackTrace();
    }
  }

  /** Find and warn customers who reserved a parking and did not show up yet.
   * @throws ServerException */
  private void warnLateCustomers() throws ServerException {
    db.performAction(conn -> {
      debugPrint("Searching late customers ");
      Collection<OnetimeService> items = OnetimeService.findLateUnwarnedCustomers(conn);
      
      for (OnetimeService entry : items) {
        sendWarning(conn, entry);
        entry.setWarned(true);
        entry.update(conn); // We assume that update always works, if it doesn't, it throws an SQL exception
      }
      
      debugPrintln("...warned %s customers", items.size());
    });
  } 

  private void sendWarning(Connection conn, OnetimeService late) {
    // TODO optional - send an email to email address
    /* I've tried to set up mailing, but unfortunately, Gmail does not allow non
     * registered applications to use it's smtp anymore. So screw it */
    System.out.printf("\nSending message to customer %d, to email %s.\n We are waiting for your car #%s, at %d, at %s\n", late.getCustomerID(), late.getEmail(), late.getCarID(), late.getLotID(),
        late.getPlannedStartTime());
  }
}