package cps.entities.models;

import cps.entities.people.Person;

public class Customer extends Person {
	private static final long serialVersionUID = 1L;
	
	public int id;
	public String email;
	public float balance;
	
	public Customer(int id, String email, float balance) {
		super(id, email);
		this.balance = balance;
	}
}
