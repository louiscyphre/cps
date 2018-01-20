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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created on: 2018-01-09 1:04:02 AM
 */
/**
 * @author firl
 *
 */
public class SubscriptionsMenuController extends CustomerActionControllerBase implements ParkingLotsController {

  /**
   * 
   */
  @FXML
  private TextFlow regularSubscriptionInfo;

  /**
   * 
   */
  @FXML
  private TextFlow fullSubscriptionInfo;

  /**
   * 
   */
  @FXML
  private ToggleGroup subscriptionRadioButtons;

  /**
   * 
   */
  @FXML
  private RadioButton regularSubscriptionRadioButton;

  /**
   * 
   */
  @FXML
  private RadioButton fullSubscriptionRadioButton;

  /**
   * 
   */
  @FXML
  private ComboBox<String> parkingLotsList;

  /**
   * 
   */
  HashMap<String, ParkingLot> parkingLotsMap = null;

  /**
   * @param list
   */
  /* (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.Collection)
   */
  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    List<String> tmp = new ArrayList<String>();
    parkingLotsMap = new HashMap<String, ParkingLot>();
    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address, i);
    }
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  /**
   * @param addresses
   */
  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  /**
   * @param choice
   */
  private void setRegularSubscriptionInfoForLot(String choice) {
    float reservedParkingPrice = parkingLotsMap.get(choice).getPriceForService(Constants.PARKING_TYPE_RESERVED);
    float subscriptionOverallPrice = reservedParkingPrice * Constants.SUBSCRIPTION_TYPE_REGULAR_ONE_CAR_HOURS;
    StringBuilder builder = new StringBuilder();
    builder.append("Park in specified lot all week daily except weekends for ");
    builder.append(subscriptionOverallPrice).append(" ILS only!");
    setInfo(regularSubscriptionInfo, builder.toString());
  }

  /**
   * 
   */
  private void setFullSubscriptionInfo() {
    float subscriptionOverallPrice = Constants.PRICE_PER_HOUR_RESERVED * Constants.SUBSCRIPTION_TYPE_FULL_HOURS;
    StringBuilder builder = new StringBuilder();
    builder.append("Park any day, any time, in any parking lot for ");
    builder.append(subscriptionOverallPrice).append(" ILS only!");
    setInfo(fullSubscriptionInfo, builder.toString());
  }

  /**
   * @param event
   */
  @FXML
  void showSubscriptionsForLot(ActionEvent event) {
    if (processing) {
      return;
    }
    setRegularSubscriptionInfoForLot(parkingLotsList.getValue());
  }

  /**
   * @param event
   */
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

  /**
   * @param event
   */
  @FXML
  void toggleFullSubscriptionChoice(ActionEvent event) {
    if (processing) {
      return;
    }
    parkingLotsList.setDisable(true);
  }

  /**
   * @param event
   */
  @FXML
  void handleNextButton(ActionEvent event) {
    if (processing) {
      return;
    }
    if (fullSubscriptionRadioButton.isSelected()) {
      ControllersClientAdapter.setStage(ControllerConstants.SceneCode.FULL_SUBSCRIPTION, 10);
      return;
    }
    if (parkingLotsList.getValue() == null) {
      displayError("Please choose a parking lot");
    }
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getCustomerContext().setChosenLotID(userChosenLotID);
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.REGULAR_SUBSCRIPTION, 10);
  }

  /**
   * 
   */
  @FXML
  void initialize() {
    super.baseInitialize();
    assert subscriptionRadioButtons != null : "fx:id=\"subscriptionRadioButtons\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    assert regularSubscriptionInfo != null : "fx:id=\"regularSubscriptionInfo\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    assert fullSubscriptionInfo != null : "fx:id=\"fullSubscriptionInfo\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'CustomerListSubscriptionsScene.fxml'.";

    setInfo(regularSubscriptionInfo, "Choose option to see specific lot's prices");
    setFullSubscriptionInfo();

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // toggles clear
    subscriptionRadioButtons.getToggles().clear();
  }

  /**
   * @param textBox
   * @param info
   */
  private void setInfo(TextFlow textBox, String info) {
    textBox.getChildren().clear();
    textBox.getChildren().add(new Text(info));
  }
}
