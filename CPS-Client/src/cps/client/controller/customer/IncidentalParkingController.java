/**
 * 
 */
package cps.client.controller.customer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import cps.api.request.IncidentalParkingRequest;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.utils.FormatValidation.InputFormats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created on: 2018-01-13 1:01:03 AM
 */
public class IncidentalParkingController implements ViewController {

  private static final String DEFAULT_INFO_LABEL = "";
  private boolean processing = false;

  @FXML
  private TextField customerIDTextField;
  @FXML
  private TextField carIDTextField;

  @FXML
  private TextFlow infoLabel;

  @FXML
  private TextField lotIDTextField;

  @FXML
  private ProgressIndicator infoProgress;
  
  @FXML
  private DatePicker  endDatePicker;

  @FXML
  private TextField endTimeTextField;

  @FXML
  private TextField emailTextField;

  @FXML
  private VBox infoBox;

  @FXML
  private Font x1;

  @FXML
  private Insets x3;

  @FXML
  void handleCancelButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU_MK2);//TODO //FIXME if needed
  }

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
      displayError("Leave date has to be future date");
      return;
    }

    // TODO replace the lotid handling from the list instead
    int lotID = getLotId();
    if (lotID < 0) {
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

    IncidentalParkingRequest request = new IncidentalParkingRequest(customerID, 
        email, carID, lotID, plannedEndDateTime);
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
    try {
      return LocalTime.parse(endTimeTextField.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  // returns lot id or -1 if empty
  private int getLotId() {
    if (lotIDTextField.getText() == null) {
      return -1;
    }
    try {
      return Integer.parseInt(lotIDTextField.getText());
    } catch (NumberFormatException e) {
      return -1;
    }
  } 

  // returns email if logged in from customer context,
  private String getEmail() {
    CustomerContext cntx = ControllersClientAdapter.getCustomerContext();
    if(cntx.isLoggedIn()) {
      return cntx.getCustomerEmail(); 
    }
    else {
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
  public void displayInfo(List<Text> formattedText) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formattedText) {
      infoLabel.getChildren().add(ft);
    }
  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleInfoMsg));
  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formettedErrorMsg) {
      infoLabel.getChildren().add(ft);
    }
  }

  @Override
  public void displayError(String simpleErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleErrorMsg));
  }

  @Override
  public void turnProcessingStateOn() {
    infoProgress.visibleProperty().set(true);
    Text text = new Text("Processing...");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(text);
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("processingLabel");
    processing = true;
  }

  @Override
  public void turnProcessingStateOff() {
    infoProgress.visibleProperty().set(false);
    processing = false;
    displayInfo(DEFAULT_INFO_LABEL);
  }

  @Override
  public void turnLoggedInStateOn() {
    emailTextField.setVisible(false);
    customerIDTextField.setVisible(false);
  }

  @Override
  public void turnLoggedInStateOff() {
    emailTextField.setVisible(true);
    customerIDTextField.setVisible(true);
  }
  
  @FXML
  void initialize() {
    assert customerIDTextField != null : "fx:id=\"customerIDTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'. ";
    assert lotIDTextField != null : "fx:id=\"lotIDTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert endTimeTextField != null : "fx:id=\"endTimeTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert emailTextField != null : "fx:id=\"emailTextField\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'IncidentalParkingScene.fxml'.";


    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.INCIDENTAL_PARKING);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

}
