package cps.client.controller.service;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.controller.ControllerConstants.SceneCode;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

//TODO stub - need to implement the initialization request and all
public class ServiceActionInitLotSceneController implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="streetAddressTF"
  private TextField streetAddressTF; // Value injected by FXMLLoader

  @FXML // fx:id="lotSizeTF"
  private TextField lotSizeTF; // Value injected by FXMLLoader

  @FXML // fx:id="infoLabel"
  private Label infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="incidentalTariffTF"
  private TextField incidentalTariffTF; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML // fx:id="reservedTariffTF"
  private TextField reservedTariffTF; // Value injected by FXMLLoader

  @FXML // fx:id="robotIpTF"
  private TextField robotIpTF; // Value injected by FXMLLoader

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
  }

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert streetAddressTF != null : "fx:id=\"streetAddressTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert lotSizeTF != null : "fx:id=\"lotSizeTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert incidentalTariffTF != null : "fx:id=\"incidentalTariffTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert reservedTariffTF != null : "fx:id=\"reservedTariffTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    assert robotIpTF != null : "fx:id=\"robotIpTF\" was not injected: check your FXML file 'ServiceActionInitLotScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_INIT_LOT);
  }

  @Override
  public void displayInfo(List<Text> formattedText) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
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

  @Override
  public void displayInfo(String simpleInfoMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(String simpleErrorMsg) {
    // TODO Auto-generated method stub

  }
}
