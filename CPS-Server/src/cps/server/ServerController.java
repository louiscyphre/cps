package cps.server;

import cps.api.action.*;
import cps.api.request.*;
import cps.api.response.*;
import cps.server.controllers.*;
import cps.server.session.UserSession;

public class ServerController implements RequestHandler {
	private final ServerConfig config;
	private final DatabaseController databaseController;
	private final LotController lotController;
	private final OnetimeParkingController onetimeParkingController;
	private final ParkingEntryController entryController;
	private final ParkingExitController exitController;
	private final SubscriptionController subscriptionController;
	private final CustomerController userController;
	private final ComplaintController complaintController;
	private final CarTransportationController transportationController;

	/**
	 * Constructs an instance of the server controller.
	 *
	 * @param config
	 *            the config
	 * @throws Exception
	 *             the exception
	 */
	public ServerController(ServerConfig config) throws Exception {
		this.config = config;

		databaseController = new DatabaseController(config.get("db.host"), config.get("db.name"),
				config.get("db.username"), config.get("db.password"));

		lotController = new LotController(this);
		onetimeParkingController = new OnetimeParkingController(this);
		entryController = new ParkingEntryController(this);
		exitController = new ParkingExitController(this);
		subscriptionController = new SubscriptionController(this);
		userController = new CustomerController(this);
		complaintController = new ComplaintController(this);
		transportationController = new CarTransportationController(this);
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

	public CarTransportationController getTransportationController() {
		return transportationController;
	}

	public ServerResponse dispatch(Request message, UserSession session) {
		ServerResponse response = message.handle(this, session);

		if (response == null) {
			return ServerResponse.error("Not implemented");
		}

		return response;
	}

	@Override
	public ServerResponse handle(CancelOnetimeParkingRequest request, UserSession session) {
		return onetimeParkingController.handle(request, session);
	}

	@Override
	public ServerResponse handle(ComplaintRequest request, UserSession session) {
		return complaintController.handle(request, session);
	}

	@Override
	public ServerResponse handle(FullSubscriptionRequest request, UserSession session) {
		return subscriptionController.handle(request, session);
	}

	@Override
	public ServerResponse handle(IncidentalParkingRequest request, UserSession session) {
		return onetimeParkingController.handle(request, session);
	}

	@Override
	public ServerResponse handle(ListOnetimeEntriesRequest request, UserSession session) {
		return onetimeParkingController.handle(request, session);
	}

	@Override
	public ServerResponse handle(ListParkingLotsRequest request, UserSession session) {
		return lotController.handle(request, session);
	}

	@Override
	public ServerResponse handle(ParkingEntryRequest request, UserSession session) {
		return entryController.handle(request, session);
	}

	@Override
	public ServerResponse handle(ParkingExitRequest request, UserSession session) {
		return exitController.handle(request, session);
	}

	@Override
	public ServerResponse handle(RegularSubscriptionRequest request, UserSession session) {
		return subscriptionController.handle(request, session);
	}

	@Override
	public ServerResponse handle(ReservedParkingRequest request, UserSession session) {
		return onetimeParkingController.handle(request, session);
	}

	@Override
	public ServerResponse handle(LoginRequest request, UserSession session) {
		return userController.handle(request, session);
	}

	@Override
	public ServerResponse handle(DisableParkingSlotsAction action, UserSession session) {
		// TODO DisableParkingSlotsAction
		return null;
	}

	@Override
	public ServerResponse handle(InitLotAction action, UserSession session) {
		return lotController.handle(action, session);
	}

	@Override
	public ServerResponse handle(RefundAction action, UserSession session) {
		// TODO RefundAction
		return null;
	}

	@Override
	public ServerResponse handle(RequestLotStateAction action, UserSession session) {
		return lotController.handle(action, session);
	}

	@Override
	public ServerResponse handle(RequestReportAction action, UserSession session) {
		// TODO: Request Report Action
		return null;
	}

	@Override
	public ServerResponse handle(ReserveParkingSlotsAction action, UserSession session) {
		// TODO: Reserve Parking Slots Action
		return null;
	}

	@Override
	public ServerResponse handle(SetFullLotAction action, UserSession session) {
		return lotController.handle(action, session);
	}

	@Override
	public ServerResponse handle(UpdatePricesAction action, UserSession session) {
		return lotController.handle(action, session);
	}
}
