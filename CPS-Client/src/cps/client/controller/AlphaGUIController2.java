package cps.client.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import cps.api.request.IncidentalParkingRequest;
import cps.client.controller.ControllersClientAdapter.SceneCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class AlphaGUIController2 implements CPSViewController {

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
      displayError(InputVerification.MISSING_USERID.msg);
      return;
    } catch (NullPointerException e) {
      displayError(InputVerification.MISSING_USERID.msg);
      return;
    }

    String email = emailTF.getText();

    String carID = carIDTF.getText();
    if (carID == null || carID.length() < 1) {
      displayError(InputVerification.MISSING_CARID.msg);
      return;
    }

    int lotID = 0;
    String strLotID = lotIDTF.getText();
    try {
      lotID = Integer.parseInt(strLotID);
    } catch (NumberFormatException e) {
      displayError(InputVerification.MISSING_LOTID.msg);
      return;
    } catch (NullPointerException e) {
      displayError(InputVerification.MISSING_LOTID.msg);
      return;
    }

    LocalDateTime date = null;
    try {
      String dateStr = plannedEndTimeTF.getText();
      date = LocalDateTime.parse(dateStr);
    } catch (DateTimeParseException e) {
      displayError(InputVerification.MISSING_PLANNEDENDTIME.msg);
      return;
    } catch (NullPointerException e) {
      displayError(InputVerification.MISSING_PLANNEDENDTIME.msg);
      return;
    }

    IncidentalParkingRequest request = new IncidentalParkingRequest(userID, email, carID, lotID, date);
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  private void displayError(String errorMsg) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setContentText(errorMsg);
    alert.showAndWait();
  }


  public enum InputVerification {
    INPUT_OK(0, "The request is valid"), MISSING_USERID(0, "Missing or bad UserID"), MISSING_EMAIL(1,
        "Missing or bad Email"), MISSING_CARID(3, "Missing or bad CarID"), MISSING_LOTID(4,
            "Missing or bad LotID"), MISSING_PLANNEDENDTIME(5, "Missing or bad Planned End Time");

    private final int    id;
    private final String msg;

    InputVerification(int id, String msg) {
      this.id = id;
      this.msg = msg;
    }

    public int getId() {
      return this.id;
    }

    public String getMsg() {
      return this.msg;
    }
  }

  @FXML
  void backHandler(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.MAIN_MENU);
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
    ControllersClientAdapter.registerCtrl(this,SceneCode.INCIDENTAL_PARKING);
  }

}
