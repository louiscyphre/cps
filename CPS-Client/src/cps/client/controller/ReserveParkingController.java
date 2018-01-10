/**
 * 
 */
package cps.client.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import cps.api.request.ReservedParkingRequest;
import cps.client.controller.ControllerConstants.SceneCode;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
//import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
//import jfxtras.scene.control.CalendarPicker;

/**
 * Created on: 2018-01-09 8:26:06 PM
 */
// TODO some pc displays kryakozyabry in month , handleBackButton implementation
// just for navigation
public class ReserveParkingController implements ViewController {

  private boolean processing = false;

  @FXML
  private Label infoLabel;

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
    ReservedParkingRequest request = new ReservedParkingRequest(getCustomerId(), getEmail(), getCarId(), getLotId(),
        getPlannedStartTime(), getPlannedEndTime());

    turnProcessingStateOn();

    ControllersClientAdapter.getClient().sendRequest(request);
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
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.RESERVE_PARKING);
    
    Platform.runLater( () -> infoBox.requestFocus() ); // to unfocus the Text Field
    
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

  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  private String getEmail() {
    String email = ControllersClientAdapter.getCustomerContext().getCustomerEmail();

    if (email == null) {
      email = emailTF.getText();
    }
    return email;
  }

  private String getCarId() {
    return carIDTextField.getText();
  }

  private int getLotId() {
    return Integer.parseInt(lotidTF.getText());
  }

  private LocalDateTime getPlannedStartTime() {
    return startDatePicker.getValue().atTime(getStartTime());
  }

  private LocalTime getStartTime() {
    return LocalTime.parse(startTimeTF.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
  }

  private LocalDateTime getPlannedEndTime() {
    return endDatePicker.getValue().atTime(getEndTime());
  }

  private LocalTime getEndTime() {
    return LocalTime.parse(endTimeTF.getText(), DateTimeFormatter.ISO_LOCAL_TIME);
  }

  @Override
  public void displayInfo(String infoMsg) {
    infoLabel.setText(infoMsg);
    infoLabel.getStyleClass().clear();
    infoLabel.getStyleClass().add("infoLabel");
  }

  @Override
  public void displayError(String errorMsg) {
    infoLabel.setText(errorMsg);
    infoLabel.getStyleClass().clear();
    infoLabel.getStyleClass().add("errorLabel");
  }

  @Override
  public void turnProcessingStateOn() {
    infoProgress.visibleProperty().set(true);
    processing = true;
  }

  @Override
  public void turnProcessingStateOff() {
    infoProgress.visibleProperty().set(false);
    processing = false;
  }
}
