package cps.server;

import com.google.gson.Gson;

import cps.api.action.*;
import cps.api.request.*;
import cps.api.response.*;
import cps.server.controllers.*;

public class ServerController {
	Gson gson = new Gson();
	private ServerConfig config;
	private DatabaseController databaseController;
	private LotController lotController;
	private OnetimeParkingController onetimeParkingController;
	private EntryExitController entryExitController;
	
	/**
	 * Constructs an instance of the server controller.
	 *
	 * @param config the config
	 * @throws Exception the exception
	 */
	public ServerController(ServerConfig config) throws Exception {
		this.config = config;
		databaseController = new DatabaseController(config.get("db.host"), config.get("db.name"),
				config.get("db.username"), config.get("db.password"));
		lotController = new LotController(this);
		onetimeParkingController = new OnetimeParkingController(this);
		entryExitController = new EntryExitController(this);
	}

	public ServerConfig getConfig() {
		return config;
	}

	public void setConfig(ServerConfig config) {
		this.config = config;
	}

	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public void setDatabaseController(DatabaseController databaseController) {
		this.databaseController = databaseController;
	}

	public LotController getLotController() {
		return lotController;
	}

	public void setRobotController(LotController lotController) {
		this.lotController = lotController;
	}

	public OnetimeParkingController getOnetimeParkingController() {
		return onetimeParkingController;
	}

	public void setOnetimeParkingController(OnetimeParkingController onetimeParkingController) {
		this.onetimeParkingController = onetimeParkingController;
	}
	
	public ServerResponse handle(Object message) {
		// TODO: replace this with a better dispatch method
		if (message instanceof IncidentalParkingRequest) {
			return onetimeParkingController.handle((IncidentalParkingRequest) message);
		} else if (message instanceof ListOnetimeEntriesRequest) {
			return onetimeParkingController.handle((ListOnetimeEntriesRequest) message);
		} else if (message instanceof ParkingEntryRequest) {
			return entryExitController.handle((ParkingEntryRequest) message);
		} else if (message instanceof ParkingExitRequest) {
			return entryExitController.handle((ParkingExitRequest) message);
		} else if (message instanceof InitLotAction) {
			return lotController.handle((InitLotAction) message);
		} else {
			return ServerResponse.error("Unknown request");
		}
	}

}
