package cps.server.devices;

public class Robot {
	private int lotID;
	
	public void insertCar(String carID, int i, int j, int k) {
		System.out.println("I was told to insert a car, but I am a useless class that does nothing.");
	}
	
	public void retrieveCar(String carID, int i, int j, int k) {
		System.out.println("I was told to retrieve a car, but I am a useless class that does nothing.");
	}

	public int getLotID() {
		return lotID;
	}

	public void setLotID(int lotID) {
		this.lotID = lotID;
	}
}
