/*
 * 
 */
package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.customer.ViewMyReservationsController.TableOnetimeService;
import cps.common.Constants;
import cps.entities.models.MonthlyReport;
import cps.entities.models.ParkingLot;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cps.api.action.GetQuarterlyReportAction;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.client.controller.ReportsController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

// TODO: Auto-generated Javadoc
/** The Class ServiceStaticticsComplaints. */
public class ServiceStaticticsComplaints extends ServiceActionControllerBase implements ParkingLotsController, ReportsController {

  @FXML
  private TableColumn<ReportsTable, String> colQuarter;

  @FXML
  private TableColumn<ReportsTable, String> colYear;

  @FXML
  private TableColumn<ReportsTable, String> colRejected;

  @FXML
  private TableColumn<ReportsTable, String> colOpenedClaims;

  @FXML
  private TableColumn<ReportsTable, String> colRefunded;

  @FXML
  private TableColumn<ReportsTable, String> colLotID;

  @FXML
  private TableView<ReportsTable> tableView;

  /** Start date DatePicker */
  @FXML
  private DatePicker startDatePicker;

  /** End date DatePicker */
  @FXML
  private DatePicker endDatePicker;

  /** List holding the entries */
  private ObservableList<ReportsTable> reportsTableEntriesList;

  /** Parking lots list ComboBox. */
  @FXML // fx:id="parkingLotsList"
  protected ComboBox<String> parkingLotsList; // Value injected by FXMLLoader

  /** Parking lots mapping to their addresses. */
  protected HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  /** Refresh function. Request from the server fresh list of Reservations */
  private void refresh() {
    if (parkingLotsMap.isEmpty()) {
      ListParkingLotsRequest request = new ListParkingLotsRequest();
      // Toggle processing state on
      turnProcessingStateOn();
      ControllersClientAdapter.getClient().sendRequest(request);
    } else {
      validateAndSend();
    }
  }

  /** Handle refresh button.
   * @param event the event */
  @FXML
  void handleRefreshButton(ActionEvent event) {// TODO//FIXME
    if (!processing) {
      refresh();
    }
  }

  /** Handle start date choice.
   * @param event the event */
  @FXML
  void handleStartDateChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    endDatePicker.setDisable(false);
  }

  /** Handle end date choice.
   * @param event the event */
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

  /** Handle lot choice.
   * @param event the event */
  @FXML
  void handleLotChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getEmployeeContext().setChosenLotID(userChosenLotID);
    validateAndSend();
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

  /** @return report start date or null if empty */
  private LocalDate getReportStartDate() {
    return getDate(startDatePicker);
  }

  /** @return report end date or null if empty */
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

  /* (non-Javadoc)
   * @see cps.client.controller.service.ServiceActionControllerBase#handleBackButton(javafx.event.ActionEvent) */
  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
  }

  /** Initialize. */
  @FXML
  void initialize() {
    super.baseInitialize();
    
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    assert colYear != null : "fx:id=\"colYear\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    assert colQuarter != null : "fx:id=\"colQuarter\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    assert colOpenedClaims != null : "fx:id=\"colOpenedClaims\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    assert colRejected != null : "fx:id=\"colRejected\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    assert colRefunded != null : "fx:id=\"colRefunded\" was not injected: check your FXML file 'ServiceStatisticsComplaints.fxml'.";
    
    // Columns cell value factories
    colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
    colQuarter.setCellValueFactory(new PropertyValueFactory<>("Quarter"));
    colOpenedClaims.setCellValueFactory(new PropertyValueFactory<>("Opened claims"));
    colRejected.setCellValueFactory(new PropertyValueFactory<>("Rejected claims"));
    colRefunded.setCellValueFactory(new PropertyValueFactory<>("Refunded claims"));
    
    // binding to list of reservations
    this.reportsTableEntriesList = FXCollections.observableArrayList();
    tableView.setItems(this.reportsTableEntriesList);

    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_COMPLAINTS);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl() */
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

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.QuarterlyReportResponse) */
  @Override
  public ServerResponse handle(QuarterlyReportResponse response) {

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("Reports retrieved successfully."));
      fillReportTable(response.getData());
      turnProcessingStateOff();
      displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("Reports retrieval did not succeeded.\n"));
      formattedMessage.add(new Text(response.getDescription()));
      turnProcessingStateOff();
      displayError(formattedMessage);
    }
    return response;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.ListParkingLotsResponse) */
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

  /* (non-Javadoc)
   * @see cps.client.controller.ReportsController#fillReportTable(java.util.Collection) */
  @Override
  public void fillReportTable(Collection<MonthlyReport> list) {
    List<ReportsTable> newEntriesList = new LinkedList<ReportsTable>();
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    list.forEach(e -> {//TODO//FIXME//Server side code needed
      //ReportsTable toAdd = new ReportsTable((e.getYear()), e.getCoplaintsCount(), e.getComplaintsRejectedCount(), e.getComplaintsRefundedCount());
      //newEntriesList.add(toAdd);
    });
    this.reportsTableEntriesList.setAll(newEntriesList);
  }

  /** Table row entry. */
  public class ReportsTable {
    private final SimpleStringProperty year;
    private final SimpleStringProperty quarter;
    private final SimpleStringProperty openedClaims;
    private final SimpleStringProperty rejectedClaims;
    private final SimpleStringProperty refundedClaims;

    /** Class to hold inner representation of Complaints Reports in Table.
     * @param year the year
     * @param quarter the quarter
     * @param openedClaims the opened claims
     * @param rejectedClaims the rejected claims
     * @param refundedClaims the refunded claims */
    public ReportsTable(Integer year, Integer quarter/* FIXME */, Integer openedClaims, Integer rejectedClaims, Integer refundedClaims) {
      this.year = new SimpleStringProperty(year.toString());
      this.quarter = new SimpleStringProperty(quarter.toString());
      this.openedClaims = new SimpleStringProperty(openedClaims.toString());
      this.rejectedClaims = new SimpleStringProperty(rejectedClaims.toString());
      this.refundedClaims = new SimpleStringProperty(refundedClaims.toString());
    }

    public String getYear() {
      return year.get();
    }

    public String getQuarter() {
      return quarter.get();
    }

    public String getOpenedClaims() {
      return openedClaims.get();
    }

    public String getRejectedClaims() {
      return rejectedClaims.get();
    }

    public String getRefundedClaims() {
      return refundedClaims.get();
    }

    public void setYear(String year) {
      this.year.set(year);
    }

    public void setQuarter(String quarter) {
      this.quarter.set(quarter);
    }

    public void setOpenedClaims(String openedClaims) {
      this.openedClaims.set(openedClaims);
    }

    public void setRejectedClaims(String rejectedClaims) {
      this.rejectedClaims.set(rejectedClaims);
    }

    public void setRefundedClaims(String refundedClaims) {
      this.refundedClaims.set(refundedClaims);
    }
  }
}
