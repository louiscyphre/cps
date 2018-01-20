package cps.client.controller.customer;

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
public class CustomerActionControllerBaseSubmitAndFinish extends CustomerActionControllerBase {

  /**
   * Sumbit button.
   */
  @FXML
  protected Button submitButton;

  /**
   * Finish button.
   */
  @FXML
  protected Button finishButton;

  /**
   * @param event
   */
  @FXML
  void handleFinishButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU, 10);
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
    finishButton.setStyle("-fx-background-color: #5CB85C");
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // toggle Submit instead of Finish
    setFinishInsteadOfSubmit(false);
  }
}
