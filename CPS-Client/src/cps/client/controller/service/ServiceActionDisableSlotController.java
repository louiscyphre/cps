package cps.client.controller.service;

import static cps.common.Utilities.between;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cps.api.action.DisableParkingSlotsAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.common.Constants;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

// TODO stub - need to implement the dynamic grid creation and the view initialization
public class ServiceActionDisableSlotController extends ServiceActionControllerBase implements ParkingLotsController {
  @FXML // fx:id="floorJTF"
  private TextField floorJTF; // Value injected by FXMLLoader

  @FXML // fx:id="rowKTF"
  private TextField rowKTF; // Value injected by FXMLLoader
  
  @FXML // fx:id="columnITF"
  private TextField columnITF; // Value injected by FXMLLoader

  @FXML
  private ComboBox<String> parkingLotsList;

  HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  @FXML
  // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    super.baseInitialize();
    assert columnITF != null : "fx:id=\"columnITF\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
    assert floorJTF != null : "fx:id=\"floorJTF\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
    assert rowKTF != null : "fx:id=\"rowKTF\" was not injected: check your FXML file 'ServiceActionDisableSlot.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_DISABLE_SLOT);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    columnITF.clear();
    floorJTF.clear();
    rowKTF.clear();

    // Get the list of parking lots
    turnProcessingStateOn();
    sendRequest(new ListParkingLotsRequest());
  }

  @Override
  void validateAndSend() {
    try {
      User user = requireLoggedInUser();
      
      ParkingLot lot = parkingLotsMap.get(parkingLotsList.getValue());
      errorIfNull(lot, "Please choose a parking lot");
      
      int j = requireInteger(floorJTF, "Floor");
      int k = requireInteger(rowKTF, "Row");
      int i = requireInteger(columnITF, "Column");

      errorIf(!between(j, 0, Constants.LOT_HEIGHT - 1), String.format("Floor must be in range [0, %s] (inclusive)", Constants.LOT_HEIGHT - 1));
      errorIf(!between(k, 0, Constants.LOT_DEPTH - 1), String.format("Row must be in range [0, %s] (inclusive)", Constants.LOT_HEIGHT - 1));
      errorIf(!between(i, 0, lot.getWidth() - 1), String.format("Column must be in range [0, %s] (inclusive)", lot.getWidth() - 1));
      
      sendRequest(new DisableParkingSlotsAction(user.getId(), lot.getId(), i, j, k));
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  private void setParkingLots(Collection<ParkingLot> list) {
    List<String> tmp = new ArrayList<String>();
    // TODO based on an assumption that street addresses are unique

    parkingLotsMap.clear();
    
    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address, i);
    }

    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
//    showAlert(response.getDescription());
    return super.handleGenericResponse(response);
  }

  @Override
  public ServerResponse handleParkingLots(ListParkingLotsResponse response) {
    setParkingLots(response.getData());
    turnProcessingStateOff();
    return response;
  }
}
