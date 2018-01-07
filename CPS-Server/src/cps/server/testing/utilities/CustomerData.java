package cps.server.testing.utilities;

public class CustomerData {
	public int customerID;
	public String email;
	public String password;
	public String carID;
	public int lotID;
	public int subsID;

	public CustomerData(int customerID, String email, String password, String carID, int lotID, int subsID) {
		this.customerID = customerID;
		this.email = email;
		this.carID = carID;
		this.lotID = lotID;
		this.subsID = subsID;
	}
}
