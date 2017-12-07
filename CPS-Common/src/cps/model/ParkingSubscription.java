package cps.model;

import java.time.LocalDate;
import java.time.LocalTime;

// Database entity for monthly parking subscriptions - regular or full both stored in the same table .

public class ParkingSubscription {
	int id;
	int type; // 1 = regular, 2 = full
	int customerID;
	int carID;
	int lotID; // if null then full, else regular
	LocalDate startDate;
	LocalTime endTime; // null for full subscription
	String email;
}
