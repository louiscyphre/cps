package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cps.common.Constants;
import cps.entities.people.User;

public class Customer implements Serializable, User {
	private static final long serialVersionUID = 1L;

	public int id;
	public String password;
	public String email;
	public float debit;
	public float credit;

	public Customer(int id, String email, float debit, float credit) {
		this.id = id;
		this.email = email;
		this.debit = debit;
		this.credit = credit;
	}

	public Customer(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getString(2), rs.getFloat(3), rs.getFloat(4));
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

	public float getDebit() {
		return debit;
	}

	public void setDebit(float debit) {
		this.debit = debit;
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	}

	public static Customer findByID(Connection conn, int id) throws SQLException {
		Customer result = null;
		PreparedStatement st = conn.prepareStatement("SELECT * FROM customer WHERE id=?");
		
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			result = new Customer(rs);
		}

		st.close();
		rs.close();
		
		return result;
	}
	
	public boolean update(Connection conn) throws SQLException {
		PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_CUSTOMER);

		int index = 1;

		st.setString(index++, email);
		st.setString(index++, password);
		st.setFloat(index++, debit);
		st.setFloat(index++, credit);

		int updated = st.executeUpdate();

		st.close();

		return updated > 0;		
	}
}
