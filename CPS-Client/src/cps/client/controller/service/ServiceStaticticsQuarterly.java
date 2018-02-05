package cps.client.controller.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cps.api.action.GetQuarterlyReportAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.client.controller.ReportsController;
import cps.common.Constants;
import cps.entities.models.MonthlyReport;
import cps.entities.models.ParkingLot;
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

// TODO represent date as 1 column
// TODO "TOTAL" row is the last int the table and passed with fieldText = "Total"

public class ServiceStaticticsQuarterly extends ServiceStatitisticsBase implements ParkingLotsController, ReportsController {

  @FXML
  private TableColumn<TableQuarterlyEntry, String> complaintsColOpenedClaims;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> complaintsColClosedClaims;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> complaintsColRefundedClaims;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> complaintsColRejectedClaims;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> complaintsColDate;

  @FXML
  private TableView<TableQuarterlyEntry> complaintsTableView;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> disabledCellsColNumberOfDisabledCells;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> disabledCellsColDate;

  @FXML
  private TableView<TableQuarterlyEntry> disabledCellsTableView;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> ordersColReservedParkings;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> ordersColIncidentalParkings;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> ordersColRegularSubscriptions;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> ordersColFullSubscriptions;

  @FXML
  private TableColumn<TableQuarterlyEntry, String> ordersColDate;

  @FXML
  private TableView<TableQuarterlyEntry> ordersTableView;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private ComboBox<String> parkingLotsList;

  private HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  /** List holding the entries */
  private ObservableList<TableQuarterlyEntry> obsEntriesList;

  
  @FXML
  void handleClearButton(ActionEvent event) {
    this.cleanCtrl();
  }

  /** Handle refresh button.
   * @param event the event */
  @FXML
  void handleRefreshButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  /** Handle start date choice.
   * @param event
   *        the event */
  @FXML
  void handleStartDateChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    endDatePicker.setDisable(false);
    validateAndSend();
  }

  /** Handle end date choice.
   * @param event
   *        the event */
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
    validateAndSend();
  }

  /** Handle lot choice.
   * @param event
   *        the event */
  @FXML
  void handleLotChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getEmployeeContext().setChosenLotID(userChosenLotID);
    validateAndSend();
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert complaintsColDate != null : "fx:id=\"complaintsCol\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColOpenedClaims != null : "fx:id=\"complaintsColOpenedClaims\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColRefundedClaims != null : "fx:id=\"complaintsColRefundedClaims\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColRejectedClaims != null : "fx:id=\"complaintsColRejectedClaims\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsTableView != null : "fx:id=\"complaintsTableView\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColNumberOfDisabledCells != null : "fx:id=\"disabledCellsColNumberOfDisabledCells\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColDate != null : "fx:id=\"disabledCellsColDate\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsTableView != null : "fx:id=\"disabledCellsTableView\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColFullSubscriptions != null : "fx:id=\"ordersColFullSubscriptions\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColRegularSubscriptions != null : "fx:id=\"ordersColRegularSubscriptions\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColIncidentalParkings != null : "fx:id=\"ordersColIncidentalParkings\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColReservedParkings != null : "fx:id=\"ordersColReservedParkings\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColDate != null : "fx:id=\"ordersColDate\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersTableView != null : "fx:id=\"ordersTableView\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";

    // binding to list of monthly reports
    obsEntriesList = FXCollections.observableArrayList();
    complaintsTableView.setItems(this.obsEntriesList);
    disabledCellsTableView.setItems(this.obsEntriesList);
    ordersTableView.setItems(this.obsEntriesList);

    // use set/get value property value factories
    complaintsColOpenedClaims.setCellValueFactory(new PropertyValueFactory<>("openedClaims"));
    complaintsColClosedClaims.setCellValueFactory(new PropertyValueFactory<>("closedClaims"));
    complaintsColRefundedClaims.setCellValueFactory(new PropertyValueFactory<>("refunded"));
    complaintsColRejectedClaims.setCellValueFactory(new PropertyValueFactory<>("rejectedClaims"));
    complaintsColDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    disabledCellsColNumberOfDisabledCells.setCellValueFactory(new PropertyValueFactory<>("numberOfDisabledCells"));
    disabledCellsColDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    ordersColReservedParkings.setCellValueFactory(new PropertyValueFactory<>("reservedParkings"));
    ordersColIncidentalParkings.setCellValueFactory(new PropertyValueFactory<>("incidentalParkings"));
    ordersColRegularSubscriptions.setCellValueFactory(new PropertyValueFactory<>("regularSubscriptions"));
    ordersColFullSubscriptions.setCellValueFactory(new PropertyValueFactory<>("fullSubscriptions"));
    ordersColDate.setCellValueFactory(new PropertyValueFactory<>("date"));

    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_QUARTERLY);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    endDatePicker.setDisable(true);
    parkingLotsList.setDisable(true);
    parkingLotsMap.clear();
    obsEntriesList.clear();
  }

  /** Validates that the fields and Sends API request to the server. */
  @Override
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
    if (reportStartDate.compareTo(reportEndDate) >= 0) {
      displayError("End date must be greater than start date");
      return;
    }
    int lotID = ControllersClientAdapter.getEmployeeContext().getChosenLotID();
    int userID = ControllersClientAdapter.getEmployeeContext().getCompanyPerson().getId();
    GetQuarterlyReportAction request = new GetQuarterlyReportAction(userID, Constants.REPORT_TYPE_QUARTERLY, reportStartDate, reportEndDate, lotID);
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

  @Override
  public void handle(ListParkingLotsResponse response) {
    if (response.success()) {
      setParkingLots(response.getData());
      displayInfo("Parking lots list retrieved successfully");
    } else {
      displayError("Can't retrieve parking lots");
    }
    turnProcessingStateOff();
  }

  @Override
  public void handle(QuarterlyReportResponse response) {
    if (response.success()) {
      fillReportTable(response.getData());
      displayInfo("Reports retrieved successfully");
    } else {
      displayError("Can't retrieve quarterly report");
    }
    turnProcessingStateOff();
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

  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  @Override
  public void fillReportTable(Collection<MonthlyReport> list) {
    List<TableQuarterlyEntry> newEntriesList = new LinkedList<TableQuarterlyEntry>();

    // All besides total
    Iterator<MonthlyReport> itr = list.iterator();
    for (int i = 0; i < list.size() - 1; i++) {
      MonthlyReport monthlyReport = itr.next();

      int year = monthlyReport.getYear();
      boolean total = false;
      int month = monthlyReport.getMonth();
      int reservedParkings = monthlyReport.getOrdReserved();
      int incidentalParkings = monthlyReport.getOrdIncidental();
      int regularSubscriptions = monthlyReport.getOrdRegular();
      int fullSubscriptions = monthlyReport.getOrdFull();
      int openedClaims = monthlyReport.getComplaintsCount();
      int closedClaims = monthlyReport.getComplaintsClosedCount();
      int refunded = monthlyReport.getComplaintsRefundedCount();
      int rejectedClaims = closedClaims - refunded;
      int numberOfDisabledCells = monthlyReport.getDisabledSlots();

      TableQuarterlyEntry toAdd = new TableQuarterlyEntry(year, total, month, reservedParkings, incidentalParkings, regularSubscriptions, fullSubscriptions,
          openedClaims, closedClaims, rejectedClaims, refunded, numberOfDisabledCells);
      newEntriesList.add(toAdd);
    }

    // Total
    MonthlyReport last = itr.next();

    newEntriesList.add(new TableQuarterlyEntry(last.getYear(), true, last.getMonth(), last.getOrdReserved(), last.getOrdIncidental(), last.getOrdRegular(),
        last.getOrdFull(), last.getComplaintsCount(), last.getComplaintsClosedCount(), last.getComplaintsRefundedCount(),
        last.getComplaintsClosedCount() - last.getComplaintsRefundedCount(), last.getDisabledSlots()));

    obsEntriesList.setAll(newEntriesList);
  }

  public class TableQuarterlyEntry {

    private SimpleStringProperty date;
    private SimpleStringProperty reservedParkings;
    private SimpleStringProperty incidentalParkings;
    private SimpleStringProperty regularSubscriptions;
    private SimpleStringProperty fullSubscriptions;
    private SimpleStringProperty openedClaims;
    private SimpleStringProperty closedClaims;
    private SimpleStringProperty rejectedClaims;
    private SimpleStringProperty refunded;
    private SimpleStringProperty numberOfDisabledCells;

    public TableQuarterlyEntry(int year, boolean total, int month, int reservedParkings, int incidentalParkings, int regularSubscriptions,
        int fullSubscriptions, int openedClaims, int closedClaims, int rejectedClaims, int refunded, int numberOfDisabledCells) {
      super();
      if (!total) {
        LocalDate dateLocalDate = LocalDate.of(year, month, 1);
        this.date = new SimpleStringProperty(dateLocalDate.toString());
      } else {
        this.date = new SimpleStringProperty("TOTAL");
      }
      this.reservedParkings = new SimpleStringProperty(Integer.toString(reservedParkings));
      this.incidentalParkings = new SimpleStringProperty(Integer.toString(incidentalParkings));
      this.regularSubscriptions = new SimpleStringProperty(Integer.toString(regularSubscriptions));
      this.fullSubscriptions = new SimpleStringProperty(Integer.toString(fullSubscriptions));
      this.openedClaims = new SimpleStringProperty(Integer.toString(openedClaims));
      this.closedClaims = new SimpleStringProperty(Integer.toString(closedClaims));
      this.rejectedClaims = new SimpleStringProperty(Integer.toString(rejectedClaims));
      this.refunded = new SimpleStringProperty(Integer.toString(refunded));
      this.numberOfDisabledCells = new SimpleStringProperty(Integer.toString(numberOfDisabledCells));
    }

    public String getDate() {
      return this.date.get();
    }

    public void setDate(SimpleStringProperty date) {
      this.date = date;
    }

    public String getReservedParkings() {
      return reservedParkings.get();
    }

    public void setReservedParkings(SimpleStringProperty reservedParkings) {
      this.reservedParkings = reservedParkings;
    }

    public String getIncidentalParkings() {
      return incidentalParkings.get();
    }

    public void setIncidentalParkings(SimpleStringProperty incidentalParkings) {
      this.incidentalParkings = incidentalParkings;
    }

    public String getRegularSubscriptions() {
      return regularSubscriptions.get();
    }

    public void setRegularSubscriptions(SimpleStringProperty regularSubscriptions) {
      this.regularSubscriptions = regularSubscriptions;
    }

    public String getFullSubscriptions() {
      return fullSubscriptions.get();
    }

    public void setFullSubscriptions(SimpleStringProperty fullSubscriptions) {
      this.fullSubscriptions = fullSubscriptions;
    }

    public String getOpenedClaims() {
      return openedClaims.get();
    }

    public void setOpenedClaims(SimpleStringProperty openedClaims) {
      this.openedClaims = openedClaims;
    }

    public String getClosedClaims() {
      return closedClaims.get();
    }

    public void setClosedClaims(SimpleStringProperty closedClaims) {
      this.closedClaims = closedClaims;
    }

    public String getRejectedClaims() {
      return rejectedClaims.get();
    }

    public void setRejectedClaims(SimpleStringProperty rejectedClaims) {
      this.rejectedClaims = rejectedClaims;
    }

    public String getRefunded() {
      return refunded.get();
    }

    public void setRefunded(SimpleStringProperty refunded) {
      this.refunded = refunded;
    }

    public String getNumberOfDisabledCells() {
      return numberOfDisabledCells.get();
    }

    public void setNumberOfDisabledCells(SimpleStringProperty numberOfDisabledCells) {
      this.numberOfDisabledCells = numberOfDisabledCells;
    }
  }

  @FXML
  public void addDummyData(ActionEvent e) {

    fillReportTable(new ArrayList<MonthlyReport>() {
      private static final long serialVersionUID = 1L;
      {
        add(new MonthlyReport(2018, 1, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 2, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 3, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 4, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 5, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 6, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 7, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
        add(new MonthlyReport(2018, 8, 1, 10, 10, 10, 10, 10, 5, 3, 5, ""));
      }
    });
  }
}
