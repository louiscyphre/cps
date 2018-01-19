package cps.client.controller;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import cps.api.response.RejectComplaintResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.RequestReportResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.Response;
import cps.api.response.ServerResponse;
import cps.api.response.ServiceLoginResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.SimpleResponse;
import cps.api.response.UpdatePricesResponse;
import cps.client.utils.InternalClientException;
import cps.client.utils.UserLevelClientException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Base class of all the Client Controllers.
 */
public abstract class ClientControllerBase implements ViewController {
  /**
   * 
   */
  protected static final String DEFAULT_INFO_LABEL = "Welcome to Car Parking System!";

  /**
   * 
   */
  @FXML // ResourceBundle that was given to the FXMLLoader
  protected ResourceBundle resources;

  /**
   * 
   */
  @FXML // URL location of the FXML file that was given to the FXMLLoader
  protected URL location;

  /**
   * 
   */
  @FXML // fx:id="infoLabel"
  protected TextFlow infoLabel; // Value injected by FXMLLoader

  /**
   * 
   */
  @FXML // fx:id="infoProgress"
  protected ProgressIndicator infoProgress; // Value injected by FXMLLoader

  /**
   * 
   */
  @FXML // fx:id="infoBox"
  protected VBox infoBox; // Value injected by FXMLLoader

  /**
   * 
   */
  @FXML
  protected BorderPane root;

  /**
   * 
   */
  protected boolean processing;

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#displayInfo(java.util.List)
   */
  @Override
  public void displayInfo(List<Text> formattedText) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formattedText) {
      infoLabel.getChildren().add(ft);
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#displayInfo(java.lang.String)
   */
  @Override
  public void displayInfo(String simpleInfoMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleInfoMsg));
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#displayError(java.util.List)
   */
  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    for (Text ft : formettedErrorMsg) {
      infoLabel.getChildren().add(ft);
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#displayError(java.lang.String)
   */
  @Override
  public void displayError(String simpleErrorMsg) {
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("errorLabel");
    infoLabel.getChildren().clear();
    infoLabel.getChildren().add(new Text(simpleErrorMsg));
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#turnProcessingStateOn()
   */
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

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#turnProcessingStateOff()
   */
  @Override
  public void turnProcessingStateOff() {
    infoProgress.visibleProperty().set(false);
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoLabel.getChildren().clear();
    processing = false;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#turnLoggedInStateOn()
   */
  @Override
  public void turnLoggedInStateOn() {
    // Does nothing for base controller
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#turnLoggedInStateOff()
   */
  @Override
  public void turnLoggedInStateOff() {
    // Does nothing for base controller
  }

  /**
   * 
   */
  protected void baseInitialize() {
    assert root != null : "fx:id=\"root\" was not injected: check your FXML file";
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file";
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    // info box clear
    infoBox.getStyleClass().clear();
    infoBox.getStyleClass().add("infoLabel");
    infoProgress.visibleProperty().set(false);
    infoLabel.getChildren().clear();
  }

  /**
   * @param request
   * @param waitForResponse
   */
  public final void sendRequest(Request request, boolean waitForResponse) {
    if (waitForResponse) {
      turnProcessingStateOn();
    }

    ControllersClientAdapter.getClient().sendRequest(request);
  }

  /**
   * @param request
   */
  public final void sendRequest(Request request) {
    sendRequest(request, true);
  }

  /**
   * @param response
   * @return
   */
  public ServerResponse handleGenericResponse(ServerResponse response) {

    if (response.success()) {
      turnProcessingStateOff();
      displayInfo(response.getDescription());
    } else {
      turnProcessingStateOff();
      displayError(response.getDescription());
    }

    return response;
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ViewController#getRoot()
   */
  @Override
  public BorderPane getRoot() {
    return root;
  }

  // ResponseHandler interface
  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#dispatch(cps.api.response.Response)
   */
  @Override
  public ServerResponse dispatch(Response response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.CancelOnetimeParkingResponse)
   */
  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ComplaintResponse)
   */
  @Override
  public ServerResponse handle(ComplaintResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.FullSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.IncidentalParkingResponse)
   */
  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListComplaintsResponse)
   */
  @Override
  public ServerResponse handle(ListComplaintsResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListMyComplaintsResponse)
   */
  @Override
  public ServerResponse handle(ListMyComplaintsResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListOnetimeEntriesResponse)
   */
  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ListParkingLotsResponse)
   */
  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ParkingEntryResponse)
   */
  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ParkingExitResponse)
   */
  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RegularSubscriptionResponse)
   */
  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ReservedParkingResponse)
   */
  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.LoginResponse)
   */
  @Override
  public ServerResponse handle(LoginResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.DisableParkingSlotsResponse)
   */
  @Override
  public ServerResponse handle(DisableParkingSlotsResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.InitLotResponse)
   */
  @Override
  public ServerResponse handle(InitLotResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RefundResponse)
   */
  @Override
  public ServerResponse handle(RefundResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RequestReportResponse)
   */
  @Override
  public ServerResponse handle(RequestReportResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ReserveParkingSlotsResponse)
   */
  @Override
  public ServerResponse handle(ReserveParkingSlotsResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.SetFullLotResponse)
   */
  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.UpdatePricesResponse)
   */
  @Override
  public ServerResponse handle(UpdatePricesResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RequestLotStateResponse)
   */
  @Override
  public ServerResponse handle(RequestLotStateResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.ServiceLoginResponse)
   */
  @Override
  public ServerResponse handle(ServiceLoginResponse response) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.SimpleResponse)
   */
  @Override
  public ServerResponse handle(SimpleResponse simpleResponse) {
    return null;
  }

  /* (non-Javadoc)
   * @see cps.api.response.ResponseHandler#handle(cps.api.response.RejectComplaintResponse)
   */
  @Override
  public ServerResponse handle(RejectComplaintResponse response) {
    return null;
  }

  // Helper methods
  /**
   * @param field
   * @return
   */
  public String getText(TextInputControl field) {
    return field == null ? "" : field.getText();
  }

  /**
   * @param value
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public String requireField(String value, String parameterName) throws UserLevelClientException {
    if (value == null || value.trim().length() == 0) {
      throw new UserLevelClientException(String.format("%s is missing", parameterName));
    }

    return value;
  }

  /**
   * @param field
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public String requireField(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireField(getText(field), parameterName);
  }

  /**
   * @param value
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public String requireFieldTrim(String value, String parameterName) throws UserLevelClientException {
    if (value == null || value.trim().length() == 0) {
      throw new UserLevelClientException(String.format("%s is missing", parameterName));
    }

    return value.trim();
  }

  /**
   * @param field
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public String requireFieldTrim(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireFieldTrim(getText(field), parameterName);
  }

  /**
   * @param value
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public int requireInteger(String value, String parameterName) throws UserLevelClientException {
    try {
      return Integer.parseInt(requireFieldTrim(value, parameterName));
    } catch (NumberFormatException e) {
      throw new UserLevelClientException(String.format("%s should be an integer", parameterName));
    }
  }

  /**
   * @param field
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public int requireInteger(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireInteger(getText(field), parameterName);
  }

  /**
   * @param value
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public float requireFloat(String value, String parameterName) throws UserLevelClientException {
    try {
      return Float.parseFloat(requireFieldTrim(value, parameterName));
    } catch (NumberFormatException e) {
      throw new UserLevelClientException(String.format("%s should be a number", parameterName));
    }
  }

  /**
   * @param field
   * @param parameterName
   * @return
   * @throws UserLevelClientException
   */
  public float requireFloat(TextInputControl field, String parameterName) throws UserLevelClientException {
    return requireFloat(getText(field), parameterName);
  }

  /**
   * @param object
   * @param name
   * @return
   * @throws InternalClientException
   */
  public <T> T notNull(T object, String name) throws InternalClientException {
    if (object == null) {
      throw new InternalClientException(String.format("%s must not be null", name));
    }
    return object;
  }

  /**
   * @param condition
   * @param errorMessage
   * @throws UserLevelClientException
   */
  public void errorIf(boolean condition, String errorMessage) throws UserLevelClientException {
    if (condition) {
      throw new UserLevelClientException(errorMessage);
    }
  }

  /**
   * @param object
   * @param errorMessage
   * @throws UserLevelClientException
   */
  public void errorIfNull(Object object, String errorMessage) throws UserLevelClientException {
    errorIf(object == null, errorMessage);
  }

  /**
   * @param message
   */
  public void showAlert(String message) {
    new Alert(AlertType.INFORMATION, message).show();
  }

  /**
   * @param text
   * @return
   */
  protected String fixTime(String text) {
    String[] parts = text.split(":");
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < parts.length; i++) {
      if (parts[i].length() < 2) {
        parts[i] = "0" + parts[i];
      }

      result.append(parts[i]);

      if (i < parts.length - 1) {
        result.append(":");
      }
    }

    return result.toString();
  }

  /**
   * @param field
   * @param parameterName
   * @return
   */
  protected LocalTime getTime(TextInputControl field, String parameterName) {
    try {
      String text = fixTime(requireFieldTrim(field, parameterName));
      return LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME);
    } catch (Exception e) {
      return null;
    }
  }

}
