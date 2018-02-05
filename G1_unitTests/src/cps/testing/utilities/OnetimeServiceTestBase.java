package cps.testing.utilities;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;

import cps.api.request.IncidentalParkingRequest;
import cps.api.request.ParkingEntryRequest;
import cps.api.request.ReservedParkingRequest;
import cps.api.response.IncidentalParkingResponse;
import cps.api.response.OnetimeParkingResponse;
import cps.api.response.ParkingEntryResponse;
import cps.api.response.ParkingExitResponse;
import cps.api.response.ReservedParkingResponse;
import cps.common.Utilities.Pair;
import cps.entities.models.OnetimeService;
import cps.server.ServerException;
import cps.server.session.SessionHolder;

public class OnetimeServiceTestBase extends ServerControllerTestBase {
  protected CustomerData custData;
  protected ParkingLotData[] lotData = new ParkingLotData[2];
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    // Setup customer data
    // Initially we set customer ID to 0, so that the system will create a new ID for us
    custData = new CustomerData(0, "user@email", "", "IL11-222-33", 1, 0);
    
    // Setup Parking Lot data
    lotData[0] = new ParkingLotData(0, "Sesame, 1", 4, 5f, 4f, "1.0.0.1");
    lotData[1] = new ParkingLotData(0, "Zanzibar, 2", 4, 5f, 4f, "1.0.0.2");
  }

  protected void requestIncidentalParking(CustomerData data, SessionHolder context, LocalDateTime plannedEndTime) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    IncidentalParkingRequest request = new IncidentalParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedEndTime);
    printObject(request);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(IncidentalParkingResponse.class));
  }
  
  protected ReservedParkingResponse requestReservedParking(CustomerData data, SessionHolder context, LocalDateTime plannedStartTime, LocalDateTime plannedEndTime, float expectedPrice) throws ServerException {
    // Holder for data to be checked later with type-specific tests
    Pair<OnetimeService, OnetimeParkingResponse> holder = new Pair<>(null, null);

    // Make the request
    ReservedParkingRequest request = new ReservedParkingRequest(data.getCustomerID(), data.getEmail(), data.getCarID(), data.getLotID(), plannedStartTime, plannedEndTime);
    printObject(request);

    // Run general tests
    requestOnetimeParking(request, context, data, holder);

    // Run type-specific tests
    assertThat(holder.getB(), instanceOf(ReservedParkingResponse.class)); 
    ReservedParkingResponse reservedParkingResponse = (ReservedParkingResponse) holder.getB(); 
    
    // Check that the payment was correct
    assertEquals(expectedPrice, reservedParkingResponse.getPayment());
    return reservedParkingResponse;
  }

  protected ParkingEntryResponse requestParkingEntry(CustomerData data, SessionHolder context) throws ServerException {
    // subscriptionID = 0 means entry by OnetimeParking license
    ParkingEntryRequest request = new ParkingEntryRequest(data.getCustomerID(), data.getSubsID(), data.getLotID(), data.getCarID());
    printObject(request);
    ParkingEntryResponse response = sendRequest(request, context, ParkingEntryResponse.class);
    assertNotNull(response);
    printObject(response);
    return response;
  }

  protected void exitParking(CustomerData custData, LocalDateTime endTime, float expectedPrice) throws ServerException {    
    // Send Parking Exit request
    setTime(endTime);
    ParkingExitResponse response = requestParkingExit(custData, getContext());
    
    // Check that the payment was correct
    assertEquals(expectedPrice, response.getPayment());
  }

}
