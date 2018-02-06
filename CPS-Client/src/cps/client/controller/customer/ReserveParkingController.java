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
import java.util.List;

import cps.api.request.ListParkingLotsRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.client.controller.ViewController;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/** Reserve Parking scene controller. */
public class ReserveParkingController extends CustomerActionControllerBaseSubmitAndFinish implements ParkingLotsController {

  /** End Date DatePicker */
  @FXML
  private DatePicker endDatePicker;

  /** Start Date DatePicker */
  @FXML
  private DatePicker startDatePicker;

  /** Start Time TextField */
  @FXML
  private TextField startTimeTF;

  /** End Time TextField */
  @FXML
  private TextField endTimeTF;

  /** Insets */
  @FXML
  private Insets x2;

  /** Car ID TextField */
  @FXML
  private TextField carIDTextField;

  /** Email TextField */
  @FXML
  private TextField emailTF;

  /** Parking Lots list */
  @FXML
  private ComboBox<String> parkingLotsList;

  /** Parking Lots Mapping */
  private HashMap<String, Integer> parkingLotsMap = null;

  /** Turns email TextField visible */
  public void showEmail() {
    emailTF.visibleProperty().set(true);
  }

  /** Turns email TextField invisible */
  public void hideEmail() {
    emailTF.visibleProperty().set(false);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#handleSubmitButton(javafx.event.ActionEvent) */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (processing) {
      return;
    }
    validateAndSend();
  }

  /** @param event */
  @FXML
  void handlePickStartDate(ActionEvent event) {
    if (processing) {
      return;
    }

  }

  /** @param event */
  @FXML
  void handlePickEndDate(ActionEvent event) {
    if (processing) {
      return;
    }
  }

  /** Request from the server the updated list of lots. */
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

  /* (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.Collection) */
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

  /** Initializes the Controller and Registers it. */
  @FXML
  void initialize() {
    super.baseInitialize();
    assert endDatePicker != null : "fx:id=\"endDatePicker\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert emailTF != null : "fx:id=\"emailTF\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert startDatePicker != null : "fx:id=\"startDatePicker\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";
    assert carIDTextField != null : "fx:id=\"carIDTextField\" was not injected: check your FXML file 'ReserveParkingScene.fxml'.";

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.RESERVE_PARKING);

    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field

    parkingLotsMap = new HashMap<String, Integer>();
  }

  /**
   * 
   */
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
    // DatePicker start date + time validation
    LocalDateTime plannedStartDateTime = getPlannedStartDateTime();
    if (plannedStartDateTime == null) {
      displayError("Invalid start date");
      return;
    }
    // DatePicker end date + time validation
    LocalDateTime plannedEndDateTime = getPlannedEndDateTime();
    if (plannedEndDateTime == null) {
      displayError("Invalid leave date");
      return;
    }
    // compare exit time to entry time
    if (plannedStartDateTime.compareTo(plannedEndDateTime) >= 0) {
      displayError("Leave date has to be after or equal to entry date");
      return;
    }

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

    ReservedParkingRequest request = new ReservedParkingRequest(customerId, email, carId, lotId, plannedStartDateTime, plannedEndDateTime);
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  // returns customer context - >=1 if logged in, 0 otherwise
  /** @return */
  private int getCustomerId() {
    int id = ControllersClientAdapter.getCustomerContext().getCustomerId();
    return id;
  }

  // returns email if logged in from customer context,
  /** @return */
  private String getEmail() {
    CustomerContext cntx = ControllersClientAdapter.getCustomerContext();
    if (cntx.isLoggedIn()) {
      return cntx.getCustomerEmail();
    } else {
      return emailTF.getText().trim();
    }
  }

  // return car id or null if empty
  /** @return */
  private String getCarId() {
    return carIDTextField.getText().trim();
  }

  // returns lot id or -1 if empty
  /** @return */
  private int getLotId() {
    if (parkingLotsList == null || parkingLotsList.valueProperty() == null || parkingLotsList.valueProperty().getValue() == null) {
      return 0;
    }
    int lotId = parkingLotsMap.get(parkingLotsList.valueProperty().getValue());
    return lotId;
  }

  // returns planned start date or null if empty
  /** @return */
  private LocalDateTime getPlannedStartDateTime() {
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
  /** @return */
  private LocalTime getStartTime() {
    return getTime(startTimeTF, "Start time");
  }

  // returns planned end date or null if empty
  /** @return */
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
  /** @return */
  private LocalTime getEndTime() {
    return getTime(endTimeTF, "End time");
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOn() */
  @Override
  public void turnLoggedInStateOn() {
    super.turnLoggedInStateOn();
    emailTF.setVisible(false);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#turnLoggedInStateOff() */
  @Override
  public void turnLoggedInStateOff() {
    super.turnLoggedInStateOn();
    emailTF.setVisible(true);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#cleanCtrl() */
  @Override
  public void cleanCtrl() {
    // info box clear
    super.cleanCtrl();
    // input fields clear
    startTimeTF.clear();
    endTimeTF.clear();
    carIDTextField.clear();
    emailTF.clear();
    parkingLotsList.getItems().clear();
    parkingLotsMap.clear();
    loadParkingLots();
  }

  /** Display the parking details if request was successful, and user
   * credentials if new user, otherwise - error message from the server. */
  @Override
  public void handle(ReservedParkingResponse response) {
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
      formattedMessage.add(new Text("Succesfully reserved parking per request!\n"));
      formattedMessage.add(new Text(String.format("Your account was debited %s ILS.", response.getPayment())));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
      setFinishInsteadOfSubmit(true);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("Could not reserve parking!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      addAlternativeLots(formattedMessage, response.getAlternativeLots());
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }

  }
}
