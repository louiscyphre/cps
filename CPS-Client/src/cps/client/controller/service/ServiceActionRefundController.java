package cps.client.controller.service;

import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.controller.ControllerConstants.SceneCode;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// TODO verify with server side how the refund should behave
public class ServiceActionRefundController implements ViewController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="infoLabel"
  private Label infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="complaintIdTF"
  private TextField complaintIdTF; // Value injected by FXMLLoader

  @FXML // fx:id="infoBox"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML // fx:id="refundAmountTF"
  private TextField refundAmountTF; // Value injected by FXMLLoader

  @FXML
  void handleCancelButton(ActionEvent event) {

  }

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert complaintIdTF != null : "fx:id=\"complaintIdTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert refundAmountTF != null : "fx:id=\"refundAmountTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert refundAmountTF != null : "fx:id=\"refundAmountTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_REFUND);
  }
}
