package cps.client.controller.customer;

import java.util.LinkedList;
import java.util.List;

import cps.api.request.ParkingExitRequest;
import cps.api.response.ParkingExitResponse;
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

public class ExitCarController extends CustomerActionControllerBaseSubmitAndFinish {

  /**
   * CarID Text Field
   */
  @FXML // fx:id="carIdTextField"
  private TextField carIdTextField; // Value injected by FXMLLoader

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#handleSubmitButton(javafx.event.ActionEvent)
   */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  /**
   * Initializes the Controller and Registers it. 
   */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert carIdTextField != null : "fx:id=\"carIdTextField\" was not injected: check your FXML file 'EnterParkingScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.EXIT_PARKING);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  /**
   * Validates that the fields and Sends API request to the server.
   */
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

    ParkingExitRequest request = new ParkingExitRequest(getCustomerId(), getLotId(), getCarId());
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  // returns customer context - >=1 if logged in, 0 otherwise
  /**
   * @return
   */
  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  // return car id or null if empty
  /**
   * @return
   */
  private String getCarId() {
    return carIdTextField.getText();
  }

  // returns lot id or -1 if empty
  /**
   * @return
   */
  private int getLotId() {
    if (ControllersClientAdapter.getLotID() == 0) {
      return -1;
    }
    return ControllersClientAdapter.getLotID();
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // input fields clear
    carIdTextField.clear();
  }

  /**
   * Display parking retrieval granted if Request was succesful, error - otherwise.
   */
  @Override
  public void handle(ParkingExitResponse response) {
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("The car retrieval is granted!\nRobot will retrieve your car shortly.\n"));
      if (response.getPayment() > 0) {
        formattedMessage.add(new Text("You have been credited "));
        formattedMessage.add(new Text(Float.toString(Math.abs(response.getPayment()))));
        formattedMessage.add(new Text(" ILS\n"));
      } else if (response.getPayment() < 0) {
        formattedMessage.add(new Text("You have been debited "));
        formattedMessage.add(new Text(Float.toString(Math.abs(response.getPayment()))));
        formattedMessage.add(new Text(" ILS\n"));
      }
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("The car retrieval is denied!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
  }
}
