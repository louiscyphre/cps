package cps.client.controller.service;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ServiceActionControllerBaseSubmitAndFinish extends ServiceActionControllerBase{
  @FXML
  protected Button submitButton;

  @FXML
  protected Button finishButton;
  
  @FXML
  void handleFinishButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.SERVICE_ACTION_MENU, 10);
  }
  
  protected void setFinishInsteadOfSubmit(boolean value) {
    submitButton.setDefaultButton(!value);
    finishButton.setDefaultButton(value);
    submitButton.setVisible(!value);
    finishButton.setVisible(value);
  }  
  
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    setFinishInsteadOfSubmit(false);
  }
}
