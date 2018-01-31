package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.api.response.PeriodicReportResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsMonthly extends ServiceActionControllerBase {

  @FXML
  private TableColumn<?, ?> colWeeklyCancelled;

  @FXML
  private TableColumn<?, ?> colWeeklyProcessed;

  @FXML
  private TableView<?> tableView1;

  @FXML
  private TableColumn<?, ?> colWeeklyDayOfTheWeek;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_MONTHLY);
  }
  
  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
  
  @Override
  public ServerResponse handle(PeriodicReportResponse response) {
    // TODO Auto-generated method stub
    return super.handle(response);
  }
}
