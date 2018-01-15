package cps.client.controller.customer;

import cps.client.controller.ClientControllerBase;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CustomerActionControllerBase extends ClientControllerBase {

  @FXML
  void handleBackButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void handleCancelButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU);
  }

}