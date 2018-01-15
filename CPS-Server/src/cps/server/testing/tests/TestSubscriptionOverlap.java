package cps.server.testing.tests;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import com.sun.swing.internal.plaf.basic.resources.basic_de;

import cps.common.Constants;
import cps.entities.models.SubscriptionService;
import cps.server.ServerConfig;
import cps.server.ServerController;
import cps.server.ServerException;
import cps.server.controllers.DatabaseController;
import cps.server.controllers.SubscriptionController;
import cps.server.session.SessionHolder;

public class TestSubscriptionOverlap {

	ServerController server;
	DatabaseController db;
	SessionHolder context = new SessionHolder();

	@Before
	public void setUp() throws Exception {
		this.server = new ServerController(ServerConfig.testing());
		this.db = server.getDatabaseController();
		db.truncateTables();
	}

	@Test
	public void testHandleSubscriptionRequestCustomerSessionSubscriptionResponseLocalDateLocalDateLocalTime() throws ServerException {
		SubscriptionController subc = server.getSubscriptionController();
		LocalDate starttime = LocalDate.now();
		LocalDate endtime = LocalDate.now().plusDays(24);
		LocalTime dailyexittime = LocalTime.of(18, 0, 0);
		db.performAction(conn -> {
			SubscriptionService.create(conn, Constants.SUBSCRIPTION_TYPE_FULL, 1, "NO@email.com", "TL-12-12", 2,
					starttime, endtime, dailyexittime);

		});

	}

}
