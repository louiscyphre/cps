package cps.entities.people;

import java.io.Serializable;

public abstract class Person implements Serializable {
	private static final long serialVersionUID = 1L;
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
