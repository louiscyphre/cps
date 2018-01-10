/**
 * 
 */
package cps.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created on: 2018-01-09 1:04:02 AM 
 */
public class SubscriptionsMenuController implements ViewController {

  @FXML
  private VBox infoBox;

  @FXML
  private Label infoLabel;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void initialize() {
      assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
      assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";

      ControllersClientAdapter.registerCtrl(this,ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  @Override
  public void displayInfo(String infoMsg) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void displayError(String errorMsg) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void turnProcessingStateOn() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void turnProcessingStateOff() {
    // TODO Auto-generated method stub
    
  }

}
