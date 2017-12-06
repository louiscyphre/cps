package cps.core;

import java.io.Serializable;

public abstract class Person implements Serializable {

	private final int id;
	private String email;
	
	public Person(int id, String email) {
		super();
		this.id = id;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}	
	
}
