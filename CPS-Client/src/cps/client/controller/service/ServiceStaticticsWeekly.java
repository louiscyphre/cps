package cps.client.controller.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import cps.api.action.GetWeeklyReportAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ServerResponse;
import cps.api.response.WeeklyReportResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.common.Constants;
import cps.common.Utilities;
import cps.entities.models.ParkingLot;
import cps.entities.models.WeeklyStatistics;
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

public class ServiceStaticticsWeekly extends ServiceStatitisticsBase implements ParkingLotsController {

  @FXML
  private TableColumn<TableWeeklyEntry, String> distColCancelled;

  @FXML
  private TableColumn<TableWeeklyEntry, String> distColLateArrivals;

  @FXML
  private TableColumn<TableWeeklyEntry, String> distColRealized;

  @FXML
  private TableColumn<TableWeeklyEntry, String> meanColCancelled;

  @FXML
  private TableColumn<TableWeeklyEntry, String> meanColLateArrivals;

  @FXML
  private TableColumn<TableWeeklyEntry, String> meanColRealized;

  @FXML
  private TableColumn<TableWeeklyEntry, String> medianColCancelled;

  @FXML
  private TableColumn<TableWeeklyEntry, String> medianColLateArrivals;

  @FXML
  private TableColumn<TableWeeklyEntry, String> medianColRealized;

  @FXML
  private ComboBox<String> parkingLotsList;

  private HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  @FXML
  private TableView<TableWeeklyEntry> tableViewMedian;

  @FXML
  private TableView<TableWeeklyEntry> tableViewDist;

  @FXML
  private TableView<TableWeeklyEntry> tableViewMean;

  @FXML
  private DatePicker weekDatePicker;

  private ObservableList<TableWeeklyEntry> obsEntriesList;

  @Override
  public void cleanCtrl() {
    weekDatePicker.setValue(null);
    parkingLotsList.setDisable(true);
    parkingLotsMap.clear();
    obsEntriesList.clear();
  }

  @Override
  public ServerResponse handle(WeeklyReportResponse response) {
    if (response.success()) {
      turnProcessingStateOff();
      fillTable(response.getData());

    } else {
      turnProcessingStateOff();
      displayError("Could not retrieve weekly report");
    }
    return super.handle(response);
  }

  private void fillTable(WeeklyStatistics data) {

    float realizedOrdersMean = data.getRealizedOrdersMean();
    float canceledOrdersMean = data.getCanceledOrdersMean();
    float lateArrivalsMean = data.getLateArrivalsMean();
    float realizedOrdersMedian = data.getRealizedOrdersMedian();
    float canceledOrdersMedian = data.getCanceledOrdersMedian();
    float lateArrivalsMedian = data.getLateArrivalsMedian();
    String realizedOrdersDist = data.getRealizedOrdersDist();
    String canceledOrdersDist = data.getCanceledOrdersDist();
    String lateArrivalsDist = data.getLateArrivalsDist();
    TableWeeklyEntry entry = new TableWeeklyEntry(realizedOrdersMean, canceledOrdersMean, lateArrivalsMean,
        realizedOrdersMedian, canceledOrdersMedian, lateArrivalsMedian, realizedOrdersDist, canceledOrdersDist,
        lateArrivalsDist);
    obsEntriesList.setAll(entry);
  }

  @FXML
  void handleLotChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getEmployeeContext().setChosenLotID(userChosenLotID);
    validateAndSend();
  }

  @Override
  void validateAndSend() {
    // validation in same order as order in the form
    int userID = ControllersClientAdapter.getEmployeeContext().getCompanyPerson().getId();
    LocalDate periodStart = Utilities.findWeekStart(weekDatePicker.getValue());
    if (periodStart == null) {
      displayError("Start date invalid");
      return;
    }
    LocalDate periodEnd = periodStart.plusWeeks(1);
    int reportType = Constants.REPORT_TYPE_WEEKLY;
    int lotID = ControllersClientAdapter.getEmployeeContext().getChosenLotID();
    GetWeeklyReportAction request = new GetWeeklyReportAction(userID, reportType, periodStart, periodEnd, lotID);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  @FXML
  void handleWeekDateChoice(ActionEvent event) {
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

  /*
   * (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.
   * Collection)
   */
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

  @FXML
  void initialize() {
    super.baseInitialize();

    // binding to list of monthly reports
    obsEntriesList = FXCollections.observableArrayList();
    this.obsEntriesList = FXCollections.observableArrayList();

    tableViewMedian.setItems(this.obsEntriesList);
    ;
    tableViewDist.setItems(this.obsEntriesList);
    ;
    tableViewMean.setItems(this.obsEntriesList);
    ;

    // use set/get value property value factories
    meanColRealized.setCellValueFactory(new PropertyValueFactory<>("realizedOrdersMean"));
    meanColCancelled.setCellValueFactory(new PropertyValueFactory<>("canceledOrdersMean"));
    meanColLateArrivals.setCellValueFactory(new PropertyValueFactory<>("lateArrivalsMean"));
    medianColRealized.setCellValueFactory(new PropertyValueFactory<>("realizedOrdersMedian"));
    medianColCancelled.setCellValueFactory(new PropertyValueFactory<>("canceledOrdersMedian"));
    medianColLateArrivals.setCellValueFactory(new PropertyValueFactory<>("lateArrivalsMedian"));
    distColCancelled.setCellValueFactory(new PropertyValueFactory<>("realizedOrdersDist"));
    distColRealized.setCellValueFactory(new PropertyValueFactory<>("canceledOrdersDist"));
    distColLateArrivals.setCellValueFactory(new PropertyValueFactory<>("lateArrivalsDist"));

    meanColRealized.prefWidthProperty().bind(medianColRealized.widthProperty());
    medianColRealized.prefWidthProperty().bind(distColRealized.widthProperty());
    distColRealized.prefWidthProperty().bind(meanColRealized.widthProperty());
    
    meanColCancelled.prefWidthProperty().bind(medianColCancelled.widthProperty());
    medianColCancelled.prefWidthProperty().bind(distColCancelled.widthProperty());
    distColCancelled.prefWidthProperty().bind(meanColCancelled.widthProperty());
    
    meanColLateArrivals.prefWidthProperty().bind(medianColLateArrivals.widthProperty());
    medianColLateArrivals.prefWidthProperty().bind(distColLateArrivals.widthProperty());
    distColLateArrivals.prefWidthProperty().bind(meanColLateArrivals.widthProperty());
    
    
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_WEEKLY);
  }

  public class TableWeeklyEntry {

    private SimpleStringProperty realizedOrdersMean;
    private SimpleStringProperty canceledOrdersMean;
    private SimpleStringProperty lateArrivalsMean;
    private SimpleStringProperty realizedOrdersMedian;
    private SimpleStringProperty canceledOrdersMedian;
    private SimpleStringProperty lateArrivalsMedian;
    private SimpleStringProperty realizedOrdersDist;
    private SimpleStringProperty canceledOrdersDist;
    private SimpleStringProperty lateArrivalsDist;

    public TableWeeklyEntry(float realizedOrdersMean, float canceledOrdersMean, float lateArrivalsMean,
        float realizedOrdersMedian, float canceledOrdersMedian, float lateArrivalsMedian, String realizedOrdersDist,
        String canceledOrdersDist, String lateArrivalsDist) {
      this.realizedOrdersMean = new SimpleStringProperty(Float.toString(realizedOrdersMean));
      this.canceledOrdersMean = new SimpleStringProperty(Float.toString(canceledOrdersMean));
      this.lateArrivalsMean = new SimpleStringProperty(Float.toString(lateArrivalsMean));
      this.realizedOrdersMedian = new SimpleStringProperty(Float.toString(realizedOrdersMedian));
      this.canceledOrdersMedian = new SimpleStringProperty(Float.toString(canceledOrdersMedian));
      this.lateArrivalsMedian = new SimpleStringProperty(Float.toString(lateArrivalsMedian));
      this.realizedOrdersDist = new SimpleStringProperty(realizedOrdersDist);
      this.canceledOrdersDist = new SimpleStringProperty(canceledOrdersDist);
      this.lateArrivalsDist = new SimpleStringProperty(lateArrivalsDist);
    }

    public String getRealizedOrdersMean() {
      return realizedOrdersMean.get();
    }

    public void setRealizedOrdersMean(SimpleStringProperty realizedOrdersMean) {
      this.realizedOrdersMean = realizedOrdersMean;
    }

    public String getCanceledOrdersMean() {
      return canceledOrdersMean.get();
    }

    public void setCanceledOrdersMean(SimpleStringProperty canceledOrdersMean) {
      this.canceledOrdersMean = canceledOrdersMean;
    }

    public String getLateArrivalsMean() {
      return lateArrivalsMean.get();
    }

    public void setLateArrivalsMean(SimpleStringProperty lateArrivalsMean) {
      this.lateArrivalsMean = lateArrivalsMean;
    }

    public String getRealizedOrdersMedian() {
      return realizedOrdersMedian.get();
    }

    public void setRealizedOrdersMedian(SimpleStringProperty realizedOrdersMedian) {
      this.realizedOrdersMedian = realizedOrdersMedian;
    }

    public String getCanceledOrdersMedian() {
      return canceledOrdersMedian.get();
    }

    public void setCanceledOrdersMedian(SimpleStringProperty canceledOrdersMedian) {
      this.canceledOrdersMedian = canceledOrdersMedian;
    }

    public String getLateArrivalsMedian() {
      return lateArrivalsMedian.get();
    }

    public void setLateArrivalsMedian(SimpleStringProperty lateArrivalsMedian) {
      this.lateArrivalsMedian = lateArrivalsMedian;
    }

    public String getRealizedOrdersDist() {
      return realizedOrdersDist.get();
    }

    public void setRealizedOrdersDist(SimpleStringProperty realizedOrdersDist) {
      this.realizedOrdersDist = realizedOrdersDist;
    }

    public String getCanceledOrdersDist() {
      return canceledOrdersDist.get();
    }

    public void setCanceledOrdersDist(SimpleStringProperty canceledOrdersDist) {
      this.canceledOrdersDist = canceledOrdersDist;
    }

    public String getLateArrivalsDist() {
      return lateArrivalsDist.get();
    }

    public void setLateArrivalsDist(SimpleStringProperty lateArrivalsDist) {
      this.lateArrivalsDist = lateArrivalsDist;
    }
  }

}
