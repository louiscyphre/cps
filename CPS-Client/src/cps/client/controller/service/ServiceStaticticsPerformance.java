package cps.client.controller.service;

import cps.api.action.GetCurrentPerformanceAction;
import cps.api.response.CurrentPerformanceResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ServiceStaticticsPerformance extends ServiceStatitisticsBase {

  @FXML
  private TableColumn<TablePerformanceEntry, String> colNumberOfSubscriptions;

  @FXML
  private TableColumn<TablePerformanceEntry, String> colNumberOfSubscriptionsWithMultipleCars;

  @FXML
  private TableView<TablePerformanceEntry> tableView;

  /** List holding the entries */
  private ObservableList<TablePerformanceEntry> obsEntriesList;

  /** Handle refresh button.
   * @param event the event */
  @FXML
  void handleRefreshButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }
  @Override
  void validateAndSend() {
    int userID = ControllersClientAdapter.getEmployeeContext().getCompanyPerson().getId();
    GetCurrentPerformanceAction request = new GetCurrentPerformanceAction(userID);
    ControllersClientAdapter.getClient().sendRequest(request);
    turnProcessingStateOn();
  }

  @Override
  public void handle(CurrentPerformanceResponse response) {
    if (response.success()) {
      turnProcessingStateOff();
      fillTable(response);
    } else {
      turnProcessingStateOff();
      displayError("Could not retrieve performance report");
    }
  }

  private void fillTable(CurrentPerformanceResponse response) {
    int numberOfSubscriptions = response.getNumberOfSubscriptions();
    int numberOfSubscriptionsWithMultipleCars = response.getNumberOfSubscriptionsWithMultipleCars();
    TablePerformanceEntry entry = new TablePerformanceEntry(numberOfSubscriptions, numberOfSubscriptionsWithMultipleCars);
    obsEntriesList.setAll(entry);
  }

  @FXML
  void initialize() {
    super.baseInitialize();
    
    // binding to list of monthly reports
    obsEntriesList = FXCollections.observableArrayList();
    tableView.setItems(this.obsEntriesList);

    // use set/get value property value factories
    colNumberOfSubscriptions.setCellValueFactory(new PropertyValueFactory<>("numberOfSubscriptions"));
    colNumberOfSubscriptionsWithMultipleCars.setCellValueFactory(new PropertyValueFactory<>("numberOfSubscriptionsWithMultipleCars"));
    
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_STATISTICS_PERFORMANCE);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    validateAndSend();
  }

  public class TablePerformanceEntry {
    private SimpleStringProperty numberOfSubscriptions;
    private SimpleStringProperty numberOfSubscriptionsWithMultipleCars;

    public TablePerformanceEntry(int numberOfSubscriptions, int numberOfSubscriptionsWithMultipleCars) {
      super();
      this.numberOfSubscriptions = new SimpleStringProperty(Integer.toString(numberOfSubscriptions));
      this.numberOfSubscriptionsWithMultipleCars = new SimpleStringProperty(
          Integer.toString(numberOfSubscriptionsWithMultipleCars));
    }

    public String getNumberOfSubscriptions() {
      return numberOfSubscriptions.get();
    }

    public void setNumberOfSubscriptions(SimpleStringProperty numberOfSubscriptions) {
      this.numberOfSubscriptions = numberOfSubscriptions;
    }

    public String getNumberOfSubscriptionsWithMultipleCars() {
      return numberOfSubscriptionsWithMultipleCars.get();
    }

    public void setNumberOfSubscriptionsWithMultipleCars(SimpleStringProperty numberOfSubscriptionsWithMultipleCars) {
      this.numberOfSubscriptionsWithMultipleCars = numberOfSubscriptionsWithMultipleCars;
    }

  }
}
