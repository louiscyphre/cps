package cps.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import cps.client.controller.ControllerConstants.SceneCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AlphaGUIController5 implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="streetAddressTF"
  private TextField streetAddressTF; // Value injected by FXMLLoader

  @FXML // fx:id="priceForReservedParkingRequestTF"
  private TextField priceForReservedParkingRequestTF; // Value injected by
                                                      // FXMLLoader

  @FXML // fx:id="carsPerRowTF"
  private TextField carsPerRowTF; // Value injected by FXMLLoader

  @FXML // fx:id="priceForIncidentalParkingRequestTF"
  private TextField priceForIncidentalParkingRequestTF; // Value injected by
                                                        // FXMLLoader

  @FXML // fx:id="robotIPTF"
  private TextField robotIPTF; // Value injected by FXMLLoader

  @FXML
  void submitHandler(ActionEvent event) {

  }

  @FXML
  void backHandler(ActionEvent event) {
    ControllersClientAdapter.setStage(ControllerConstants.SceneCode.MAIN_MENU);
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert streetAddressTF != null : "fx:id=\"streetAddressTF\" was not injected: check your FXML file 'AlphaGUI_5.fxml'.";
    assert priceForReservedParkingRequestTF != null : "fx:id=\"priceForReservedParkingRequestTF\" was not injected: check your FXML file 'AlphaGUI_5.fxml'.";
    assert carsPerRowTF != null : "fx:id=\"carsPerRowTF\" was not injected: check your FXML file 'AlphaGUI_5.fxml'.";
    assert priceForIncidentalParkingRequestTF != null : "fx:id=\"priceForIncidentalParkingRequestTF\" was not injected: check your FXML file 'AlphaGUI_5.fxml'.";
    assert robotIPTF != null : "fx:id=\"robotIPTF\" was not injected: check your FXML file 'AlphaGUI_5.fxml'.";
    ControllersClientAdapter.registerCtrl(this,ControllerConstants.SceneCode.INIT_PARKING_LOT);
  }

  @Override
  public void displayInfo(String infoMsg) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void displayError(String errorMsg) {
    // TODO Auto-generated method stub
    
  }

}
