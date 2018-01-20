package cps.client.controller.service;

import cps.client.context.EmployeeContext;
import cps.client.controller.ClientControllerBase;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Base Class for Service controllers.
 */
public abstract class ServiceActionControllerBase extends ClientControllerBase {

  /**
   * Returns company person to main menu.
   * 
   * @param event
   */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
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
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
  }

  /**
   * Submit button handling.
   * 
   * @param event
   */
  @FXML
  void handleFinishButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
  }

  /**
   * @param event
   */
  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  /**
   * Submit button handling.
   * 
   * @param event
   */
  @FXML
  void handleOkButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  /**
   * Validate and send function, validates that input is valid and sends server
   * corresponding API request.
   */
  void validateAndSend() {

  }

  /**
   * @return employee context
   * @throws InternalClientException if employee context is not defined.
   */
  EmployeeContext requireEmployeeContext() throws InternalClientException {
    return notNull(ControllersClientAdapter.getEmployeeContext(), "Emlpoyee Context");
  }

  /**
   * @return company person
   * @throws InternalClientException if company person is not defined
   */
  CompanyPerson requireLoggedInUser() throws InternalClientException {
    return requireEmployeeContext().requireCompanyPerson();
  }
}
