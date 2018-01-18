/**
 * 
 */
package cps.client.controller.customer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import cps.api.request.IncidentalParkingRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ServerResponse;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created on: 2018-01-13 1:01:03 AM
 */
public class IncidentalParkingController extends CustomerActionControllerBaseSubmitAndFinish {
  @FXML
  private TextField carIDTextField;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private TextField endTimeTextField;

  @FXML
  private TextField emailTextField;

  @FXML
  private Font x1;

  @FXML
  private Insets x3;

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  @FXML
  void handlePickEndDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }

  private void validateAndSend() {
    // validation in same order as order in the form
    // out of form
    // customer validation
    int customerID = getCustomerId();
    if (customerID < 0) {
      displayError("Invalid customer ID");
      return;
    }
    // inside the form
    // car id validation
    String carID = getCarId();
    if (!InputFormats.CARID.validate(carID)) {
      displayError(InputFormats.CARID.errorMsg());
      return;
    }
    // end time validation
    LocalTime endTime = getEndTime();
    if (endTime == null) {
      displayError("Invalid end time");
      return;
    }
    // date picker end date + time validation
    LocalDateTime plannedEndDateTime = getPlannedEndDateTime();
    if (plannedEndDateTime == null) {
      displayError("Invalid leave date");
      return;
    }
    // compare exit time to entry time
    LocalDateTime time = LocalDateTime.now();
    if (time.compareTo(plannedEndDateTime) >= 0) {
      displayError("Start date must be today, or future date");
      return;
    }

    // TODO replace the lotid handling from the list instead lotid may be 1 or 0
    // - check
    int lotID = getLotId();
    if (lotID <= 0) {
      displayError("Invalid lot ID");
      return;
    }

    // email validation - maybe be visible - not logged in, invisible -
    // otherwise
    String email = getEmail();
    if (!InputFormats.isValidEmailAddress(email)) {
      displayError(InputFormats.InputValidation.BAD_EMAIL.getMsg());
      return;
    } else {
      ControllersClientAdapter.getCustomerContext().setPendingEmail(email);
    }

    IncidentalParkingRequest request = new IncidentalParkingRequest(customerID, email, carID, lotID,
        plannedEndDateTime);
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
    return carIDTextField.getText();
  }

  // returns planned end date or null if empty
  private LocalDateTime getPlannedEndDateTime() {
    if (endDatePicker.getValue() == null) {
      return null;
    }
    try {
      return endDatePicker.getValue().atTime(getEndTime());
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  // returns planned end time or null if empty
  private LocalTime getEndTime() {
    return getTime(endTimeTextField, "End time");
  }

  // returns lot id or -1 if empty
  private int getLotId() {
    if (ControllersClientAdapter.getLotID() == 0) {
      return -1;
    }
    return ControllersClientAdapter.getLotID();
  }

  // returns email if logged in from customer context,
  private String getEmail() {
    CustomerContext cntx = ControllersClientAdapter.getCustomerContext();
    if (cntx.isLoggedIn()) {
      return cntx.getCustomerEmail();
    } else {
      return emailTextField.getText();
    }
  }

  // show Email field
  public void showEmail() {
    emailTextField.visibleProperty().set(true);
  }

  //
  public void hideEmail() {
    emailTextField.visibleProperty().set(false);
  }

  @Override
  public void turnLoggedInStateOn() {
    super.turnLoggedInStateOn();
    emailTextField.setVisible(false);
  }

  @Override
  public void turnLoggedInStateOff() {
    super.turnLoggedInStateOff();
    emailTextField.setVisible(true);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert endTimeTextField != null : "fx:id=\"endTimeTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.INCIDENTAL_PARKING);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();

    // text fields clear
    emailTextField.clear();
    carIDTextField.clear();
    endDatePicker.getEditor();
    endTimeTextField.clear();
  }
  
  public ServerResponse handle(IncidentalParkingResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();

    // if request fails customer id is 0 TODO
    // new customer
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId() && response.success()) {
      // outputting the customer id on screen
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));
      // password part
      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));
      // binding new user to application context
      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    // logged in customer
    if (response.success()) {
      formattedMessage.add(new Text("Entry granted\n"));
      formattedMessage.add(new Text("Robot will collect your car shortly\n"));
      Text warning = new Text("Don't forget: Password is required to collect the Car");
      Font defaultFont = warning.getFont();
      warning.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(new Text("\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else { // request failed
      formattedMessage.add(new Text("Could not grant parking at this moment!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }

    return response;
  }


}
