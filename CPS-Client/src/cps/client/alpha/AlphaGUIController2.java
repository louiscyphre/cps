package cps.client.alpha;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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

  }

  @FXML
  void backHandler(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert userIdTF != null : "fx:id=\"userIdTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert lotIDTF != null : "fx:id=\"lotIDTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert emailTF != null : "fx:id=\"emailTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert carIDTF != null : "fx:id=\"carIDTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    assert plannedEndTimeTF != null : "fx:id=\"plannedEndTimeTF\" was not injected: check your FXML file 'AlphaGUI_2.fxml'.";
    ControllersClientAdapter.registerCtrl(this);
  }

//    primaryStage.setTitle("Alpha Client - Incidental Parking");

  @Override
  public String getCtrlId() {
    return "alpha2";
  }
}
