package cps.server.testing.utilities;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import com.google.gson.Gson;

import cps.api.request.FullSubscriptionRequest;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.OnetimeParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.request.SubscriptionRequest;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;
import cps.api.response.SubscriptionResponse;
import cps.common.Utilities.Pair;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.server.session.SessionHolder;
import junit.framework.TestCase;

//@SuppressWarnings("unused")
public abstract class ServerControllerTest extends TestCase {
	protected ServerController server;
	protected DatabaseController db;
	protected Gson gson = new Gson();
	private boolean silent = false;
	private SessionHolder context;

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public SessionHolder getContext() {
		return context;
	}

	@Override
	protected void setUp() throws Exception {
		this.context = new SessionHolder();
		this.server = new ServerController(ServerConfig.testing());
		this.db = server.getDatabaseController();
		db.truncateTables();
	}

	protected void printObject(String label, Object object) {
		if (!silent) {
			System.out.println(String.format("%s: %s", label, gson.toJson(object)));
		}
	}

	protected void printObject(Object object) {
		if (!silent) {
			System.out.println(String.format("%s: %s", object.getClass().getSimpleName(), gson.toJson(object)));
		}
	}
	
	protected void header(String title) {
		if (!silent) {
			System.out.println("=== " + title + " ===");
		}
	}
	
	protected void requestSubscription(SubscriptionRequest request, SessionHolder context, CustomerData data,
			Pair<SubscriptionService, SubscriptionResponse> holder) {
		// Test the response
		ServerResponse response = server.dispatch(request, context);
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

	protected void requestFullSubscription(CustomerData data, SessionHolder context) {
		// Holder for data to be checked later with type-specific tests
		Pair<SubscriptionService, SubscriptionResponse> holder = new Pair<>(null, null);

		// Make the request
		LocalDate startDate = LocalDate.now();
		FullSubscriptionRequest request = new FullSubscriptionRequest(data.customerID, data.email, data.carID,
				startDate);

		// Run general tests
		requestSubscription(request, context, data, holder);

		// Run type-specific tests
		assertThat(holder.getB(), instanceOf(FullSubscriptionResponse.class));
	}

	protected void requestRegularSubscription(CustomerData data, SessionHolder context) {
		// Holder for data to be checked later with type-specific tests
		Pair<SubscriptionService, SubscriptionResponse> holder = new Pair<>(null, null);

		// Make the request
		LocalDate startDate = LocalDate.now();
		LocalTime dailyExitTime = LocalTime.of(17, 30);
		RegularSubscriptionRequest request = new RegularSubscriptionRequest(data.customerID, data.email, data.carID,
				startDate, data.lotID, dailyExitTime);

		// Run general tests
		requestSubscription(request, context, data, holder);

		// Run type-specific tests
		SubscriptionService entry = holder.getA();
		assertEquals(entry.getDailyExitTime(), request.getDailyExitTime());
		assertThat(holder.getB(), instanceOf(RegularSubscriptionResponse.class));
	}

	protected void requestOnetimeParking(OnetimeParkingRequest request, SessionHolder context, CustomerData data,
			Pair<OnetimeService, OnetimeParkingResponse> holder) {
		// Test the response
		ServerResponse response = server.dispatch(request, context);
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

	protected void requestIncidentalParking(CustomerData data, SessionHolder context) {
		// Holder for data to be checked later with type-specific tests
		Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

		// Make the request
		LocalDateTime plannedEndTime = LocalDateTime.now().plusHours(8).withNano(0);
		IncidentalParkingRequest request = new IncidentalParkingRequest(data.customerID, data.email, data.carID,
				data.lotID, plannedEndTime);

		// Run general tests
		requestOnetimeParking(request, context, data, holder);

		// Run type-specific tests
		assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
	}

	protected void requestReservedParking(CustomerData data, Duration delta, SessionHolder context) {
		// Holder for data to be checked later with type-specific tests
		Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

		// Make the request
		LocalDateTime plannedStartTime = LocalDateTime.now().plus(delta).withNano(0);
		LocalDateTime plannedEndTime = plannedStartTime.plusHours(8);
		ReservedParkingRequest request = new ReservedParkingRequest(data.customerID, data.email, data.carID, data.lotID,
				plannedStartTime, plannedEndTime);

		// Run general tests
		requestOnetimeParking(request, context, data, holder);

		// Run type-specific tests
		OnetimeService entry = holder.getA();
		assertEquals(entry.getPlannedStartTime().toLocalDateTime(), request.getPlannedStartTime());
		assertThat(holder.getB(), instanceOf(ReservedParkingResponse.class));
	}

	protected void requestReservedParking(CustomerData data, SessionHolder context) {
		requestReservedParking(data, Duration.ZERO, context);
	}

	protected ParkingLot initParkingLot() {
		ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Lot Address", 3, 5, 4, "113.0.1.14"));
		assertNotNull(lot);
		printObject(lot);
		assertEquals(1, db.countEntities("parking_lot"));
		return lot;
	}

	protected void requestParkingEntry(CustomerData data, SessionHolder context) {
		// subscriptionID = 0 means entry by OnetimeParking license
		ParkingEntryRequest request = new ParkingEntryRequest(data.customerID, data.subsID, data.lotID, data.carID);
		ServerResponse response = server.dispatch(request, context);
		printObject(response);
		assertTrue(response.success());

		assertEquals(1, db.countEntities("car_transportation"));

		CarTransportation entry = db.performQuery(conn -> CarTransportation.findByCarId(conn, data.carID, data.lotID));
		printObject(entry);
	}

	protected void requestParkingExit(CustomerData data, SessionHolder context) {
		ParkingExitRequest request = new ParkingExitRequest(data.customerID, data.lotID, data.carID);

		ServerResponse response = server.dispatch(request, context);
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
	
	protected Customer makeCustomer(CustomerData data) {
		Customer customer = db.performQuery(conn -> Customer.create(conn, data.email, data.password));
		assertNotNull(customer);
		printObject(customer);
		return customer;
	}
}
