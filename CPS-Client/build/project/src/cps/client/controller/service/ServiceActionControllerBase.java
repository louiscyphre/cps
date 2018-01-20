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
 * @author firl
 *
 */
public abstract class ServiceActionControllerBase extends ClientControllerBase {

  /**
   * @param event
   */
  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
  }

  /**
   * @param event
   */
  @FXML
  void handleCancelButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
  }

  /**
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
   * @param event
   */
  @FXML
  void handleOkButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  /**
   * 
   */
  void validateAndSend() {

  }

  /**
   * @return
   * @throws InternalClientException
   */
  EmployeeContext requireEmployeeContext() throws InternalClientException {
    return notNull(ControllersClientAdapter.getEmployeeContext(), "Emlpoyee Context");
  }

  /**
   * @return
   * @throws InternalClientException
   */
  CompanyPerson requireLoggedInUser() throws InternalClientException {
    return requireEmployeeContext().requireCompanyPerson();
  }
}
