package cps.server.testing.utilities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import cps.api.action.ListComplaintsAction;
import cps.api.action.ParkingCellSetDisabledAction;
import cps.api.action.RefundAction;
import cps.api.action.RejectComplaintAction;
import cps.api.action.RequestLotStateAction;
import cps.api.action.ServiceAction;
import cps.api.request.Request;
import cps.api.response.DisableParkingSlotsResponse;
import cps.api.response.ListComplaintsResponse;
import cps.api.response.RefundResponse;
import cps.api.response.RejectComplaintResponse;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.common.Utilities.Pair;
import cps.entities.models.Complaint;
import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;
import cps.entities.people.CompanyPerson;
import cps.entities.people.User;
import cps.server.ServerException;
import cps.server.session.CompanyPersonService;
import cps.server.session.ServiceSession;
import cps.server.session.SessionHolder;

public class EmployeeActor implements Actor {
  SessionHolder context = new SessionHolder();
  int           token;

  enum State {
    INITIAL, READY
  };

  State state;

  public EmployeeActor(World world, int token) {
    this.token = token;
    this.state = State.INITIAL;
  }

  int roll(int min, int max) {
    return ThreadLocalRandom.current().nextInt(min, max + 1);
  }

  long roll(long min, long max) {
    return ThreadLocalRandom.current().nextLong(min, max + 1);
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
  public void act(World world) throws ServerException {
    switch (state) {
      case INITIAL:
        actInitial(world);
        break;
      case READY:
        actReady(world);
        break;
      default:
        break;
    }
  }

  private void actInitial(World world) {
    CompanyPerson person = CompanyPersonService.getEmployee(token);
    ServiceSession session = context.acquireServiceSession();
    User user = session.login(person.getUsername(), person.getPassword());
    assertNotNull(user);
    state = State.READY;
  }

  private void actReady(World world) throws ServerException { // decide on an action
    User user = context.getServiceSession().getUser();

    if (roll(1, 10) > 1) {
      return;
    }

    if (user.canAccessDomain(Constants.ACCESS_DOMAIN_CUSTOMER_SERVICE)) {
      actCheckComplaints(world);
    } else if (user.canAccessDomain(Constants.ACCESS_DOMAIN_PARKING_LOT)) {
      actDisableSlot(world);
    }
  }

  private void actCheckComplaints(World world) throws ServerException {

    CompanyPerson user = context.getServiceSession().requireCompanyPerson();
    Collection<Complaint> complaints = getComplaints(world, user);

    Complaint[] a = new Complaint[0];
    a = complaints.toArray(a);

    if (a.length == 0) {
      return;
    }
    
    int index = 0;
    
    for(index = 0 ; index < a.length ; index++) {
      if(a[index].getStatus() == Constants.COMPLAINT_STATUS_PROCESSING) {
        break;
      }
    }
    if(index == a.length) {
      return;
    }

    int type = roll(0, 1);

    ServiceAction action;
    ServerResponse response;
    if (type == 0) {
      action = new RejectComplaintAction(user.getId(), a[index].getId(), "Some reason");
      response = world.sendRequest(action, context, RejectComplaintResponse.class);
    } else {
      action = new RefundAction(user.getId(), a[index].getId(), roll(1, 1000000), "Some reason");
      response = world.sendRequest(action, context, RefundResponse.class);
    }
    handleResponse(world, action, response);
  }

  private Pair<ParkingLot, ParkingCell[][][]> getLot(World world, User user, int lotID) {
    RequestLotStateAction action = new RequestLotStateAction(user.getId(), lotID);
    RequestLotStateResponse response = world.sendRequest(action, context, RequestLotStateResponse.class);
    handleResponse(world, action, response);

    ParkingLot lot = response.getLot();
    assertNotNull(lot);

    ParkingCell[][][] content = response.getContent();
    assertNotNull(content);

    return new Pair<ParkingLot, ParkingCell[][][]>(lot, content);
  }

  private Collection<Complaint> getComplaints(World world, CompanyPerson employee) {
    ListComplaintsAction action = new ListComplaintsAction();
    ListComplaintsResponse response = world.sendRequest(action, context, ListComplaintsResponse.class);
    handleResponse(world, action, response);

    Collection<Complaint> complaints = response.getData();
    assertNotNull(complaints);

    return complaints;
  }

  private void actDisableSlot(World world) throws ServerException {
    if (roll(1, 10) > 1) {
      return;
    }

    CompanyPerson user = context.getServiceSession().requireCompanyPerson();
    int lotID = user.getDepartmentID();

    Pair<ParkingLot, ParkingCell[][][]> lotResult = getLot(world, user, lotID);
    ParkingLot lot = lotResult.getA();
    ParkingCell[][][] content = lotResult.getB();

    int i = roll(0, lot.getWidth() - 1);
    int j = roll(0, lot.getHeight() - 1);
    int k = roll(0, lot.getDepth() - 1);

    ParkingCell cell = content[i][j][k];

    if (cell.containsCar()) {
      return;
    }

    boolean disable = !cell.isDisabled();

    ParkingCellSetDisabledAction action = new ParkingCellSetDisabledAction(user.getId(), lotID, i, j, k, disable);
    DisableParkingSlotsResponse response = world.sendRequest(action, context, DisableParkingSlotsResponse.class);
    handleResponse(world, action, response);
  }

}
