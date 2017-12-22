package cps.entities.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
// Database entity for monthly parking subscriptions - regular or full both stored in the same table .

public class SubscriptionService extends Entity {
	private static final long serialVersionUID = 1L;

	int id;
	int type; // 1 = regular, 2 = full
	int customerID;
	int carID;
	int lotID; // if null then full, else regular
	Timestamp startDate;
	Timestamp endDate;
	Timestamp endTime; // null for full subscription
	String email;

	public static SubscriptionService buildFromQueryResult(ResultSet rs) throws SQLException {
		return new SubscriptionService(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getTimestamp(6),rs.getTimestamp(7),rs.getTimestamp(8),rs.getString(9));}
	
}
