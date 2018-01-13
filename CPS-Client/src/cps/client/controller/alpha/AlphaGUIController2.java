package cps.client.controller.alpha;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;

import cps.api.request.IncidentalParkingRequest;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AlphaGUIController2 implements ViewController {

  Scene myScene;

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="userIdTF"
  private TextField userIdTF; // Value injected by FXMLLoader

  @FXML // fx:id="lotIDTF"
  private TextField lotIDTF; // Value injected by FXMLLoader

  @FXML // fx:id="emailTF"
  private TextField emailTF; // Value injected by FXMLLoader

  @FXML // fx:id="carIDTF"
  private TextField carIDTF; // Value injected by FXMLLoader

  @FXML // fx:id="plannedEndTimeTF"
  private TextField plannedEndTimeTF; // Value injected by FXMLLoader

  @FXML
  void submitHandler(ActionEvent event) {

    int userID = 0;
    String strUserID = userIdTF.getText();
    try {
      userID = Integer.parseInt(strUserID);
    } catch (NumberFormatException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_USERID.getMsg());
      return;
    } catch (NullPointerException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_USERID.getMsg());
      return;
    }

    String email = emailTF.getText();

    String carID = carIDTF.getText();
    if (carID == null || carID.length() < 1) {
      // displayError(ControllerConstants.InputVerification.MISSING_CARID.getMsg());
      return;
    }

    int lotID = 0;
    String strLotID = lotIDTF.getText();
    try {
      lotID = Integer.parseInt(strLotID);
    } catch (NumberFormatException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_LOTID.getMsg());
      return;
    } catch (NullPointerException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_LOTID.getMsg());
      return;
    }

    LocalDateTime date = null;
    try {
      String dateStr = plannedEndTimeTF.getText();
      date = LocalDateTime.parse(dateStr);
    } catch (DateTimeParseException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_PLANNEDENDTIME.getMsg());
      return;
    } catch (NullPointerException e) {
      // displayError(ControllerConstants.InputVerification.MISSING_PLANNEDENDTIME.getMsg());
      return;
    }

    IncidentalParkingRequest request = new IncidentalParkingRequest(userID, email, carID, lotID, date);
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  @FXML
  void backHandler(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.ALPHA_MAIN_MENU);
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert userIdTF != null : "fx:id=\"userIdTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert lotIDTF != null : "fx:id=\"lotIDTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert emailTF != null : "fx:id=\"emailTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert carIDTF != null : "fx:id=\"carIDTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert plannedEndTimeTF != null : "fx:id=\"plannedEndTimeTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    plannedEndTimeTF.setText(LocalDateTime.now().toString());
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.ALPHA_INCIDENTAL_PARKING);
  }

  @Override
  public void displayInfo(List<Text> formattedText) {
    // TODO Auto-generated method stub

  }

  public void displayError(List<Text> formettedErrorMsg) {
    Alert alert = new Alert(AlertType.ERROR);
    // alert.setContentText(formettedErrorMsg);
    alert.showAndWait();
  }

  @Override
  public void turnProcessingStateOn() {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnProcessingStateOff() {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(String simpleErrorMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnLoggedInStateOn() {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnLoggedInStateOff() {
    // TODO Auto-generated method stub

  }

  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub

  }

}
