package cps.client.controller.service;

import cps.api.action.ParkingCellSetDisabledAction;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class ServiceActionDisableSlotController extends ServiceActionLotStateController {

  @FXML
  private Button disableSlotButton;

  @FXML
  void handleDisableSlot(ActionEvent event) {
    if (!processing) {
      if (selectedCar != null && !disableSlotButton.isDisabled()) {
        User user;
        try {
          user = requireLoggedInUser();
          ParkingLot lot = parkingLotsMap.get(parkingLotsList.getValue());
          turnProcessingStateOn();
          sendRequest(new ParkingCellSetDisabledAction(user.getId(), lot.getId(), selectedCell.width,
              selectedCell.height, selectedCell.depth, (!selectedCell.isDisabled())));
        } catch (Exception e) {
          displayError(e.getMessage());
        }
      }
    }
  }

  protected void registerCtrl() {
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_DISABLE_SLOT);
  }

  @FXML
  void initialize() {
    super.initialize();
    assert disableSlotButton != null : "fx:id=\"disableSlotButton\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
  }

  @Override
  protected void onMouseClickedHandler(Rectangle currentRectangle) {
    if (processing)
      return;
    super.onMouseClickedHandler(currentRectangle);
    if (selectedCar != null) {
      disableSlotButton.setDisable(false);
    } else {
      disableSlotButton.setDisable(true);
    }
  }

  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    super.handleGenericResponse(response);
    if (response.success()) {
      validateAndSend();
      disableSlotButton.setDisable(true);
    }
    return null;
  }
}
