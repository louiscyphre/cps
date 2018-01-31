package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsPerformance extends ServiceStatitisticsBase {

  @FXML
  private TableColumn<?, ?> colMonth;

  @FXML
  private TableColumn<?, ?> colNumberOfDisabledCells;

  @FXML
  private TableView<?> tableView1;

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
