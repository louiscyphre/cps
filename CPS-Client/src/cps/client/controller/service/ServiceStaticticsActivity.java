package cps.client.controller.service;

import cps.api.response.PeriodicReportResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsActivity extends ServiceStatitisticsBase {

  @FXML
  private TableColumn<?, ?> colWeeklyCancelled;

  @FXML
  private TableColumn<?, ?> colWeeklyProcessed;

  @FXML
  private TableView<?> tableView1;

  @FXML
  private TableColumn<?, ?> colWeeklyDayOfTheWeek;

  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_ACTIVITY);
  }
  
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
  }
  
  @Override
  public void handle(PeriodicReportResponse response) {
    super.handle(response);
  }
}
