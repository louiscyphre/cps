package cps.api.request;

public class IncidentalParkingRequest extends OnetimeParkingRequest {
	private static final long serialVersionUID = 1L;
	public static final int TYPE = 1;

	/*public IncidentalParkingRequest(int customerID, String email, String carID, int lotID, LocalDateTime plannedEndTime) {
		super(customerID, email, carID, lotID, plannedEndTime);
		// TODO Auto-generated constructor stub
	}*/

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		buffer.append("customerID: " + getCustomerID() + ", ");
		buffer.append("email: " + getEmail() + ", ");
		buffer.append("carID: " + getCarID() + ", ");
		buffer.append("lotID: " + getLotID() + ", ");
		buffer.append("plannedEndTime: " + getPlannedEndTime() + "}");
		return buffer.toString();
	}
}
