package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Base Class for Service controllers.
 */
public abstract class ServiceStatitisticsBase extends ServiceActionControllerBase {

  /**
   * Returns company person to main menu.
   * 
   * @param event
   */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
    }
  }

  /**
   * Returns company person to main menu.
   * 
   * @param event
   */
  @FXML
  void handleCancelButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_CHOICE);
    }
  }
}
