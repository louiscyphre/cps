package cps.client.controller.service;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Class with simple submit and finish mechanics.
 * Prior to successful submission  the default and visible button is Submit.
 * After successful submission the default and visible button is Finish.
 */
public class ServiceActionControllerBaseSubmitAndFinish extends ServiceActionControllerBase {
  /** Submit button  */
  @FXML
  protected Button submitButton;

  /** Finish button  */
  @FXML
  protected Button finishButton;

  /**
   * Returns the user to main menu.
   * @param event
   */
  @FXML
  void handleFinishButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.SERVICE_ACTION_MENU, 10);
  }

  /**
   * Toggle function, to turn one Submit button to Finish.
   * @param value
   */
  protected void setFinishInsteadOfSubmit(boolean value) {
    submitButton.setDefaultButton(!value);
    finishButton.setDefaultButton(value);
    submitButton.setVisible(!value);
    finishButton.setVisible(value);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    setFinishInsteadOfSubmit(false);
  }
}
