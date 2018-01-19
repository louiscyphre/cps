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
 * Created on: 2018-01-15 2:34:23 AM
 */
public class FullSubscriptionController extends CustomerActionControllerBaseSubmitAndFinish {

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
  void handleBackButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS, 10);
  }

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
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

  public ServerResponse handle(FullSubscriptionResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId() && response.success()) {
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));

      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));

      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("Subscription purchased successfully!\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("Could not proceed with purchase!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
    return response;
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    carIDTextField.clear();
    emailTextField.clear();
  }
}
