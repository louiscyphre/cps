package cps.testing.order_service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestFullSubscription.class, TestIncidentalParking.class, TestRegularSubscription.class, TestReservedParking.class })
public class AllTests {

}
