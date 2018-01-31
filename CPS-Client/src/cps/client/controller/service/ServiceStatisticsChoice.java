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
  void handleQuarterlyReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_QUARTERLY);
  }

  @FXML
  void handleWeeklyReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_WEEKLY);
  }

  @FXML
  void handleActivityReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_ACTIVITY);
  }

  @FXML
  void handlePerformanceReportsButton(ActionEvent event) {
    ControllersClientAdapter.setStage(SceneCode.SERVICE_STATISTICS_PERFORMANCE);
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
