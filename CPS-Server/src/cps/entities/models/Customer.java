package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cps.common.Constants;
import cps.entities.people.User;
import cps.server.ServerException;

public class Customer implements Serializable, User {
	private static final long serialVersionUID = 1L;

	public int id;
	public String email;
	public String password;
	public float debit;
	public float credit;

	public Customer(int id, String email, String password, float debit, float credit) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.debit = debit;
		this.credit = credit;
	}

	public Customer(ResultSet rs) throws SQLException {
		this(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getFloat(5));
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public float getDebit() {
		return debit;
	}

	public void setDebit(float debit) {
		this.debit = debit;
	}

	public void addDebit(float debit) {
		this.debit += debit;		
	}

	public float getCredit() {
		return credit;
	}

	public void setCredit(float credit) {
		this.credit = credit;
	}

	public void addCredit(float credit) {
		this.credit += credit;		
	}

	public static Customer create(Connection conn, String email, String password) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_CUSTOMER,
				Statement.RETURN_GENERATED_KEYS);

		int field = 1;
		stmt.setString(field++, email);
		stmt.setString(field++, password);
		stmt.executeUpdate();

		ResultSet keys = stmt.getGeneratedKeys();
		int newID = 0;

		if (keys != null && keys.next()) {
			newID = keys.getInt(1);
			keys.close();
		}

		stmt.close();

		return new Customer(newID, email, password, 0f, 0f);

	}

	public static Customer findByID(Connection conn, int id) throws SQLException {
		Customer result = null;
		PreparedStatement st = conn.prepareStatement(Constants.SQL_FIND_CUSTOMER_BY_ID);
		
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			result = new Customer(rs);
		}

		st.close();
		rs.close();
		
		return result;
	}
	
	public void update(Connection conn) throws SQLException, ServerException {
		PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_CUSTOMER);

		int index = 1;

		st.setString(index++, email);
		st.setString(index++, password);
		st.setFloat(index++, debit);
		st.setFloat(index++, credit);
		st.setInt(index++, id);

		int updated = st.executeUpdate();

		st.close();

		if (updated <= 0) {
			throw new ServerException("Failed to update customer");
		}
	}

	public static Customer findByEmailAndPassword(Connection conn, String email, String password) throws SQLException {
		Customer result = null;
		PreparedStatement st = conn.prepareStatement("SELECT * FROM customer WHERE email=? AND password=?");
		
		st.setString(1, email);
		st.setString(2, password);
		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			result = new Customer(rs);
		}

		st.close();
		rs.close();
		
		return result;
	}

	public static Customer findByIDNotNull(Connection conn, int id) throws SQLException, ServerException {
		Customer result = findByID(conn, id);

		if (result == null) {
			throw new ServerException("Customer with id " + id + " does not exist");
		}

		return result;
	}
	
	public void pay(Connection conn, float sum) throws SQLException, ServerException {
		addDebit(sum);
		update(conn);
	}
	
	public void receiveRefund(Connection conn, float sum) throws SQLException, ServerException {
		addCredit(sum);
		update(conn);
	}

	@Override
	public int getUserType() {
		return Constants.USER_TYPE_CUSTOMER;
	}

	@Override
	public int getAccessLevel() {
		return 0;
	}

	@Override
	public boolean canAccessDomain(int domain) {
		return false;
	}
}
