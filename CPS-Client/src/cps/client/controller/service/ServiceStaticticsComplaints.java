package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.utils.FormatValidation.InputFormats;
import cps.entities.models.ParkingLot;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cps.api.action.ListComplaintsAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsComplaints extends ServiceActionControllerBase implements ParkingLotsController {

  @FXML
  private TableColumn<?, ?> colQuarter;

  @FXML
  private TableColumn<?, ?> colYear;

  @FXML
  private TableColumn<?, ?> colRejected;

  @FXML
  private TableColumn<?, ?> colOpenedClaims;

  @FXML
  private TableColumn<?, ?> colRefunded;

  @FXML
  private TableColumn<?, ?> colLotID;

  @FXML
  private TableView<?> tableView1;

  /** Start date DatePicker */
  @FXML
  private DatePicker datePicker;

  /** Parking lots list ComboBox */
  @FXML // fx:id="parkingLotsList"
  protected ComboBox<String> parkingLotsList; // Value injected by FXMLLoader

  /** Parking lots mapping to their addresses */
  protected HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  /** @param event */
  @FXML
  void handleDateChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    if (!parkingLotsList.getItems().isEmpty()) {
      parkingLotsList.getItems().clear();
      parkingLotsMap.clear();
    }
    // Get the list of parking lots
    turnProcessingStateOn();
    sendRequest(new ListParkingLotsRequest());
  }

  /** @param event */
  @FXML
  void handleLotChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getEmployeeContext().setChosenLotID(userChosenLotID);
  }

  /** Validates that the fields and Sends API request to the server. */
  void validateAndSend() {
    // validation in same order as order in the form

    int lotID = ControllersClientAdapter.getEmployeeContext().getChosenLotID();
    LocalDate wantedDate = getWantedDate();

    ListComplaintsAction request = new ListComplaintsAction();// FIXME//TODO Constructor
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  /** @return planned start date or null if empty */
  private LocalDate getWantedDate() {
    if (datePicker.getValue() == null) {
      return null;
    }
    try {
      return datePicker.getValue();
    } catch (DateTimeException e) {
      return null;
    }
  }

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE, 10);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";

    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_COMPLAINTS);

  }

  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
    if (parkingLotsList.getItems() != null) {
      parkingLotsList.getItems().clear();
    }
    parkingLotsList.setDisable(true);
    parkingLotsMap.clear();
    // Get the list of parking lots
    turnProcessingStateOn();
    sendRequest(new ListParkingLotsRequest());
  }

  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    // TODO Auto-generated method stub
    return super.handle(response);
  }

  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    setParkingLots(response.getData());
    turnProcessingStateOff();
    return response;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.
   * Collection) */
  @Override
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

  /** @param addresses */
  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }
}
