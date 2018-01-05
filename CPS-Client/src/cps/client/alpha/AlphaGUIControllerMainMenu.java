package cps.client.alpha;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AlphaGUIControllerMainMenu implements CPSViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="quitBtn"
  private Button quitBtn; // Value injected by FXMLLoader

  @FXML // fx:id="incidentalParkingBtn"
  private Button incidentalParkingBtn; // Value injected by FXMLLoader

  @FXML // fx:id="viewParkingRequestsBtn"
  private Button viewParkingRequestsBtn; // Value injected by FXMLLoader

  @FXML // fx:id="requestParkingEntryBtn"
  private Button requestParkingEntryBtn; // Value injected by FXMLLoader

  @FXML // fx:id="initParkingLot"
  private Button initParkingLot; // Value injected by FXMLLoader

  @FXML
  void handleQuitBtn(ActionEvent event) {
    // get a handle to the stage
    Stage stage = (Stage) quitBtn.getScene().getWindow();
    // do what you have to do
    stage.close();
    Platform.exit();
    System.exit(0);
  }

  @FXML
  void handleRequestIncidentalParkingBtn(ActionEvent event) {
    Scene scene = ControllersClientAdapter.fetchScene("alpha2");
    CPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }

  @FXML
  void handleViewParkingRequestsBtn(ActionEvent event) {
    Scene scene = ControllersClientAdapter.fetchScene("alpha3");
    CPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene); 
  }

  @FXML
  void handleRequestParkingEntryBtn(ActionEvent event) {
    Scene scene = ControllersClientAdapter.fetchScene("alpha4");
    CPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }

  @FXML
  void handleInitParkingLot(ActionEvent event) {
    Scene scene = ControllersClientAdapter.fetchScene("alpha5");
    CPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert requestParkingEntryBtn != null : "fx:id=\"requestParkingEntryBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert initParkingLot != null : "fx:id=\"initParkingLot\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert viewParkingRequestsBtn != null : "fx:id=\"viewParkingRequestsBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert quitBtn != null : "fx:id=\"quitBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert incidentalParkingBtn != null : "fx:id=\"incidentalParkingBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    ControllersClientAdapter.registerCtrl(this);
  }

  @Override
  public String getCtrlId() {
    return "alphaMain";
  }
}
