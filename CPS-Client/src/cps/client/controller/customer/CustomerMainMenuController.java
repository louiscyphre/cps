/**
 * 
 */
package cps.client.controller.customer;

import java.util.List;

import cps.client.context.CustomerContext;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CustomerMainMenuController extends CustomerActionControllerBase {

  @FXML // fx:id="exitParkingButton"
  private Button exitParkingButton; // Value injected by FXMLLoader

  @FXML // fx:id="buySubscriptionButton"
  private Button buySubscriptionButton; // Value injected by FXMLLoader

  @FXML // fx:id="parkNowButton"
  private Button parkNowButton; // Value injected by FXMLLoader

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

  @FXML
  void handleLogoutButton(ActionEvent event) {
    ControllersClientAdapter.turnLoggedInStateOff();
  }

  @FXML
  void handleEnterParkingButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ENTER_PARKING);
    if (processing) {
      return;
    }
  }

  @FXML
  void handleExitParkingButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.EXIT_PARKING);
    if (processing) {
      return;
    }
  }

  @FXML
  void handleViewMyReservationsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.VIEW_MY_RESERVATION);
    if (processing) {
      return;
    }
  }

  @FXML
  void handleLoginButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.LOGIN);
  }

  @FXML
  void handleReserveParkingButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.RESERVE_PARKING);
  }

  @FXML
  void handleBuySubscriptionButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  @FXML
  void handleParkNowButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.INCIDENTAL_PARKING);
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  private void initialize() {
    super.baseInitialize();
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

  @Override
  public void displayInfo(List<Text> formattedText) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formattedText) {
      infoLabel.getChildren().add(ft);
    }
  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleInfoMsg));
  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formettedErrorMsg) {
      infoLabel.getChildren().add(ft);
    }
  }

  @Override
  public void displayError(String simpleErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleErrorMsg));
  }

  @Override
  public void turnProcessingStateOn() {
    infoProgress.visibleProperty().set(true);
    Text text = new Text("Processing...");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(text);
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("processingLabel");
    processing = true;
  }

  @Override
  public void turnProcessingStateOff() {
    processing = false;
  }

  @Override
  public void turnLoggedInStateOn() {
    buttonsVbox.getChildren().remove(logInButton);
    buttonsVbox.getChildren().add(0, logOutButton);
    buttonsVbox.getChildren().add(4, enterParkingButton);
    buttonsVbox.getChildren().add(5, exitParkingButton);
    buttonsVbox.getChildren().add(6, viewMyReservationsButton);
    buttonsVbox.getChildren().forEach(node -> {
      node.getStyleClass().add("smallButton");
    });
  }

  @Override
  public void turnLoggedInStateOff() {
    cleanCtrl();
    if (!buttonsVbox.getChildren().contains(logInButton)) {
      buttonsVbox.getChildren().add(0, logInButton);
    }
    buttonsVbox.getChildren().remove(logOutButton);
    buttonsVbox.getChildren().remove(enterParkingButton);
    buttonsVbox.getChildren().remove(exitParkingButton);
    buttonsVbox.getChildren().remove(viewMyReservationsButton);
    buttonsVbox.getChildren().forEach(node -> {
      node.getStyleClass().remove("smallButton");
    });
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    if (context.isLoggedIn()) {
      infoLabel.getChildren().add(new Text("Logged in as : " + context.getCustomerEmail()));
    }
  }

}
