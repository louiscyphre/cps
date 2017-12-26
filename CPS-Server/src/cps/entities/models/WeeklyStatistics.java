package cps.entities.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class WeeklyStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	// `start` date NOT NULL,
	// `realized_orders_mean` float NOT NULL DEFAULT '0',
	// `realized_orders_median` float NOT NULL DEFAULT '0',
	// `canceled_orders_mean` float NOT NULL DEFAULT '0',
	// `canceled_orders_median` float NOT NULL DEFAULT '0',
	// `late_arrivals_mean` float NOT NULL DEFAULT '0',
	// `late_arrivals_median` float NOT NULL DEFAULT '0',
	// `realized_orders_dist` varchar(300) NOT NULL,
	// `canceled_orders_dist` varchar(300) NOT NULL,
	// `late_arrivals_dist` varchar(300) NOT NULL,
	private LocalDate start;
	private float realizedOrdersMean;
	private float canceledOrdersMean;
	private float lateArrivalsMean;
	private float realizedOrdersMedian;
	private float canceledOrdersMedian;
	private float lateArrivalsMedian;
	private String realizedOrdersDist;
	private String canceledOrdersDist;
	private String lateArrivalsDist;

	public WeeklyStatistics(LocalDate start, float realizedOrdersMean, float canceledOrdersMean, float lateArrivalsMean,
			float realizedOrdersMedian, float canceledOrdersMedian, float lateArrivalsMedian, String realizedOrdersDist,
			String canceledOrdersDist, String lateArrivalsDist) {
		super();
		this.start = start;
		this.realizedOrdersMean = realizedOrdersMean;
		this.canceledOrdersMean = canceledOrdersMean;
		this.lateArrivalsMean = lateArrivalsMean;
		this.realizedOrdersMedian = realizedOrdersMedian;
		this.canceledOrdersMedian = canceledOrdersMedian;
		this.lateArrivalsMedian = lateArrivalsMedian;
		this.realizedOrdersDist = realizedOrdersDist;
		this.canceledOrdersDist = canceledOrdersDist;
		this.lateArrivalsDist = lateArrivalsDist;
	}

	public WeeklyStatistics(ResultSet rs) throws SQLException {
		this(rs.getDate(1).toLocalDate(), rs.getFloat(2), rs.getFloat(3), rs.getFloat(4), rs.getFloat(5),
				rs.getFloat(6), rs.getFloat(7), rs.getString(8), rs.getString(9), rs.getString(10));
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public float getRealizedOrdersMean() {
		return realizedOrdersMean;
	}

	public void setRealizedOrdersMean(float realizedOrdersMean) {
		this.realizedOrdersMean = realizedOrdersMean;
	}

	public float getCanceledOrdersMean() {
		return canceledOrdersMean;
	}

	public void setCanceledOrdersMean(float canceledOrdersMean) {
		this.canceledOrdersMean = canceledOrdersMean;
	}

	public float getLateArrivalsMean() {
		return lateArrivalsMean;
	}

	public void setLateArrivalsMean(float lateArrivalsMean) {
		this.lateArrivalsMean = lateArrivalsMean;
	}

	public float getRealizedOrdersMedian() {
		return realizedOrdersMedian;
	}

	public void setRealizedOrdersMedian(float realizedOrdersMedian) {
		this.realizedOrdersMedian = realizedOrdersMedian;
	}

	public float getCanceledOrdersMedian() {
		return canceledOrdersMedian;
	}

	public void setCanceledOrdersMedian(float canceledOrdersMedian) {
		this.canceledOrdersMedian = canceledOrdersMedian;
	}

	public float getLateArrivalsMedian() {
		return lateArrivalsMedian;
	}

	public void setLateArrivalsMedian(float lateArrivalsMedian) {
		this.lateArrivalsMedian = lateArrivalsMedian;
	}

	public String getRealizedOrdersDist() {
		return realizedOrdersDist;
	}

	public void setRealizedOrdersDist(String realizedOrdersDist) {
		this.realizedOrdersDist = realizedOrdersDist;
	}

	public String getCanceledOrdersDist() {
		return canceledOrdersDist;
	}

	public void setCanceledOrdersDist(String canceledOrdersDist) {
		this.canceledOrdersDist = canceledOrdersDist;
	}

	public String getLateArrivalsDist() {
		return lateArrivalsDist;
	}

	public void setLateArrivalsDist(String lateArrivalsDist) {
		this.lateArrivalsDist = lateArrivalsDist;
	}
}
