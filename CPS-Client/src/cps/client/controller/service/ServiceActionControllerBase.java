package cps.client.controller.service;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ServiceActionControllerBase implements ViewController {
  protected static final String DEFAULT_INFO_LABEL = "Welcome to Car Parking System!";

  @FXML // ResourceBundle that was given to the FXMLLoader
  protected ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  protected URL location;

  @FXML // fx:id="infoLabel"
  protected TextFlow infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  protected ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  protected VBox infoBox; // Value injected by FXMLLoader

  protected boolean processing;

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
  }

  @FXML
  void handleCancelButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
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
    infoProgress.visibleProperty().set(false);
    processing = false;
    displayInfo(DEFAULT_INFO_LABEL);
  }

  @Override
  public void turnLoggedInStateOn() {
    // Does nothing for base controller
  }

  @Override
  public void turnLoggedInStateOff() {
    // Does nothing for base controller
  }

  void initialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    infoBox.getStyleClass().add("infoLabel");
    infoProgress.visibleProperty().set(false);
    infoLabel.getChildren().clear();
  }

}
