package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;

// TODO stub - need to implement the dynamic grid creation and the view initialization
public class ServiceActionLotStateController extends ServiceActionControllerBase {
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_LOT_STATE);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
  }
}
