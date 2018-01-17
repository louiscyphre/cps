/**
 * 
 */
package cps.client.controller.customer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import cps.api.request.ListParkingLotsRequest;
import cps.api.request.ReservedParkingRequest;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.client.utils.FormatValidation.InputFormats;
import cps.entities.models.ParkingLot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

/**
 * Created on: 2018-01-09 8:26:06 PM
 */
// TODO some pc displays kryakozyabry in month
public class ReserveParkingController extends CustomerActionControllerBase implements ParkingLotsController {

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private TextField startTimeTF;

  @FXML
  private TextField endTimeTF;

  @FXML
  private Font x1;

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
  private ComboBox<String> parkingLotsList;

  private HashMap<String, Integer> parkingLotsMap = null;

  // show Email field
  public void showEmail() {
    emailTF.visibleProperty().set(true);
  }

  //
  public void hideEmail() {
    emailTF.visibleProperty().set(false);
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

  void loadParkingLots() {
    if (processing) {
      return;
    }
    if (parkingLotsMap.isEmpty() && parkingLotsList.getItems().isEmpty()) {
      ListParkingLotsRequest request = new ListParkingLotsRequest();
      turnProcessingStateOn();
      ControllersClientAdapter.getClient().sendRequest(request);
    }
  }

  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    LinkedList<String> tmp = new LinkedList<String>();
    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      parkingLotsMap.put(address, i.getId());
      tmp.add(address);
    }
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    parkingLotsList.getItems().setAll(addresses);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert emailTF != null : "fx:id=\"emailTF\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    startDatePicker.setOnShowing(e -> Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH));
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.RESERVE_PARKING);

    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field

    parkingLotsMap = new HashMap<String, Integer>();
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
    // datepicker start date + time validation
    LocalDateTime plannedStartTime = getPlannedStartTime();
    if (plannedStartTime == null) {
      displayError("Invalid start date");
      return;
    }
    // datepicker end date + time validation
    LocalDateTime plannedEndTime = getPlannedEndTime();
    if (plannedEndTime == null) {
      displayError("Invalid leave date");
      return;
    }
    // compare exit time to entry time
    if (plannedStartTime.compareTo(plannedEndTime) >= 0) {
      displayError("Leave date has to be after entry date");
      return;
    }

    // TODO replace the lotid handling from the list instead
    int lotId = getLotId();
    if (lotId <= 0) {
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
    CustomerContext cntx = ControllersClientAdapter.getCustomerContext();
    if (cntx.isLoggedIn()) {
      return cntx.getCustomerEmail();
    } else {
      return emailTF.getText().trim();
    }
  }

  // return car id or null if empty
  private String getCarId() {
    return carIDTextField.getText().trim();
  }

  // returns lot id or -1 if empty
  private int getLotId() {
    if (parkingLotsList == null || parkingLotsList.valueProperty() == null
        || parkingLotsList.valueProperty().getValue() == null) {
      return 0;
    }
    int lotId = parkingLotsMap.get(parkingLotsList.valueProperty().getValue());
    return lotId;
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
    return getTime(startTimeTF, "Start time");
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
    return getTime(endTimeTF, "End time");
  }

  @Override
  public void turnLoggedInStateOn() {
    super.turnLoggedInStateOn();
    emailTF.setVisible(false);
  }

  @Override
  public void turnLoggedInStateOff() {
    super.turnLoggedInStateOn();
    emailTF.setVisible(true);
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // input fields clear
    startDatePicker.getEditor().clear();
    startTimeTF.clear();
    endDatePicker.getEditor().clear();
    endTimeTF.clear();
    carIDTextField.clear();
    emailTF.clear();
    parkingLotsList.getItems().clear();
    parkingLotsMap.clear();
    loadParkingLots();
  }
}
