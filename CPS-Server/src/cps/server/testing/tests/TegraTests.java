package cps.server.testing.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import cps.api.action.InitLotAction;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ServerResponse;
import cps.common.Utilities;
import cps.common.Utilities.Pair;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.server.testing.utilities.CustomerData;

@SuppressWarnings("unused")
public class TegraTests {

	ServerController server;
	DatabaseController db;

	@Before
	public void setUp() throws Exception {
		this.server = new ServerController(ServerConfig.testing());
		this.db = server.getDatabaseController();
		// db.truncateTables();
	}

	@Test
	public void testInsertCar() {
		/*
		 * Create parking lot 
		 * Create incidental parking request 
		 * Insert the car
		 */

		ParkingLot lot = initParkingLot();
		CustomerData data = new CustomerData((int) Math.random() * 1000,
				Utilities.randomString("abcdefghijklmnopqrstuvwxyz", 8), Utilities.randomString("1234567890", 4),
				Utilities.randomString("1234567890ABCDTRUOTSKL", 7), 1, 0);
		

	}

	private ParkingLot initParkingLot() {
		ParkingLot lt = null;
		InitLotAction request = new InitLotAction(1, "Sesam 2", 4, 5, 3, "12.f.t43");
		ServerResponse response = server.dispatch(request);
		InitLotResponse re2 = (InitLotResponse) response;
		lt = db.performQuery(conn -> ParkingLot.findByID(conn, re2.getLotID()));
		return lt;
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
