package cps.client.controller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cps.api.action.ListComplaintsAction;
import cps.api.action.RefundAction;
import cps.api.action.RejectComplaintAction;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RejectComplaintResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.common.Constants;
import cps.common.Utilities;
import cps.entities.models.Complaint;
import cps.entities.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 *
 */
public class ServiceActionRefundController extends ServiceActionControllerBase {

  /**
   * 
   */
  @FXML
  private ListView<String> complaintsList;

  /**
   * 
   */
  @FXML
  private TextField refundTF;

  /**
   * 
   */
  @FXML
  private TextField reasonTF;

  /**
   * 
   */
  @FXML
  private TextArea complaintContent;

  /**
   * 
   */
  @FXML
  private Button rejectButton;

  /**
   * 
   */
  @FXML
  private Button refundButton;

  /**
   * 
   */
  private HashMap<String, Complaint> complaintsMap;

  /**
   * @param event
   */
  @FXML
  void handleRejectButton(ActionEvent event) {
    if (!processing) {
      validateAndSend(true);
    }
  }

  /**
   * @param event
   */
  @FXML
  void handleRefundButton(ActionEvent event) {
    if (!processing) {
      validateAndSend(false);
    }
  }

  /**
   * 
   */
  void handleComplaintSelected() {
    if (!processing) {
      String selectedMeta = complaintsList.getSelectionModel().getSelectedItem();
      if (selectedMeta != null) {
        Complaint selectedComplaint = complaintsMap.get(selectedMeta);
        if (selectedComplaint.getDescription() != null) {
          complaintContent.setText(selectedComplaint.getDescription());
        }
        ArrayList<Text> formattedText = new ArrayList<Text>();
        int status = selectedComplaint.getStatus();
        //
        formattedText.add(new Text("Created at: "));
        formattedText.add(new Text(selectedComplaint.getCreatedAt().toString()));
        formattedText.add(new Text("\n"));
        formattedText.add(new Text("Status: "));
        if (status == Constants.COMPLAINT_STATUS_PROCESSING) {
          rejectButton.setDisable(false);
          refundButton.setDisable(false);
          formattedText.add(new Text("Processing"));
          formattedText.add(new Text("\n"));
        } else if (status == Constants.COMPLAINT_STATUS_ACCEPTED) {
          rejectButton.setDisable(true);
          refundButton.setDisable(true);
          formattedText.add(new Text("Accepted"));
          formattedText.add(new Text("\n"));
          formattedText.add(new Text("Resolved at: "));
          formattedText.add(new Text(selectedComplaint.getResolvedAt().toString()));
          formattedText.add(new Text("\n"));
          formattedText.add(new Text("Refund amount: "));
          formattedText.add(new Text(Float.toString(selectedComplaint.getRefundAmount())));
          formattedText.add(new Text("\n"));
          formattedText.add(new Text("Reason: "));
          formattedText.add(new Text(Utilities.emptyIfNull(selectedComplaint.getReason())));
          formattedText.add(new Text("\n"));
        } else if (status == Constants.COMPLAINT_STATUS_REJECTED) {
          rejectButton.setDisable(true);
          refundButton.setDisable(true);
          formattedText.add(new Text("Rejected"));
          formattedText.add(new Text("\n"));
          formattedText.add(new Text("Resolved at: "));
          formattedText.add(new Text(selectedComplaint.getResolvedAt().toString()));
          formattedText.add(new Text("\n"));
          formattedText.add(new Text("Reason: "));
          formattedText.add(new Text(Utilities.emptyIfNull(selectedComplaint.getReason())));
          formattedText.add(new Text("\n"));
        }
        displayInfo(formattedText);
      }
    }
  }

  /**
   * 
   */
  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert complaintsList != null : "fx:id=\"complaintsList\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert refundTF != null : "fx:id=\"refundTF\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert complaintContent != null : "fx:id=\"complaintContent\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert rejectButton != null : "fx:id=\"rejectButton\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    assert refundButton != null : "fx:id=\"refundButton\" was not injected: check your FXML file 'ServiceActionRefund.fxml'.";
    complaintsMap = new HashMap<String, Complaint>();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_REFUND);
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#cleanCtrl()
   */
  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
    complaintContent.clear();
    if (refundTF != null)
      refundTF.clear();
    if (reasonTF != null)
      reasonTF.clear();
    if (complaintsList.getItems() != null)
      complaintsList.getItems().clear();
    sendRequest(new ListComplaintsAction());
    rejectButton.setDisable(true);
    refundButton.setDisable(true);
  }

  /**
   * @param reject
   */
  void validateAndSend(boolean reject) {
    try {
      String selectedMeta = complaintsList.getSelectionModel().getSelectedItem();
      int complaintId = complaintsMap.get(selectedMeta).getId();
      requireLoggedInUser();
      User user = ControllersClientAdapter.getEmployeeContext().requireCompanyPerson();
      if (reject) {
        String reason = requireFieldTrim(reasonTF, "Reason");
        sendRequest(new RejectComplaintAction(user.getId(), complaintId, reason));
      } else {
        float refundAmount = requireFloat(refundTF, "Refund Amount");
        String reason = getText(reasonTF).trim(); // reason is not required
        sendRequest(new RefundAction(user.getId(), complaintId, refundAmount, reason));
      }
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.RefundResponse)
   */
  @Override
  public void handle(RefundResponse response) {
    super.handleGenericResponse(response);
    cleanCtrl();
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.RejectComplaintResponse)
   */
  @Override
  public void handle(RejectComplaintResponse response) {
    super.handleGenericResponse(response);
    cleanCtrl();
  }

  /* (non-Javadoc)
   * @see cps.client.controller.ClientControllerBase#handle(cps.api.response.ListComplaintsResponse)
   */
  @Override
  public void handle(ListComplaintsResponse response) {
    turnProcessingStateOff();
    List<String> tmp = new ArrayList<String>();
    complaintsMap.clear();
    for (Complaint i : response.getData()) {
//      String complaintMeta = new String(
//          "[id: " + i.getId() + "]" + "[Employee: " + i.getEmployeeID() + "]" + "[Customer: " + i.getCustomerID() + "]");

      String complaintMeta = String.format("id: %s, Employee: %s, Customer: %s, Status: %s", i.getId(), i.getEmployeeID(), i.getCustomerID(), i.getStatusText());

      tmp.add(complaintMeta);
      complaintsMap.put(complaintMeta, i);
    }
    ObservableList<String> complaintMetas = FXCollections.observableList(tmp);
    complaintsList.getItems().addAll(complaintMetas);
    complaintsList.getSelectionModel().selectedItemProperty().addListener(e -> {
      handleComplaintSelected();
    });
    complaintsList.setDisable(false);
  }

}
