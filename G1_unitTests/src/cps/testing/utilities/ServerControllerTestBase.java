package cps.testing.utilities;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotEquals;
import static cps.common.Utilities.getLastElement;

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
import cps.api.request.Request;
import cps.api.request.ReservedParkingRequest;
import cps.api.request.SubscriptionRequest;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ParkingExitResponse;
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
import cps.server.ServerException;
import cps.server.database.DatabaseController;
import cps.server.session.SessionHolder;
import junit.framework.TestCase;

// @SuppressWarnings("unused")
public abstract class ServerControllerTestBase extends TestCase {
  protected ServerController   server;
  protected DatabaseController db;
  protected Gson               gson   = new Gson();
  private boolean              silent = false;
  private SessionHolder        context;
  private MockTimeProvider     clock;
  private ServerConfig         config = null;

  public boolean isSilent() {
    return silent;
  }

  public void setSilent(boolean silent) {
    this.silent = silent;
  }

  public SessionHolder getContext() {
    return context;
  }

  public MockTimeProvider getClock() {
    return clock;
  }

  public void setClock(MockTimeProvider clock) {
    this.clock = clock;
  }

  public LocalDateTime getTime() {
    return clock.now();
  }

  public void setTime(LocalDateTime time) {
    clock.set(time);
  }

  public ServerConfig getConfig() {
    return config;
  }

  public void setConfig(ServerConfig config) {
    this.config = config;
  }

  public ServerControllerTestBase() {
    this.config = ServerConfig.testing();
  }

  public ServerControllerTestBase(ServerConfig config) {
    this.config = config;
  }

  @Override
  protected void setUp() throws Exception {
    this.context = new SessionHolder();
    this.clock = new MockTimeProvider();
    this.server = new ServerController(config, clock);
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

  public String formatObject(Object object) {
    return String.format("%s: %s", object.getClass().getSimpleName(), gson.toJson(object));
  }

  protected void header(String title) {
    if (!silent) {
      System.out.println("=== " + title + " ===");
    }
  }

  protected void requestSubscription(SubscriptionRequest request, SessionHolder context, CustomerData data,
      Pair<SubscriptionService, SubscriptionResponse> holder) throws ServerException {
    // Test the response
    ServerResponse response = server.dispatch(request, context);
    printObject(response);
    assertTrue(response.success());

    // Retrieve customer
    assertThat(response, instanceOf(SubscriptionResponse.class));
    SubscriptionResponse specificResponse = (SubscriptionResponse) response;
    data.setCustomerID(specificResponse.getCustomerID());
    assertEquals(1, data.getCustomerID());

    // Update subscription ID
    data.setSubsID(specificResponse.getServiceID());

    // Test database result
    assertEquals(1, db.countEntities("customer"));
    assertEquals(1, db.countEntities("subscription_service"));

    Collection<SubscriptionService> entries = db.performQuery(conn -> SubscriptionService.findByCustomerID(conn, data.getCustomerID()));
    assertEquals(1, entries.size());

    SubscriptionService entry = entries.iterator().next();
    assertNotNull(entry);

    assertEquals(entry.getSubscriptionType(), request.getSubscriptionType());
    assertEquals(entry.getCustomerID(), specificResponse.getCustomerID());
    assertEquals(entry.getEmail(), request.getEmail());
    assertEquals(entry.getCarID(), request.getCarID());
    assertEquals(entry.getLotID(), request.getLotID());
    assertEquals(entry.getStartDate(), request.getStartDate());
    assertEquals(entry.getEndDate(), request.getStartDate().plusDays(27));

    holder.setA(entry);
    holder.setB(specificResponse);
  }

  protected void requestFullSubscription(CustomerData data, SessionHolder context) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<SubscriptionService, SubscriptionResponse> holder = new Pair<>(null, null);

    // Make the request
    LocalDate startDate = getTime().toLocalDate();
    FullSubscriptionRequest request = new FullSubscriptionRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), startDate);

    // Run general tests
    requestSubscription(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(FullSubscriptionResponse.class));
  }

  protected void requestRegularSubscription(CustomerData data, SessionHolder context) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<SubscriptionService, SubscriptionResponse> holder = new Pair<>(null, null);

    // Make the request
    LocalDate startDate = getTime().toLocalDate();
    LocalTime dailyExitTime = LocalTime.of(17, 30);
    RegularSubscriptionRequest request = new RegularSubscriptionRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), startDate, data.getLotID(), dailyExitTime);

    // Run general tests
    requestSubscription(request, context, data, holder);

    // Run type-specific tests
    SubscriptionService entry = holder.getA();
    assertEquals(entry.getDailyExitTime(), request.getDailyExitTime());
    assertThat(holder.getB(), instanceOf(RegularSubscriptionResponse.class));
  }

  protected void requestOnetimeParking(OnetimeParkingRequest request, SessionHolder context, CustomerData data,
      Pair<OnetimeService, OnetimeParkingResponse> holder) throws ServerException {
    // Test the response
    OnetimeParkingResponse response = sendRequest(request, context, OnetimeParkingResponse.class);
    printObject(response);
    assertTrue(response.success());

    // Retrieve customer
    
    if (response.getCustomerID() != 0) {
      data.setCustomerID(response.getCustomerID());
      data.setPassword(response.getPassword());
    }
    
    assertNotEquals(0, data.getCustomerID());

    // Update service id
    data.setOnetimeServiceID(response.getServiceID());

    Collection<OnetimeService> entries = db.performQuery(conn -> OnetimeService.findByCustomerID(conn, data.getCustomerID()));
    assertTrue(entries.size() > 0);

    OnetimeService entry = getLastElement(entries);
    assertNotNull(entry);

    assertEquals(request.getParkingType(), entry.getParkingType());
    assertEquals(response.getCustomerID(), entry.getCustomerID());
    assertEquals(request.getEmail(), entry.getEmail());
    assertEquals(request.getCarID(), entry.getCarID());
    assertEquals(request.getLotID(), entry.getLotID());
    assertEquals(request.getPlannedEndTime(), entry.getPlannedEndTime().toLocalDateTime());
    assertEquals(false, entry.isCanceled());

    holder.setA(entry);
    holder.setB(response);
  }

  protected void requestIncidentalParking(CustomerData data, SessionHolder context) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    LocalDateTime plannedEndTime = getTime().plusHours(8).withNano(0);
    IncidentalParkingRequest request = new IncidentalParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedEndTime);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
  }

  protected void requestReservedParking(CustomerData data, Duration delta, SessionHolder context) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    LocalDateTime plannedStartTime = getTime().plus(delta).withNano(0);
    LocalDateTime plannedEndTime = plannedStartTime.plusHours(8);
    ReservedParkingRequest request = new ReservedParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedStartTime, plannedEndTime);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    OnetimeService entry = holder.getA();
    assertEquals(entry.getPlannedStartTime().toLocalDateTime(), request.getPlannedStartTime());
    assertThat(holder.getB(), instanceOf(ReservedParkingResponse.class));
  }

  protected void requestReservedParking(CustomerData data, SessionHolder context) throws ServerException {
    requestReservedParking(data, Duration.ofMillis(1500), context);
  }

  protected ParkingLot initParkingLot(String lotAddress, int width, float price1, float price2, String robotIP) throws ServerException {
    ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, lotAddress, width, price1, price2, robotIP));
    assertNotNull(lot);
    return lot;
  }

  protected ParkingLot initParkingLot(ParkingLotData data) throws ServerException {
    ParkingLot lot = initParkingLot(data.getStreetAddress(), data.getWidth(), data.getPrice1(), data.getPrice2(), data.getRobotIP());
    if (lot != null) {
      data.setLotID(lot.getId());
    }
    return lot;
  }

  protected boolean requestParkingEntry(CustomerData data, SessionHolder context, boolean weekend) throws ServerException {
    // subscriptionID = 0 means entry by OnetimeParking license
    ParkingEntryRequest request = new ParkingEntryRequest(data.getCustomerID(), data.getSubsID(), data.getLotID(), data.getCarID());
    ServerResponse response = server.dispatch(request, context);
    assertNotNull(response);
    printObject(response);

    if (!weekend) {
      assertTrue(response.success());
      assertEquals(1, db.countEntities("car_transportation"));
      CarTransportation entry = db.performQuery(conn -> CarTransportation.findByCarId(conn, data.getCarID(), data.getLotID()));
      printObject(entry);
    } else {
      assertFalse(response.success());
    }

    return response.success();
  }

  protected boolean requestParkingEntry(CustomerData data, SessionHolder context) throws ServerException {
    return requestParkingEntry(data, context, false);
  }

  protected ParkingExitResponse requestParkingExit(CustomerData data, SessionHolder context) throws ServerException {
    ParkingExitRequest request = new ParkingExitRequest(data.getCustomerID(), data.getLotID(), data.getCarID());
    printObject(request);
    
    int numCarTransportations = db.countEntities("car_transportation");

    ParkingExitResponse response = sendRequest(request, context, ParkingExitResponse.class);
    printObject(response);
    assertTrue(response.success());
    
    // Test database result
    assertEquals(numCarTransportations, db.countEntities("car_transportation"));
    Collection<CarTransportation> entries = db.performQuery(conn -> CarTransportation.findByLotID(conn, data.getLotID()));
    assertNotNull(entries);
    assertTrue(entries.size() > 0);

    // Check that the car_transportation record's removed_at field was updated
    CarTransportation entry = entries.iterator().next();
    assertNotNull(entry);
    assertNotNull(entry.getRemovedAt());
    
    return response;
  }

  protected Customer makeCustomer(CustomerData data) throws ServerException {
    Customer customer = db.performQuery(conn -> Customer.create(conn, data.getEmail(), data.getPassword()));
    assertNotNull(customer);
    printObject(customer);
    return customer;
  }

  public <T extends ServerResponse> T sendRequest(Request request, SessionHolder context, Class<T> type) {
    ServerResponse response = server.dispatch(request, context);
    assertNotNull(response);
    assertThat(response, instanceOf(type));
    return type.cast(response);
  }
}
