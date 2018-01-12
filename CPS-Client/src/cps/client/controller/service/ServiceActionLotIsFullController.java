package cps.client.controller.service;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ViewController;
import cps.client.controller.ControllerConstants.SceneCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

//TODO stub - requires verification regarding how the lot is full works on server-side
public class ServiceActionLotIsFullController implements ViewController{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="infoLabel"
    private Label infoLabel; // Value injected by FXMLLoader

    @FXML // fx:id="infoProgress"
    private ProgressIndicator infoProgress; // Value injected by FXMLLoader

    @FXML // fx:id="infoBox"
    private VBox infoBox; // Value injected by FXMLLoader

    @FXML // fx:id="lotId"
    private TextField lotId; // Value injected by FXMLLoader

    @FXML
    void handleCancelButton(ActionEvent event) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
    }

    @FXML
    void handleOkButton(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
        assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
        assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
        assert lotId != null : "fx:id=\"lotId\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
        ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_LOT_IS_FULL);
    }

    @Override
    public void displayInfo(List<Text> formattedText) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public void displayError(List<Text> formettedErrorMsg) {
      // TODO Auto-generated method stub
      
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
}
