package cps.client.controller.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cps.api.action.UpdatePricesAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.UpdatePricesResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 *  Controller class for Updating Prices.
 */
public class ServiceActionUpdatePricesController extends ServiceActionControllerBaseSubmitAndFinish implements ParkingLotsController {

  @FXML // fx:id="newReservedPriceTextField"
  private TextField newReservedPriceTextField; // Value injected by FXMLLoader

  @FXML // fx:id="newReservedPriceTextField"
  private TextField newIncidentalPriceTextField; // Value injected by FXMLLoader

  @FXML
  private ComboBox<String> parkingLotsList;

  HashMap<String, ParkingLot> parkingLotsMap = null;

  String userLotChoice = null;

  /**
   * @param event
   */
  @FXML
  void handleComboBoxAction(ActionEvent event) {
    if (processing || parkingLotsMap != null) {
      return;
    }
    userLotChoice = parkingLotsList.getValue();
  }

  /**
   * 
   */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert newReservedPriceTextField != null : "fx:id=\"newReservedPriceTextField\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";
    assert newIncidentalPriceTextField != null : "fx:id=\"newIncidentalPriceTextField\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceActionUpdatePrices.fxml'.";

    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_UPDATE_PRICES);

    // Update the price information when the current lot changes
    parkingLotsList.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          refreshPrices();
        });
  }

  /**
   * Helper function. Updates the output message, accordingly.
   */
  private void refreshPrices() {
    ParkingLot lot = parkingLotsMap.get(parkingLotsList.getValue());
    if (lot != null) {
      List<Text> formattedMessage = new LinkedList<Text>();
      formattedMessage.add(new Text(String.format("Incidental price: %s ILS per hour\n", lot.getPrice1())));
      formattedMessage.add(new Text(String.format("Reserved price: %s ILS per hour", lot.getPrice2())));
      displayInfo(formattedMessage);
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
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

  /* (non-Javadoc)
   * @see cps.client.controller.service.ServiceActionControllerBase#validateAndSend()
   */
  @Override
  void validateAndSend() {
    try {
      float newReservedParkingPrice = requireFloat(newReservedPriceTextField, "New reserved parking price");
      float newIncidentalParkingPrice = requireFloat(newIncidentalPriceTextField, "New incidental parking price");

      User user = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson();
      ParkingLot lot = notNull(parkingLotsMap.get(parkingLotsList.getValue()), "Please choose a parking lot");
      UpdatePricesAction action = new UpdatePricesAction(user.getId(), lot.getId(), newIncidentalParkingPrice,
          newReservedParkingPrice);
      ControllersClientAdapter.getClient().sendRequest(action);
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.Collection)
   */
  public void setParkingLots(Collection<ParkingLot> list) {
    List<String> tmp = new ArrayList<String>();
    parkingLotsMap = new HashMap<String, ParkingLot>();
    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address, i);
    }
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  /**
   * @param addresses
   */
  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  /** Handles the response from the server , displays error is request failed.*/
  @Override
  public void handle(ListParkingLotsResponse response) {
    if (response.success()) {
      setParkingLots(response.getData());
    }
    super.handleGenericResponse(response);
  }

  /** Handles the response from the server , displays error is request failed.*/
  @Override
  public void handle(UpdatePricesResponse response) {
    if (response.success()) {
      ParkingLot lot = parkingLotsMap.get(response.getStreetAddress());

      if (lot != null) {
        lot.setPrice1(response.getPrice1());
        lot.setPrice2(response.getPrice2());
      }
    }

    super.handleGenericResponse(response);
  }
}
