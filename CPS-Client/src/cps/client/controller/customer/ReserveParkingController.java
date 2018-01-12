/**
 * 
 */
package cps.client.controller.customer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import cps.api.request.ReservedParkingRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.utils.FormatValidation.InputFormats;
//import jfxtras.scene.control.CalendarPicker;
import javafx.application.Platform;

//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Locale;

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
 * Created on: 2018-01-09 8:26:06 PM
 */
// TODO some pc displays kryakozyabry in month , handleBackButton implementation
// just for navigation
public class ReserveParkingController implements ViewController {

  private static final String DEFAULT_INFO_LABEL = "";

  private boolean processing = false;

  @FXML
  private TextFlow infoLabel;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private ProgressIndicator infoProgress;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private TextField startTimeTF;

  @FXML
  private TextField endTimeTF;

  @FXML
  private Font x1;

  @FXML
  private VBox infoBox;

  @FXML
  private Insets x2;

  @FXML
  private Insets x3;

  @FXML
  private Insets x4;

  @FXML
  private TextField carIDTextField;

  @FXML
  private TextField emailTF;

  @FXML
  private TextField lotidTF;

  // show Email field
  public void showEmail() {
    emailTF.visibleProperty().set(true);
  }

  //
  public void hideEmail() {
    emailTF.visibleProperty().set(false);
  }

  @FXML
  void handleCancelButton(ActionEvent event) {
    if (processing) {
      return;
    }
    ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU);
  }

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  @FXML
  void handlePickStartDate(ActionEvent event) {
    if (processing) {
      return;
    }

  }

  @FXML
  void handlePickEndDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }

  @FXML
  void initialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert emailTF != null : "fx:id=\"emailTF\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    startDatePicker.setOnShowing(e -> Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH));
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.RESERVE_PARKING);

    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field

    // startDatePicker = new DateTimePicker();
    // startDatePicker.withCalendar(Calendar.getInstance());
    // startDatePicker.withShowTime(Boolean.TRUE);
    // startDatePicker.withLocale(Locale.ENGLISH);
    // startDatePicker.calendarProperty().addListener(new
    // ChangeListener<Calendar>() {

    // @Override
    // public void changed(ObservableValue<? extends Calendar> ov, Calendar t,
    // Calendar t1) {
    // System.out.println("Selected date: "+t1.getTime().toString());
    // }
    // });

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
    // start time validation
    LocalTime startTime = getStartTime();
    if (startTime == null) {
      displayError("Invalid start time");
      return;
    }
    // end time validation
    LocalTime endTime = getEndTime();
    if (endTime == null) {
      displayError("Invalid end time");
      return;
    }
    // datepicker start date + ime validation
    LocalDateTime plannedStartTime = getPlannedStartTime();
    if (plannedStartTime == null) {
      displayError("Invalid start date");
      return;
    }
    // datepicker end date + ime validation
    LocalDateTime plannedEndTime = getPlannedEndTime();
    if (plannedEndTime == null) {
      displayError("Invalid leave date");
      return;
    }
    // compare exit time to entry time
    if (plannedStartTime.compareTo(plannedEndTime) >= 0) {
      displayError("Leave date has to be before entry date");
      return;
    }

    // TODO replace the lotid handling from the list instead
    int lotId = getLotId();
    if (lotId < 0) {
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

    ReservedParkingRequest request = new ReservedParkingRequest(customerId, email, carId, lotId, plannedStartTime,
        plannedEndTime);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  // returns customer context - >=1 if logged in, 0 otherwise
  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  // returns email if logged in from customer context,
  private String getEmail() {
    String email = ControllersClientAdapter.getCustomerContext().getCustomerEmail();

    if (email == null) {
      email = emailTF.getText();
    }
    return email;
  }

  // return car id or null if empty
  private String getCarId() {
    return carIDTextField.getText();
  }

  // returns lot id or -1 if empty
  private int getLotId() {
    if (lotidTF.getText() == null) {
      return -1;
    }
    try {
      return Integer.parseInt(lotidTF.getText());
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  // returns planned start date or null if empty
  private LocalDateTime getPlannedStartTime() {
    if (startDatePicker.getValue() == null) {
      return null;
    }
    try {
      return startDatePicker.getValue().atTime(getStartTime());
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  // returns planned start time or null if empty
  private LocalTime getStartTime() {
    try {
      return LocalTime.parse(startTimeTF.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  // returns planned end date or null if empty
  private LocalDateTime getPlannedEndTime() {
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
      return LocalTime.parse(endTimeTF.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
    } catch (DateTimeParseException e) {
      return null;
    }
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
}
