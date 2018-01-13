/**
 * 
 */
package cps.client.controller.customer;

import java.util.List;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created on: 2018-01-09 1:04:02 AM
 */
public class SubscriptionsMenuController implements ViewController {

  @FXML
  private TextFlow infoLabel;

  @FXML
  private ProgressIndicator infoProgress;

  @FXML
  private VBox infoBox;

  @FXML
  private ToggleGroup subscriptionRadioButtons;

  private boolean processing;

  @FXML
  void handleBackButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void handleNextButton(ActionEvent event) {
    if (processing) {
      return;
    }
    // TODO
    // ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void initialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    assert subscriptionRadioButtons != null : "fx:id=\"subscriptionRadioButtons\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
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
    processing = false;
  }

  @Override
  public void turnProcessingStateOff() {
    processing = true;
  }

  @Override
  public void turnLoggedInStateOn() {

  }

  @Override
  public void turnLoggedInStateOff() {

  }

  @Override
  public void cleanCtrl() {
    // info box clear
    infoBox.getStyleClass().add("infoLabel");
    infoProgress.visibleProperty().set(false);
    infoLabel.getChildren().clear();
    // toggles clear
    subscriptionRadioButtons.getToggles().clear();
  }
}
