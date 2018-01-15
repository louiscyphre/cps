package cps.api.request;

import cps.api.action.*;
import cps.api.response.*;

public interface RequestHandler<SessionType> {
  // Customer requests
  public ServerResponse handle(CancelOnetimeParkingRequest request, SessionType session);

  public ServerResponse handle(ComplaintRequest request, SessionType session);

  public ServerResponse handle(FullSubscriptionRequest request, SessionType session);

  public ServerResponse handle(IncidentalParkingRequest request, SessionType session);

  public ServerResponse handle(ListOnetimeEntriesRequest request, SessionType session);

  public ServerResponse handle(ListParkingLotsRequest request, SessionType session);

  public ServerResponse handle(ParkingEntryRequest request, SessionType session);

  public ServerResponse handle(ParkingExitRequest request, SessionType session);

  public ServerResponse handle(RegularSubscriptionRequest request, SessionType session);

  public ServerResponse handle(ReservedParkingRequest request, SessionType session);

  public ServerResponse handle(LoginRequest request, SessionType session);

  // CompanyPerson actions  
  public ServerResponse handle(DisableParkingSlotsAction action, SessionType session);

  public ServerResponse handle(InitLotAction action, SessionType session);

  public ServerResponse handle(RefundAction action, SessionType session);

  public ServerResponse handle(RequestLotStateAction action, SessionType session);

  public ServerResponse handle(RequestReportAction action, SessionType session);

  public ServerResponse handle(ReserveParkingSlotsAction action, SessionType session);
  
  public ServerResponse handle(ServiceLoginAction action, SessionType session);

  public ServerResponse handle(SetFullLotAction action, SessionType session);

  public ServerResponse handle(UpdatePricesAction action, SessionType session);
}
