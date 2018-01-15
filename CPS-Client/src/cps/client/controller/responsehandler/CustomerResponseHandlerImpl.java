package cps.client.controller.responsehandler;

import java.util.LinkedList;
import java.util.List;

import cps.api.response.CancelOnetimeParkingResponse;
import cps.api.response.ComplaintResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ListOnetimeEntriesResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.LoginResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;
import cps.client.context.CustomerContext;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.client.controller.ViewController;
import cps.entities.models.ParkingLot;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

class CustomerResponseHandlerImpl implements CustomerResponseHandler {

  // handlers
  @Override
  public ServerResponse handle(CancelOnetimeParkingResponse response) {
    // TODO Auto-generated method stub
    return response;
  }

  @Override
  public ServerResponse handle(ComplaintResponse response) {
    // TODO Auto-generated method stub
    return response;
  }

  @Override
  public ServerResponse handle(IncidentalParkingResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();

    // if request fails customer id is 0 TODO
    // new customer
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId() && response.success()) {
      // outputting the customer id on screen
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));
      // password part
      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));
      // binding new user to application context
      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    // logged in customer
    if (response.success()) {
      formattedMessage.add(new Text("Your incidental parking reserved!\n"));
      formattedMessage.add(new Text("Use your password in 'Enter Parking' to fill this reservation.\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
    } else { // request failed
      formattedMessage.add(new Text("Could not reserve parking at this moment!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }

    return response;
  }

  @Override
  public ServerResponse handle(ListOnetimeEntriesResponse response) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ServerResponse handle(LoginResponse response) {

    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));

      Text loginSuccessfulMessage = new Text("Login was successful");
      formattedMessage.add(loginSuccessfulMessage);
      formattedMessage.add(new Text("\n"));

      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
      ControllersClientAdapter.getCurrentCtrl().turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);
    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      ctrl.displayError(response.getDescription());
      ControllersClientAdapter.getCurrentCtrl().turnProcessingStateOff();
    }

    return response;
  }

  @Override
  public ServerResponse handle(ParkingEntryResponse response) {
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("The parking entry is granted!\nRobot will collect your car shortly.\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("The parking entry is denied!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
    return response;
  }

  @Override
  public ServerResponse handle(ParkingExitResponse response) {
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    List<Text> formattedMessage = new LinkedList<Text>();

    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("The car retrieval is granted!\nRobot will retrieve your car shortly.\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("The car retrieval is denied!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
    return response;
  }

  @Override
  public ServerResponse handle(ListParkingLotsResponse response) {
    ParkingLotsController ctrl = (ParkingLotsController) ControllersClientAdapter.getCurrentCtrl();                                                                     // normally
    List<ParkingLot> list = new LinkedList<ParkingLot>(response.getData());
    ctrl.setParkingLots(list);
    ctrl.turnProcessingStateOff();
    return response;
  }

  @Override
  public ServerResponse handle(ReservedParkingResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId()) {
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));

      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));

      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }

    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("Succesfully reserved parking per request!\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("Could not reserve parking!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }

    return response;
  }

  @Override
  public ServerResponse handle(RegularSubscriptionResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId()) {
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));

      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));

      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("Subscription purchased successfuly!\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("Could not proceed with purchase!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
    return response;
  }

  @Override
  public ServerResponse handle(FullSubscriptionResponse response) {
    CustomerContext context = ControllersClientAdapter.getCustomerContext();
    ViewController ctrl = ControllersClientAdapter.getCurrentCtrl();

    int responseCustomerId = response.getCustomerID();
    List<Text> formattedMessage = new LinkedList<Text>();
    if (responseCustomerId != ControllersClientAdapter.getCustomerContext().getCustomerId()) {
      context.setCustomerId(responseCustomerId);
      formattedMessage.add(new Text("Your Customer ID:"));
      Text customerIdText = new Text(Integer.toString(response.getCustomerID()));
      Font defaultFont = customerIdText.getFont();
      customerIdText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(customerIdText);
      formattedMessage.add(new Text("\n"));

      formattedMessage.add(new Text("Your Password:"));
      Text password = new Text(response.getPassword());
      defaultFont = password.getFont();
      password.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize()));
      formattedMessage.add(password);
      formattedMessage.add(new Text("\n"));

      context.setCustomerId(responseCustomerId);
      context.acceptPendingEmail();
      ControllersClientAdapter.turnLoggedInStateOn();
    }
    if (response.getStatus() == ServerResponse.STATUS_OK) {
      formattedMessage.add(new Text("Subscription purchased successfuly!\n"));
      ctrl.turnProcessingStateOff();
      ctrl.displayInfo(formattedMessage);

    } else if (response.getStatus() == ServerResponse.STATUS_ERROR) {
      formattedMessage.add(new Text("Could not proceed with purchase!\n"));
      formattedMessage.add(new Text(response.getDescription()));
      ctrl.turnProcessingStateOff();
      ctrl.displayError(formattedMessage);
    }
    return response;
  }

  @Override
  public ServerResponse handle(ServerResponse response) {
    // TODO Auto-generated method stub
    return response;
  }
}
