package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.server.session.CustomerSession;
import cps.server.session.ServiceSession;
import cps.server.session.SessionHolder;
import cps.server.session.UserSession;
import cps.server.testing.utilities.CustomerData;
import cps.server.testing.utilities.ServerControllerTest;
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
import cps.entities.people.User;
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
public class TestComplaint extends ServerControllerTest {
  @Test
  public void testComplaintAndRefund() {
    header("testComplaint");

    // Create user
    CustomerData data = new CustomerData(0, "user@email", "1234", "IL11-222-33", 1, 0);
    Customer customer = makeCustomer(data);

    // Test complaint request
    Complaint complaint = makeComplaint(customer, getContext());

    // Test refund request
    makeRefund(complaint, getContext());
  }

  protected Complaint makeComplaint(Customer customer, SessionHolder context) {
    // Create new customer session
    CustomerSession session = context.acquireCustomerSession();
    session.setCustomer(customer);

    // Make request
    ComplaintRequest request = new ComplaintRequest(customer.getId(), "My car was damaged");

    // Test response
    ServerResponse response = server.dispatch(request, context);
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
    return entry;
  }

  private void makeRefund(Complaint complaint, SessionHolder context) {
    // Create new customer service employee session
    ServiceSession session = context.acquireServiceSession();
    User user = session.login("eli", "9012");

    // Make request
    float refundAmount = 1000000f;
    RefundAction request = new RefundAction(user.getId(), refundAmount, complaint.getId());

    // Test response
    ServerResponse response = server.dispatch(request, context);
    assertNotNull(response);
    printObject(response);
    assertThat(response, instanceOf(RefundResponse.class));

    RefundResponse specificResponse = (RefundResponse) response;
    assertEquals(request.getComplaintID(), specificResponse.getComplaintID());

    // Test database result
    Complaint updatedComplaint = db.performQuery(conn -> Complaint.findByID(conn, specificResponse.getComplaintID()));
    assertNotNull(updatedComplaint);
    printObject(updatedComplaint);
    assertEquals(complaint.getId(), updatedComplaint.getId());
    assertEquals(Constants.COMPLAINT_STATUS_ACCEPTED, updatedComplaint.getStatus());
    assertNotNull(updatedComplaint.getResolvedAt());
    assertEquals(refundAmount, updatedComplaint.getRefundAmount());
    assertEquals(user.getId(), updatedComplaint.getEmployeeID());
  }
}