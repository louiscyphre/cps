package cps.server.controllers;

import java.util.Collection;

import cps.api.action.DisableParkingSlotsAction;
import cps.api.action.InitLotAction;
import cps.api.action.RequestLotStateAction;
import cps.api.action.ReserveParkingSlotsAction;
import cps.api.action.SetFullLotAction;
import cps.api.action.UpdatePricesAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.InitLotResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ServerResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;
import cps.entities.models.ParkingLot;
import cps.server.ServerController;
import cps.server.session.UserSession;

/**
 * The Class LotController.
 */
public class LotController extends RequestController {

  /**
   * Instantiates a new lot controller.
   *
   * @param serverController
   *          serverApplication object
   */
  public LotController(ServerController serverController) {
    super(serverController);
  }

  /**
   * Handle ListParkingLotsRequest
   * 
   * @param request
   * @param session
   * @return Collection of ParkingLot objects
   */
  public ServerResponse handle(ListParkingLotsRequest request, UserSession session) {
    return database.performQuery(new ListParkingLotsResponse(), (conn, response) -> {
      Collection<ParkingLot> result = ParkingLot.findAll(conn);

      // Filter out information that customers shouldn't see
      result.forEach(lot -> {
        lot.setContent(null);
        lot.setRobotIP(null);
      });

      response.setData(result);
      response.setSuccess("ListParkingLotsRequest completed successfully");
      return response;
    });
  }

  /**
   * Handle InitLotAction.
   *
   * @param request
   *          the request
   * @return the server response
   */
  public InitLotResponse handle(InitLotAction request, UserSession session) {
    return database.performQuery(new InitLotResponse(), (conn, response) -> {
      ParkingLot result = ParkingLot.create(conn, request.getStreetAddress(), request.getSize(), request.getPrice1(),
          request.getPrice2(), request.getRobotIP());

      errorIfNull(result, "Failed to create parking lot");

      response.setLotID(result.getId());
      response.setSuccess("Parking lot created successfully");
      return response;
    });
  }

  /**
   * Handle Update Prices Action.
   *
   * @param action
   *          the action
   * @return the update prices response
   */
  public UpdatePricesResponse handle(UpdatePricesAction action, UserSession session) {
    return database.performQuery(new UpdatePricesResponse(), (conn, response) -> {
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, action.getLotID());

      errorIf(action.getPrice1() < 0f, "Price cannot be negative");
      errorIf(action.getPrice2() < 0f, "Price cannot be negative");

      lot.setPrice1(action.getPrice1());
      lot.setPrice2(action.getPrice2());

      lot.update(conn);
      response.setSuccess("Prices updates succsessfully");
      return response;
    });
  }

  public SetFullLotResponse handle(SetFullLotAction action, UserSession session) {
    return database.performQuery(new SetFullLotResponse(), (conn, response) -> {
      ParkingLot lot = ParkingLot.findByID(conn, action.getLotID());
      lot.setLotFull(action.getLotFull());

      // TODO this should be a list of alternative lot IDs
      lot.setAlternativeLots(Integer.toString(action.getAlternativeLotID()));
      lot.update(conn);
      response.setSuccess("ParkingLot status updated");
      return response;
    });
  }

  public RequestLotStateResponse handle(RequestLotStateAction action, UserSession session) {
    return database.performQuery(new RequestLotStateResponse(), (conn, response) -> {
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, action.getLotID());

      response.setLot(lot);
      response.setSuccess("RequestLotState request completed successfully");
      return response;
    });
  }

  public ServerResponse handle(ReserveParkingSlotsAction action, UserSession session) {
    // TODO implement ReserveParkingSlotsAction
    return null;
  }

  public ServerResponse handle(DisableParkingSlotsAction action, UserSession session) {
    // TODO implement DisableParkingSlotsAction
    return null;
  }

}
