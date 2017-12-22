package cps.entities.models;

public class Customer extends Entity implements User {
	private static final long serialVersionUID = 1L;

	public int id;
	public String email;
	public float balance;

	public Customer(int id, String email, float balance) {
		this.id = id;
		this.email = email;
		this.balance = balance;
	}
}
