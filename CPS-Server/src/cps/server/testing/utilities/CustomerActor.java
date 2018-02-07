package cps.server.testing.utilities;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import cps.api.request.ComplaintRequest;
import cps.api.request.FullSubscriptionRequest;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.Request;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.ComplaintResponse;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ReservedParkingResponse;
import cps.api.response.ServerResponse;
import cps.common.Utilities;
import cps.server.session.SessionHolder;

public class CustomerActor extends CustomerData implements Actor {
  enum State {
    INITIAL, PURCHASED_RESERVATION, PURCHASED_REGULAR_SUBSCRIPTION, PURCHASED_FULL_SUBSCRIPTION, PARKED, READY
  };

  int                        token;
  SessionHolder              context                = new SessionHolder();
  State                      state                  = State.INITIAL;
  State                      prevState              = null;
  LocalDateTime              plannedStartTime       = null;
  LocalDateTime              plannedEndTime         = null;
  Duration                   plannedEndTimeOffset   = null;
  Duration                   plannedStartTimeOffset = null;
  private LocalDate          startDate              = null;
  private LocalDate          endDate                = null;
  private LocalTime          dailyExitTime          = null;
  float                      credit                 = 0f;
  float                      debit                  = 0f;
  private HashSet<LocalDate> usedRegular            = new HashSet<>();

  public CustomerActor(World world, int token) {
    this.token = token;
  }

  int roll(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  long roll(long min, long max) {
    return ThreadLocalRandom.current().nextLong(min, max + 1);
  }

  private void handlePayment(float sum) {
    if (sum > 0f) {
      debit += sum;
    } else {
      credit -= sum;
    }
  }

  private void handleResponse(World world, Request request, ServerResponse response, boolean assertSuccess) {
    if (!response.success()) {
      System.out.println(world.formatObject(request));
      System.out.println(world.formatObject(response));
    }

    if (assertSuccess) {
      assertTrue(response.success());
    }
  }

  private void handleResponse(World world, Request request, ServerResponse response) {
    handleResponse(world, request, response, true);
  }

  @Override
  public void act(World world) {

    // randomly choose one of the possible actions depending on current state
    switch (state) {
    case INITIAL:
    case READY:
      // decide whether to act
      if (roll(1, 10) == 1) {
        // There is a 1/10 chance that the actor will act
        // This is done to prevent all actors from choosing an initial action at the same time
        actInitial(world);
      }
      break;
    case PARKED:
      actWaitToExitParking(world);
      break;
    case PURCHASED_RESERVATION:
      actWaitForReservation(world);
      break;
    case PURCHASED_REGULAR_SUBSCRIPTION:
      actUseRegularSubscription(world);
      break;
    case PURCHASED_FULL_SUBSCRIPTION:
      actUseFullSubscription(world);
      break;
    default:
      break;
    }
  }

  private void actWaitForReservation(World world) {
    if (world.getTime().isBefore(plannedStartTime.plus(plannedStartTimeOffset))) {
      return;
    }

    ParkingEntryRequest request = new ParkingEntryRequest(customerID, 0, lotID, carID);
    ParkingEntryResponse response = world.sendRequest(request, context, ParkingEntryResponse.class);
    handleResponse(world, request, response, false);
    
    if (response.success()) {
      pushState(State.PARKED);
    } else {
      pushState(State.READY);
    }
  }

  private void actWaitToExitParking(World world) {
    if (world.getTime().isBefore(plannedEndTime.plus(plannedEndTimeOffset))) {
      return;
    }

    ParkingExitRequest request = new ParkingExitRequest(customerID, lotID, carID);
    ParkingExitResponse response = world.sendRequest(request, context, ParkingExitResponse.class);
    handleResponse(world, request, response);
    handlePayment(response.getPayment());

    switch (prevState) {
    case PURCHASED_REGULAR_SUBSCRIPTION:
    case PURCHASED_FULL_SUBSCRIPTION:
      pushState(prevState);
    default:
      pushState(State.READY);
    }
  }

  void actInitial(World world) {
    int r;

    if (customerID == 0) { // not registered
      // initialize customer and do one of the actions that an unregistered customer can do
      email = String.format("user%d@%s.com", token, Utilities.randomString("angjurufjfjsl", 5));
      carID = String.format("CAR-%04d", token);
      r = roll(1, 4);
    } else { // already registered
      // do one of the actions that registered customers can do
      r = roll(1, 5);
    }

    // decide on an action
    switch (r) {
    case 1: // park now
      actParkNow(world);
      break;
    case 2: // reserve parking
      actReserveParking(world);
      break;
    case 3:
      actBuyRegularSubscription(world);
      break;
    case 4:
      actBuyFullSubscription(world);
      break;
    case 5:
      if (roll(1, 3) == 1) {
        // 1/3 chance to complain
        actFileComplaint(world);
      }
      break;
    default:
      break;
    }
  }

  private void actFileComplaint(World world) {
    String text = String.format("I am very disappointed with your company because %s and therefore I demand a compensation",
        Utilities.randomString("angjurufjfjsl  ", roll(10, 50)));
    ComplaintRequest request = new ComplaintRequest(customerID, lotID, text);
    ComplaintResponse response = world.sendRequest(request, context, ComplaintResponse.class);
    handleResponse(world, request, response);
  }

  private void actBuyFullSubscription(World world) {
    lotID = roll(1, world.getNumberOfParkingLots());
    startDate = world.getTime().toLocalDate().plusDays(roll(1, 10));
    endDate = startDate.plusDays(27);

    FullSubscriptionRequest request = new FullSubscriptionRequest(customerID, email, carID, startDate, lotID);
    FullSubscriptionResponse response = world.sendRequest(request, context, FullSubscriptionResponse.class);
    handleResponse(world, request, response, false);

    if (!response.success()) {
      return;
    }

    if (customerID == 0) {
      customerID = response.getCustomerID();
      password = response.getPassword();
    }

    handlePayment(response.getPayment());
    subsID = response.getServiceID(); // TODO handle multiple cars

    pushState(State.PURCHASED_FULL_SUBSCRIPTION);
  }

  private void actBuyRegularSubscription(World world) {
    lotID = roll(1, world.getNumberOfParkingLots());

    startDate = world.getTime().toLocalDate().plusDays(roll(1, 10));
    endDate = startDate.plusDays(27);
    dailyExitTime = LocalTime.of(roll(9, 23), roll(0, 59));

    RegularSubscriptionRequest request = new RegularSubscriptionRequest(customerID, email, carID, startDate, lotID, dailyExitTime);
    RegularSubscriptionResponse response = world.sendRequest(request, context, RegularSubscriptionResponse.class);
    handleResponse(world, request, response, false);

    if (!response.success()) {
      return;
    }

    if (customerID == 0) {
      customerID = response.getCustomerID();
      password = response.getPassword();
    }

    handlePayment(response.getPayment());
    subsID = response.getServiceID(); // TODO handle multiple cars
    usedRegular.clear();

    pushState(State.PURCHASED_REGULAR_SUBSCRIPTION);

  }

  private void actUseFullSubscription(World world) {
    lotID = roll(1, world.getNumberOfParkingLots());
    LocalDate today = world.getTime().toLocalDate();

    if (today.isAfter(endDate)) {
      // Subscription expired
      pushState(State.READY);
      return;
    }

    // Can park
    if (roll(1, 10) > 1) {
      // 1/10 chance to park
      return;
    }

    // TODO multiple cars
    ParkingEntryRequest request = new ParkingEntryRequest(customerID, subsID, lotID, carID);
    ParkingEntryResponse response = world.sendRequest(request, context, ParkingEntryResponse.class);
    handleResponse(world, request, response);

    plannedEndTime = world.getTime().plusHours(roll(0, 10)).plusMinutes(roll(0, 59));

    // Decide if should exit parking late, or early, or in time
    int m = 5;
    int r = roll(1, m);
    Duration full = Duration.between(world.getTime(), plannedEndTime);

    try {
      if (r == 1) { // early
        plannedEndTimeOffset = Duration.ofMinutes(-roll(0, full.getSeconds() / 60 / 2));
      } else if (r == m) { // late
        plannedEndTimeOffset = Duration.ofMinutes(roll(0, full.getSeconds() / 60 / 2));
      } else { // in time
        plannedEndTimeOffset = Duration.ZERO;
      }

    } catch (Exception ex) {
      System.out.printf("%s, %s\n", world.getTime(), plannedEndTime);
      throw ex;
    }

    pushState(State.PARKED);
  }

  private void actUseRegularSubscription(World world) {
    LocalDate today = world.getTime().toLocalDate();
    LocalTime time = world.getTime().toLocalTime();

    if (today.isAfter(endDate)) {
      // Subscription expired
      pushState(State.READY);
      return;
    }

    if (Utilities.isWeekend(today.getDayOfWeek())) {
      // Can't park on weekends
      return;
    }

    if (usedRegular.contains(today)) {
      // Already used this subscription today
      return;
    }

    if (!time.isBefore(dailyExitTime)) {
      // Too late to park
      if (today.isEqual(endDate)) {
        // Subscription expired
        pushState(State.READY);
      }
      return;
    }

    // Can park
    if (roll(1, 10) > 1) {
      // 1/10 chance to park
      return;
    }

    // TODO multiple cars
    ParkingEntryRequest request = new ParkingEntryRequest(customerID, subsID, lotID, carID);
    ParkingEntryResponse response = world.sendRequest(request, context, ParkingEntryResponse.class);
    handleResponse(world, request, response);

    plannedEndTime = today.atTime(dailyExitTime);

    // Decide if should exit parking late, or early, or in time
    int m = 5;
    int r = roll(1, m);
    Duration full = Duration.between(world.getTime(), plannedEndTime);

    try {
      if (r == 1) { // early
        plannedEndTimeOffset = Duration.ofMinutes(-roll(0, full.getSeconds() / 60 / 2));
      } else if (r == m) { // late
        plannedEndTimeOffset = Duration.ofMinutes(roll(0, full.getSeconds() / 60 / 2));
      } else { // in time
        plannedEndTimeOffset = Duration.ZERO;
      }

    } catch (Exception ex) {
      System.out.printf("%s, %s\n", world.getTime(), plannedEndTime);
      throw ex;
    }

    pushState(State.PARKED);
  }

  private void actReserveParking(World world) {
    lotID = roll(1, world.getNumberOfParkingLots());

    plannedStartTime = world.getTime().plusHours(roll(1, 100));
    plannedEndTime = plannedStartTime.plusHours(roll(1, 10));

    ReservedParkingRequest request = new ReservedParkingRequest(customerID, email, carID, lotID, plannedStartTime, plannedEndTime);
    ReservedParkingResponse response = world.sendRequest(request, context, ReservedParkingResponse.class);
    handleResponse(world, request, response);

    if (customerID == 0) {
      customerID = response.getCustomerID();
      password = response.getPassword();
    }

    handlePayment(response.getPayment());

    // Decide if should come to parking late or in time
    int m = 3;
    int r = roll(1, m);
    Duration full = Duration.between(world.getTime(), plannedEndTime);

    if (r == m) { // late
      plannedStartTimeOffset = Duration.ofMinutes(roll(1, full.getSeconds() / 60 / 2));
    } else { // in time
      plannedStartTimeOffset = Duration.ZERO;
    }

    // Decide if should exit parking late, or early, or in time
    m = 5;
    r = roll(1, m);

    if (r == 1) { // early
      plannedEndTimeOffset = Duration.ofMinutes(-roll(1, full.getSeconds() / 60 / 2));
    } else if (r == m) { // late
      plannedEndTimeOffset = Duration.ofMinutes(roll(1, full.getSeconds() / 60 / 2));
    } else { // in time
      plannedEndTimeOffset = Duration.ZERO;
    }

    pushState(State.PURCHASED_RESERVATION);
  }

  private void actParkNow(World world) {
    lotID = roll(1, world.getNumberOfParkingLots());
    plannedEndTime = world.getTime().plusHours(roll(1, 10));
    IncidentalParkingRequest request = new IncidentalParkingRequest(customerID, email, carID, lotID, plannedEndTime);
    IncidentalParkingResponse response = world.sendRequest(request, context, IncidentalParkingResponse.class);
    handleResponse(world, request, response);

    if (customerID == 0) {
      customerID = response.getCustomerID();
      password = response.getPassword();
    }

    // Decide if should exit parking late, or early, or in time
    int m = 5;
    int r = roll(1, m);
    Duration full = Duration.between(world.getTime(), plannedEndTime);

    if (r == 1) { // early
      plannedEndTimeOffset = Duration.ofMinutes(-roll(1, full.getSeconds() / 60 / 2));
    } else if (r == m) { // late
      plannedEndTimeOffset = Duration.ofMinutes(roll(1, full.getSeconds() / 60 / 2));
    } else { // in time
      plannedEndTimeOffset = Duration.ZERO;
    }

    pushState(State.PARKED);
  }

  private void pushState(State newState) {
    prevState = state;
    state = newState;
  }

}
