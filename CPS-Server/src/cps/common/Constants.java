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
}
