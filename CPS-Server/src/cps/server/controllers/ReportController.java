package cps.server.controllers;

import cps.api.action.RequestReportAction;
import cps.api.response.ServerResponse;
import cps.server.ServerController;
import cps.server.session.UserSession;

public class ReportController extends RequestController {

  public ReportController(ServerController serverController) {
    super(serverController);
  }

  public ServerResponse handle(RequestReportAction action, UserSession session) {
    // TODO implement RequestReportAction
    return null;
  }
}
