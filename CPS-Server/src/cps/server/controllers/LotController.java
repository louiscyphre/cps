package cps.server.controllers;

import static cps.common.Utilities.between;
import static cps.common.Utilities.isEmpty;
import static cps.common.Utilities.valueOrDefault;

import java.util.Collection;

import cps.api.action.DisableParkingSlotsAction;
import cps.api.action.InitLotAction;
import cps.api.action.RequestLotStateAction;
import cps.api.action.ReserveParkingSlotsAction;
import cps.api.action.SetFullLotAction;
import cps.api.action.UpdatePricesAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.ListParkingLotsResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ReserveParkingSlotsResponse;
import cps.api.response.ServerResponse;
import cps.api.response.SetFullLotResponse;
import cps.api.response.UpdatePricesResponse;
import cps.common.Constants;
import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingCell.ParkingCellVisitor;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import cps.server.ServerController;
import cps.server.session.ServiceSession;
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
      // TODO don't filter for employees
      result.forEach(lot -> {
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
  public InitLotResponse handle(InitLotAction request, ServiceSession session) {
    return database.performQuery(new InitLotResponse(), (conn, response) -> {
      // Require a logged in employee
      User user = session.requireUser();

      // Require the employee to have access rights to this action
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT), "You cannot perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_LOCAL_WORKER, "You cannot perform this action");

      // Check request parameters
      errorIf(request.getPrice1() < 0f, "Price cannot be negative");
      errorIf(request.getPrice2() < 0f, "Price cannot be negative");
      errorIf(request.getSize() < 1, "Number of cars per row must be at least one.");
      errorIf(isEmpty(request.getStreetAddress()), "Street address must be non-empty");
      errorIf(isEmpty(request.getRobotIP()), "Robot IP address must be non-empty");

      // Create parking lot
      ParkingLot result = ParkingLot.create(conn, request.getStreetAddress(), request.getSize(), request.getPrice1(), request.getPrice2(),
          request.getRobotIP());

      errorIfNull(result, "Failed to create parking lot");

      // Success
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
  public UpdatePricesResponse handle(UpdatePricesAction action, ServiceSession session) {
    return database.performQuery(new UpdatePricesResponse(), (conn, response) -> {
      // Require a logged in employee
      User user = session.requireUser();

      // Require the employee to have access rights to this action
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT), "You cannot perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_LOCAL_MANAGER, "You cannot perform this action");

      // Require a parking lot
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

  public SetFullLotResponse handle(SetFullLotAction action, ServiceSession session) {
    return database.performQuery(new SetFullLotResponse(), (conn, response) -> {
      // Require a logged in employee
      User user = session.requireUser();

      // Require the employee to have access rights to this action
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT), "You cannot perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_LOCAL_WORKER, "You cannot perform this action");

      // Check request parameters
      int[] alternativeLots = valueOrDefault(action.getAlternativeLots(), new int[] {});
      errorIf(alternativeLots.length > 10, "There can be at most 10 alternative lots");

      // Require a parking lot
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, action.getLotID());

      // Update parking lot
      lot.setLotFull(action.getLotFull());
      lot.setAlternativeLots(gson.toJson(alternativeLots));
      lot.update(conn);

      // Success
      response.setSuccess("ParkingLot status updated");
      return response;
    });
  }

  public RequestLotStateResponse handle(RequestLotStateAction action, ServiceSession session) {
    return database.performQuery(new RequestLotStateResponse(), (conn, response) -> {
      // Require a logged in employee
      session.requireUser();

      // Require a parking lot
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, action.getLotID());
      ParkingCell[][][] content = lot.constructContentArray(conn);

      response.setLot(lot);
      response.setContent(content);
      response.setSuccess("RequestLotState request completed successfully");
      return response;
    });
  }

  public ServerResponse reserveOrDisable(ServiceSession session, ServerResponse serverResponse, int lotID, int i, int j, int k, ParkingCellVisitor visitor,
      String successMessage) {
    return database.performQuery(serverResponse, (conn, response) -> {
      // Require a logged in employee
      User user = session.requireUser();

      // Require the employee to have access rights to this action
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT), "You cannot perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_LOCAL_WORKER, "You cannot perform this action");

      // Require a parking lot
      ParkingLot lot = ParkingLot.findByIDNotNull(conn, lotID);

      // Check request parameters
      errorIf(!between(j, 0, Constants.LOT_HEIGHT - 1), String.format("Parameter j must be in range [0, %s] (inclusive)", Constants.LOT_HEIGHT - 1));
      errorIf(!between(k, 0, Constants.LOT_DEPTH - 1), String.format("Parameter k must be in range [0, %s] (inclusive)", Constants.LOT_HEIGHT - 1));
      errorIf(!between(i, 0, lot.getWidth() - 1), String.format("Parameter i must be in range [0, %s] (inclusive)", lot.getWidth() - 1));

      ParkingCell cell = ParkingCell.find(conn, lot.getId(), i, j, k);
      errorIfNull(cell, String.format("Parking cell with coordinates %s, %s, %s not found", i, j, k));

      // TODO don't allow reserve/disable action if there is a car in the cell?
      visitor.call(cell);
      cell.update(conn);

      response.setSuccess(successMessage);
      return response;
    });
  }

  public ServerResponse handle(ReserveParkingSlotsAction action, ServiceSession session) {
    return reserveOrDisable(session, new ReserveParkingSlotsResponse(), action.getLotID(), action.getLocationI(), action.getLocationJ(), action.getLocationK(),
        cell -> cell.setReserved(true), "Parking cell reserved successfully");
  }

  public ServerResponse handle(DisableParkingSlotsAction action, ServiceSession session) {
    return reserveOrDisable(session, new DisableParkingSlotsResponse(), action.getLotID(), action.getLocationI(), action.getLocationJ(), action.getLocationK(),
        cell -> cell.setDisabled(true), "Parking cell disabled successfully");
  }
}
