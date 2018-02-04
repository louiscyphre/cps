package cps.server.testing.tests;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Test;

import cps.common.Constants;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerException;
import cps.server.testing.utilities.ServerControllerTest;

public class TestOrderedCells extends ServerControllerTest {

	@Test
	public void testCountOrderedCells() throws ServerException {
		// Create parking lot
	  // Create a reserved parking service
		// Insert the car
		ParkingLot lot = initParkingLot();

		Timestamp[] a = new Timestamp[10];
		a[0] = Timestamp.valueOf(getTime());
		a[1] = Timestamp.valueOf(getTime().minusMinutes(10));
		a[2] = Timestamp.valueOf(getTime().plusMinutes(10));
		a[3] = Timestamp.valueOf(getTime().plusMinutes(20));
		a[4] = Timestamp.valueOf(getTime().minusMinutes(20));
		a[5] = Timestamp.valueOf(getTime().plusMinutes(4).plusDays(1));

		db.performAction(conn -> {
			OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, 3, "no@email.com", "123-sdf",
					lot.getId(), a[4], a[1]);
			OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, 3, "no@email.com", "254-sdf",
					lot.getId(), a[0], a[3]);
			OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, 3, "no@email.com", "458-sdf",
					lot.getId(), a[0], a[5]);
			OnetimeService.create(db.getConnection(), Constants.PARKING_TYPE_RESERVED, 3, "no@email.com", "984-sdf",
					lot.getId(), a[3], a[5]);
			assertTrue(3 == ParkingLot.countOrderedCells(conn, lot.getId(), a[2], a[5]));
		});
	}

	protected ParkingLot initParkingLot() throws ServerException {
		ParkingLot lot = db.performQuery(conn -> ParkingLot.create(conn, "Sesam 2", 4, 5, 3, "12.f.t43"));
		return lot;
	}
}
