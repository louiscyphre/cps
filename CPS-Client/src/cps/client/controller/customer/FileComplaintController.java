package cps.client.controller.customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import cps.api.request.ComplaintRequest;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.ComplaintResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.entities.models.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * File Complaint controller.
 */
public class FileComplaintController extends CustomerActionControllerBaseSubmitAndFinish implements ParkingLotsController {
  /**
   * Complaint Text Area.
   */
  @FXML // fx:id="complaintContent"
  private TextArea complaintContent; // Value injected by FXMLLoader

  /**
   * Initializes the Controller and Registers it.
   */
  @FXML
  private ComboBox<String> parkingLotsList;

  /**
   * 
   */
  HashMap<String, ParkingLot> parkingLotsMap = null;

  /**
   * 
   */
  @FXML
  // This method is called by the FXMLLoader when initialization is complete
  void initialize() {
    super.baseInitialize();
    assert complaintContent != null : "fx:id=\"complaintContent\" was not injected: check your FXML file 'FileComplaintScene.fxml'.";
//    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'FileComplaintScene.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.FILE_COMPLAINT);
    parkingLotsMap = new HashMap<String, ParkingLot>();
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBase#sendMainRequest()
   */
  @Override
  void sendMainRequest() {
    try {
      int customerId = notNull(ControllersClientAdapter.getCustomerContext(), "CustomerContext").getCustomerId();
      String content = requireField(complaintContent, "Complaint Content");
      sendRequest(new ComplaintRequest(customerId, content));
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.ComplaintResponse)
   */
  @Override
  public void handle(ComplaintResponse response) {
    super.handleGenericResponse(response);
    if (response.success()) {
      setFinishInsteadOfSubmit(true);
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.customer.CustomerActionControllerBaseSubmitAndFinish#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    complaintContent.clear();
    
    if (parkingLotsList.getItems() != null) {
      parkingLotsList.getItems().clear();
    }
    
    parkingLotsMap.clear();

    // Get the list of parking lots
    turnProcessingStateOn();
    sendRequest(new ListParkingLotsRequest());
  }

  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    LinkedList<String> tmp = new LinkedList<String>();

    parkingLotsMap.clear();
    
    for (ParkingLot lot : list) {
      String address = new String(lot.getStreetAddress());
      parkingLotsMap.put(address, lot);
      tmp.add(address);
    }
    
    ObservableList<String> addresses = FXCollections.observableList(tmp);
    parkingLotsList.getItems().setAll(addresses);    
  }

}
