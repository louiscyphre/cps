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
import cps.server.ServerException;
import cps.server.ServerController;
import cps.server.session.UserSession;
import cps.entities.models.ParkingLot;

/**
 * The Class LotController.
 */
public class LotController extends RequestController {

	/**
	 * Instantiates a new lot controller.
	 *
	 * @param serverController
	 *            serverApplication object
	 */
	public LotController(ServerController serverController) {
		super(serverController);
	}
	
	/**
	 * Handle ListParkingLotsRequest
	 * @param request
	 * @param session 
	 * @return Collection of ParkingLot objects
	 */
	public ServerResponse handle(ListParkingLotsRequest request, UserSession session) {
		return database.performQuery(conn -> {
			Collection<ParkingLot> result = ParkingLot.findAll(conn);
			
			// Filter out information that customers shouldn't see
			result.forEach(lot -> {
				lot.setContent(null);
				lot.setRobotIP(null);
			});
			
			return new ListParkingLotsResponse(result);
		});
	}


	/**
	 * Handle InitLotAction.
	 *
	 * @param request
	 *            the request
	 * @return the server response
	 */
	public InitLotResponse handle(InitLotAction request, UserSession session) {
		return database.performQuery(conn -> {
			ParkingLot result = ParkingLot.create(conn, request.getStreetAddress(), request.getSize(),
					request.getPrice1(), request.getPrice2(), request.getRobotIP());

			if (result == null) {
				return new InitLotResponse(false, "Failed to create parking lot", 0);
			}

			return new InitLotResponse(true, "Parking lot created successfully", result.getId());
		});
	}

	/**
	 * Handle Update Prices Action.
	 *
	 * @param action
	 *            the action
	 * @return the update prices response
	 */
	public UpdatePricesResponse handle(UpdatePricesAction action, UserSession session) {
		return database.performQuery(conn -> {
			ParkingLot lot = ParkingLot.findByID(conn, action.getLotID());
			lot.setPrice1(action.getPrice1());
			lot.setPrice2(action.getPrice2());
			
			try {
				lot.update(conn);
				return new UpdatePricesResponse(true, "Prices updates succsessfully");
			} catch (ServerException ex) {
				return new UpdatePricesResponse(false, ex.getMessage());
			}
		});
	}

	public SetFullLotResponse handle(SetFullLotAction action, UserSession session) {
		return database.performQuery(conn -> {
			ParkingLot lot = ParkingLot.findByID(conn, action.getLotID());
			lot.setLotFull(action.getLotFull());
			lot.setAlternativeLots(Integer.toString(action.getAlternativeLotID()));
			
			try {
				lot.update(conn);
				return new SetFullLotResponse(true, "Lot status set");
			} catch (ServerException ex) {
				return new SetFullLotResponse(false, ex.getMessage());
			}
		});
	}


	public RequestLotStateResponse handle(RequestLotStateAction action, UserSession session) {
		return database.performQuery(conn -> {
			RequestLotStateResponse response = new RequestLotStateResponse(false, "", null);
			
			try {
				ParkingLot lot = ParkingLot.findByIDNotNull(conn, action.getLotID());				
				response.setLot(lot);
				response.setSuccess("RequestLotState request completed successfully");
			} catch (ServerException e) {
				response.setError(e.getMessage());
			}
			
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
