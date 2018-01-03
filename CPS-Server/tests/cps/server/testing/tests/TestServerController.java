package cps.server.testing.tests;

import java.time.LocalDateTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.api.request.*;
import cps.api.action.*;
import cps.api.response.*;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;

import junit.framework.TestCase;
import org.junit.Test;

import com.google.gson.Gson;

import org.junit.Assert;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("unused")
public class TestServerController extends TestCase {
	ServerController server;
	DatabaseController db;
	Gson gson = new Gson();

	@Override
	protected void setUp() throws Exception {
		this.server = new ServerController(ServerConfig.testing());
		this.db = server.getDatabaseController();
		db.truncateTables();
	}

	private void requestIncidentalParking(int customerID, String email, String carID, int lotID) {
		// Make the request
		LocalDateTime plannedEndTime = LocalDateTime.parse("2018-01-21T09:00:00");
		IncidentalParkingRequest request = new IncidentalParkingRequest(customerID, email, carID, lotID, plannedEndTime);
		
		// Test the response
		ServerResponse response = server.dispatch(request);	
		assertTrue(response.success());
		
		// Test database result 
		assertEquals(1, db.countEntities("onetime_service"));
		
		Collection<OnetimeService> entries = db.performQuery(conn -> OnetimeService.findByCustomerID(conn, customerID));
		assertEquals(1, entries.size());

		OnetimeService entry = entries.iterator().next();
		assertEquals(entry.getType(), IncidentalParkingRequest.TYPE);
		assertEquals(entry.getCustomerID(), customerID);
		assertEquals(entry.getEmail(), email);
		// TODO: finish
	}
	
	private void initParkingLot() {
		InitLotAction request = new InitLotAction(1000, "Lot Address", 3, 5, 4, "113.0.1.14");		
		ServerResponse response = server.dispatch(request);
		assertTrue(response.success());
		assertEquals(1, db.countEntities("parking_lot"));
		ParkingLot lot = db.performQuery(conn -> ParkingLot.findByID(conn, 1));
//		System.out.println(gson.toJson(lot));
	}
	
	private void requestParkingEntry(int customerID, String carID, int lotID) {
		ParkingEntryRequest request = new ParkingEntryRequest(customerID, 0, lotID, carID);
		ServerResponse response = server.dispatch(request);
		assertTrue(response.success());
		assertEquals(1, db.countEntities("car_transportation"));
		// TODO: fetch the CarTransportation and check fields
	}
	
	@Test
	public void testParkingScenario() {
		int customerID = 1;
		String email = "user@email";
		String carID = "IL11-222-33";
		int lotID = 1;
		
		initParkingLot();
		requestIncidentalParking(customerID, email, carID, lotID);
		requestParkingEntry(customerID, carID, lotID);
	}

}
