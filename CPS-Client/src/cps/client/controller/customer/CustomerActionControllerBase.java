package cps.client.controller.customer;

import cps.api.response.CurrentPerformanceResponse;
import cps.api.response.PeriodicReportResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.ServerResponse;
import cps.api.response.WeeklyReportResponse;
import cps.client.controller.ClientControllerBase;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Base Class for Customer controllers, both Web and Kiosk.
 */
public class CustomerActionControllerBase extends ClientControllerBase {

  /**
   * Returns customer to main menu.
   * @param event
   */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU, 10);
    }
  }

  /**
   * Returns customer to main menu.
   * @param event
   */
  @FXML
  void handleCancelButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.CUSTOMER_INITIAL_MENU, 10);
    }
  }

  /**
   * Submit button handling.
   * @param event
   */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (!processing) {
      sendMainRequest();
    }
  }

  /**
   * Ok button handling.
   * @param event
   */
  @FXML
  void handleOkButton(ActionEvent event) {
    if (!processing) {
      sendMainRequest();
    }
  }

  /**
   * Stub for a main request function.
   */
  void sendMainRequest() {

  }

  @Override
  public ServerResponse handle(WeeklyReportResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(QuarterlyReportResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(CurrentPerformanceResponse response) {
    return null;
  }

  @Override
  public ServerResponse handle(PeriodicReportResponse response) {
    return null;
  }
}
