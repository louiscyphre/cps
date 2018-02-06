/**
 * 
 */
package cps.client.controller;

import java.util.Collection;

import cps.entities.models.MonthlyReport;

/**
 * Interface for controllers with Monthly Reports
 */
public interface ReportsController extends ViewController {

  public void fillReportTable(Collection<MonthlyReport> list);
}
