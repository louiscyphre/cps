/**
 * 
 */
package cps.server.background;

import cps.entities.models.OnetimeService;
import cps.server.controllers.DatabaseController;

/**
 * The Class Reminder.
 *
 * @author Tegra
 */
public class Reminder extends Thread {
  
  
  DatabaseController        db       = null;
  
  /** The Constant INTERVAL. */
  private static final long INTERVAL = 60000;

  public Reminder(DatabaseController databasecontroller) {
    db = databasecontroller;
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

      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
