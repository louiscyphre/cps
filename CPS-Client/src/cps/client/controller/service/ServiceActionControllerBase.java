package cps.client.controller.service;

import cps.client.context.EmployeeContext;
import cps.client.controller.ClientControllerBase;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.utils.InternalClientException;
import cps.entities.people.CompanyPerson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class ServiceActionControllerBase extends ClientControllerBase {

  @FXML
  void handleBackButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
  }

  @FXML
  void handleCancelButton(ActionEvent event) {
    if (!processing) {
      ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU, 10);
    }
  }

  @FXML
  void handleSubmitButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  @FXML
  void handleOkButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  void validateAndSend() {

  }

  EmployeeContext requireEmployeeContext() throws InternalClientException {
    return notNull(ControllersClientAdapter.getEmployeeContext(), "Emlpoyee Context");
  }

  CompanyPerson requireLoggedInUser() throws InternalClientException {
    return requireEmployeeContext().requireCompanyPerson();
  }
}
