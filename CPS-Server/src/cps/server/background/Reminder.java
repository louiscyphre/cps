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
        otpc.warnLateCustomers();

      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ServerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
