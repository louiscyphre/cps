package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

public class ServiceStaticticsPerformance extends ServiceStatitisticsBase {

  @FXML
  private TableColumn<?, ?> colRejected;

  @FXML
  private ComboBox<?> parkingLotsList;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private TableColumn<?, ?> colRefunded;

  @FXML
  private VBox infoBox;

  @FXML
  private TableColumn<?, ?> colOpened;

  @FXML
  private TableView<?> tableView1;

  @FXML
  private TableColumn<?, ?> colQuarter;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private TableColumn<?, ?> colYear;

  @FXML
  private TableColumn<?, ?> colLotID;

  @FXML
  void handleStartDateChoice(ActionEvent event) {

  }

  @FXML
  void handleEndDateChoice(ActionEvent event) {

  }

  @FXML
  void handleLotChoice(ActionEvent event) {

  }

  @FXML
  void handleBackButton(ActionEvent event) {

  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_PERFORMANCE);
  }
  
  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
}
