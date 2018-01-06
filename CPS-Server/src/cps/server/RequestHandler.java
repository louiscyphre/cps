package cps.server;

import cps.api.request.*;
import cps.api.action.*;
import cps.api.response.*;

public interface RequestHandler {
	// Customer requests
	public ServerResponse handle(CancelOnetimeParkingRequest request);
	public ServerResponse handle(ComplaintRequest request);
	public ServerResponse handle(FullSubscriptionRequest request);
	public ServerResponse handle(IncidentalParkingRequest request);
	public ServerResponse handle(ListOnetimeEntriesRequest request);
	public ServerResponse handle(OnetimeParkingRequest request);
	public ServerResponse handle(ParkingEntryRequest request);
	public ServerResponse handle(ParkingExitRequest request);
	public ServerResponse handle(RegularSubscriptionRequest request);
	public ServerResponse handle(ReservedParkingRequest request);
	public ServerResponse handle(SubscriptionRequest request);

	// CompanyPerson actions
	public ServerResponse handle(DisableParkingSlotsAction action);
	public ServerResponse handle(InitLotAction action);
	public ServerResponse handle(RefundAction action);
	public ServerResponse handle(RequestLotStateAction action);
	public ServerResponse handle(RequestReportAction action);
	public ServerResponse handle(ReserveParkingSlotsAction action);
	public ServerResponse handle(SetFullLotAction action);
	public ServerResponse handle(UpdatePricesAction action);
}
