package cps.client.controller.service;

import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.people.CompanyPerson;
import cps.entities.people.LocalEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServiceStatisticsChoice extends ServiceActionControllerBase {

  @FXML
  private Label jobTitleLabel;

  @FXML
  private Label usernameLabel;

  @FXML
  void handleMonthlyReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_MONTHLY);
  }

  @FXML
  void handleOrdersReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_ORDERS);
  }

  @FXML
  void handleDisabledCellsReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_DISABLED_PARKING_CELLS);
  }

  @FXML
  void handleComplaintsReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_COMPLAINTS);
  }

  @FXML
  void handleBackButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_ACTION_MENU);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    CompanyPerson companyPerson = ControllersClientAdapter.getEmployeeContext().getCompanyPerson();
    if (companyPerson != null) {
      usernameLabel.setText(companyPerson.getFirstName() + " " + companyPerson.getLastName());

      if (companyPerson instanceof LocalEmployee) {
        jobTitleLabel
            .setText(String.format("%s - lotID: %s", companyPerson.getJobTitle(), companyPerson.getDepartmentID()));
      } else {
        jobTitleLabel.setText(companyPerson.getJobTitle());
      }
    }
  }
  @FXML
  void initialize() {
    super.baseInitialize();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_CHOICE);
  }
}
