/**
 * 
 */
package cps.client.controller.customer;

import java.util.ArrayList;
import java.util.Collection;
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
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * Created on: 2018-01-09 1:04:02 AM
 */
public class SubscriptionsMenuController extends CustomerActionControllerBase implements ParkingLotsController {
  
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

  HashMap<String, ParkingLot> parkingLotsMap = null;

  /**
   * @param list
   */
  public void setParkingLots(Collection <ParkingLot> list) {
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
    builder.append(subscriptionOverallPrice).append(" NIS only!");
    regularSubscriptionInfo.setText(builder.toString());
    fullSubscriptionInfo.setMaxWidth(Double.MAX_VALUE);
    fullSubscriptionInfo.setAlignment(Pos.CENTER);//FIXME somehow
  }
  
  private void  setFullSubscriptionInfo() {
    float subscriptionOverallPrice = Constants.PRICE_PER_HOUR_RESERVED * Constants.SUBSCRIPTION_TYPE_FULL_HOURS;
    StringBuilder builder = new StringBuilder();
    builder.append("Full subscription:").append(Constants.SUBSCRIPTION_TYPE_FULL_HOURS).append(" parking hours in any parking lot for ");
    builder.append(subscriptionOverallPrice).append(" NIS only!");
    fullSubscriptionInfo.setText(builder.toString());
    fullSubscriptionInfo.setMaxWidth(Double.MAX_VALUE);
    fullSubscriptionInfo.setAlignment(Pos.CENTER);//FIXME somehow
  }
  
  @FXML
  void showSubscriptionsForLot(ActionEvent event) {
    if (processing) {
      return;
    }
    // TODO SubscriptionsMenuController::showSubscriptionsForLot
    setRegularSubscriptionInfoForLot(parkingLotsList.getValue());
  }

  @FXML
  void toggleRegularSubscriptionChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    parkingLotsList.setDisable(false);
    if (parkingLotsMap != null) {
      return;
    }
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
    if (fullSubscriptionRadioButton.isSelected()) {
      ControllersClientAdapter.setStage(ControllerConstants.SceneCode.FULL_SUBSCRIPTION);
      return;
    }
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getCustomerContext().setChosenLotID(userChosenLotID);
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.REGULAR_SUBSCRIPTION);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert subscriptionRadioButtons != null : "fx:id=\"subscriptionRadioButtons\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";

    regularSubscriptionInfo.setText("Choose option to see specific lot's prices");
    setFullSubscriptionInfo();
    
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
