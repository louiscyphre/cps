package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceStaticticsClaimsStatus extends ServiceActionControllerBase {

  @FXML
  private TableColumn<?, ?> colQuarter;

  @FXML
  private TableColumn<?, ?> colYear;

  @FXML
  private TableColumn<?, ?> colRejected;

  @FXML
  private TableColumn<?, ?> colOpenedClaims;

  @FXML
  private TableColumn<?, ?> colRefunded;

  @FXML
  private TableColumn<?, ?> colLotID;

  @FXML
  private TableView<?> tableView1;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE, 10);
  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_CLAIMS_STATUS);
  }

  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub
    super.cleanCtrl();
  }
  
  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    // TODO Auto-generated method stub
    return super.handle(response);
  }
}
