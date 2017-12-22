package cps.entities.models;

import cps.entities.people.User;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}
}
