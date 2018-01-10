package cps.server.testing.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.controllers.DatabaseController;

public class TegraTests {

	ServerController server;
	DatabaseController db;
	
	@Before
	public void setUp() throws Exception {
		this.server = new ServerController(ServerConfig.testing());
		this.db = server.getDatabaseController();
		//db.truncateTables();
	}

	@Test
	public void testInsertCar() {
		/*
		 * Create parking lot
		 * Create incidental parking request
		 * Insert the car
		 */
		
		
		fail("Not yet implemented");
	}

	@Test
	public void testRetrieveCar() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleInitLotAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleUpdatePricesAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testHandleSetFullLotAction() {
		fail("Not yet implemented");
	}

}
