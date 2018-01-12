package cps.client.controller.alpha;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AlphaGUIControllerMainMenu implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML
  private Label infoLabel; // test button

  private boolean infoToggled = true;

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
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ALPHA_INCIDENTAL_PARKING);
  }

  @FXML
  void handleViewParkingRequestsBtn(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ALPHA_VIEW_MY_REQUESTS);
  }

  @FXML
  void handleRequestParkingEntryBtn(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ALPHA_REQUEST_PARKING_ENTRY);
  }

  @FXML
  void handleInitParkingLot(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ALPHA_INIT_PARKING_LOT);
  }

  @FXML
  void toggleLabelStyle(ActionEvent event) {
    if (infoToggled) {
      infoToggled = false;
      // displayError("infoToggled = " + infoToggled + " and count is " +
      // ++count);
    } else {
      infoToggled = true;
      // displayInfo("infoToggled = " + infoToggled + " and count is " +
      // ++count);
    }
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert requestParkingEntryBtn != null : "fx:id=\"requestParkingEntryBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert initParkingLot != null : "fx:id=\"initParkingLot\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert viewParkingRequestsBtn != null : "fx:id=\"viewParkingRequestsBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert quitBtn != null : "fx:id=\"quitBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    assert incidentalParkingBtn != null : "fx:id=\"incidentalParkingBtn\" was not injected: check your FXML file 'AlphaGUI.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.ALPHA_MAIN_MENU);
  }

  // TODO @Michael check this idea out
  @Override
  public void displayInfo(List<Text> formattedText) {
    // infoLabel.setText(formattedText);
    infoLabel.getStyleClass().clear();
    infoLabel.getStyleClass().add("infoLabel");
  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    // infoLabel.setText(formettedErrorMsg);
    infoLabel.getStyleClass().clear();
    infoLabel.getStyleClass().add("errorLabel");
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

  @Override
  public void turnLoggedInStateOn() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void turnLoggedInStateOff() {
    // TODO Auto-generated method stub
    
  }

}
