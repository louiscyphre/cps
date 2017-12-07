package cps.model;

import java.time.LocalDateTime;

// Database entity for one-time parking services - incidental parking or reserved parking both stored in the same table.

public class OnetimeParking {
	int id;
	int type; // 1 = incidental, 2 = reserved
	int customerID;
	int carID;
	int lotID;
	LocalDateTime plannedStartTime; // null for incidental
	LocalDateTime plannedEndTime;
	LocalDateTime actualStartTime;
	LocalDateTime actualEndTime;
	String email;
}
