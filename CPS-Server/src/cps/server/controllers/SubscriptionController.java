package cps.server.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import cps.api.request.FullSubscriptionRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.SubscriptionRequest;
import cps.api.response.FullSubscriptionResponse;
import cps.api.response.RegularSubscriptionResponse;
import cps.api.response.ServerResponse;
import cps.api.response.SubscriptionResponse;
import cps.common.Constants;
import cps.common.Utilities;
import cps.entities.models.Customer;
import cps.entities.models.ParkingLot;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;
import cps.server.session.CustomerSession;
import cps.server.statistics.StatisticsCollector;

/** Processes customer requests for purchasing a monthly subscription - full or regular. */
public class SubscriptionController extends RequestController {

  /** Instantiates a new subscription controller.
   * @param serverController the server controller */
  public SubscriptionController(ServerController serverController) {
    super(serverController);
  }

  /** Is called when the customer wants to purchase a full subscription.
   * Calls the generalized handler with the appropriate parameters.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(FullSubscriptionRequest request, CustomerSession session) {
    LocalDate startDate = request.getStartDate();
    LocalDate endDate = startDate.plusDays(27);
    LocalTime dailyExitTime = LocalTime.of(0, 0, 0);
    FullSubscriptionResponse response = new FullSubscriptionResponse();
    return handle(request, session, response, startDate, endDate, dailyExitTime);
  }

  /** Is called when the customer wants to purchase a full subscription.
   * Calls the generalized handler with the appropriate parameters.
   * @param request the request
   * @param session the session
   * @return the server response */
  public ServerResponse handle(RegularSubscriptionRequest request, CustomerSession session) {
    LocalDate startDate = request.getStartDate();
    LocalDate endDate = startDate.plusDays(27);
    LocalTime dailyExitTime = request.getDailyExitTime();
    RegularSubscriptionResponse response = new RegularSubscriptionResponse();
    return handle(request, session, response, startDate, endDate, dailyExitTime);
  }

  /** Generalized handler for Subscription Requests.
   * 
   * Checks the input parameters, updates database records, calculates the payment, and charges the customer's account.
   * 
   * If the user was not logged in while making this request, will create a new user and send them the password.
   * After this, the user will be able to log in with their email address and the generated password.
   * @param request the request
   * @param session the session
   * @param serverResponse the server response
   * @param startDate the start date
   * @param endDate the end date
   * @param dailyExitTime the daily exit time
   * @return the server response */
  public ServerResponse handle(SubscriptionRequest request, CustomerSession session,
      SubscriptionResponse serverResponse, LocalDate startDate, LocalDate endDate, LocalTime dailyExitTime) {
    return database.performQuery(serverResponse, (conn, response) -> {

      // Check that the start date is not in the past
      errorIf(request.getStartDate().isBefore(now().toLocalDate()), "The specified starting date is in the past");
      errorIf(request.getCarIDs() == null || request.getCarIDs().length < 1, "The list of car IDs should be not empty");

      if (request.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR) {
        // Check that lot exists
        ParkingLot lot = ParkingLot.findByIDNotNull(conn, request.getLotID());

        // Check that lot is not full
        session.requireLotNotFull(conn, gson, lot, response);
      }

      // check overlapping subscriptions with the same car ID
      Set<String> carIDs = Utilities.unique(request.getCarIDs());
      
      int numCars = carIDs.size();
      
      for (String carID : carIDs) {
        if (request.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR) {
          errorIf(
              SubscriptionService.overlapExists(conn, carID, request.getSubscriptionType(),
                  request.getLotID(), startDate, endDate),
              "Subscription for this car for this parking lot already exists in this timeframe");
        } else {
          errorIf(SubscriptionService.overlapExists(conn, carID, request.getSubscriptionType(), 0, startDate,
              endDate), "Subscription for this car already exists in this timeframe");
        }
      }
      
      // Handle login
      Customer customer = session.requireRegisteredCustomer(conn, request.getCustomerID(), request.getEmail());
      
      int subscriptionIDs[] = new int[numCars];
      int i = 0;
        
      for (String carID : carIDs) {
        SubscriptionService service = SubscriptionService.create(conn, request.getSubscriptionType(), customer.getId(),
            request.getEmail(), carID, request.getLotID(), startDate, endDate, dailyExitTime);
        errorIfNull(service, "Failed to create SubscriptionService entry");
  
        // XXX Statistics
        StatisticsCollector.increaseSubscription(conn, now().toLocalDate(), service.getSubscriptionType(), service.getLotID());
        subscriptionIDs[i++] = service.getId();
      }

      // Calculate payment
      int totalCars = SubscriptionService.countForCustomer(conn, customer.getId(), request.getSubscriptionType());
      float payment = paymentForSubscription(conn, customer, request, totalCars) * numCars;

      // Write payment
      customer.pay(conn, payment);

      // Success
      response.setCustomerData(customer);
      response.setSubscriptionIDs(subscriptionIDs);
      response.setServiceID(subscriptionIDs[0]);
      response.setPayment(payment);
      response.setSuccess("SubscriptionRequest completed successfully");

      return response;
    });
  }

  private float paymentForSubscription(Connection conn, Customer customer, SubscriptionRequest request, int numCars)
      throws SQLException {
    if (request.getSubscriptionType() == Constants.SUBSCRIPTION_TYPE_REGULAR) {
      // Regular monthly subscription
      ParkingLot lot = ParkingLot.findByID(conn, request.getLotID());
      float pricePerHour = lot.getPrice2();

      if (numCars > 1) {
        return pricePerHour * 54f;
      }

      return pricePerHour * 60f;
    } else {
      // Full monthly subscription
      float pricePerHour = Constants.PRICE_PER_HOUR_RESERVED;
      return pricePerHour * 72f;
    }
  }
}
