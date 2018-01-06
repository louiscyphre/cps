package cps.common;

public interface Constants {
	public final int DEFAULT_PORT = 5555;
	public final String DEFAULT_HOST = "127.0.0.1";
	public final String DB_HOST = "localhost:3306";
	public final String DB_NAME = "cps";
	public final String DB_USERNAME = "cps";
	public final String DB_PASSWORD = "project";
	public final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public final String SQL_CREATE_ONETIME_SERVICE = "INSERT INTO onetime_service values(default, ?, ?, ?, ?, ?, ?, ?, ?)";
	public final String SQL_CREATE_CAR_TRANSPORTATION = "INSERT INTO car_transportation values(?, ?, ?, ?, ?, default, default)";
	public final String SQL_CREATE_PARKING_LOT = "INSERT INTO parking_lot values(default, ?, ?, default, ?, ?, default, ?)";
	public final int PARKING_TYPE_INCIDENTAL = 1;
	public final int PARKING_TYPE_RESERVED = 2;
	public final int SUBSCRIPTION_TYPE_REGULAR = 1;
	public final int SUBSCRIPTION_TYPE_FULL = 2;
	public final String SQL_CREATE_NEW_DAY = "INSERT INTO daily_statistics(? ,? ,default ,default ,default ,default)";
	public final String CHECK_DATE = "SELECT * FROM daily_statistics DS WHERE ds.day=?";
	public final String INCREASE_REALIZED_ORDER = "UPDATE daily_statistics SET realized_orders = ? WHERE day=? AND lot_id=?";
	public final String INCREASE_CANCELED_ORDER = "UPDATE daily_statistics SET canceled_orders = ? WHERE day=? AND lot_id=?";
	public final String INCREASE_LATE_ARRIVAL = "UPDATE daily_statistics SET late_arrivals = ? WHERE day=? AND lot_id=?";
	public final String INCREASE_INACTIVE_SLOTS= "UPDATE daily_statistics SET inactive_slots = ? WHERE day=? AND lot_id=?";
	public final String GET_ONETIME_SERVICE_BY_CUSTOMER_ID= "SELECT * FROM onetime_service WHERE customer_id = ? ORDER BY id";
	public final String GET_ONETIME_SERVICE_BY_CUSTID_CARID_LOTID= "SELECT * FROM onetime_service WHERE customer_id = ? AND car_id = ? AND lot_id = ? ORDER BY id DESC LIMIT 1";
}
