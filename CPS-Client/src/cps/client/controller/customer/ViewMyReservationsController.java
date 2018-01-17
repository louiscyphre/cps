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
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.OnetimeEntriesController;
import cps.client.controller.ParkingLotsController;
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
import javafx.util.Callback;

public class ViewMyReservationsController extends CustomerActionControllerBase
    implements ParkingLotsController, OnetimeEntriesController {

  @FXML // fx:id="tableView"
  private TableView<TableOnetimeService> tableView; // Value injected by
                                                    // FXMLLoader

  @FXML
  private TableColumn<TableOnetimeService, String> colType;

  @FXML
  private TableColumn<TableOnetimeService, String> colCarId;

  @FXML
  private TableColumn<TableOnetimeService, String> colLot;

  @FXML
  private TableColumn<TableOnetimeService, String> colStart;

  @FXML
  private TableColumn<TableOnetimeService, String> colLeave;

  @FXML
  private TableColumn<TableOnetimeService, String> colCancel;
  private ObservableList<TableOnetimeService>      obsEntriesList;

  private HashMap<Integer, String> parkingLotsMap;

  @FXML
  void handleRefreshButton(ActionEvent event) {
    if (processing) {
      return;
    }

    if (parkingLotsMap.isEmpty()) {
      ListParkingLotsRequest request = new ListParkingLotsRequest();
      turnProcessingStateOn();
      ControllersClientAdapter.getClient().sendRequest(request);
    } else {
      ListOnetimeEntriesRequest request = new ListOnetimeEntriesRequest(
          ControllersClientAdapter.getCustomerContext().getCustomerId());
      turnProcessingStateOn();
      ControllersClientAdapter.getClient().sendRequest(request);
    }
  }

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
    assert colCancel != null : "fx:id=\"colCancel\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";

    // Columns cell value factories
    colType.setCellValueFactory(new PropertyValueFactory<>("type"));
    colCarId.setCellValueFactory(new PropertyValueFactory<>("carID"));
    colLot.setCellValueFactory(new PropertyValueFactory<>("lotName"));
    colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    colLeave.setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
    colCancel.setCellValueFactory(new PropertyValueFactory<>("DUMMY")); // cancel
                                                                        // button
                                                                        // workaround

    parkingLotsMap = new HashMap<Integer, String>();

    // cancel button
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
                  btn.setOnAction(event -> {
                    TableOnetimeService service = getTableView().getItems().get(getIndex());

                    CancelOnetimeParkingRequest request = new CancelOnetimeParkingRequest(
                        ControllersClientAdapter.getCustomerContext().getCustomerId(), service.getServiceID());

                    turnProcessingStateOn();
                    ControllersClientAdapter.getClient().sendRequest(request);
                  });
                  btn.setMaxWidth(Double.MAX_VALUE);
                  setGraphic(btn);
                  setText(null);
                }
              }
            };
            return cell;
          }

        };

    colCancel.setCellFactory(cellFactory);

    this.obsEntriesList = FXCollections.observableArrayList();
    tableView.setItems(this.obsEntriesList);

    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.VIEW_MY_RESERVATION);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    parkingLotsMap.clear();
    obsEntriesList.clear();
  }

  @Override
  public void setOnetimeEntries(Collection<OnetimeService> list) {
    List<TableOnetimeService> newEntriesList = new LinkedList<TableOnetimeService>();
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    list.forEach(e -> {
      if(e.getPlannedEndTime().compareTo(now) > 0 && !e.isCanceled()) {
        TableOnetimeService toAdd = new TableOnetimeService((e.getParkingType()), e.getCarID(), e.getLotID(),
            e.getPlannedStartTime().toString(), e.getPlannedEndTime().toString(), e.getId());
        newEntriesList.add(toAdd);
      }
    });
    this.obsEntriesList.setAll(newEntriesList);
  }

  @Override
  public void removeEntry(int onetimeServiceID) {
    obsEntriesList.remove(new TableOnetimeService(0, null, 0, null, null, onetimeServiceID));
  }

  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    parkingLotsMap = new HashMap<Integer, String>();
    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      parkingLotsMap.put(i.getId(), address);
    }
    ListOnetimeEntriesRequest request = new ListOnetimeEntriesRequest(
        ControllersClientAdapter.getCustomerContext().getCustomerId());
    turnProcessingStateOn();
    ControllersClientAdapter.getClient().sendRequest(request);
  }

  // Table row entry
  public class TableOnetimeService {
    private final SimpleStringProperty type;
    private final SimpleStringProperty carID;
    private final SimpleStringProperty lotName;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty leaveDate;
    private int                        serviceID;

    public TableOnetimeService(int type, String carID, int lotid, String startDate, String leaveDate, int serviceID) {
      this.type = new SimpleStringProperty(type == 1 ? "Incidental" : (type == 2 ? "Reserved" : "-"));
      this.carID = new SimpleStringProperty(carID);
      this.lotName = new SimpleStringProperty(parkingLotsMap.get(lotid));
      this.startDate = new SimpleStringProperty(startDate);
      this.leaveDate = new SimpleStringProperty(leaveDate);
      this.serviceID = serviceID;
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

    public void setServiceId(int serviceID) {
      this.serviceID = serviceID;
    }

    @Override
    public boolean equals(Object obj) {
      return this.serviceID == ((TableOnetimeService) obj).serviceID;
    }

  }

  // @FXML
  void addDummyData(ActionEvent event) {

    List<ParkingLot> parkingLotList = new LinkedList<ParkingLot>();

    parkingLotList.add(new ParkingLot(1, "1st street", 4, 12, 12, null, "12.2.f.1", false));
    parkingLotList.add(new ParkingLot(2, "2nd street", 4, 12, 12, null, "12.2.f.1", false));
    parkingLotList.add(new ParkingLot(3, "3rd street", 4, 12, 12, null, "12.2.f.1", false));
    parkingLotList.add(new ParkingLot(4, "4th street", 4, 12, 12, null, "12.2.f.1", false));

    setParkingLots(parkingLotList);

    List<OnetimeService> list = new LinkedList<OnetimeService>();
    for (int i = 0; i < 10; i++) {
      list.add(new OnetimeService(0, 1 + (i % 2), 0, "email" + i, "carid" + i, 1,
          Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), false, false, false));
    }

    List<TableOnetimeService> entriesList = new LinkedList<TableOnetimeService>();

    list.forEach(e -> {
      TableOnetimeService toAdd = new TableOnetimeService((e.getParkingType()), e.getCarID(), e.getLotID(),
          e.getPlannedStartTime().toString(), e.getPlannedEndTime().toString(), e.getId());
      entriesList.add(toAdd);
    });

    obsEntriesList.addAll(entriesList);
  }

}
