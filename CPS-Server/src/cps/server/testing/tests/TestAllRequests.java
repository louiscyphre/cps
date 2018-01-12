package cps.server.testing.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCancelOnetimeParking.class, TestComplaint.class, TestFullSubscription.class,
    TestIncidentalParking.class, TestListParkingLots.class, TestLogin.class, TestRegularSubscription.class,
    TestReservedParking.class })
public class TestAllRequests {

}
