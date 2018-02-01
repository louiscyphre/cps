package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStatisticsOrders extends ServiceActionControllerBase {

  @FXML
  private TableColumn<?, ?> colNumberOfOrders;

  @FXML
  private TableColumn<?, ?> colParkingType;

  @FXML
  private TableColumn<?, ?> colNumberOfOrders21;

  @FXML
  private TableColumn<?, ?> colNumberOfOrders1;

  @FXML
  private TableColumn<?, ?> colNumberOfOrders2;

  @FXML
  private TableView<?> tableView1;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_ORDERS);
  }
  
  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
  
  
}
