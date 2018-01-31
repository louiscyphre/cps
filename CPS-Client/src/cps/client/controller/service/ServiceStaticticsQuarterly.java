package cps.client.controller.service;

import java.util.ArrayList;

import cps.api.response.QuarterlyReportResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.models.MonthlyReport;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

// TODO extract controller for each tab to a separate class

public class ServiceStaticticsQuarterly extends ServiceStatitisticsBase {

  @FXML
  private TableColumn<?, ?> complaintsColLotID;

  @FXML
  private TableColumn<?, ?> complaintsColOpenedClaims;

  @FXML
  private TableColumn<?, ?> complaintsColQuarter;

  @FXML
  private TableColumn<?, ?> complaintsColRefundedClaims;

  @FXML
  private TableColumn<?, ?> complaintsColRejectedClaims;

  @FXML
  private TableColumn<?, ?> complaintsColYear;

  @FXML
  private TableView<?> complaintsTableView;

  @FXML
  private TableColumn<?, ?> disabledCellsColLotID;

  @FXML
  private TableColumn<?, ?> disabledCellsColMonth;

  @FXML
  private TableColumn<?, ?> disabledCellsColNumberOfDisabledCells;

  @FXML
  private TableColumn<?, ?> disabledCellsColQuarter;

  @FXML
  private TableColumn<?, ?> disabledCellsColYear;

  @FXML
  private TableView<?> disabledCellsTableView;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private TableColumn<?, ?> ordersColLotID;

  @FXML
  private TableColumn<?, ?> ordersColQuarter;

  @FXML
  private TableColumn<?, ?> ordersColWeeklyCancelled;

  @FXML
  private TableColumn<?, ?> ordersColWeeklyDayOfTheWeek;

  @FXML
  private TableColumn<?, ?> ordersColWeeklyProcessed;

  @FXML
  private TableColumn<?, ?> ordersColYear;

  @FXML
  private TableView<?> ordersTableView;

  @FXML
  private ComboBox<?> parkingLotsList;

  @FXML
  private DatePicker startDatePicker;

  private ArrayList<MonthlyReport> monthlyReporstList;

  @FXML
  void handleBackButton(ActionEvent event) {

  }

  @FXML
  void handleEndDateChoice(ActionEvent event) {

  }

  @FXML
  void handleLotChoice(ActionEvent event) {

  }

  @FXML
  void handleStartDateChoice(ActionEvent event) {

  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert complaintsColLotID != null : "fx:id=\"complaintsColLotID\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColOpenedClaims != null : "fx:id=\"complaintsColOpenedClaims\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColQuarter != null : "fx:id=\"complaintsColQuarter\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColRefundedClaims != null : "fx:id=\"complaintsColRefundedClaims\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColRejectedClaims != null : "fx:id=\"complaintsColRejectedClaims\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsColYear != null : "fx:id=\"complaintsColYear\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert complaintsTableView != null : "fx:id=\"complaintsTableView\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColLotID != null : "fx:id=\"disabledCellsColLotID\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColMonth != null : "fx:id=\"disabledCellsColMonth\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColNumberOfDisabledCells != null : "fx:id=\"disabledCellsColNumberOfDisabledCells\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColQuarter != null : "fx:id=\"disabledCellsColQuarter\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsColYear != null : "fx:id=\"disabledCellsColYear\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert disabledCellsTableView != null : "fx:id=\"disabledCellsTableView\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColLotID != null : "fx:id=\"ordersColLotID\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColQuarter != null : "fx:id=\"ordersColQuarter\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColWeeklyCancelled != null : "fx:id=\"ordersColWeeklyCancelled\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColWeeklyDayOfTheWeek != null : "fx:id=\"ordersColWeeklyDayOfTheWeek\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColWeeklyProcessed != null : "fx:id=\"ordersColWeeklyProcessed\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersColYear != null : "fx:id=\"ordersColYear\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert ordersTableView != null : "fx:id=\"ordersTableView\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ServiceStaticticsQuarterly.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_QUARTERLY);
  }

  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }

  @Override
  public ServerResponse handle(QuarterlyReportResponse response) {
    
    monthlyReporstList = new ArrayList<MonthlyReport>(response.getData());
    
    
    
    return super.handle(response);
  }

  public class TableQuarterlyBase {
    private SimpleStringProperty year;
    private SimpleStringProperty quarter;
    private SimpleStringProperty lotId;
  }

  public class TableQuarterlyOrders extends TableQuarterlyBase {
    private SimpleStringProperty dayOfWeek;
    private SimpleStringProperty processedOrders;
    private SimpleStringProperty cancelledOrders;

    public TableQuarterlyOrders() {
      super(year, quarter, lotId);
      this.dayOfWeek = new SimpleStringProperty(dayOfWeek);
      this.processedOrders = new SimpleStringProperty(processedOrders);
      this.cancelledOrders = new SimpleStringProperty(cancelledOrders);
    }
  }

}
