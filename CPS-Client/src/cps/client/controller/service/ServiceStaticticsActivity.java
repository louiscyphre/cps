package cps.client.controller.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

import cps.api.action.GetPeriodicReportAction;
import cps.api.response.PeriodicReportResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.InternalClientException;
import cps.entities.models.PeriodicReport;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/** Controller class for Service Activity scene. */
public class ServiceStaticticsActivity extends ServiceStatitisticsBase {

  @FXML
  private TableColumn<TableActivityEntry, String> colCanceledOrders;

  @FXML
  private TableColumn<TableActivityEntry, String> colDisabledParkingHours;

  @FXML
  private TableColumn<TableActivityEntry, String> colIdentifier;

  @FXML
  private TableColumn<TableActivityEntry, String> colRealizedOrders;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private DatePicker endDatePicker;

  /** List holding the entries */
  private ObservableList<TableActivityEntry> obsEntriesList;

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

  @FXML
  private TableView<TableActivityEntry> tableView;

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    startDatePicker.setValue(null);
    endDatePicker.setValue(null);
    endDatePicker.setDisable(true);
    obsEntriesList.clear();
  }

  @FXML
  void handleStartDateChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    endDatePicker.setDisable(false);
  }

  @FXML
  void handleEndDateChoice(ActionEvent event) {

    if (processing) {
      return;
    }
    validateAndSend();
  }

  @FXML
  void initialize() {
    super.baseInitialize();

    // binding to list of monthly reports
    obsEntriesList = FXCollections.observableArrayList();
    tableView.setItems(this.obsEntriesList);

    // use set/get value property value factories
    colCanceledOrders.setCellValueFactory(new PropertyValueFactory<>("canceledOrders"));
    colDisabledParkingHours.setCellValueFactory(new PropertyValueFactory<>("disabledParkingHours"));
    colRealizedOrders.setCellValueFactory(new PropertyValueFactory<>("realizedOrders"));
    colIdentifier.setCellValueFactory(new PropertyValueFactory<>("rowName"));

    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_ACTIVITY);
  }
  
  /** Handles the response from the server containing the data with periodic report
   * and populates inner data structures accordingly. */
  @Override
  public void handle(PeriodicReportResponse response) {
    if (response.success()) {
      fillReportTable(response.getData());
      turnProcessingStateOff();
      displayInfo("Reports retrieved successfully");
    } else {
      turnProcessingStateOff();
      displayError("Can't retrieve quarterly report");
    }
  }

  private void fillReportTable(PeriodicReport data) {
    int numRows = data.getNumRows();

    ArrayList<TableActivityEntry> list = new ArrayList<TableActivityEntry>();

    double realizedOrders[] = data.getData("realizedOrders").getValues();
    double disabledParkingHours[] = data.getData("disabledParkingHours").getValues();
    double canceledOrders[] = data.getData("canceledOrders").getValues();
    String rowNames[] = data.getRowNames();

    for (int i = 0; i < numRows; i++) {
      TableActivityEntry entry = new TableActivityEntry(realizedOrders[i], canceledOrders[i], disabledParkingHours[i], rowNames[i]);
      list.add(entry);
    }

    // Total
    list.add(new TableActivityEntry(data.getData("realizedOrders").getTotal(), data.getData("canceledOrders").getTotal(),
        data.getData("disabledParkingHours").getTotal(), "Total"));

    // Average
    list.add(new TableActivityEntry(data.getData("realizedOrders").getMean(), data.getData("canceledOrders").getMean(),
        data.getData("disabledParkingHours").getMean(), "Average"));

    // Median
    list.add(new TableActivityEntry(data.getData("realizedOrders").getMedian(), data.getData("canceledOrders").getMedian(),
        data.getData("disabledParkingHours").getMedian(), "Median"));

    obsEntriesList.setAll(list);
  }

  @Override
  void validateAndSend() {
    try {
      int userID = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson().getId();
      LocalDate periodStart = getReportStartDate();
      LocalDate periodEnd = getReportEndDate();
      GetPeriodicReportAction request = new GetPeriodicReportAction(userID, periodStart, periodEnd);
      ControllersClientAdapter.getClient().sendRequest(request);
      turnProcessingStateOn();
    } catch (InternalClientException e) {
      displayError(e.getMessage());
      ;
    }
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

  /**
   * Class representing the table entry for Activity View.
   */
  public class TableActivityEntry {

    private SimpleStringProperty canceledOrders;
    private SimpleStringProperty disabledParkingHours;
    private SimpleStringProperty realizedOrders;
    private SimpleStringProperty rowName;

    public TableActivityEntry(double realizedOrders, double canceledOrders, double disabledParkingHours, String rowName) {
      super();
      this.realizedOrders = new SimpleStringProperty(Double.toString(realizedOrders));
      this.canceledOrders = new SimpleStringProperty(Double.toString(canceledOrders));
      this.disabledParkingHours = new SimpleStringProperty(Double.toString(disabledParkingHours));
      this.rowName = new SimpleStringProperty(rowName);
    }

    public String getCanceledOrders() {
      return canceledOrders.get();
    }

    public String getDisabledParkingHours() {
      return disabledParkingHours.get();
    }

    public String getRealizedOrders() {
      return realizedOrders.get();
    }

    public String getRowName() {
      return rowName.get();
    }

    public void setCanceledOrders(SimpleStringProperty canceledOrders) {
      this.canceledOrders = canceledOrders;
    }

    public void setDisabledParkingHours(SimpleStringProperty disabledParkingHours) {
      this.disabledParkingHours = disabledParkingHours;
    }

    public void setRealizedOrders(SimpleStringProperty realizedOrders) {
      this.realizedOrders = realizedOrders;
    }

    public void setRowName(SimpleStringProperty rowName) {
      this.rowName = rowName;
    }
  }
}
