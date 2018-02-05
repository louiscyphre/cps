package cps.testing.order_service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestIncidentalParking.class, TestReservedParking.class, TestRegularSubscription.class, TestFullSubscription.class })
public class AllTests {

}
