package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.Duration;
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
import cps.common.Constants;
import cps.common.Utilities.Pair;
import cps.entities.models.CarTransportation;
import cps.entities.models.Complaint;
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

	private void printObject(String label, Object object) {
		System.out.println(String.format("%s: %s", label, gson.toJson(object)));
	}

	private void printObject(Object object) {
		System.out.println(String.format("%s: %s", object.getClass().getSimpleName(), gson.toJson(object)));
	}

	@Test
	public void testLogin() {
		/*
		 * Scenario: 1. Create user 2. Attempt to login as user 3. Attempt to login with
		 * a wrong password 4. Attempt to login with a wrong email
		 */

		System.out.println("=== testLogin ===");

		// Create user
		CustomerData data = new CustomerData(0, "user@email", "1234", "IL11-222-33", 1, 0);
		Customer customer = db.performQuery(conn -> Customer.create(conn, data.email, data.password));
		assertNotNull(customer);
		printObject(customer);

		// Create login request
		LoginRequest request = new LoginRequest(data.email, data.password);

		// Test the response
		ServerResponse response = server.handle(request);
		printObject(response);
		assertThat(response, instanceOf(LoginResponse.class));
		assertTrue(response.success());

		LoginResponse loginResponse = (LoginResponse) response;
		assertEquals(customer.getId(), loginResponse.getCustomerID());
	}

	@Test
	public void testIncidentalParking() {
		/*
		 * Scenario: 1. Create Parking Lot 2. Send Incidental Parking request 3. Send
		 * Parking Entry request - license: IncidentalParking 4. Send Parking Exit
		 * request
		 */

		System.out.println("=== testIncidentalParking ===");
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

		initParkingLot();
		requestIncidentalParking(data);
		requestParkingEntry(data);
		requestParkingExit(data);
	}

	@Test
	public void testReservedParking() {
		/*
		 * Scenario: 1. Create Parking Lot 2. Send Reserved Parking request 3. Send
		 * Parking Entry request - license: ReservedParking 4. Send Parking Exit request
		 */

		System.out.println("=== testReservedParking ===");
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

		initParkingLot();
		requestReservedParking(data);
		requestParkingEntry(data);
		requestParkingExit(data);
	}

	@Test
	public void testFullSubscription() {
		/*
		 * Scenario: 1. Create Parking Lot 2. Send Full Subscription request 3. Send
		 * Parking Entry request - license: FullSubscription 4. Send Parking Exit
		 * request
		 */

		System.out.println("=== testFullSubscription ===");
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

		initParkingLot();
		requestFullSubscription(data);
		requestParkingEntry(data);
		requestParkingExit(data);

	}

	@Test
	public void testRegularSubscription() {
		/*
		 * Scenario: 1. Create Parking Lot 2. Send Full Subscription request 3. Send
		 * Parking Entry request - license: FullSubscription 4. Send Parking Exit
		 * request
		 */

		System.out.println("=== testRegularSubscription ===");
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

		initParkingLot();
		requestRegularSubscription(data);
		requestParkingEntry(data);
		requestParkingExit(data);

	}

	@Test
	public void testCancelOnetimeParking() {
		/*
		 * Scenario: 1. Create Parking Lot 2. Send Reserved Parking request 3. Send
		 * Cancel Onetime Parking request
		 */

		System.out.println("=== testCancelOnetimeParking ===");
		CustomerData data = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);

		initParkingLot();
		requestReservedParking(data, Duration.ofHours(3).plusMinutes(1));

		// Make request
		CancelOnetimeParkingRequest request = new CancelOnetimeParkingRequest(data.customerID, data.onetimeServiceID);

		// Test response
		ServerResponse response = server.handle(request);
		printObject(response);
		assertTrue(response.success());
		assertThat(response, instanceOf(CancelOnetimeParkingResponse.class));
		CancelOnetimeParkingResponse specificResponse = (CancelOnetimeParkingResponse) response;
		assertEquals(request.getCustomerID(), specificResponse.getCustomerID());
		assertEquals(request.getOnetimeServiceID(), specificResponse.getOnetimeServiceID());
	}

	@Test
	public void testListParkingLots() {
		System.out.println("=== testListParkingLots ===");

		// Create lots
		for (int i = 1; i <= 3; i++) {
			InitLotAction request = new InitLotAction(1000, "Lot " + i + " Address", 3, 5, 4, "113.0.1.1" + i);
			ServerResponse response = server.dispatch(request);
			assertTrue(response.success());
		}

		assertEquals(3, db.countEntities("parking_lot"));

		// Make request
		ListParkingLotsRequest request = new ListParkingLotsRequest();

		// Test response
		ServerResponse response = server.handle(request);
		assertNotNull(response);
		printObject(response);
		assertTrue(response.success());
		assertThat(response, instanceOf(ListParkingLotsResponse.class));

		ListParkingLotsResponse specificResponse = (ListParkingLotsResponse) response;
		Collection<ParkingLot> parkingLots = specificResponse.getData();
		assertNotNull(parkingLots);
		assertEquals(3, parkingLots.size());
	}

	@Test
	public void testComplaint() {
		System.out.println("=== testComplaint ===");

		// Create user
		CustomerData data = new CustomerData(0, "user@email", "1234", "IL11-222-33", 1, 0);
		Customer customer = db.performQuery(conn -> Customer.create(conn, data.email, data.password));
		assertNotNull(customer);
		printObject(customer);

		// Make request
		ComplaintRequest request = new ComplaintRequest(data.customerID, "My car was damaged");

		// Test response
		ServerResponse response = server.handle(request);
		assertNotNull(response);
		printObject(response);
		assertThat(response, instanceOf(ComplaintResponse.class));
		
		ComplaintResponse specificResponse = (ComplaintResponse) response;

		// Test database result
		assertEquals(1, db.countEntities("complaint"));
		Collection<Complaint> entries = db
				.performQuery(conn -> db.findAll(conn, "complaint", "id", resultSet -> new Complaint(resultSet)));
		assertNotNull(entries);
		assertEquals(1, entries.size());
		Complaint entry = entries.iterator().next();
		assertNotNull(entry);
		printObject(entry);
		assertEquals(specificResponse.getComplaintID(), entry.getId());
		assertEquals(request.getCustomerID(), entry.getCustomerID());
		assertEquals(request.getContent(), entry.getDescription());
		assertEquals(Constants.COMPLAINT_STATUS_PROCESSING, entry.getStatus());
		assertEquals(0, entry.getEmployeeID());
	}

	private void requestSubscription(CustomerData data, SubscriptionRequest request,
			Pair<SubscriptionService, SubscriptionResponse> holder) {
		// Test the response
		ServerResponse response = server.dispatch(request);
		printObject(response);
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

		Collection<SubscriptionService> entries = db
				.performQuery(conn -> SubscriptionService.findByCustomerID(conn, data.customerID));
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
		FullSubscriptionRequest request = new FullSubscriptionRequest(data.customerID, data.email, data.carID,
				startDate);

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
		RegularSubscriptionRequest request = new RegularSubscriptionRequest(data.customerID, data.email, data.carID,
				startDate, data.lotID, dailyExitTime);

		// Run general tests
		requestSubscription(data, request, holder);

		// Run type-specific tests
		SubscriptionService entry = holder.getA();
		assertEquals(entry.getDailyExitTime(), request.getDailyExitTime());
		assertThat(holder.getB(), instanceOf(RegularSubscriptionResponse.class));
	}

	private void requestOnetimeParking(CustomerData data, OnetimeParkingRequest request,
			Pair<OnetimeService, OnetimeParkingResponse> holder) {
		// Test the response
		ServerResponse response = server.dispatch(request);
		printObject(response);
		assertTrue(response.success());

		// Retrieve customer
		assertThat(response, instanceOf(OnetimeParkingResponse.class));
		OnetimeParkingResponse specificResponse = (OnetimeParkingResponse) response;
		data.customerID = specificResponse.getCustomerID();
		assertEquals(1, data.customerID);

		// Update service id
		data.onetimeServiceID = specificResponse.getServiceID();

		// Test database result
		assertEquals(1, db.countEntities("customer"));
		assertEquals(1, db.countEntities("onetime_service"));

		Collection<OnetimeService> entries = db
				.performQuery(conn -> OnetimeService.findByCustomerID(conn, data.customerID));
		assertEquals(1, entries.size());

		OnetimeService entry = entries.iterator().next();
		assertNotNull(entry);

		assertEquals(request.getParkingType(), entry.getParkingType());
		assertEquals(specificResponse.getCustomerID(), entry.getCustomerID());
		assertEquals(request.getEmail(), entry.getEmail());
		assertEquals(request.getCarID(), entry.getCarID());
		assertEquals(request.getLotID(), entry.getLotID());
		assertEquals(request.getPlannedEndTime(), entry.getPlannedEndTime().toLocalDateTime());
		assertEquals(false, entry.isCanceled());

		holder.setA(entry);
		holder.setB(specificResponse);
	}

	private void requestIncidentalParking(CustomerData data) {
		// Holder for data to be checked later with type-specific tests
		Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

		// Make the request
		LocalDateTime plannedEndTime = LocalDateTime.now().plusHours(8).withNano(0);
		IncidentalParkingRequest request = new IncidentalParkingRequest(data.customerID, data.email, data.carID,
				data.lotID, plannedEndTime);

		// Run general tests
		requestOnetimeParking(data, request, holder);

		// Run type-specific tests
		assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
	}

	private void requestReservedParking(CustomerData data, Duration delta) {
		// Holder for data to be checked later with type-specific tests
		Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

		// Make the request
		LocalDateTime plannedStartTime = LocalDateTime.now().plus(delta).withNano(0);
		LocalDateTime plannedEndTime = plannedStartTime.plusHours(8);
		ReservedParkingRequest request = new ReservedParkingRequest(data.customerID, data.email, data.carID, data.lotID,
				plannedStartTime, plannedEndTime);

		// Run general tests
		requestOnetimeParking(data, request, holder);

		// Run type-specific tests
		OnetimeService entry = holder.getA();
		assertEquals(entry.getPlannedStartTime().toLocalDateTime(), request.getPlannedStartTime());
		assertThat(holder.getB(), instanceOf(ReservedParkingResponse.class));
	}

	private void requestReservedParking(CustomerData data) {
		requestReservedParking(data, Duration.ZERO);
	}

	private void initParkingLot() {
		InitLotAction request = new InitLotAction(1000, "Lot Address", 3, 5, 4, "113.0.1.14");
		ServerResponse response = server.dispatch(request);
		printObject(response);
		assertTrue(response.success());
		assertEquals(1, db.countEntities("parking_lot"));
		// ParkingLot lot = db.performQuery(conn -> ParkingLot.findByID(conn, 1));
		// printObject(lot);
	}

	private void requestParkingEntry(CustomerData data) {
		ParkingEntryRequest request = new ParkingEntryRequest(data.customerID, data.subsID, data.lotID, data.carID); // subscriptionID
																														// =
																														// 0
																														// means
																														// entry
																														// by
																														// OnetimeParking
																														// license

		ServerResponse response = server.dispatch(request);
		printObject(response);
		assertTrue(response.success());

		assertEquals(1, db.countEntities("car_transportation"));

		CarTransportation entry = db.performQuery(conn -> CarTransportation.findByCarId(conn, data.carID, data.lotID));
		printObject(entry);
	}

	private void requestParkingExit(CustomerData data) {
		ParkingExitRequest request = new ParkingExitRequest(data.customerID, data.lotID, data.carID);

		ServerResponse response = server.dispatch(request);
		printObject(response);
		assertTrue(response.success());
		assertEquals(1, db.countEntities("car_transportation"));

		Collection<CarTransportation> entries = db
				.performQuery(conn -> CarTransportation.findByLotID(conn, data.lotID));
		assertEquals(1, entries.size());

		CarTransportation entry = entries.iterator().next();
		assertNotNull(entry);
		assertNotNull(entry.getRemovedAt());
		printObject(entry);
	}
}
