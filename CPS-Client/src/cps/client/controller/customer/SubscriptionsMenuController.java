/**
 * 
 */
package cps.client.controller.customer;

import java.util.ArrayList;
import java.util.List;

import cps.api.request.ListParkingLotsRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.models.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * Created on: 2018-01-09 1:04:02 AM
 */
public class SubscriptionsMenuController extends CustomerActionControllerBase {
  
  @FXML
  private Label             regularSubscriptionInfo;
  @FXML
  private Label             fullSubscriptionInfo;

  @FXML
  private ToggleGroup subscriptionRadioButtons;

  @FXML
  private RadioButton regularSubscriptionRadioButton;

  @FXML
  private RadioButton fullSubscriptionRadioButton;

  @FXML
  private ComboBox<String> parkingLotsList;

  List<ParkingLot> parkingLotsMap;

  /**
   * @param parkingLots
   */
  public void setParkingLots(List<ParkingLot> list) {
    parkingLotsMap = list;
    List<String> tmp = new ArrayList<String>();
    for (ParkingLot i : parkingLotsMap) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
    }
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  @FXML
  void showSubscriptionsForLot(ActionEvent event) {
    if (processing) {
      return;
    }
    String choice = parkingLotsList.getValue();
    // TODO SubscriptionsMenuController::showSubscriptionsForLot
    // regularSubscriptionInfo.setText(getRegularSubscriptionDetailsForLot(choice));
    // Will continue from here
    // fullSubscriptionInfo.setText(getFullSubscriptionDetailsForLot(choice));
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
    parkingLotsList.setDisable(true);
  }

  @FXML
  void handleNextButton(ActionEvent event) {
    if (processing) {
      return;
    }
    // TODO SubscriptionsMenuController::handleNextButton
    // ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert subscriptionRadioButtons != null : "fx:id=\"subscriptionRadioButtons\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // toggles clear
    subscriptionRadioButtons.getToggles().clear();
  }
}
