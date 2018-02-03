package cps.client.controller.customer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cps.api.request.CancelOnetimeParkingRequest;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.OnetimeEntriesController;
import cps.client.controller.ParkingLotsController;
import cps.client.controller.ViewController;
import cps.common.Utilities;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Callback;

/** Controller Class for View My Reservation scene. */
public class ViewMyReservationsController extends CustomerActionControllerBase implements ParkingLotsController, OnetimeEntriesController {

  /** TableView containing all the reservations. */
  @FXML // fx:id="tableView"
  private TableView<TableOnetimeService> tableView; // Value injected by
                                                    // FXMLLoader

  /** Type column */
  @FXML
  private TableColumn<TableOnetimeService, String> colType;

  /** Car ID column */
  @FXML
  private TableColumn<TableOnetimeService, String> colCarId;

  /** Lot column */
  @FXML
  private TableColumn<TableOnetimeService, String> colLot;

  /** Start Date column */
  @FXML
  private TableColumn<TableOnetimeService, String> colStart;

  /** Leave Date column */
  @FXML
  private TableColumn<TableOnetimeService, String> colLeave;

  /** Status column */
  @FXML
  private TableColumn<TableOnetimeService, String> colStatus;

  /** Cancel button column */
  @FXML
  private TableColumn<TableOnetimeService, String> colCancel;

  /** List holding the entries */
  private ObservableList<TableOnetimeService> obsEntriesList;

  /** Parking Lots mapping */
  private HashMap<Integer, String> parkingLotsMap;

  /** Refresh function. Request from the server fresh list of Reservations */
  private void refresh() {
    if (parkingLotsMap.isEmpty()) {
      ListParkingLotsRequest request = new ListParkingLotsRequest();
      // Toggle processing state on
      turnProcessingStateOn();
      ControllersClientAdapter.getClient().sendRequest(request);
    } else {
      ListOnetimeEntriesRequest request = new ListOnetimeEntriesRequest(ControllersClientAdapter.getCustomerContext().getCustomerId());
      // Toggle processing state on
      turnProcessingStateOn();
      ControllersClientAdapter.getClient().sendRequest(request);
    }
  }

  /** @param event */
  @FXML
  void handleRefreshButton(ActionEvent event) {
    if (!processing) {
      refresh();
    }
  }

  /** Initializes the Controller and Registers it. */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";

    assert colType != null : "fx:id=\"colType\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert colCarId != null : "fx:id=\"colCarId\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert colLot != null : "fx:id=\"colLot\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert colStart != null : "fx:id=\"colStart\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert colLeave != null : "fx:id=\"colLeave\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert colStatus != null : "fx:id=\"colStatus\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert colCancel != null : "fx:id=\"colCancel\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";

    // Columns cell value factories
    colType.setCellValueFactory(new PropertyValueFactory<>("type"));
    colCarId.setCellValueFactory(new PropertyValueFactory<>("carID"));
    colLot.setCellValueFactory(new PropertyValueFactory<>("lotName"));
    colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    colLeave.setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
    colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    colCancel.setCellValueFactory(new PropertyValueFactory<>("DUMMY")); // cancel button workaround

    parkingLotsMap = new HashMap<Integer, String>();

    // cancel button in cancel column
    Callback<TableColumn<TableOnetimeService, String>, TableCell<TableOnetimeService, String>> cellFactory = //
        new Callback<TableColumn<TableOnetimeService, String>, TableCell<TableOnetimeService, String>>() {

          @Override
          public TableCell<TableOnetimeService, String> call(TableColumn<TableOnetimeService, String> param) {
            final TableCell<TableOnetimeService, String> cell = new TableCell<TableOnetimeService, String>() {
              final Button btn = new Button("Cancel");

              @Override
              public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                  setGraphic(null);
                  setText(null);
                } else {
                  TableOnetimeService service = getTableView().getItems().get(getIndex());
                  
                  btn.setOnAction(event -> {
                    CancelOnetimeParkingRequest request = new CancelOnetimeParkingRequest(ControllersClientAdapter.getCustomerContext().getCustomerId(),
                        service.getServiceID());

                    turnProcessingStateOn();
                    ControllersClientAdapter.getClient().sendRequest(request);
                  });
                  
                  btn.setMaxWidth(Double.MAX_VALUE);
                  
                  if (service.isCancelable()) {
                    setGraphic(btn);
                  } else {
                    setGraphic(null);
                  }
                  
                  setText(null);
                }
              }
            };
            return cell;
          }

        };

    // cancel column factory
    colCancel.setCellFactory(cellFactory);

    // binding to list of reservations
    this.obsEntriesList = FXCollections.observableArrayList();
    tableView.setItems(this.obsEntriesList);

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.VIEW_MY_RESERVATION);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl() */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    parkingLotsMap.clear();
    obsEntriesList.clear();
    refresh();
  }

  /* (non-Javadoc)
   * @see
   * cps.client.controller.OnetimeEntriesController#setOnetimeEntries(java.util.
   * Collection) */
  @Override
  public void setOnetimeEntries(Collection<OnetimeService> list) {
    List<TableOnetimeService> newEntriesList = new LinkedList<TableOnetimeService>();
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    list.forEach(e -> {
      if (e.getPlannedEndTime().compareTo(now) > 0 && !e.isCanceled()) {
        String startTime = Utilities.dateToString(e.getPlannedStartTime().toLocalDateTime());
        String endTime = Utilities.dateToString(e.getPlannedEndTime().toLocalDateTime());
        newEntriesList.add(new TableOnetimeService(e.getParkingType(), e.getCarID(), e.getLotID(), startTime, endTime, e.getId(), e.isParked(), e.isCompleted(),
            e.isCanceled(), e.isWarned()));
      }
    });
    this.obsEntriesList.setAll(newEntriesList);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.OnetimeEntriesController#removeEntry(int) */
  @Override
  public void removeEntry(int onetimeServiceID) {
    obsEntriesList.remove(new TableOnetimeService(0, null, 0, null, null, onetimeServiceID));
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.
   * Collection) */
  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    parkingLotsMap = new HashMap<Integer, String>();
    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      parkingLotsMap.put(i.getId(), address);
    }
    ListOnetimeEntriesRequest request = new ListOnetimeEntriesRequest(ControllersClientAdapter.getCustomerContext().getCustomerId());
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  /** Class to hold inner representation of OnetimeServices in Table. */
  public class TableOnetimeService {
    private final SimpleStringProperty type;
    private final SimpleStringProperty carID;
    private final SimpleStringProperty lotName;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty leaveDate;
    private final SimpleStringProperty status;
    private int                        serviceID;
    private boolean                    parked;
    private boolean                    completed;
    private boolean                    canceled;
    private boolean                    warned;

    public TableOnetimeService(int type, String carID, int lotid, String startDate, String leaveDate, int serviceID) {
      this(type, carID, lotid, startDate, leaveDate, serviceID, false, false, false, false);
    }

    public TableOnetimeService(int type, String carID, int lotid, String startDate, String leaveDate, int serviceID, boolean parked, boolean completed,
        boolean canceled, boolean warned) {
      this.type = new SimpleStringProperty(type == 1 ? "Incidental" : (type == 2 ? "Reserved" : "-"));
      this.carID = new SimpleStringProperty(carID);
      this.lotName = new SimpleStringProperty(parkingLotsMap.get(lotid));
      this.startDate = new SimpleStringProperty(startDate);
      this.leaveDate = new SimpleStringProperty(leaveDate);
      this.serviceID = serviceID;
      this.parked = parked;
      this.completed = completed;
      this.canceled = canceled;
      this.warned = warned;
      this.status = new SimpleStringProperty(OnetimeService.getStatusDescription(parked, completed, canceled, warned));
    }

    public String getType() {
      return type.get();
    }

    public String getCarID() {
      return carID.get();
    }

    public String getLotName() {
      return lotName.get();
    }

    public String getStartDate() {
      return startDate.get();
    }

    public String getLeaveDate() {
      return leaveDate.get();
    }

    public void setType(String type) {
      this.type.set(type);
    }

    public void setCarID(String carID) {
      this.carID.set(carID);
    }

    public void setLotName(String lotName) {
      this.lotName.set(lotName);
    }

    public void setStartDate(String startDate) {
      this.startDate.set(startDate);
    }

    public void setLeaveDate(String leaveDate) {
      this.leaveDate.set(leaveDate);
    }

    public int getServiceID() {
      return this.serviceID;
    }

    public void setServiceID(int serviceID) {
      this.serviceID = serviceID;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof TableOnetimeService) {
        return this.serviceID == ((TableOnetimeService) obj).serviceID;
      }

      return false;
    }

    public boolean isParked() {
      return parked;
    }

    public void setParked(boolean parked) {
      this.parked = parked;
      updateStatus();
    }

    public boolean isCanceled() {
      return canceled;
    }

    public void setCanceled(boolean canceled) {
      this.canceled = canceled;
      updateStatus();
    }

    public boolean isCompleted() {
      return completed;
    }

    public void setCompleted(boolean completed) {
      this.completed = completed;
      updateStatus();
    }

    public boolean isWarned() {
      return warned;
    }

    public void setWarned(boolean warned) {
      this.warned = warned;
      updateStatus();
    }

    public String getStatus() {
      return status.get();
    }

    public void setStatus(String status) {
      this.status.set(status);
    }

    void updateStatus() {
      this.status.set(OnetimeService.getStatusDescription(parked, completed, canceled, warned));
    }

    public boolean isCancelable() {
      return !parked && !completed && !canceled;
    }
  }

  /** Handles the response from the server containing the data about reservations
   * and populates inner data structures accordingly. */
  @Override
  public void handle(ListOnetimeEntriesResponse response) {
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.getStatus() == ServerResponse.STATUS_OK) {

      formattedMessage.add(new Text("Onetime Entries for customer with id : "));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);

      setOnetimeEntries(response.getData());

      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("The parking entry is denied!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
  }

  /** Handles the response from the server regarding entry which was requested to
   * be cancelled and populates inner data structures accordingly. */
  @Override
  public void handle(CancelOnetimeParkingResponse response) {
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.success()) {
      formattedMessage.add(new Text("The following reservation had been canceled!\n"));
      formattedMessage.add(new Text(String.format("The account had been refunded %s ILS", decimal(response.getRefundAmount()))));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
      OnetimeEntriesController casted = (OnetimeEntriesController) ctrl;
      casted.removeEntry(response.getOnetimeServiceID());
    } else { // request failed
      formattedMessage.add(new Text("Could not cancel this reservation!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }

  }
}
