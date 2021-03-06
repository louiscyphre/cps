package cps.server.controllers;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedList;

import cps.api.action.GetCurrentPerformanceAction;
import cps.api.action.GetPeriodicReportAction;
import cps.api.action.GetQuarterlyReportAction;
import cps.api.action.GetWeeklyReportAction;
import cps.api.response.CurrentPerformanceResponse;
import cps.api.response.PeriodicReportResponse;
import cps.api.response.QuarterlyReportResponse;
import cps.api.response.ServerResponse;
import cps.api.response.WeeklyReportResponse;
import cps.common.Constants;
import cps.common.Utilities;
import cps.entities.models.MonthlyReport;
import cps.entities.models.PeriodicReport;
import cps.entities.models.SubscriptionService;
import cps.entities.models.WeeklyStatistics;
import cps.entities.people.CompanyPerson;
import cps.server.ServerController;
import cps.server.session.ServiceSession;

/** The Class ReportController. */
public class ReportController extends RequestController {

  /** Instantiates a new report controller.
   * @param serverController the server controller */
  public ReportController(ServerController serverController) {
    super(serverController);
  }

  /** Invoked when the Global Manager wants to receive a weekly statistical report.
   * @param action the action
   * @param session the session
   * @return the server response */
  public ServerResponse handle(GetWeeklyReportAction action, ServiceSession session) {
    return database.performQuery(new WeeklyReportResponse(), (conn, response) -> {
      CompanyPerson user = session.requireCompanyPerson();
      
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_STATISTICS), "You do not have permission to perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_GLOBAL_MANAGER && user.getDepartmentID() != action.getLotID(),
          "A LocalManager can perform this action only on their lot");
      
      LocalDate weekStart = Utilities.findWeekStart(now().toLocalDate());
      
      response.setSuccess("Weekly Report retrieved succesfully");
      response.setPeriod(weekStart, weekStart.plusDays(7), Duration.ofDays(7));
      response.setData(WeeklyStatistics.createUpdateWeeklyReport(conn, weekStart, action.getLotID()));
      
      return response;
    });
  }

  /** Invoked when the Global Manager wants to receive a quarterly statistical report..
   * @param action the action
   * @param session the session
   * @return the server response */
  public ServerResponse handle(GetQuarterlyReportAction action, ServiceSession session) {
    return database.performQuery(new QuarterlyReportResponse(), (conn, response) -> {
      CompanyPerson user = session.requireCompanyPerson();
      
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_STATISTICS), "You do not have permission to perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_GLOBAL_MANAGER && user.getDepartmentID() != action.getLotID(),
          "A LocalManager can perform this action only on their lot");

      LocalDate start = action.getPeriodStart();
      LocalDate end = action.getPeriodEnd();

      LinkedList<MonthlyReport> data = new LinkedList<>();

      MonthlyReport total = new MonthlyReport(action.getPeriodStart().getYear(), action.getPeriodStart().getMonthValue(), 0, 0, 0, 0, 0, 0, 0, 0, 0, "Total");
      MonthlyReport report = null;

      while (start.isBefore(end)) {
        int year = start.getYear();
        int month = start.getMonthValue();
        report = MonthlyReport.getMonthlyReport(conn, year, month, action.getLotID());
        total.add(report);
        data.add(report);
        start = start.plusMonths(1);
      }

      data.add(total);

      response.setSuccess("Quarterly Report retrieved succesfully");
      response.setPeriod(action.getPeriodStart(), action.getPeriodEnd(), Duration.ofDays(30));
      response.setData(data);
      
      return response;
    });
  }

  /** Invoked when the Global Manager wants to see current performance statistics..
   * @param action the action
   * @param session the session
   * @return the server response */
  public ServerResponse handle(GetCurrentPerformanceAction action, ServiceSession session) {
    return database.performQuery(new CurrentPerformanceResponse(), (conn, response) -> {
      CompanyPerson user = session.requireCompanyPerson();
      
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_STATISTICS), "You do not have permission to perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_GLOBAL_MANAGER, "Only the Global Manager can perform this action");
      
      response.setDate(now().toLocalDate());
      response.setNumberOfSubscriptions(SubscriptionService.countAll(conn));
      response.setNumberOfSubscriptionsWithMultipleCars(SubscriptionService.countWithMultipleCars(conn));
      response.setSuccess("Performance Report retrieved succesfully");
      
      return response;
    });
  }

  /** Invoked when the Global Manager wants to see periodic activity statistics.
   * @param action the action
   * @param session the session
   * @return the server response */
  public ServerResponse handle(GetPeriodicReportAction action, ServiceSession session) {
    return database.performQuery(new PeriodicReportResponse(), (conn, response) -> {
      CompanyPerson user = session.requireCompanyPerson();
      
      errorIf(!user.canAccessDomain(Constants.ACCESS_DOMAIN_STATISTICS), "You do not have permission to perform this action");
      errorIf(user.getAccessLevel() < Constants.ACCESS_LEVEL_GLOBAL_MANAGER, "Only the Global Manager can perform this action");
     
      LocalDate period = action.getPeriodEnd();
      int year = period.getYear();
      int month = period.getMonthValue();
      
      response.setSuccess("Periodic Report retrieved succesfully");
      response.setData(PeriodicReport.generate(conn, year, month));
      response.setPeriod(action.getPeriodStart(), action.getPeriodEnd(), Duration.ofDays(7));
      
      return response;
    });
  }
}
