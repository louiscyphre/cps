package cps.client.controller.service;

import cps.client.controller.ClientControllerBase;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class ServiceActionControllerBase extends ClientControllerBase {

  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
    }
  }

  @FXML
  void handleCancelButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
    }
  }

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  @FXML
  void handleOkButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  void validateAndSend() {
    
  }
}
