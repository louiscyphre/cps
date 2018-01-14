/**
 * 
 */
package cps.client.controller.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cps.api.request.ListParkingLotsRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.common.Constants;
import cps.entities.models.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created on: 2018-01-09 1:04:02 AM
 */
public class SubscriptionsMenuController implements ParkingLotsController {

  @FXML
  private TextFlow infoLabel;

  @FXML
  private ProgressIndicator infoProgress;
  @FXML
  private Label             regularSubscriptionInfo;
  @FXML
  private Label             fullSubscriptionInfo;

  @FXML
  private VBox infoBox;

  @FXML
  private ToggleGroup subscriptionRadioButtons;

  @FXML
  private RadioButton regularSubscriptionRadioButton;

  @FXML
  private RadioButton fullSubscriptionRadioButton;

  @FXML
  private ComboBox<String> parkingLotsList;

  HashMap<String, ParkingLot> parkingLotsMap;

  private boolean processing;

  /**
   * @param parkingLots
   */
  public void setParkingLots(List<ParkingLot> list) {
    List<String> tmp = new ArrayList<String> ();
    parkingLotsMap = new HashMap<String, ParkingLot>();
    for (ParkingLot i: list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address,  i);
    }
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  private void setRegularSubscriptionInfoForLot(String choice) {
    float reservedParkingPrice = parkingLotsMap.get(choice).getPriceForService(Constants.PARKING_TYPE_RESERVED);
    float subscriptionOverallPrice = reservedParkingPrice * Constants.SUBSCRIPTION_TYPE_REGULAR_ONE_CAR_HOURS;
    StringBuilder builder = new StringBuilder();
    builder.append("Regular subscription:").append(Constants.SUBSCRIPTION_TYPE_REGULAR_ONE_CAR_HOURS).append(" parking hours for ");
    builder.append(subscriptionOverallPrice).append("$ only!");
    regularSubscriptionInfo.setText(builder.toString());
  }
  
  private void  setFullSubscriptionInfo() {
    float subscriptionOverallPrice = Constants.PRICE_PER_HOUR_RESERVED * Constants.SUBSCRIPTION_TYPE_FULL_HOURS;
    StringBuilder builder = new StringBuilder();
    builder.append("Full subscription:").append(Constants.SUBSCRIPTION_TYPE_FULL_HOURS).append(" parking hours in any parking lot for ");
    builder.append(subscriptionOverallPrice).append("$ only!");
    String info = builder.toString();
    fullSubscriptionInfo.setText(info);
  }
  
  @FXML
  void showSubscriptionsForLot(ActionEvent event) {
    if (processing) {
      return;
    }
    setRegularSubscriptionInfoForLot(parkingLotsList.getValue());
  }

  @FXML
  void toggleRegularSubscriptionChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    parkingLotsList.setDisable(false);
    ListParkingLotsRequest request = new ListParkingLotsRequest();
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  @FXML
  void toggleFullSubscriptionChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    setFullSubscriptionInfo();
    parkingLotsList.setDisable(true);
  }

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
    processing = true;
  }

  @Override
  public void turnProcessingStateOff() {
    processing = false;
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
