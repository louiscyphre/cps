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
	private final ParkingEntryController entryController;
	private final ParkingExitController exitController;
	private final SubscriptionController subscriptionController;

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
		return onetimeParkingController.handle(request);
	}

	@Override
	public ServerResponse handle(ComplaintRequest request) {
		// TODO : handle Complaint Request
		return null;
	}

	@Override
	public ServerResponse handle(FullSubscriptionRequest request) {
		return subscriptionController.handle(request);
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
	public ServerResponse handle(ParkingEntryRequest request) {
		return entryController.handle(request);
	}

	@Override
	public ServerResponse handle(ParkingExitRequest request) {
		return exitController.handle(request);
	}

	@Override
	public ServerResponse handle(RegularSubscriptionRequest request) {
		return subscriptionController.handle(request);
	}

	@Override
	public ServerResponse handle(ReservedParkingRequest request) {
		return onetimeParkingController.handle(request);
	}

	@Override
	public ServerResponse handle(DisableParkingSlotsAction action) {
		// TODO DisableParkingSlotsAction
		return null;
	}

	@Override
	public ServerResponse handle(InitLotAction action) {
		return lotController.handle(action);
	}

	@Override
	public ServerResponse handle(RefundAction action) {
		// TODO RefundAction
		return null;
	}

	@Override
	public ServerResponse handle(RequestLotStateAction action) {
		// TODO: Request Lot State Action
		return null;
	}

	@Override
	public ServerResponse handle(RequestReportAction action) {
		// TODO: Request Report Action
		return null;
	}

	@Override
	public ServerResponse handle(ReserveParkingSlotsAction action) {
		// TODO: Reserve Parking Slots Action
		return null;
	}

	@Override
	public ServerResponse handle(SetFullLotAction action) {
		return lotController.handle(action);
	}

	@Override
	public ServerResponse handle(UpdatePricesAction action) {
		return lotController.handle(action);
	}

  @Override
  public ServerResponse handle(LoginRequest request) {
    // TODO: Customer Login Request
    return null;
  }

}
