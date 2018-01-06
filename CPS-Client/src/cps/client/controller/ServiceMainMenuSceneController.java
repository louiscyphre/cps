/**
 * Sample Skeleton for 'ServiceMainMenu.fxml' Controller Class
 */

package cps.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ServiceMainMenuSceneController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="managerLoginBtn"
    private Button managerLoginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="customerServiceLoginBtn"
    private Button customerServiceLoginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="x12"
    private Font x12; // Value injected by FXMLLoader

    @FXML // fx:id="x1"
    private Font x1; // Value injected by FXMLLoader

    @FXML // fx:id="x11"
    private Font x11; // Value injected by FXMLLoader

    @FXML // fx:id="employeeLoginBtn"
    private Button employeeLoginBtn; // Value injected by FXMLLoader

    @FXML // fx:id="globalManagerLoginBtn"
    private Button globalManagerLoginBtn; // Value injected by FXMLLoader

    @FXML
    void loginAsEmployee(ActionEvent event) {

    }

    @FXML
    void loginAsManager(ActionEvent event) {

    }

    @FXML
    void loginAsGlobalManager(ActionEvent event) {

    }

    @FXML
    void loginAsCustomerService(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert managerLoginBtn != null : "fx:id=\"managerLoginBtn\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
        assert customerServiceLoginBtn != null : "fx:id=\"customerServiceLoginBtn\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
        assert x12 != null : "fx:id=\"x12\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
        assert x11 != null : "fx:id=\"x11\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
        assert employeeLoginBtn != null : "fx:id=\"employeeLoginBtn\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
        assert globalManagerLoginBtn != null : "fx:id=\"globalManagerLoginBtn\" was not injected: check your FXML file 'ServiceMainMenu.fxml'.";
    }
    
    
}
