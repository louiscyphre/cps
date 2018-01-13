/**
 * 
 */
package cps.client.controller.customer;

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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CustomerMainMenuController implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="exitParkingButton"
  private Button exitParkingButton; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox1"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML // fx:id="buySubscriptionButton"
  private Button buySubscriptionButton; // Value injected by FXMLLoader

  @FXML // fx:id="parkNowButton"
  private Button parkNowButton; // Value injected by FXMLLoader

  @FXML // fx:id="infoLabel1"
  private TextFlow infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="enterParkingButton"
  private Button enterParkingButton; // Value injected by FXMLLoader

  @FXML // fx:id="logOutButton"
  private Button logOutButton; // Value injected by FXMLLoader

  @FXML // fx:id="root"
  private BorderPane root; // Value injected by FXMLLoader

  @FXML // fx:id="reserveParkingButton"
  private Button reserveParkingButton; // Value injected by FXMLLoader

  @FXML // fx:id="viewMyReservationsButton"
  private Button viewMyReservationsButton; // Value injected by FXMLLoader

  @FXML // fx:id="buttonsVbox"
  private VBox buttonsVbox; // Value injected by FXMLLoader

  @FXML // fx:id="logInButton"
  private Button logInButton; // Value injected by FXMLLoader

  private boolean processing;

  @FXML
  void handleLogoutButton(ActionEvent event) {
    ControllersClientAdapter.turnLoggedInStateOff();
  }

  @FXML
  void handleEnterParkingButton(ActionEvent event) {
    // TODO
    // ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ENTER_PARKING);
  }

  @FXML
  void handleExitParkingButton(ActionEvent event) {
    // TODO
    // ControllersClientAdapter.setStage(ControllerConstants.SceneCode.EXIT_PARKING);
  }

  @FXML
  void handleViewMyReservationsButton(ActionEvent event) {
    // TODO
    // ControllersClientAdapter.setStage(ControllerConstants.SceneCode.VIEW_MY_REGISTRATIONS);
  }

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

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert exitParkingButton != null : "fx:id=\"exitParkingButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox1\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert buySubscriptionButton != null : "fx:id=\"buySubscriptionButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert parkNowButton != null : "fx:id=\"parkNowButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert infoLabel != null : "fx:id=\"infoLabel1\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert enterParkingButton != null : "fx:id=\"enterParkingButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert logOutButton != null : "fx:id=\"logOutButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert reserveParkingButton != null : "fx:id=\"reserveParkingButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert viewMyReservationsButton != null : "fx:id=\"viewMyReservationsButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert buttonsVbox != null : "fx:id=\"buttonsVbox\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    assert logInButton != null : "fx:id=\"logInButton\" was not injected: check your FXML file 'CustomerInitialMenuSceneMk2.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  // TODO @Michael check this idea out
  @Override
  public void displayInfo(List<Text> formattedText) {
    // TODO change the layout element to text flow
  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    // TODO change the layout element to text flow
  }

  @Override
  public void turnProcessingStateOn() {
    // TODO
    processing = true;
  }

  @Override
  public void turnProcessingStateOff() {
    // TODO
    processing = false;
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
    buttonsVbox.getChildren().remove(logInButton);
    buttonsVbox.getChildren().add(0, logOutButton);
    buttonsVbox.getChildren().add(4, enterParkingButton);
    buttonsVbox.getChildren().add(5, exitParkingButton);
    buttonsVbox.getChildren().add(6, viewMyReservationsButton);
  }

  @Override
  public void turnLoggedInStateOff() {
    buttonsVbox.getChildren().remove(logOutButton);
    buttonsVbox.getChildren().remove(enterParkingButton);
    buttonsVbox.getChildren().remove(exitParkingButton);
    buttonsVbox.getChildren().remove(viewMyReservationsButton);
  }

}
