package cps.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cps.client.controller.ControllersClientAdapter.SceneCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AlphaGUIController4 implements ViewController {
  

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="userIDTF"
  private TextField userIDTF; // Value injected by FXMLLoader

  @FXML // fx:id="lotIDTF"
  private TextField lotIDTF; // Value injected by FXMLLoader

  @FXML // fx:id="carIDTF"
  private TextField carIDTF; // Value injected by FXMLLoader

  @FXML // fx:id="subIDTF"
  private TextField subIDTF; // Value injected by FXMLLoader

  @FXML
  void submitHandler(ActionEvent event) {

  }

  @FXML
  void backHandler(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.MAIN_MENU);
  }

  @FXML // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
      assert userIDTF != null : "fx:id=\"userIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
      assert lotIDTF != null : "fx:id=\"lotIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
      assert carIDTF != null : "fx:id=\"carIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
      assert subIDTF != null : "fx:id=\"subIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
      ControllersClientAdapter.registerCtrl(this,SceneCode.REQUEST_PARKING_ENTRY);
  }
  
}
