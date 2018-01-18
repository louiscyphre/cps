package cps.client.controller.customer;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class EnterCarController extends CustomerActionControllerBaseSubmitAndFinish {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="infoLabel"
  private TextFlow infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="subscriptionIdTextField"
  private TextField subscriptionIdTextField; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML // fx:id="carIdTextField"
  private TextField carIdTextField; // Value injected by FXMLLoader

  private boolean processing = false;

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert subscriptionIdTextField != null : "fx:id=\"subscriptionIdTextField\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    assert carIdTextField != null : "fx:id=\"carIdTextField\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.ENTER_PARKING);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
  }

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
  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  // return car id or null if empty
  private String getCarId() {
    return carIdTextField.getText();
  }

  // returns lot id or 0 if empty
  private int getLotId() {
    if (ControllersClientAdapter.getLotID() == 0) {
      return 0;
    }
    return ControllersClientAdapter.getLotID();
  }

  // returns lot id or 0 if empty or 0 if not a number
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

  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // input fields clear
    carIdTextField.clear();
    subscriptionIdTextField.clear();
  }

  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
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
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
    return response;
  }
}
