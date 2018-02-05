/**
 * 
 */
package cps.client.controller.customer;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import cps.api.request.FullSubscriptionRequest;
import cps.api.response.FullSubscriptionResponse;
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
 * Full Subscription controller. 
 */
public class FullSubscriptionController extends CustomerActionControllerBaseSubmitAndFinish {

  /** Start Date date picker  */
  @FXML
  private DatePicker startDatePicker;

  /** email TextField  */
  @FXML
  private TextField emailTextField;

  /**
   * Font injected by FXML
   */
  @FXML
  private Font x1;

  /**
   * Insets injected by FXML
   */
  @FXML
  private Insets x3;

  /**
   * CarID Text Field
   */
  @FXML
  private TextField carIDTextField;

  /**
   * Handles date pick.
   * 
   * @param event
   */
  @FXML
  void handlePickStartDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#
   * handleBackButton(javafx.event.ActionEvent)
   */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
  }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#
   * handleSubmitButton(javafx.event.ActionEvent)
   */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOn()
   */
  @Override
  public void turnLoggedInStateOn() {
    super.turnLoggedInStateOn();
    emailTextField.setVisible(false);
  }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOff()
   */
  @Override
  public void turnLoggedInStateOff() {
    super.turnLoggedInStateOff();
    emailTextField.setVisible(true);
  }

  /**
   * Validates the fields and Sends API request to the server.
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
    // inside the form
    // car id validation
    String[] carIDs = getCarIDs();
    
    if (carIDs == null) {
      displayError(InputFormats.CARID.errorMsg());
      return;
    }
    
    for (String carID : carIDs) {
      if (!InputFormats.CARID.validate(carID)) {
        displayError(InputFormats.CARID.errorMsg());
        return;
      }
    }
    
    // start time validation
    LocalDate today = LocalDate.now();
    // DatePicker start date validation
    LocalDate plannedStartDate = getPlannedStartDate();
    if (plannedStartDate == null) {
      displayError("Invalid start date");
      return;
    }

    // compare exit time to entry time
    if (today.compareTo(plannedStartDate) > 0) {
      displayError("Start date must be today, or future date");
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

    FullSubscriptionRequest request = new FullSubscriptionRequest(customerID, email, carIDs, plannedStartDate);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  // returns customer context - >=1 if logged in, 0 otherwise
  /**
   * @return customer context id
   */
  private int getCustomerID() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  /**
   * @return email from customer context, or value of the email field if not logged in
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
   * @return car id list or null if empty
   */
  private String[] getCarIDs() {
    String text = carIDTextField.getText();
    
    if (text == null || text.trim().length() < 1) {
      return null;
    }
    
    String[] values = text.split(",");
    for (int i = 0; i < values.length; i++) {
      values[i] = values[i].trim();
    }
    return values;
  }

  /**
   * @return planned parking start date or null if empty
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
   * Initializes the Controller and Registers it.
   */
  @FXML
  void initialize() {
    super.baseInitialize();
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'FullSubscriptionScene.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'FullSubscriptionScene.fxml'.";
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'FullSubscriptionScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.FULL_SUBSCRIPTION);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  /**
   * Display the Subscription Details Request was successful, and user
   * credentials if new user, otherwise - error.
   */
  public void handle(FullSubscriptionResponse response) {
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
      addSubscriptionIDs(formattedMessage, response.getSubscriptionIDs());
      ctrl.turnProcessingStateOff();
      // Display the whole formatted message
      ctrl.displayInfo(formattedMessage);
      // Submit->Finish
      setFinishInsteadOfSubmit(true);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      // Error message creation
      formattedMessage.add(new Text("Could not proceed with purchase!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      addAlternativeLots(formattedMessage, response.getAlternativeLots());
      // Turning off the processing state
      ctrl.turnProcessingStateOff();
      // Display the whole formatted message, as an error
      ctrl.displayError(formattedMessage);
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#
   * cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    carIDTextField.clear();
    emailTextField.clear();
  }
}
