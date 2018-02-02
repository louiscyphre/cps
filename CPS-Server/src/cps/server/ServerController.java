package cps.server;

import cps.api.action.GetCurrentPerformanceAction;
import cps.api.action.GetPeriodicReportAction;
import cps.api.action.GetQuarterlyReportAction;
import cps.api.action.GetWeeklyReportAction;
import cps.api.action.InitLotAction;
import cps.api.action.ListComplaintsAction;
import cps.api.action.ParkingCellSetDisabledAction;
import cps.api.action.ParkingCellSetReservedAction;
import cps.api.action.RefundAction;
import cps.api.action.RejectComplaintAction;
import cps.api.action.RequestLotStateAction;
import cps.api.action.ServiceLoginAction;
import cps.api.action.SetFullLotAction;
import cps.api.action.UpdatePricesAction;
import cps.api.request.CancelOnetimeParkingRequest;
import cps.api.request.ComplaintRequest;
import cps.api.request.FullSubscriptionRequest;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ListMyComplaintsRequest;
import cps.api.request.ListOnetimeEntriesRequest;
import cps.api.request.ListParkingLotsRequest;
import cps.api.request.LoginRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.request.ParkingExitRequest;
import cps.api.request.RegularSubscriptionRequest;
import cps.api.request.Request;
import cps.api.request.RequestHandler;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.ServerResponse;
import cps.api.response.SimpleResponse;
import cps.server.controllers.CarTransportationController;
import cps.server.controllers.CarTransportationControllerA;
import cps.server.controllers.ComplaintController;
import cps.server.controllers.LotController;
import cps.server.controllers.OnetimeParkingController;
import cps.server.controllers.ParkingEntryController;
import cps.server.controllers.ParkingExitController;
import cps.server.controllers.ReportController;
import cps.server.controllers.SubscriptionController;
import cps.server.controllers.UserController;
import cps.server.database.DatabaseController;
import cps.server.session.SessionHolder;

public class ServerController implements RequestHandler<SessionHolder> {
  private final ServerConfig                config;
  private final DatabaseController          databaseController;
  private final LotController               lotController;
  private final OnetimeParkingController    onetimeParkingController;
  private final ParkingEntryController      entryController;
  private final ParkingExitController       exitController;
  private final SubscriptionController      subscriptionController;
  private final UserController              userController;
  private final ComplaintController         complaintController;
  private final CarTransportationController transportationController;
  private final ReportController            reportController;

  /** Constructs an instance of the server controller.
   * @param config
   *        the config
   * @throws Exception
   *         the exception */
  public ServerController(ServerConfig config) throws Exception {
    this.config = config;
    databaseController = new DatabaseController(config);
    lotController = new LotController(this);
    onetimeParkingController = new OnetimeParkingController(this);
    entryController = new ParkingEntryController(this);
    exitController = new ParkingExitController(this);
    subscriptionController = new SubscriptionController(this);
    userController = new UserController(this);
    complaintController = new ComplaintController(this);
    transportationController = new CarTransportationControllerA(this);
    reportController = new ReportController(this);
  }

  public ServerConfig getConfig() {
    return config;
  }

  public DatabaseController getDatabaseController() {
    return databaseController;
  }

  public LotController getLotController() {
    return lotController;
  }

  public OnetimeParkingController getOnetimeParkingController() {
    return onetimeParkingController;
  }

  public SubscriptionController getSubscriptionController() {
    return subscriptionController;
  }

  public CarTransportationController getTransportationController() {
    return transportationController;
  }

  public ReportController getReportController() {
    return reportController;
  }

  public ParkingEntryController getEntryController() {
    return entryController;
  }

  public ParkingExitController getExitController() {
    return exitController;
  }

  public UserController getUserController() {
    return userController;
  }

  public ComplaintController getComplaintController() {
    return complaintController;
  }

  public ServerResponse dispatch(Request message, SessionHolder context) {
    ServerResponse response = message.handle(this, context);

    if (response == null) {
      return SimpleResponse.error("Not implemented");
    }

    return response;
  }

  @Override
  public ServerResponse handle(CancelOnetimeParkingRequest request, SessionHolder context) {
    return onetimeParkingController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ComplaintRequest request, SessionHolder context) {
    return complaintController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(FullSubscriptionRequest request, SessionHolder context) {
    return subscriptionController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(IncidentalParkingRequest request, SessionHolder context) {
    return onetimeParkingController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ListMyComplaintsRequest request, SessionHolder context) {
    return complaintController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ListOnetimeEntriesRequest request, SessionHolder context) {
    return onetimeParkingController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ListParkingLotsRequest request, SessionHolder context) {
    return lotController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(LoginRequest request, SessionHolder context) {
    return userController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ParkingEntryRequest request, SessionHolder context) {
    return entryController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ParkingExitRequest request, SessionHolder context) {
    return exitController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(RegularSubscriptionRequest request, SessionHolder context) {
    return subscriptionController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ReservedParkingRequest request, SessionHolder context) {
    return onetimeParkingController.handle(request, context.acquireCustomerSession());
  }

  @Override
  public ServerResponse handle(ParkingCellSetDisabledAction action, SessionHolder context) {
    return lotController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(InitLotAction action, SessionHolder context) {
    return lotController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(ListComplaintsAction action, SessionHolder context) {
    return complaintController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(RefundAction action, SessionHolder context) {
    return complaintController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(RejectComplaintAction action, SessionHolder context) {
    return complaintController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(RequestLotStateAction action, SessionHolder context) {
    return lotController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(ParkingCellSetReservedAction action, SessionHolder context) {
    return lotController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(ServiceLoginAction action, SessionHolder context) {
    return userController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(SetFullLotAction action, SessionHolder context) {
    return lotController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(UpdatePricesAction action, SessionHolder context) {
    return lotController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(GetWeeklyReportAction action, SessionHolder context) {
    return reportController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(GetQuarterlyReportAction action, SessionHolder context) {
    return reportController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(GetCurrentPerformanceAction action, SessionHolder context) {
    return reportController.handle(action, context.acquireServiceSession());
  }

  @Override
  public ServerResponse handle(GetPeriodicReportAction action, SessionHolder context) {
    return reportController.handle(action, context.acquireServiceSession());
  }

  public void removeSession(SessionHolder context) {
    userController.removeSession(context);    
  }
}
