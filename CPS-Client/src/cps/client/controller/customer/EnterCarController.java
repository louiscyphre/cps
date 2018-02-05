package cps.client.controller.customer;

import java.util.LinkedList;
import java.util.List;

import cps.api.request.ParkingEntryRequest;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/** Enter Car controller. May be in either LoggedIn or LoggedOut state. */
public class EnterCarController extends CustomerActionControllerBaseSubmitAndFinish {

  /** Subscription TextField */
  @FXML // fx:id="subscriptionIdTextField"
  private TextField subscriptionIdTextField; // Value injected by FXMLLoader

  /** CarID Text Field */
  @FXML // fx:id="carIdTextField"
  private TextField carIdTextField; // Value injected by FXMLLoader

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#
   * handleSubmitButton(javafx.event.ActionEvent) */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  /** Initializes the Controller and Registers it. */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert subscriptionIdTextField != null : "fx:id=\"subscriptionIdTextField\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    assert carIdTextField != null : "fx:id=\"carIdTextField\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.ENTER_PARKING);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
  }

  /** Validates that the fields and Sends API request to the server. */
  private void validateAndSend() {
    // validation in same order as order in the form
    // out of form
    // customer validation
    int customerId = getCustomerId();
    if (customerId < 0) {
      displayError("Invalid customer ID");
      return;
    }
    // inside the form
    // car id validation
    String carId = getCarId();
    if (!InputFormats.CARID.validate(carId)) {
      displayError(InputFormats.CARID.errorMsg());
      return;
    }

    // lotid handling from the list instead
    int lotId = getLotId();
    if (lotId < 0) {
      displayError("Invalid lot ID");
      return;
    }
    ParkingEntryRequest request = new ParkingEntryRequest(getCustomerId(), getSubscriptionId(), getLotId(), getCarId());
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  // returns customer context - >=1 if logged in, 0 otherwise
  /** Retrieves customer ID.
   * @return */
  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  // return car id or null if empty
  /** Retrieves the Car ID.
   * @return */
  private String getCarId() {
    return carIdTextField.getText();
  }

  // returns lot id or 0 if empty
  /** Retrieves the Lot ID.
   * @return */
  private int getLotId() {
    if (ControllersClientAdapter.getLotID() == 0) {
      return 0;
    }
    return ControllersClientAdapter.getLotID();
  }

  // returns lot id or 0 if empty or 0 if not a number
  /** Retrieves the Subscription ID
   * @return */
  private int getSubscriptionId() {
    if (subscriptionIdTextField.getText() == null) {
      return 0;
    }
    try {
      return Integer.parseInt(subscriptionIdTextField.getText());
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /* (non-Javadoc)
   * @see
   * cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#
   * cleanCtrl() */
  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // input fields clear
    carIdTextField.clear();
    subscriptionIdTextField.clear();
  }

  /** Display parking entry granted if Request was succesful, error - otherwise. */
  @Override
  public void handle(ParkingEntryResponse response) {
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("The parking entry is granted!\nRobot will collect your car shortly.\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("The parking entry is denied!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      addAlternativeLots(formattedMessage, response.getAlternativeLots());
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
  }
}
