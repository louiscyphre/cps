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

/** Incidental Parking controller. */
public class IncidentalParkingController extends CustomerActionControllerBaseSubmitAndFinish {
  /** Car ID TextField */
  @FXML
  private TextField carIDTextField;

  /** End Date picker */
  @FXML
  private DatePicker endDatePicker;

  /** End Time Text Field */
  @FXML
  private TextField endTimeTextField;

  /** Email Text Field */
  @FXML
  private TextField emailTextField;

  /** Font */
  @FXML
  private Font x1;

  /** Insets */
  @FXML
  private Insets x3;

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

  /** @param event */
  @FXML
  void handlePickEndDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }

  /** Validates that the fields and Sends API request to the server. */
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

    IncidentalParkingRequest request = new IncidentalParkingRequest(customerID, email, carID, lotID, plannedEndDateTime);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  /** @return customer id from context */
  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  /** @return car ID */
  private String getCarId() {
    return carIDTextField.getText();
  }

  /** @return planned leave date */
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

  /** @return planned end time */
  private LocalTime getEndTime() {
    return getTime(endTimeTextField, "End time");
  }

  /** @return lot id from the client application */
  private int getLotId() {
    if (ControllersClientAdapter.getLotID() == 0) {
      return -1;
    }
    return ControllersClientAdapter.getLotID();
  }

  /** @return email from context */
  private String getEmail() {
    CustomerContext cntx = ControllersClientAdapter.getCustomerContext();
    if (cntx.isLoggedIn()) {
      return cntx.getCustomerEmail();
    } else {
      return emailTextField.getText();
    }
  }

  /** Toggles the email TextField visible */
  public void showEmail() {
    emailTextField.visibleProperty().set(true);
  }

  /** Toggles the email TextField invisible */
  public void hideEmail() {
    emailTextField.visibleProperty().set(false);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOn() */
  @Override
  public void turnLoggedInStateOn() {
    super.turnLoggedInStateOn();
    emailTextField.setVisible(false);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOff() */
  @Override
  public void turnLoggedInStateOff() {
    super.turnLoggedInStateOff();
    emailTextField.setVisible(true);
  }

  /** Initializes the Controller and Registers it. */
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

  /* (non-Javadoc)
   * @see
   * cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#
   * cleanCtrl() */
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

  /** Display the parking details if request was successful, and user
   * credentials if new user, otherwise - error message from the server. */
  public void handle(IncidentalParkingResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();

    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId() && response.success()) {
      // Customer ID
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID: "));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      // Making bold font
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));
      // Password
      formattedMessage.add(new Text("Your Password: "));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      // Making bold font
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));
      // Applying customer ID
      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      // Turning the LoggedIn state, application-wide
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    // logged in customer
    if (response.success()) {
      // Success info creation
      formattedMessage.add(new Text("Entry granted\n"));
      formattedMessage.add(new Text("Robot will collect your car shortly\n"));
      Text warning = new Text("Don't forget: Password is required to collect the car\n");
      // Making bold font
      Font defaultFont = warning.getFont();
      warning.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(warning);
      formattedMessage.add(new Text("\n"));
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      // Display the whole formatted message
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else { // request failed
      // Error message creation
      formattedMessage.add(new Text("Could not grant parking at this moment!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      addAlternativeLots(formattedMessage, response.getAlternativeLots());
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      // Display the whole formatted message, as an error
      ctrl.displayError(formattedMessage);
    }

  }

}
