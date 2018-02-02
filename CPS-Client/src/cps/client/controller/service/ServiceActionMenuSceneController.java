package cps.client.controller.service;

import cps.api.action.ServiceLogoutAction;
import cps.api.response.RefundResponse;
import cps.api.response.ServerResponse;
import cps.api.response.SimpleResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;
import cps.entities.people.LocalEmployee;
import cps.entities.people.User;
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
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_INIT_LOT);
  }

  /**
   * @param event
   */
  @FXML
  void handleManageLotButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT, (ACCESS_LEVEL_LOCAL_WORKER)
    // ACCESS_LEVEL_GLOBAL_MANAGER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MANAGE_LOT);
  }

  /**
   * @param event
   */
  @FXML
  void handleRefundButton(ActionEvent event) {
    // ACCESS_DOMAIN_CUSTOMER_SERVICE, ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_REFUND);
  }

  /**
   * @param event
   */
  @FXML
  void handleLotIsFullButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT, ACCESS_LEVEL_LOCAL_WORKER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_LOT_IS_FULL);
  }

  /**
   * @param event
   */
  @FXML
  void handleUpdatePricesButton(ActionEvent event) {
    // ACCESS_DOMAIN_PARKING_LOT, ACCESS_LEVEL_LOCAL_MANAGER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_UPDATE_PRICES);
  }

  /**
   * @param event
   */
  @FXML
  void handleStatisticsButton(ActionEvent event) {
    // ACCESS_DOMAIN_STATISTICS,
    // ACCESS_LEVEL_GLOBAL_MANAGER
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
  }

  /**
   * @param event
   */
  @FXML
  void handleLogoutButton(ActionEvent event) {    
    // Send logout request
    try {
      User user = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson();
      sendRequest(new ServiceLogoutAction(user.getId()), false);
      ControllersClientAdapter.getEmployeeContext().logContextOut();
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_LOGIN);
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }
  
  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.SimpleResponse)
   */ 
  @Override
  public ServerResponse handle(SimpleResponse response) {
    // TODO change to ServiceLogoutResponse
    super.handleGenericResponse(response);
    cleanCtrl();
    return null;
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

      if (companyPerson instanceof LocalEmployee) {
        jobTitleLabel.setText(String.format("%s - lotID: %s", companyPerson.getJobTitle(), companyPerson.getDepartmentID()));
      } else {
        jobTitleLabel.setText(companyPerson.getJobTitle());
      }
    } else { // companyPerson == null
      usernameLabel.setText("Not logged in");
      jobTitleLabel.setText("");
    }
  }
}
