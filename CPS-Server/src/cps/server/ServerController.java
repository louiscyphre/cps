package cps.server;

import cps.api.action.*;
import cps.api.request.*;
import cps.api.response.*;
import cps.server.controllers.*;

public class ServerController implements RequestHandler {
	private final ServerConfig config;
	private final DatabaseController databaseController;
	private final LotController lotController;
	private final OnetimeParkingController onetimeParkingController;
	private final EntryExitController entryExitController;
	private final SubscriptionController subscriptionController;
	
	/**
	 * Constructs an instance of the server controller.
	 *
	 * @param config the config
	 * @throws Exception the exception
	 */
	public ServerController(ServerConfig config) throws Exception {
		this.config = config;
		
		databaseController = new DatabaseController(
				config.get("db.host"), config.get("db.name"),
				config.get("db.username"), config.get("db.password"));
		
		lotController = new LotController(this);
		onetimeParkingController = new OnetimeParkingController(this);
		entryExitController = new EntryExitController(this);
		subscriptionController = new SubscriptionController(this);
	}

	public ServerConfig getConfig() {
		return config;
	}

	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public LotController getLotController() {
		return lotController;
	}

	public OnetimeParkingController getOnetimeParkingController() {
		return onetimeParkingController;
	}
	
	public SubscriptionController getSubscriptionController() {
		return subscriptionController;
	}

	public ServerResponse dispatch(Request message) {
		ServerResponse response = message.handle(this);
		
		if (response == null) {
			return ServerResponse.error("Not implemented");
		}
		
		return response;
	}

	@Override
	public ServerResponse handle(CancelOnetimeParkingRequest request) {
		// TODO:Tegra - Under construction
		onetimeParkingController.handle(request);
		return null;
	}

	@Override
	public ServerResponse handle(ComplaintRequest request) {
		return null;
	}

	@Override
	public ServerResponse handle(FullSubscriptionRequest request) {
		return null;
	}

	@Override
	public ServerResponse handle(IncidentalParkingRequest request) {
		return onetimeParkingController.handle(request);
	}

	@Override
	public ServerResponse handle(ListOnetimeEntriesRequest request) {
		return onetimeParkingController.handle(request);
	}

	@Override
	public ServerResponse handle(OnetimeParkingRequest request) {
		return null;
	}

	@Override
	public ServerResponse handle(ParkingEntryRequest request) {
		return entryExitController.handle(request);
	}

	@Override
	public ServerResponse handle(ParkingExitRequest request) {
		return entryExitController.handle(request);
	}

	@Override
	public ServerResponse handle(RegularSubscriptionRequest request) {
		return null;
	}

	@Override
	public ServerResponse handle(ReservedParkingRequest request) {
		return onetimeParkingController.handle(request);
	}

	@Override
	public ServerResponse handle(SubscriptionRequest request) {
		return null;
	}

	@Override
	public ServerResponse handle(DisableParkingSlotsAction action) {
		return null;
	}

	@Override
	public ServerResponse handle(InitLotAction action) {
		return lotController.handle(action);
	}

	@Override
	public ServerResponse handle(RefundAction action) {
		return null;
	}

	@Override
	public ServerResponse handle(RequestLotStateAction action) {
		return null;
	}

	@Override
	public ServerResponse handle(RequestReportAction action) {
		return null;
	}

	@Override
	public ServerResponse handle(ReserveParkingSlotsAction action) {
		return null;
	}

	@Override
	public ServerResponse handle(SetFullLotAction action) {
		return null;
	}

	@Override
	public ServerResponse handle(UpdatePricesAction action) {
		return null;
	}

}
