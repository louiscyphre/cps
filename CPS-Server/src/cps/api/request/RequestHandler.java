package cps.api.request;

import cps.api.action.*;
import cps.api.response.*;
import cps.server.session.*;

public interface RequestHandler {
	// Customer requests
	public ServerResponse handle(CancelOnetimeParkingRequest request, UserSession session);
	public ServerResponse handle(ComplaintRequest request, UserSession session);
	public ServerResponse handle(FullSubscriptionRequest request, UserSession session);
	public ServerResponse handle(IncidentalParkingRequest request, UserSession session);
	public ServerResponse handle(ListOnetimeEntriesRequest request, UserSession session);
	public ServerResponse handle(ListParkingLotsRequest request, UserSession session);
	public ServerResponse handle(ParkingEntryRequest request, UserSession session);
	public ServerResponse handle(ParkingExitRequest request, UserSession session);
	public ServerResponse handle(RegularSubscriptionRequest request, UserSession session);
	public ServerResponse handle(ReservedParkingRequest request, UserSession session);
	public ServerResponse handle(LoginRequest request, UserSession session);

	// CompanyPerson actions
	public ServerResponse handle(DisableParkingSlotsAction action, UserSession session);
	public ServerResponse handle(InitLotAction action, UserSession session);
	public ServerResponse handle(RefundAction action, UserSession session);
	public ServerResponse handle(RequestLotStateAction action, UserSession session);
	public ServerResponse handle(RequestReportAction action, UserSession session);
	public ServerResponse handle(ReserveParkingSlotsAction action, UserSession session);
	public ServerResponse handle(SetFullLotAction action, UserSession session);
	public ServerResponse handle(UpdatePricesAction action, UserSession session);
}
