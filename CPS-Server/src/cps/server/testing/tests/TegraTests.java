package cps.server.testing.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import cps.api.action.InitLotAction;
import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.InitLotResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ServerResponse;
import cps.common.Constants;
import cps.common.Utilities;
import cps.common.Utilities.Pair;
import cps.entities.models.CarTransportation;
import cps.entities.models.Customer;
import cps.entities.models.OnetimeService;
import cps.entities.models.ParkingLot;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.OnetimeParkingController;
import cps.server.session.CustomerSession;
import cps.server.testing.utilities.CustomerData;

@SuppressWarnings("unused")
public class TegraTests {

	ServerController server;
	DatabaseController db;

	@Before
	public void setUp() throws Exception {
		this.server = new ServerController(ServerConfig.testing());
		this.db = server.getDatabaseController();
		db.truncateTables();
	}

	@Test
	public void testInsertCars() {
		int parkingRequestsNo = 5;
		/*
		 * Create parking lot 
		 * Create incidental parking request 
		 * Insert the car
		 */

		ParkingLot lot = initParkingLot();
		CustomerData data = new CustomerData((int) Math.random() * 1000,
				Utilities.randomString("abcdefghijklmnopqrstuvwxyz", 8), Utilities.randomString("1234567890", 4),
				Utilities.randomString("1234567890ABCDTRUOTSKL", 7), 1, 0);
		OnetimeService[] incidentalParkingRequests = new OnetimeService[parkingRequestsNo];

		for (int i = 0; i < parkingRequestsNo; i++) {
			incidentalParkingRequests[i] = newIncidentalParking(lot.getId());
		}
		CarTransportation[] req = new CarTransportation[parkingRequestsNo];
		for (int i = 0; i < parkingRequestsNo; i++) {
			req[i] = newParkingEntry(incidentalParkingRequests[i]);
		}

	}

	private CarTransportation newParkingEntry(OnetimeService _cdata) {
		ParkingEntryRequest request = new ParkingEntryRequest(_cdata.getCustomerID(), 0, _cdata.getLotID(),
				_cdata.getCarID());
		ParkingEntryResponse respo = (ParkingEntryResponse) server.dispatch(request);
		assertNotNull(respo);
		assertTrue(ServerResponse.STATUS_OK == respo.getStatus());
		return new CarTransportation(respo.getCustomerID(), _cdata.getCarID(),
				Constants.LICENSE_TYPE_ONETIME, _cdata.getId(), _cdata.getLotID(),
				Timestamp.valueOf(LocalDateTime.now()), null);
	}

	private OnetimeService newIncidentalParking(int lotId) {
		LocalDateTime endTime = LocalDateTime.now().plusHours((long) (Math.random() * 12) + 2)
				.plusMinutes((long) Math.random() * 55);
		IncidentalParkingRequest request = new IncidentalParkingRequest((int) Math.random() * 500,
				Utilities.randomString("angjurufjfjsl@", 10), Utilities.randomString("ILBTA1234567890-", 7), lotId,
				endTime);
		IncidentalParkingResponse response = (IncidentalParkingResponse) server.dispatch(request);
		assertNotNull(response);
		assertTrue(response.getStatus() == ServerResponse.STATUS_OK);
		return new OnetimeService(response.getServiceID(), 1, response.getCustomerID(), request.getEmail(),
				request.getCarID(), lotId, Timestamp.valueOf(LocalDateTime.now()),
				Timestamp.valueOf(request.getPlannedEndTime()), false);
	}

	private ParkingLot initParkingLot() {
		ParkingLot lt = null;
		InitLotAction request = new InitLotAction(1, "Sesam 2", 4, 5, 3, "12.f.t43");
		ServerResponse response = server.dispatch(request);
		InitLotResponse re2 = (InitLotResponse) response;
		lt = db.performQuery(conn -> ParkingLot.findByID(conn, re2.getLotID()));
		assertNotNull(lt);
		return lt;
	}
	/*
	 * @Test public void testRetrieveCar() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testHandleInitLotAction() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testHandleUpdatePricesAction() {
	 * fail("Not yet implemented"); }
	 * 
	 * @Test public void testHandleSetFullLotAction() { fail("Not yet implemented");
	 * }
	 */
}
