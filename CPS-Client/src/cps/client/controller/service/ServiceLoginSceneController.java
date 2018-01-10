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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ServiceLoginSceneController implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="usernameTF"
  private TextField usernameTF; // Value injected by FXMLLoader

  @FXML // fx:id="infoLabel"
  private Label infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="passwordTF"
  private TextField passwordTF; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML
  void handleCancelButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MAIN_MENU);
  }

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert usernameTF != null : "fx:id=\"usernameTF\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    assert passwordTF != null : "fx:id=\"passwordTF\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceLoginScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_LOGIN);

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
