/**
 * 
 */
package cps.client.controller.customer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

import cps.api.request.RegularSubscriptionRequest;
import cps.api.response.RegularSubscriptionResponse;
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
 *
 */
public class RegularSubscriptionController extends CustomerActionControllerBaseSubmitAndFinish {

  /**
   * End time TextField
   */
  @FXML
  private TextField endTimeTextField;

  /**
   * End time TextField
   */
  @FXML
  private TextField emailTextField;

  /**
   * Start date DatePicker
   */
  @FXML
  private DatePicker startDatePicker;

  /**
   * Font
   */
  @FXML
  private Font x1;

  /**
   * Insets
   */
  @FXML
  private Insets x3;

  /**
   * Car ID TextField
   */
  @FXML
  private TextField carIDTextField;

  /**
   * @param event
   */
  @FXML
  void handlePickStartDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#handleBackButton(javafx.event.ActionEvent)
   */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS, 10);
  }

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

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOn()
   */
  @Override
  public void turnLoggedInStateOn() {
    super.turnLoggedInStateOn();
    emailTextField.setVisible(false);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOff()
   */
  @Override
  public void turnLoggedInStateOff() {
    super.turnLoggedInStateOff();
    emailTextField.setVisible(true);
  }

  /**
   * Validates that the fields and Sends API request to the server.
   */
  private void validateAndSend() {
    // validation in same order as order in the form
    // out of form
    // customer validation
    int customerID = getCustomerID();
    if (customerID < 0) {
      displayError("Invalid customer ID");
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

    // inside the form
    // car id validation
    String carID = getCarID();
    if (!InputFormats.CARID.validate(carID)) {
      displayError(InputFormats.CARID.errorMsg());
      return;
    }

    int lotID = ControllersClientAdapter.getCustomerContext().getChosenLotID();
    // start time validation
    LocalDate today = LocalDate.now();
    // DatePicker start date validation
    LocalDate plannedStartDate = getPlannedStartDate();
    // compare exit time to entry time
    if (today.compareTo(plannedStartDate) > 0) {
      displayError("Start date must be today, or future date");
      return;
    }

    // daily exit time time validation
    LocalTime dailyExitTime = getPlannedDailyExitTime();
    if (dailyExitTime == null) {
      displayError("Invalid start time");
      return;
    }

    RegularSubscriptionRequest request = new RegularSubscriptionRequest(customerID, email, carID, plannedStartDate,
        lotID, dailyExitTime);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  /**
   * @return customer id from context - >=1 if logged in, 0 otherwise
   */
  private int getCustomerID() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  /**
   * @return email from customer context if logged in, or the value of the email field otherwise
   */
  private String getEmail() {
    CustomerContext cntx = ControllersClientAdapter.getCustomerContext();
    if (cntx.isLoggedIn()) {
      return cntx.getCustomerEmail();
    } else {
      return emailTextField.getText();
    }
  }

  /**
   * @return car id or null if empty
   */
  private String getCarID() {
    return carIDTextField.getText();
  }
  
  /**
   * @return planned start date or null if empty
   */
  private LocalDate getPlannedStartDate() {
    if (startDatePicker.getValue() == null) {
      return null;
    }
    try {
      return startDatePicker.getValue();
    } catch (DateTimeException e) {
      return null;
    }
  }

  /**
   * @return planned exit time or null if empty
   */
  private LocalTime getPlannedDailyExitTime() {
    try {
      return LocalTime.parse(endTimeTextField.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  /**
   * Initializes the Controller and Registers it.
   */
  @FXML
  void initialize() {
    super.baseInitialize();
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'RegularSubscriptionScene.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'RegularSubscriptionScene.fxml'.";
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'RegularSubscriptionScene.fxml'.";
    assert endTimeTextField != null : "fx:id=\"endTimeTextField\" was not injected: check your FXML file 'RegularSubscriptionScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.REGULAR_SUBSCRIPTION);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.RegularSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId() && response.success()) {
      // Customer ID
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      // Making bold font
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));
      // Password
      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      // Making bold font
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));
      // Applying customer ID
      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      // Success info creation
      formattedMessage.add(new Text("Subscription purchased successfully!\n"));
      formattedMessage.add(new Text(String.format("Your account was debited %s ILS.", response.getPayment())));
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      // Display the whole formatted message
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      // Error message creation
      formattedMessage.add(new Text("Could not proceed with purchase!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      // Display the whole formatted message, as an error
      ctrl.displayError(formattedMessage);
    }
    return response;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    carIDTextField.clear();
    emailTextField.clear();
  }
}
