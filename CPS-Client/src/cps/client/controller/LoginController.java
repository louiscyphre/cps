/**
 * 
 */
package cps.client.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import cps.api.request.IncidentalParkingRequest;
import cps.client.controller.AlphaGUIController2.InputVerification;
import cps.client.controller.ControllersClientAdapter.SceneCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created on: 2018-01-06 6:43:00 PM
 */
public class LoginController implements ViewController {

  @FXML // fx:id="emailTextField"
  private TextField emailTextField; // Value injected by FXMLLoader

  @FXML // fx:id="passwordTextField"
  private TextField passwordTextField; // Value injected by FXMLLoader


  public static boolean isValidEmailAddress(String email) {
    boolean result = true;
    try {
      InternetAddress emailAddr = new InternetAddress(email);
      emailAddr.validate();
    } catch (AddressException ex) {
      result = false;
    }
    return result;
  }
  
  public enum InputValidation {
    BAD_EMAIL(0, "Missing or bad Email"), WRONG_DATA(1, "Bad email or password, please try again");

    private final int    id;
    private final String msg;

    InputValidation(int id, String msg) {
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
  void submitHandler(ActionEvent event) {

    String email = emailTextField.getText();
    if (!isValidEmailAddress(email)) {
      //displayError(InputValidation.BAD_EMAIL.getMsg());
      return;
    }
    String password = emailTextField.getText();


    //LoginRequest request = new LoginRequest(email, password);
    //ControllersClientAdapter.getClient().sendRequest(request);
  }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.Notification#displayInfo(java.lang.String)
   */
 // @Override
 // public void displayInfo(String infoMsg) {
    // TODO Auto-generated method stub

 // }

  /*
   * (non-Javadoc)
   * @see cps.client.controller.Notification#displayError(java.lang.String)
   */
 // @Override
 // public void displayError(String errorMsg) {
    // TODO Auto-generated method stub

 // }

}
