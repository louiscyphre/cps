/**
 * 
 */
package cps.server.background;

import cps.entities.models.OnetimeService;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.OnetimeParkingController;

/**
 * The Class Reminder.
 *
 * @author Tegra
 */
public class Reminder extends Thread {

  DatabaseController        db       = null;
  OnetimeParkingController  otpc     = null;
  /** The Constant INTERVAL. */
  private static final long INTERVAL = 60000;

  public Reminder(DatabaseController databasecontroller, OnetimeParkingController oneTimeParkingController) {
    db = databasecontroller;
    otpc = oneTimeParkingController;
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
        otpc.warnLateCustomers();

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

}
