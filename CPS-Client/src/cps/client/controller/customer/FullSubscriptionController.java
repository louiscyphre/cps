/**
 * 
 */
package cps.client.controller.customer;


import java.time.DateTimeException;
import java.time.LocalDate;

import cps.api.request.FullSubscriptionRequest;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * Created on: 2018-01-15 2:34:23 AM 
 */
public class FullSubscriptionController extends CustomerActionControllerBase {

  @FXML
  private TextField customerIDTextField;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private TextField emailTextField;

  @FXML
  private Font x1;

  @FXML
  private Insets x3;

  @FXML
  private TextField carIDTextField;

  @FXML
  void handlePickStartDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }
  
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }
  

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
    String carID = getCarID();
    if (!InputFormats.CARID.validate(carID)) {
      displayError(InputFormats.CARID.errorMsg());
      return;
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
    if (today.compareTo(plannedStartDate) >= 0) {
      displayError("Start date must be future date");
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

    FullSubscriptionRequest request = new FullSubscriptionRequest(customerID, email, carID, plannedStartDate);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }
  

  // returns customer context - >=1 if logged in, 0 otherwise
  private int getCustomerID() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
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
  
  // return car id or null if empty
  private String getCarID() {
    return carIDTextField.getText();
  }
  
  // returns planned start date or null if empty
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
}
