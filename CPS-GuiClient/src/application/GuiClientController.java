/**
 * Sample Skeleton for "MainScene.fxml" Controller Class
 * You can copy and paste this code into your favorite IDE
 **/

package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GuiClientController {
  Logger logger;

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML 
  private Button enterParkingButton; // Value injected by FXMLLoader

  @FXML 
  private Button incidentalParkingButton; // Value injected by FXMLLoader
  
  @FXML 
  private Button goBackButton; // Value injected by FXMLLoader

  @FXML
  void switchToEnterParkingScene(ActionEvent event) throws IOException {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("view/OnEnterParkingScene.fxml"));
      Stage stage = (Stage) enterParkingButton.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      logger.severe("File view/OnEnterParkingScene.fxml doesn't exist");
    }
  }

  @FXML
  void switchToIncidentalParkingScene(ActionEvent event) throws IOException {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("view/IncidentalParkingScene.fxml"));
      Stage stage = (Stage) incidentalParkingButton.getScene().getWindow();
      Scene scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      logger.severe("File view/IncidentalParkingScene.fxml doesn't exist");
    }
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert enterParkingButton != null : "fx:id=\"enterParkingButton\" was not injected: check your FXML file 'MainScene.fxml'.";
    assert incidentalParkingButton != null: "fx:id=\"incidentalParkingButton\" was not injected: check your FXML file 'OnParkingEnterScene.fxml'.";
    assert goBackButton != null: "fx:id=\"goBackButton\" was not injected: check your FXML file 'OnParkingEnterScene.fxml'.";
    // Initialize your logic here: all @FXML variables will have been injected

  }

}
