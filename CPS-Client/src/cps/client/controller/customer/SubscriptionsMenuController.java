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
 * Subscription menu controller.
 */
public class SubscriptionsMenuController extends CustomerActionControllerBase implements ParkingLotsController {

  /** TextFlow containing Regular Subscription details text*/
  @FXML
  private TextFlow regularSubscriptionInfo;

  /** TextFlow containing Full Subscription details text*/
  @FXML
  private TextFlow fullSubscriptionInfo;

  /** Toggle Group for choice of radio buttons  */
  @FXML
  private ToggleGroup subscriptionRadioButtons;

  /** Regular Subscription radio button  */
  @FXML
  private RadioButton regularSubscriptionRadioButton;

  /** Full Subscription radio button  */
  @FXML
  private RadioButton fullSubscriptionRadioButton;

  /** ComboBox containing parking lots */
  @FXML
  private ComboBox<String> parkingLotsList;

  /** Parking Lots mapping to addresses  */
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
   * Helper function assigning the given collection of parking names to combo box.
   * @param addresses
   */
  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  /**
   * Updates the regular subscription info in accordance with button choice.
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
   * Updates the full subscription info in accordance with button choice.
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
  void handleNextButton(ActionEvent event) {
    if (processing) {
      return;
    }
    
    if (parkingLotsList.getValue() == null) {
      displayError("Please choose a parking lot");
      return;
    }
    
    int userChosenLotID = parkingLotsMap.get(parkingLotsList.getValue()).getId();
    ControllersClientAdapter.getCustomerContext().setChosenLotID(userChosenLotID);
    
    if (fullSubscriptionRadioButton.isSelected()) {
      ControllersClientAdapter.setStage(ControllerConstants.SceneCode.FULL_SUBSCRIPTION);
      return;
    }
    
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.REGULAR_SUBSCRIPTION);
  }
  /**
   * Initializes the Controller and Registers it. 
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
   * Updates the chosen text box with the supplied info.
   * @param textBox
   * @param info
   */
  private void setInfo(TextFlow textBox, String info) {
    textBox.getChildren().clear();
    textBox.getChildren().add(new Text(info));
  }
}
