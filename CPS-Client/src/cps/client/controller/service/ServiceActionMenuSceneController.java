package cps.client.controller.service;

import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.controller.ControllerConstants.SceneCode;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

// TODO implement access-level dependent button style/handling 
public class ServiceActionMenuSceneController implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="infoLabel"
  private Label infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="jobTitleLabel"
  private Label jobTitleLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML // fx:id="usernameLabel"
  private Label usernameLabel; // Value injected by FXMLLoader

  @FXML
  void handleInitializeLotButton(ActionEvent event) {

  }

  @FXML
  void handleDisableSlotButton(ActionEvent event) {

  }

  @FXML
  void handleRefundButton(ActionEvent event) {

  }

  @FXML
  void handleLotIsFullButton(ActionEvent event) {

  }

  @FXML
  void handleReserveSlotButton(ActionEvent event) {

  }

  @FXML
  void handleUpdatePricesButton(ActionEvent event) {

  }

  @FXML
  void handleLotStateButton(ActionEvent event) {

  }

  @FXML
  void handleLogoutButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    assert jobTitleLabel != null : "fx:id=\"jobTitleLabel\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    assert usernameLabel != null : "fx:id=\"usernameLabel\" was not injected: check your FXML file 'ServiceActionMenuScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_MENU);
    
  }
}
