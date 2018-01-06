package cps.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cps.api.request.ParkingExitRequest;
import cps.entities.models.CarTransportation;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;

public abstract class Utilities {
	static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);

	public static int stringToInteger(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	public static String dateToString(LocalDateTime localDateTime) {
		return localDateTime.format(dateTimeFormatter);
	}

	public static class Holder<T> {
		private T value;

		public Holder(T value) {
			setValue(value);
		}

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
	}

	public static void ChargeCustomer(Connection conn, CarTransportation _tr, ParkingExitRequest _er)
			throws SQLException {
		double sum = 0, tariffLow = 0, tariffHigh = 0;
		Long late1, inside, late2;
		// determine if this is a subscription or one time
		if (_tr.getAuthType() == OnetimeService.TYPE) {
			// if customer did not park by subscription charge him according to park type
			// Determine incidental or reserved parking
			PreparedStatement st = conn.prepareStatement(Constants.GET_ONETIME_SERVICE_BY_ID);
			st.setInt(1, _tr.getAuthID());
			ResultSet rs = st.executeQuery();
			OnetimeService _ots = null;

			if (rs.next()) {
				_ots = new OnetimeService(rs);
			} else {
				rs.close();
				st.close();
				return;
			}
			rs.close();
			st.close();

			// Determine prices at that parking lot

			st = conn.prepareStatement(Constants.SQL_GET_LOT_BY_ID);
			st.setInt(1, _er.getLotID());
			rs = st.executeQuery();
			ParkingLot _PL = null;

			if (rs.next()) {
				_PL = new ParkingLot(rs);
			} else {
				rs.close();
				st.close();
				return;
			}
			rs.close();
			st.close();
			if (_ots.getParkingType() == Constants.PARKING_TYPE_INCIDENTAL) {
				tariffLow = _PL.getPrice1() / 60; // get incidental price
				tariffHigh = tariffLow / 60; // fine price equals regular price
			} else {
				tariffLow = _PL.getPrice2() / 60; // get reserved price
				tariffHigh = _PL.getPrice1() / 60; // fine price is higher
			}
			// was the customer late or early? negative means early positive means late
			late1 = _tr.getInsertedAt().getTime() - _ots.getPlannedStartTime().getTime();
			late1 = late1 / (60 * 1000); // convert to minutes

			// how much time the customer ordered?
			inside = _ots.getPlannedEndTime().getTime() - _ots.getPlannedStartTime().getTime();
			inside = inside / (60 * 1000);

			// Has the customer left late or early? negative means early positive means late
			late2 = _tr.getRemovedAt().getTime() - _ots.getPlannedEndTime().getTime();
			late2 = late2 / (60 * 1000);

			// if the customer running late less that 30 minutes - discard being late and
			// charge full ordered time
			if (late1 < 30) {
				late1 = (long) 0;
			} else {
				// if the customer came in early simply add minutes to total time and charge
				// normal
				if (late1 < 0) {
					inside -= late1;
				}
			}

			// if the customer exits early - charge full time and discard early minutes
			if (late2 < 0) {
				late2 = (long) 0;
			}
			// At last we calculate the sum
			// The formula is according to the demands
			sum = late1 * tariffLow * 1.2 + inside * tariffLow + late2 * tariffHigh;
			st = conn.prepareStatement("SELECT balance FROM customer WHERE id=?");
			st.setInt(1, _er.getCustomerID());
			rs = st.executeQuery();
			st.close();
			if (rs.next()) {
				sum = sum + rs.getFloat(1);
			} else {
				rs.close();
				return;
			}
			rs.close();
			st = conn.prepareStatement("UPDATE customer SET balance=? WHERE id=?");
			st.setFloat(1, (float) sum);
			st.setInt(2, _er.getCustomerID());
			st.executeUpdate();
		}
		// TODO:if customer has subscription - charge him only for being late
	}
}
