package cps.client.controller.customer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import cps.client.controller.ClientControllerBase;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.entities.models.ParkingLot;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/** Base Class for Customer controllers, both Web and Kiosk. */
public class CustomerActionControllerBase extends ClientControllerBase {

  private DecimalFormat decimalFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

  /** Instantiates a new customer action controller base. */
  public CustomerActionControllerBase() {
    decimalFormat.setMaximumFractionDigits(3);
  }
  
  String decimal(float value) {
    return decimalFormat.format(value);
  }

  /** Returns customer to main menu.
   * @param event */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU);
    }
  }

  /** Returns customer to main menu.
   * @param event */
  @FXML
  void handleCancelButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU);
    }
  }

  /** Submit button handling.
   * @param event */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (!processing) {
      sendMainRequest();
    }
  }

  /** Ok button handling.
   * @param event */
  @FXML
  void handleOkButton(ActionEvent event) {
    if (!processing) {
      sendMainRequest();
    }
  }

  /** Stub for a main request function. */
  void sendMainRequest() {

  }

  void addAlternativeLots(List<Text> formattedMessage, Collection<ParkingLot> alternativeLots) {
    if (alternativeLots != null) {
      for (ParkingLot lot : alternativeLots) {
        formattedMessage.add(new Text(lot.getStreetAddress()));
      }
    }
  }
}
