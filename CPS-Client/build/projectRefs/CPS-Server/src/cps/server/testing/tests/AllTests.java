package cps.server.testing.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCancelOnetimeParking.class, TestComplaint.class, TestFullSubscription.class, TestIncidentalParking.class, TestInsertionAlgorithm.class,
    TestListParkingLots.class, TestLogin.class, TestOrderedCells.class, TestParkingCell.class, TestRegularSubscription.class, TestRequestLotState.class,
    TestReservedParking.class, TestReserveOrDisable.class, TestSubscriptionOverlap.class })
public class AllTests {

}
