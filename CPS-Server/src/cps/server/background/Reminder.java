/**
 * 
 */
package cps.server.background;

import java.util.Collection;

import cps.entities.models.OnetimeService;
import cps.entities.models.WarningMessage;
import cps.server.ServerConfig;
import cps.server.ServerException;
import cps.server.controllers.DatabaseController;
import static cps.common.Utilities.debugPrint;
import static cps.common.Utilities.debugPrintln;

/**
 * The Class Reminder.
 *
 * @author Tegra
 */
public class Reminder extends Thread {
  DatabaseController        db       = null;
  
  /** Number of milliseconds to wait until the next check. */
  private static final long INTERVAL = 60000; // Once in 1 minute
  // private static final long INTERVAL = 6000; // Once in 6 seconds

  public Reminder(ServerConfig config) throws Exception {
    db = new DatabaseController(config);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Thread#run()
   */
  @Override
  public void run() {

    try {
      while (true) {
        Thread.sleep(INTERVAL);
        // TODO check one time services where customer has to park within 30
        // minutes

        /*
         * This function will send messages to users that are late by at least
         * one second
         */
        warnLateCustomers();

        /*
         * This function will find users that are late more than 30 minutes and
         * has not been authorized to do that and will cancel their one time
         * order
         */
        // otpc.deleteTooLate();

      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ServerException e) {
      e.printStackTrace();
    }
  }

  /**
   * Find and warn customers who reserved a parking and did not show up yet.
   * @throws ServerException
   */
  private void warnLateCustomers() throws ServerException {
    db.performAction(conn -> {
      debugPrint("Searching late customers ");
      Collection<WarningMessage> messages = OnetimeService.findLateUnwarnedCustomers(conn);
      for (WarningMessage mess : messages) {
        mess.warn(conn);
        if (!mess.setsend(conn)) {
          System.out.println(String.format("Failed to update database for customer %d, car %s, entry time %s",
              mess.getCustomerId(), mess.getCarid(), mess.getPlannedStartTime().toString()));
        }
      }
      debugPrintln("...warned %s customers", messages.size());
    });
  }
}
