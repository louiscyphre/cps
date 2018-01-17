package cps.client.controller.service;

import cps.api.action.ReserveParkingSlotsAction;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class ServiceActionReserveSlotController extends ServiceActionLotStateController {

  @FXML
  private Button reserveSlotButton;

  @FXML
  void handleReserveSlot(ActionEvent event) {
    if (!processing) {
      if (selectedCar != null && !reserveSlotButton.isDisabled()) {
        User user;
        try {
          user = requireLoggedInUser();
          ParkingLot lot = parkingLotsMap.get(parkingLotsList.getValue());
          turnProcessingStateOn();
          sendRequest(new ReserveParkingSlotsAction(user.getId(), lot.getId(), selectedCell.width, selectedCell.height,
              selectedCell.depth));
        } catch (Exception e) {
          displayError(e.getMessage());
        }
      }
    }
  }

  protected void registerCtrl() {
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_RESERVE_SLOT);
  }

  @FXML
  void initialize() {
    super.initialize();
    assert reserveSlotButton != null : "fx:id=\"disableSlotButton\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
  }

  @Override
  protected void onMouseClickedHandler(Rectangle currentRectangle) {
    if(processing)
      return;
    super.onMouseClickedHandler(currentRectangle);
    if (selectedCar != null) {
      reserveSlotButton.setDisable(false);
    } else {
      reserveSlotButton.setDisable(true);
    }
  }

  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    super.handleGenericResponse(response);
    if(response.success()) {
      validateAndSend();
    }
    return null;
  }
}
