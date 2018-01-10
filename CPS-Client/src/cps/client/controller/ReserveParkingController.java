/**
 * 
 */
package cps.client.controller;

//import java.time.LocalDate;
//import java.util.Calendar;
//import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
//import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
//import jfxtras.scene.control.CalendarPicker;

/**
 * Created on: 2018-01-09 8:26:06 PM 
 */
public class ReserveParkingController implements ViewController {


  @FXML
  private Label infoLabel;

  @FXML
  private DatePicker endDatePicker;

  @FXML
  private ProgressIndicator infoProgress;

  @FXML
  private DatePicker startDatePicker;

  @FXML
  private Font x1;

  @FXML
  private VBox infoBox;

  @FXML
  private Insets x2;

  @FXML
  private Insets x3;

  @FXML
  private Insets x4;

  @FXML
  private TextField carIDTextField;

    @FXML
    void handleCancelButton(ActionEvent event) {

    }

    @FXML
    void handleSubmitButton(ActionEvent event) {

    }
    
    @FXML
    void handlePickStartDate(ActionEvent event) {

    }

    @FXML
    void handlePickEndDate(ActionEvent event) {

    }
    
    @FXML
    void initialize() {
      assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'LoginScene.fxml'.";
      assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'LoginScene.fxml'.";
      assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'LoginScene.fxml'.";

      ControllersClientAdapter.registerCtrl(this,ControllerConstants.SceneCode.RESERVE_PARKING);

      //startDatePicker = new DateTimePicker();
      //startDatePicker.withCalendar(Calendar.getInstance());
      //startDatePicker.withShowTime(Boolean.TRUE);
      //startDatePicker.withLocale(Locale.ENGLISH);
      //startDatePicker.calendarProperty().addListener(new ChangeListener<Calendar>() {

      //    @Override
       //   public void changed(ObservableValue<? extends Calendar> ov, Calendar t, Calendar t1) {
       //       System.out.println("Selected date: "+t1.getTime().toString());
       //   }
     // });

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
