package cps.client.alpha;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AlphaGUIController3 implements CPSViewController {

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
    Scene scene = ControllersClientAdapter.fetchScene("alphaMain");
    CPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert userIDTF != null : "fx:id=\"userIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
    assert lotIDTF != null : "fx:id=\"lotIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
    assert carIDTF != null : "fx:id=\"carIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
    assert subIDTF != null : "fx:id=\"subIDTF\" was not injected: check your FXML file 'AlphaGUI_4.fxml'.";
    ControllersClientAdapter.registerCtrl(this);
  }

  @Override
  public String getCtrlId() {
    return "alpha3";
  }
}