package cps.server.controllers;

import java.time.LocalDate;

import cps.api.action.GetCurrentPerformanceAction;
import cps.api.action.GetPeriodicReportAction;
import cps.api.action.GetQuarterlyReportAction;
import cps.api.action.GetWeeklyReportAction;
import cps.api.response.CurrentPerformanceResponse;
import cps.api.response.PeriodicReportResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.ServerResponse;
import cps.api.response.WeeklyReportResponse;
import cps.entities.models.SubscriptionService;
import cps.server.ServerController;
import cps.server.session.ServiceSession;
import cps.server.session.UserSession;

public class ReportController extends RequestController {

  public ReportController(ServerController serverController) {
    super(serverController);
  }

  public ServerResponse handle(GetWeeklyReportAction action, UserSession session) {
    return database.performQuery(new WeeklyReportResponse(), (conn, response) -> {
      return response;
    });
  }

  public ServerResponse handle(GetQuarterlyReportAction action, ServiceSession acquireServiceSession) {
    return database.performQuery(new QuarterlyReportResponse(), (conn, response) -> {
      return response;
    });
  }

  public ServerResponse handle(GetCurrentPerformanceAction action, ServiceSession acquireServiceSession) {
    return database.performQuery(new CurrentPerformanceResponse(), (conn, response) -> {
      response.setDate(LocalDate.now());
      response.setNumberOfSubscriptions(SubscriptionService.countAll(conn));
      response.setNumberOfSubscriptionsWithMultipleCars(SubscriptionService.countWithMultipleCars(conn));
      return response;
    });
  }

  public ServerResponse handle(GetPeriodicReportAction action, ServiceSession acquireServiceSession) {
    return database.performQuery(new PeriodicReportResponse(), (conn, response) -> {
      return response;
    });
  }
}
