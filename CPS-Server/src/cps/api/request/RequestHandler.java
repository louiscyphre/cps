package cps.api.request;

import cps.api.action.*;
import cps.api.response.*;

public interface RequestHandler<SessionType> {
  // Customer requests - only Customer can use these
  public ServerResponse handle(CancelOnetimeParkingRequest request, SessionType session);

  public ServerResponse handle(ComplaintRequest request, SessionType session);

  public ServerResponse handle(FullSubscriptionRequest request, SessionType session);

  public ServerResponse handle(IncidentalParkingRequest request, SessionType session);

  public ServerResponse handle(ListMyComplaintsRequest request, SessionType session);

  public ServerResponse handle(ListOnetimeEntriesRequest request, SessionType session);

  public ServerResponse handle(ParkingEntryRequest request, SessionType session);

  public ServerResponse handle(ParkingExitRequest request, SessionType session);

  public ServerResponse handle(RegularSubscriptionRequest request, SessionType session);

  public ServerResponse handle(ReservedParkingRequest request, SessionType session);

  public ServerResponse handle(LoginRequest request, SessionType session);
  
  // Common requests - both Customer and CompanyPerson can use these
  public ServerResponse handle(ListParkingLotsRequest request, SessionType session);

  // CompanyPerson actions - only CompanyPerson can use these
  public ServerResponse handle(ParkingCellSetDisabledAction action, SessionType session);

  public ServerResponse handle(InitLotAction action, SessionType session);

  public ServerResponse handle(ListComplaintsAction action, SessionType session);

  public ServerResponse handle(RefundAction action, SessionType session);

  public ServerResponse handle(RejectComplaintAction action, SessionType session);

  public ServerResponse handle(RequestLotStateAction action, SessionType session);

  public ServerResponse handle(ParkingCellSetReservedAction action, SessionType session);
  
  public ServerResponse handle(ServiceLoginAction action, SessionType session);

  public ServerResponse handle(SetFullLotAction action, SessionType session);

  public ServerResponse handle(UpdatePricesAction action, SessionType session);

  public ServerResponse handle(GetWeeklyReportAction action, SessionType session);

  public ServerResponse handle(GetQuarterlyReportAction action, SessionType session);
}
