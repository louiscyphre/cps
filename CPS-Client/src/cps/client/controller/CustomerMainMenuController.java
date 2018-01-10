/**
 * 
 */
package cps.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

/**
 * Created on: 2018-01-08 11:17:39 PM
 */
/**
 * Created on: 2018-01-08 11:41:25 PM 
 */
public class CustomerMainMenuController implements ViewController {

  @FXML
  private ResourceBundle resources;

  @FXML
  private VBox infoBox;

  @FXML
  private Label infoLabel;


  @FXML
  void handleLoginButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.LOGIN);
  }

  @FXML
  void handleReserveParkingButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.RESERVE_PARKING);
  }

  @FXML
  void handleBuySubscriptionButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  @FXML
  void handleParkNowButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.INCIDENTAL_PARKING);
  }

  @FXML
  void initialize() {
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file" + ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU;
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file" + ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU;
    
    ControllersClientAdapter.registerCtrl(this,ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }

  // TODO @Michael check this idea out
  @Override
  public void displayInfo(String infoMsg) {
    infoLabel.setText(infoMsg);
    infoLabel.getStyleClass().clear();
    infoLabel.getStyleClass().add("infoLabel");
  }

  @Override
  public void displayError(String errorMsg) {
    infoLabel.setText(errorMsg);
    infoLabel.getStyleClass().clear();
    infoLabel.getStyleClass().add("errorLabel");    
  }

  @Override
  public void turnProcessingStateOn() {
    throw new UnsupportedOperationException(); // TODO check if better throw exception or leave stub
  }

  @Override
  public void turnProcessingStateOff() {
    throw new UnsupportedOperationException(); // TODO check if better throw exception or leave stub
  }

}
