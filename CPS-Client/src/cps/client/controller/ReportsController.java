/**
 * 
 */
package cps.client.controller;

import java.util.Collection;

import cps.entities.models.MonthlyReport;

/**
 * Created on: 2018-01-28 2:42:53 AM 
 */
public interface ReportsController extends ViewController {

  public void fillReportTable(Collection<MonthlyReport> list);
}
