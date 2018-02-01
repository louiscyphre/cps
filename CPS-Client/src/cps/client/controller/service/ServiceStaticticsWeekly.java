package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.api.response.ServerResponse;
import cps.api.response.WeeklyReportResponse;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsWeekly extends ServiceActionControllerBase {

  @FXML
  private TableColumn<?, ?> colFri;

  @FXML
  private TableColumn<?, ?> colWed;

  @FXML
  private TableView<?> tableView1;

  @FXML
  private TableColumn<?, ?> colSun;

  @FXML
  private TableColumn<?, ?> colMon;

  @FXML
  private TableColumn<?, ?> colMedian;

  @FXML
  private TableColumn<?, ?> colSat;

  @FXML
  private TableColumn<?, ?> colAverage;

  @FXML
  private TableColumn<?, ?> colThu;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_WEEKLY);
  }
  
  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
  
  @Override
  public ServerResponse handle(WeeklyReportResponse response) {
    // TODO Auto-generated method stub
    return super.handle(response);
  }
}
