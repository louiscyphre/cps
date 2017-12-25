package cps.server.controllers;

import cps.server.ServerApplication;

public abstract class RequestController {
	protected final DatabaseController databaseController;
	protected final ServerApplication serverApplication;
	
	public RequestController(ServerApplication serverApplication) {
		this.databaseController = serverApplication.getDatabaseController();
		this.serverApplication = serverApplication;
	}
	
	public DatabaseController getDatabaseController() {
		return databaseController;
	}

	public ServerApplication getServerApplication() {
		return serverApplication;
	}

}
