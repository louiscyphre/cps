package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsDisabledParkingCells extends ServiceActionControllerBase {

  @FXML
  private TableColumn<?, ?> colMonth;

  @FXML
  private TableColumn<?, ?> colNumberOfDisabledCells;

  @FXML
  private TableView<?> tableView1;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE, 10);
  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_DISABLED_PARKING_CELLS);
  }
  
  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
}
