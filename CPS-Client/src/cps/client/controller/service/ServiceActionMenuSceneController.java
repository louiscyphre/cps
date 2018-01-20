package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.people.CompanyPerson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

/**
 * Service Main Menu scene controller.
 */
public class ServiceActionMenuSceneController extends ServiceActionControllerBase {

  /**   */
  @FXML // fx:id="jobTitleLabel"
  private Label jobTitleLabel; // Value injected by FXMLLoader

  /**   */
  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  /**   */
  @FXML // fx:id="usernameLabel"
  private Label usernameLabel; // Value injected by FXMLLoader

  /**
   * @param event
   */
  @FXML
  void handleInitializeLotButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT , ACCESS_LEVEL_LOCAL_WORKER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_INIT_LOT, 10);
  }

  /**
   * @param event
   */
  @FXML
  void handleManageLotButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT, (ACCESS_LEVEL_LOCAL_WORKER)
    // ACCESS_LEVEL_GLOBAL_MANAGER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MANAGE_LOT, 10);
  }

  /**
   * @param event
   */
  @FXML
  void handleRefundButton(ActionEvent event) {
    // ACCESS_DOMAIN_CUSTOMER_SERVICE, ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_REFUND, 10);
  }

  /**
   * @param event
   */
  @FXML
  void handleLotIsFullButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT, ACCESS_LEVEL_LOCAL_WORKER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_LOT_IS_FULL, 10);
  }

  /**
   * @param event
   */
  @FXML
  void handleUpdatePricesButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT, ACCESS_LEVEL_LOCAL_MANAGER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_UPDATE_PRICES, 10);
  }

  /**
   * @param event
   */
  @FXML
  void handleStatisticsButton(ActionEvent event) {
    // ACCESS_DOMAIN_STATISTICS,
    // ACCESS_LEVEL_GLOBAL_MANAGER
  }

  /**
   * @param event
   */
  @FXML
  void handleLogoutButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_LOGIN, 10);
    ControllersClientAdapter.getEmployeeContext().logContextOut();
  }

  /**
   * 
   */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert jobTitleLabel != null : "fx:id=\"jobTitleLabel\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    assert usernameLabel != null : "fx:id=\"usernameLabel\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_MENU);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    CompanyPerson companyPerson = ControllersClientAdapter.getEmployeeContext().getCompanyPerson();
    if (companyPerson != null) {
      usernameLabel.setText(companyPerson.getFirstName() + " " + companyPerson.getLastName());
      jobTitleLabel.setText(companyPerson.getJobTitle());
    }
  }
}
