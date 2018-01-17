package cps.client.controller.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cps.api.action.UpdatePricesAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.ServerResponse;
import cps.api.response.UpdatePricesResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ServiceActionUpdatePricesController extends ServiceActionControllerBase implements ParkingLotsController {

  @FXML // fx:id="newReservedPriceTextField"
  private TextField newReservedPriceTextField; // Value injected by FXMLLoader

  @FXML // fx:id="newReservedPriceTextField"
  private TextField newIncidentalPriceTextField; // Value injected by FXMLLoader
  
  @FXML
  private ComboBox<String> parkingLotsList;

  HashMap<String, ParkingLot> parkingLotsMap = null;
  
  String userLotChoice = null;
  
  @FXML
  void handleComboBoxAction(ActionEvent event) {
    if (processing || parkingLotsMap != null) {
      return;
    }
    userLotChoice = parkingLotsList.getValue();
  }
  
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert newReservedPriceTextField != null : "fx:id=\"newReservedPriceTextField\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";
    assert newIncidentalPriceTextField != null : "fx:id=\"newIncidentalPriceTextField\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";

    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_UPDATE_PRICES);
  }

  
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    newReservedPriceTextField.clear();
    newIncidentalPriceTextField.clear();

    if (parkingLotsMap != null) {
      return;
    }
    ListParkingLotsRequest request = new ListParkingLotsRequest();
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  @Override
  void validateAndSend() {
    try {
      float newReservedParkingPrice = requireFloat(newReservedPriceTextField, "New reserved parking price");
      float newIncidentalParkingPrice = requireFloat(newIncidentalPriceTextField, "New incidental parking price");
  
      User user = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson();
      int lotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
      UpdatePricesAction action = new UpdatePricesAction(user.getId(), lotID, newReservedParkingPrice, newIncidentalParkingPrice);
      ControllersClientAdapter.getClient().sendRequest(action);
    } catch (Exception e) {
      displayError(e.getMessage());
    }  
  }
  
  /**
   * @param list
   */
  public void setParkingLots(Collection <ParkingLot> list) {
    List<String> tmp = new ArrayList<String> ();
    parkingLotsMap = new HashMap<String, ParkingLot>();
    for (ParkingLot i: list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address,  i);
    }
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }
  
  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    if (response.success()) {
      setParkingLots(response.getData());
    }
    return super.handleGenericResponse(response); 
  }
  
  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    super.handleGenericResponse(response); 
    if (response.success()) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
    return response;
  }
}
