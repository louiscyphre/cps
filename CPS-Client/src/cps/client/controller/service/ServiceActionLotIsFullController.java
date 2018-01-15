package cps.client.controller.service;

import cps.api.response.ServerResponse;
import cps.api.response.SetFullLotResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

//TODO stub - requires verification regarding how the lot is full works on server-side
public class ServiceActionLotIsFullController extends ServiceActionControllerBase {
  @FXML // fx:id="lotId"
  private TextField lotId; // Value injected by FXMLLoader

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert lotId != null : "fx:id=\"lotId\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_LOT_IS_FULL);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    lotId.clear();
  }

  @Override
  void validateAndSend() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    // TODO Auto-generated method stub
    return super.handle(response);
  }
}
