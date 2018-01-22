package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.common.Constants;
import cps.entities.models.ParkingLot;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cps.api.action.GetQuarterlyReportAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.ServerResponse;
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
  private DatePicker startDatePicker;
  
  /** End date DatePicker */
  @FXML
  private DatePicker endDatePicker;

  /** Parking lots list ComboBox */
  @FXML // fx:id="parkingLotsList"
  protected ComboBox<String> parkingLotsList; // Value injected by FXMLLoader

  /** Parking lots mapping to their addresses */
  protected HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  /** @param event */
  @FXML
  void handleStartDateChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    endDatePicker.setDisable(false);
  }

  /** @param event */
  @FXML
  void handleEndDateChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    parkingLotsList.setDisable(false);
    
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
    LocalDate reportStartDate = getReportStartDate();
    if (reportStartDate == null) {
      displayError("Start date invalid");
      return;
    }
    LocalDate reportEndDate = getReportEndDate();
    if (reportEndDate == null) {
      displayError("End date invalid");
      return;
    }
    if (reportStartDate.compareTo(reportEndDate) > 0) {
      displayError("Start date must be today, or future date");
      return;
    }
    int lotID = ControllersClientAdapter.getEmployeeContext().getChosenLotID();
    int userID = ControllersClientAdapter.getEmployeeContext().getCompanyPerson().getId();
    GetQuarterlyReportAction request = new GetQuarterlyReportAction(userID, Constants.REPORT_TYPE_QUARTERLY, reportEndDate, reportEndDate, lotID);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  /**
   * @return report start date or null if empty
   */
  private LocalDate getReportStartDate() {
    return getDate(startDatePicker);
  }
  
  /**
   * @return report end date or null if empty
   */
  private LocalDate getReportEndDate() {
    return getDate(endDatePicker);
  }
  
  private LocalDate getDate(DatePicker picker) {
    if (picker.getValue() == null) {
      return null;
    }
    try {
      return picker.getValue();
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
  }

  @Override
  public ServerResponse handle(QuarterlyReportResponse response) {
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
