package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.server.testing.utilities.CustomerData;
import cps.api.request.*;
import cps.api.action.*;
import cps.api.response.*;
import cps.common.Utilities.Pair;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
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
	
	@Test
	public void testLogin() {
		/* Scenario:
		 * 1. Create user
		 * 2. Attempt to login as user
		 * 3. Attempt to login with a wrong password
		 * 4. Attempt to login with a wrong email
		 */

		// Create user
		CustomerData data = new CustomerData(0, "user@email", "1234", "IL11-222-33", 1, 0);
		Customer customer = db.performQuery(conn -> Customer.create(conn, data.email, data.password));
	 	assertNotNull(customer);
		System.out.println(gson.toJson(customer));
		
		// Create login request
		LoginRequest request = new LoginRequest(data.email, data.password);
		
		// Test the response
		ServerResponse response = server.handle(request); 	
		System.out.println(gson.toJson(response));
		assertThat(response, instanceOf(LoginResponse.class));
		assertTrue(response.success());
		
		LoginResponse loginResponse = (LoginResponse) response;
		assertEquals(customer.getId(), loginResponse.getCustomerID());
	}
	
	@Test
	public void testIncidentalParking() {
		/* Scenario:
		 * 1. Create Parking Lot
		 * 2. Send Incidental Parking request
		 * 3. Send Parking Entry request - license: IncidentalParking
		 * 4. Send Parking Exit request */
		
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
		
		initParkingLot();
		requestIncidentalParking(data);
		requestParkingEntry(data);
		requestParkingExit(data);
	}
	
	@Test
	public void testReservedParking() {
		/* Scenario:
		 * 1. Create Parking Lot
		 * 2. Send Reserved Parking request
		 * 3. Send Parking Entry request - license: ReservedParking
		 * 4. Send Parking Exit request */
		
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
		
		initParkingLot();
		requestReservedParking(data);
		requestParkingEntry(data);
		requestParkingExit(data);
	}
	
	@Test
	public void testFullSubscription() {
		/* Scenario:
		 * 1. Create Parking Lot
		 * 2. Send Full Subscription request
		 * 3. Send Parking Entry request - license: FullSubscription
		 * 4. Send Parking Exit request */
		
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
		
		initParkingLot();
		requestFullSubscription(data);
		requestParkingEntry(data);
		requestParkingExit(data);
		
	}
	
	@Test
	public void testRegularSubscription() {
		/* Scenario:
		 * 1. Create Parking Lot
		 * 2. Send Full Subscription request
		 * 3. Send Parking Entry request - license: FullSubscription
		 * 4. Send Parking Exit request */
		
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
		
		initParkingLot();
		requestRegularSubscription(data);
		requestParkingEntry(data);
		requestParkingExit(data);
		
	}
	
	private void requestSubscription(CustomerData data, SubscriptionRequest request, Pair<SubscriptionService, SubscriptionResponse> holder) {		
		// Test the response
		ServerResponse response = server.dispatch(request);
//		System.out.println(gson.toJson(response));
		assertTrue(response.success());
		
		// Retrieve customer
		assertThat(response, instanceOf(SubscriptionResponse.class));
		SubscriptionResponse specificResponse = (SubscriptionResponse) response;
		data.customerID = specificResponse.getCustomerID();
		assertEquals(1, data.customerID);
		
		// Update subscription ID
		data.subsID = specificResponse.getServiceID();
		
		// Test database result
		assertEquals(1, db.countEntities("customer"));
		assertEquals(1, db.countEntities("subscription_service"));
		
		Collection<SubscriptionService> entries = db.performQuery(conn -> SubscriptionService.findByCustomerID(conn, data.customerID));
		assertEquals(1, entries.size());

		SubscriptionService entry = entries.iterator().next();
		assertNotNull(entry);		

		assertEquals(entry.getSubscriptionType(), request.getSubscriptionType());
		assertEquals(entry.getCustomerID(), specificResponse.getCustomerID());
		assertEquals(entry.getEmail(), request.getEmail());
		assertEquals(entry.getCarID(), request.getCarID());
		assertEquals(entry.getLotID(), request.getLotID());
		assertEquals(entry.getStartDate(), request.getStartDate());
		assertEquals(entry.getEndDate(), request.getStartDate().plusDays(28));
		
		holder.setA(entry);
		holder.setB(specificResponse);
	}
	
	private void requestFullSubscription(CustomerData data) {
		// Holder for data to be checked later with type-specific tests
		Pair<SubscriptionService, SubscriptionResponse> holder = new Pair<>(null, null);
		
		// Make the request
		LocalDate startDate = LocalDate.now();
		FullSubscriptionRequest request = new FullSubscriptionRequest(data.customerID, data.email, data.carID, startDate);
		
		// Run general tests
		requestSubscription(data, request, holder);
		
		// Run type-specific tests
		assertThat(holder.getB(), instanceOf(FullSubscriptionResponse.class));
	}
	
	private void requestRegularSubscription(CustomerData data) {
		// Holder for data to be checked later with type-specific tests
		Pair<SubscriptionService, SubscriptionResponse> holder = new Pair<>(null, null);
		
		// Make the request
		LocalDate startDate = LocalDate.now();
		LocalTime dailyExitTime = LocalTime.of(17, 30);
		RegularSubscriptionRequest request = new RegularSubscriptionRequest(data.customerID, data.email, data.carID, startDate, data.lotID, dailyExitTime);
		
		// Run general tests
		requestSubscription(data, request, holder);
		
		// Run type-specific tests
		SubscriptionService entry = holder.getA();
		assertEquals(entry.getDailyExitTime(), request.getDailyExitTime());
		assertThat(holder.getB(), instanceOf(RegularSubscriptionResponse.class));
	}
	
	private void requestOnetimeParking(CustomerData data, OnetimeParkingRequest request, Pair<OnetimeService, OnetimeParkingResponse> holder) {
		// Test the response
		ServerResponse response = server.dispatch(request); 	
//		System.out.println(gson.toJson(response));
		assertTrue(response.success());
		
		// Retrieve customer
		assertThat(response, instanceOf(OnetimeParkingResponse.class));
		OnetimeParkingResponse specificResponse = (OnetimeParkingResponse) response;
		data.customerID = specificResponse.getCustomerID();
		assertEquals(1, data.customerID);
		
		// Test database result
		assertEquals(1, db.countEntities("customer"));
		assertEquals(1, db.countEntities("onetime_service"));
		
		Collection<OnetimeService> entries = db.performQuery(conn -> OnetimeService.findByCustomerID(conn, data.customerID));
		assertEquals(1, entries.size());

		OnetimeService entry = entries.iterator().next();
		assertNotNull(entry);		

		assertEquals(entry.getParkingType(), request.getParkingType());
		assertEquals(entry.getCustomerID(), specificResponse.getCustomerID());
		assertEquals(entry.getEmail(), request.getEmail());
		assertEquals(entry.getCarID(), request.getCarID());
		assertEquals(entry.getLotID(), request.getLotID());
		assertEquals(entry.getPlannedEndTime().toLocalDateTime(), request.getPlannedEndTime());
		assertEquals(entry.isCanceled(), false);
		
		holder.setA(entry);
		holder.setB(specificResponse);
	}

	private void requestIncidentalParking(CustomerData data) {
		// Holder for data to be checked later with type-specific tests
		Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);
		
		// Make the request
		LocalDateTime plannedEndTime = LocalDateTime.parse("2018-01-21T17:00:00");
		IncidentalParkingRequest request = new IncidentalParkingRequest(data.customerID, data.email, data.carID, data.lotID, plannedEndTime);
		
		// Run general tests
		requestOnetimeParking(data, request, holder);
		
		// Run type-specific tests
		assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
	}

	private void requestReservedParking(CustomerData data) {
		// Holder for data to be checked later with type-specific tests
		Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);
		
		// Make the request
		LocalDateTime plannedStartTime = LocalDateTime.parse("2018-01-21T09:00:00");
		LocalDateTime plannedEndTime = LocalDateTime.parse("2018-01-21T17:00:00");
		ReservedParkingRequest request = new ReservedParkingRequest(data.customerID, data.email, data.carID, data.lotID, plannedStartTime, plannedEndTime);
		
		// Run general tests
		requestOnetimeParking(data, request, holder);
		
		// Run type-specific tests
		OnetimeService entry = holder.getA();
		assertEquals(entry.getPlannedStartTime().toLocalDateTime(), request.getPlannedStartTime());
		assertThat(holder.getB(), instanceOf(ReservedParkingResponse.class));
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
	
	private void requestParkingEntry(CustomerData data) {
		ParkingEntryRequest request = new ParkingEntryRequest(data.customerID, data.subsID, data.lotID, data.carID); // subscriptionID = 0 means entry by OnetimeParking license
		ServerResponse response = server.dispatch(request);
//		System.out.println(gson.toJson(response));
		assertTrue(response.success());
		assertEquals(1, db.countEntities("car_transportation"));
		// TODO: fetch the CarTransportation and check fields
	}
	
	private void requestParkingExit(CustomerData data) {
		ParkingExitRequest request = new ParkingExitRequest(data.customerID, data.lotID, data.carID);
		ServerResponse response = server.dispatch(request);
//		System.out.println(gson.toJson(response));
		assertTrue(response.success());
		assertEquals(1, db.countEntities("car_transportation"));
		// TODO: fetch the CarTransportation and check fields
	}
}
