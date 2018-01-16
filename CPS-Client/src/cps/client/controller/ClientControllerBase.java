package cps.client.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cps.api.request.Request;
import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.ListMyComplaintsResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.LoginResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.RequestReportResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.Response;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;
import cps.client.utils.InternalClientException;
import cps.client.utils.UserLevelClientException;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public abstract class ClientControllerBase implements ViewController {
  protected static final String DEFAULT_INFO_LABEL = "Welcome to Car Parking System!";

  @FXML // ResourceBundle that was given to the FXMLLoader
  protected ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  protected URL location;

  @FXML // fx:id="infoLabel"
  protected TextFlow infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  protected ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  protected VBox infoBox; // Value injected by FXMLLoader

  protected boolean processing;

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
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
    }
    infoProgress.visibleProperty().set(false);
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    processing = false;
  }

  @Override
  public void turnLoggedInStateOn() {
    // Does nothing for base controller
  }

  @Override
  public void turnLoggedInStateOff() {
    // Does nothing for base controller
  }

  protected void baseInitialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceActionReserveSlot.fxml'.";
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text Field
  }

  @Override
  public void cleanCtrl() {
    // info box clear
    infoBox.getStyleClass().add("infoLabel");
    infoProgress.visibleProperty().set(false);
    infoLabel.getChildren().clear();
  }
  
  public final void sendRequest(Request request, boolean waitForResponse) {
    if (waitForResponse) {
      turnProcessingStateOn();
    }
    
    ControllersClientAdapter.getClient().sendRequest(request);
  }
  
  public final void sendRequest(Request request) {
    sendRequest(request, true);
  }

  public ServerResponse handleGenericResponse(ServerResponse response) {
    
    if (response.success()) {
      turnProcessingStateOff();
      displayInfo(response.getDescription());
    } else {
      turnProcessingStateOff();
      displayError(response.getDescription());
    }
    
    return null;
  }

  // ResponseHandler interface
  @Override
  public ServerResponse dispatch(Response response) {
    return null;
  }

  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ComplaintResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ListMyComplaintsResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(LoginResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ServerResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(InitLotResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(RefundResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(RequestReportResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(ServiceLoginResponse response) {
    return null;
  }

  // Helper methods
  public String getText(TextInputControl field) {
    return field == null ? "" : field.getText();
  }

  public String requireField(String value, String parameterName) throws UserLevelClientException {
    if (value == null || value.trim().length() == 0) {
      throw new UserLevelClientException(String.format("%s is missing", parameterName));
    }

    return value;
  }

  public String requireField(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireField(getText(field), parameterName);
  }

  public String requireFieldTrim(String value, String parameterName) throws UserLevelClientException {
    if (value == null || value.trim().length() == 0) {
      throw new UserLevelClientException(String.format("%s is missing", parameterName));
    }

    return value.trim();
  }

  public String requireFieldTrim(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireFieldTrim(getText(field), parameterName);
  }

  public int requireInteger(String value, String parameterName) throws UserLevelClientException {
    try {
      return Integer.parseInt(requireFieldTrim(value, parameterName));
    } catch (NumberFormatException e) {
      throw new UserLevelClientException(String.format("%s should be an integer", parameterName));
    }
  }

  public int requireInteger(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireInteger(getText(field), parameterName);
  }

  public float requireFloat(String value, String parameterName) throws UserLevelClientException {
    try {
      return Float.parseFloat(requireFieldTrim(value, parameterName));
    } catch (NumberFormatException e) {
      throw new UserLevelClientException(String.format("%s should be a number", parameterName));
    }
  }

  public float requireFloat(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireFloat(getText(field), parameterName);
  }
  
  public <T> T notNull(T object, String name) throws InternalClientException {
    if (object == null) {
      throw new InternalClientException(String.format("%s must not be null", name));
    }
    return object;
  }
  
  public void errorIf(boolean condition, String errorMessage) throws UserLevelClientException {
    if (condition) {
      throw new UserLevelClientException(errorMessage);
    }
  }
  
  public void errorIfNull(Object object, String errorMessage) throws UserLevelClientException {
    errorIf(object == null, errorMessage);
  }
  
  public void showAlert(String message) {
    new Alert(AlertType.INFORMATION, message).show();
  }

}
