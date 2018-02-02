package cps.client.controller.service;

import cps.api.response.PeriodicReportResponse;
import cps.api.response.ServerResponse;
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
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
  
  @Override
  public ServerResponse handle(PeriodicReportResponse response) {
    // TODO Auto-generated method stub
    response.getData();
    return super.handle(response);
  }
}
