package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.server.testing.utilities.CustomerData;
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

	private void requestIncidentalParking(CustomerData data) {
		// Make the request
		LocalDateTime plannedEndTime = LocalDateTime.parse("2018-01-21T17:00:00");
		IncidentalParkingRequest request = new IncidentalParkingRequest(data.customerID, data.email, data.carID, data.lotID, plannedEndTime);
		
		// Test the response
		ServerResponse response = server.dispatch(request);	
		assertTrue(response.success());
		
		// Test database result 
		assertEquals(1, db.countEntities("onetime_service"));
		
		Collection<OnetimeService> entries = db.performQuery(conn -> OnetimeService.findByCustomerID(conn, data.customerID));
		assertEquals(1, entries.size());

		OnetimeService entry = entries.iterator().next();
		checkEntryFields(entry, request);
	}

	private void requestReservedParking(CustomerData data) {
		// Make the request
		LocalDateTime plannedStartTime = LocalDateTime.parse("2018-01-21T09:00:00");
		LocalDateTime plannedEndTime = LocalDateTime.parse("2018-01-21T17:00:00");
		ReservedParkingRequest request = new ReservedParkingRequest(data.customerID, data.email, data.carID, data.lotID, plannedStartTime, plannedEndTime);
		
		// Test the response
		ServerResponse response = server.dispatch(request);	
		assertTrue(response.success());
		
		// Test database result 
		assertEquals(1, db.countEntities("onetime_service"));
		
		Collection<OnetimeService> entries = db.performQuery(conn -> OnetimeService.findByCustomerID(conn, data.customerID));
		assertEquals(1, entries.size());

		OnetimeService entry = entries.iterator().next();
		checkEntryFields(entry, request);
		assertEquals(entry.getPlannedStartTime().toLocalDateTime(), request.getPlannedStartTime());
	}
	
	private void checkEntryFields(OnetimeService entry, OnetimeParkingRequest request) {
		assertEquals(entry.getParkingType(), request.getParkingType());
		assertEquals(entry.getCustomerID(), request.getCustomerID());
		assertEquals(entry.getEmail(), request.getEmail());
		assertEquals(entry.getCarID(), request.getCarID());
		assertEquals(entry.getLotID(), request.getLotID());
		assertEquals(entry.getPlannedEndTime().toLocalDateTime(), request.getPlannedEndTime());
		assertEquals(entry.isCanceled(), false);
		
	}
	
	private void initParkingLot() {
		InitLotAction request = new InitLotAction(1000, "Lot Address", 3, 5, 4, "113.0.1.14");		
		ServerResponse response = server.dispatch(request);
//		System.out.println(gson.toJson(response));
		assertTrue(response.success());
		assertEquals(1, db.countEntities("parking_lot"));
		ParkingLot lot = db.performQuery(conn -> ParkingLot.findByID(conn, 1));
//		System.out.println(gson.toJson(lot));
	}
	
	private void requestEntryForOnetimeParking(CustomerData data) {
		ParkingEntryRequest request = new ParkingEntryRequest(data.customerID, 0, data.lotID, data.carID); // subscriptionID = 0 means entry by OnetimeParking license
		ServerResponse response = server.dispatch(request);
		assertTrue(response.success());
		assertEquals(1, db.countEntities("car_transportation"));
		// TODO: fetch the CarTransportation and check fields
	}
	
	private void requestParkingExit(CustomerData data) {
		ParkingExitRequest request = new ParkingExitRequest(data.customerID, data.lotID, data.carID);
		ServerResponse response = server.dispatch(request);
		System.out.println(gson.toJson(response));
		assertTrue(response.success());
		assertEquals(1, db.countEntities("car_transportation"));
		// TODO: fetch the CarTransportation and check fields
	}
	
	@Test
	public void testIncidentalParking() {
		/* Scenario:
		 * 1. Create Parking Lot
		 * 2. Send Incidental Parking request
		 * 3. Send Parking Entry request - license: IncidentalParking
		 * 4. Send Parking Exit request */
		
		CustomerData data = new CustomerData(1, "user@email", "", "IL11-222-33", 1);
		
		initParkingLot();
		requestIncidentalParking(data);
		requestEntryForOnetimeParking(data);
		requestParkingExit(data);
	}
	
	@Test
	public void testReservedParking() {
		/* Scenario:
		 * 1. Create Parking Lot
		 * 2. Send Reserved Parking request
		 * 3. Send Parking Entry request - license: ReservedParking
		 * 4. Send Parking Exit request */
		
		CustomerData data = new CustomerData(1, "user@email", "", "IL11-222-33", 1);
		
		initParkingLot();
		requestReservedParking(data);
		requestEntryForOnetimeParking(data);
		requestParkingExit(data);
	}
}
