package cps.api.action;

public class StatisticalReportAction extends ServiceAction {
	private static final long serialVersionUID = 1L;
	private String reportType; 

	public StatisticalReportAction(int userID, String reportType) {
		super(userID);
		this.reportType = reportType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}	
}
