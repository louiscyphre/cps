package cps.client.controller.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cps.api.action.SetFullLotAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ServerResponse;
import cps.api.response.SetFullLotResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * @author firl
 *
 */
public class ServiceActionLotIsFullController extends ServiceActionControllerBase implements ParkingLotsController {

  /**
   * 
   */
  @FXML
  private CheckBox setFullStateCheckBox;

  /**
   * 
   */
  @FXML
  private CheckBox setAlternativeLotsCheckBox;

  /**
   * 
   */
  @FXML
  private ListView<String> alternativeLotsList;

  /**
   * 
   */
  @FXML
  private ComboBox<String> lotsList;

  /**
   * 
   */
  private HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  /**
   * @param event
   */
  @FXML
  void handleSetAlternativeLotsCheckBox(ActionEvent event) {
    if (!processing) {
      alternativeLotsList.setVisible(setAlternativeLotsCheckBox.isSelected());

      if (setAlternativeLotsCheckBox.isSelected()) {
        refreshAlternativeLots();
      }
    }
  }

  /**
   * 
   */
  void refreshAlternativeLots() {
    ParkingLot selectedLot = parkingLotsMap.get(lotsList.getValue());
    if (selectedLot != null) {
      alternativeLotsList.getItems().setAll(parkingLotsMap.entrySet().stream()
          .filter(e -> e.getValue().getId() != selectedLot.getId()).map(e -> e.getKey()).collect(Collectors.toList()));
    }
  }

  /**
   * 
   */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert lotsList != null : "fx:id=\"lotsList\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
    assert setFullStateCheckBox != null : "fx:id=\"setFullStateCheckBox\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
    assert setAlternativeLotsCheckBox != null : "fx:id=\"setAlternativeLotsCheckBox\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
    assert alternativeLotsList != null : "fx:id=\"alternativeLotsList\" was not injected: check your FXML file 'ServiceActionLotIsFull.fxml'.";
    alternativeLotsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_LOT_IS_FULL);

    // Update the list of alternative lots when the current lot changes
    lotsList.getSelectionModel().selectedItemProperty()
        .addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          refreshAlternativeLots();
        });
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    if (lotsList.getItems() != null) {
      lotsList.getItems().clear();
    }
    if (alternativeLotsList.getItems() != null) {
      alternativeLotsList.getItems().clear();
    }
    setFullStateCheckBox.setSelected(false);

    // Get the list of parking lots
    turnProcessingStateOn();
    sendRequest(new ListParkingLotsRequest());
  }

  /* (non-Javadoc)
   * @see cps.client.controller.service.ServiceActionControllerBase#validateAndSend()
   */
  @Override
  void validateAndSend() {
    try {
      User user = requireLoggedInUser();
      ParkingLot lot = parkingLotsMap.get(lotsList.getValue());
      errorIfNull(lot, "Please choose a parking lot");
      ObservableList<String> alternativeLotNames = alternativeLotsList.getSelectionModel().getSelectedItems();
      if (setAlternativeLotsCheckBox.isSelected()) {
        ArrayList<Integer> alternativeLotsIds = new ArrayList<Integer>();
        for (String name : alternativeLotNames) {
          ParkingLot alternativeLot = parkingLotsMap.get(name);
          if (alternativeLot.getId() != lot.getId()) {
            alternativeLotsIds.add(alternativeLot.getId());
          }
        }
      }
      ArrayList<Integer> alternativeLotsIds = new ArrayList<Integer>();
      for (String name : alternativeLotNames) {
        ParkingLot alternativeLot = parkingLotsMap.get(name);
        alternativeLotsIds.add(alternativeLot.getId());
      }
      int[] arrayToSend = new int[alternativeLotsIds.size()];
      for (int i = 0; i < alternativeLotsIds.size(); i++) {
        arrayToSend[i] = alternativeLotsIds.get(i);
      }
      errorIf(setAlternativeLotsCheckBox.isSelected() && alternativeLotsIds.size() < 1,
          "Please choose alternative parking lot");
      turnProcessingStateOn();
      sendRequest(new SetFullLotAction(user.getId(), lot.getId(), setFullStateCheckBox.isSelected(), arrayToSend));
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.SetFullLotResponse)
   */
  @Override
  public ServerResponse handle(SetFullLotResponse response) {
    return super.handleGenericResponse(response);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ParkingLotsController#setParkingLots(java.util.Collection)
   */
  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    List<String> tmp = new ArrayList<String>();

    parkingLotsMap.clear();

    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address, i);
    }

    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  /**
   * @param addresses
   */
  private void fillComboBoxItems(ObservableList<String> addresses) {
    lotsList.getItems().addAll(addresses);
  }
}
