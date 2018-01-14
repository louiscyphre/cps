package cps.client.controller.customer;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.OnetimeEntriesController;
import cps.client.controller.ParkingLotsController;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

public class ViewMyReservationsController implements ParkingLotsController, OnetimeEntriesController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="infoLabel"
  private TextFlow infoLabel; // Value injected by FXMLLoader

  @FXML // fx:id="infoProgress"
  private ProgressIndicator infoProgress; // Value injected by FXMLLoader

  @FXML // fx:id="tableView"
  private TableView<TableOnetimeService> tableView; // Value injected by
                                                    // FXMLLoader

  @FXML // fx:id="infoBox"
  private VBox infoBox; // Value injected by FXMLLoader

  @FXML
  void handleBackButton(ActionEvent event) {

  }

  @FXML
  void handleRefreshButton(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    assert infoLabel != null : "fx:id=\"infoLabel\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert infoProgress != null : "fx:id=\"infoProgress\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    assert infoBox != null : "fx:id=\"infoBox\" was not injected: check your FXML file 'ViewMyReservationsScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.CUSTOMER_INITIAL_MENU);
    Platform.runLater(() -> infoBox.requestFocus()); // to unfocus the Text
                                                     // Field

  }

  @Override
  public void displayInfo(List<Text> formattedText) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayInfo(String simpleInfoMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(List<Text> formettedErrorMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void displayError(String simpleErrorMsg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnProcessingStateOn() {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnProcessingStateOff() {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnLoggedInStateOn() {
    // TODO Auto-generated method stub

  }

  @Override
  public void turnLoggedInStateOff() {
    // TODO Auto-generated method stub

  }

  @Override
  public void cleanCtrl() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setOnetimeEntries(List<OnetimeService> list) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setParkingLots(List<ParkingLot> list) {
    // TODO Auto-generated method stub

  }

  private static class TableOnetimeService {

    private final SimpleStringProperty type;
    private final SimpleStringProperty carID;
    private final SimpleStringProperty lotName;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty leaveDate;

    public TableOnetimeService(String strType, String strCarID, String strLotName, String strStartDate,
        String strLeaveDate) {
      this.type = new SimpleStringProperty(strType);
      this.carID = new SimpleStringProperty(strCarID);
      this.lotName = new SimpleStringProperty(strLotName);
      this.startDate = new SimpleStringProperty(strStartDate);
      this.leaveDate = new SimpleStringProperty(strLeaveDate);
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

    public void setType(String strType) {
      this.type.set(strType);
    }

    public void setCarID(String strCarID) {
      this.carID.set(strCarID);
    }

    public void setLotName(String strLotName) {
      this.lotName.set(strLotName);
    }

    public void setStartDate(String strStartDate) {
      this.startDate.set(strStartDate);
    }

    public void setLeaveDate(String strLeaveDate) {
      this.leaveDate.set(strLeaveDate);
    }
  }

  // TODO testing
  @FXML
  void addDummyData(ActionEvent event) {
    List<OnetimeService> list = new LinkedList<OnetimeService>();
    for (int i = 0; i < 10; i++) {
      list.add((new OnetimeService(0, 1 + (i % 2), 0, "email" + i, "carid" + i, 1,
          Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), false)));
    }

    TableColumn typeCol = tableView.getColumns().get(0);
    TableColumn carIDCol = tableView.getColumns().get(1);
    TableColumn lotCol = tableView.getColumns().get(2);
    TableColumn startdateCol = tableView.getColumns().get(3);
    TableColumn leavedateCol = tableView.getColumns().get(4);
    TableColumn actionCol = new TableColumn("Action");
    actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

    Callback<TableColumn<TableOnetimeService, String>, TableCell<TableOnetimeService, String>> cellFactory
            = //
            new Callback<TableColumn<TableOnetimeService, String>, TableCell<TableOnetimeService, String>>() {
        @Override
        public TableCell call(final TableColumn<TableOnetimeService, String> param) {
            final TableCell<TableOnetimeService, String> cell = new TableCell<TableOnetimeService, String>() {

                final Button btn = new Button("Just Do It");

                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                          TableOnetimeService person = getTableView().getItems().get(getIndex());
                          System.out.println("button pressed");
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        }
    };

    actionCol.setCellFactory(cellFactory);
  }

}
