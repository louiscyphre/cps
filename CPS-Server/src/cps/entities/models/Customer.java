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

/** Represents a customer as a data object that can be sent through the network and persisted in the database. */
public class Customer implements Serializable, User {
  private static final long serialVersionUID = 1L;

  /** The customer's id. */
  public int    id;
  
  /** The customer's email. */
  public String email;
  
  /** The customer's password. */
  public String password;
  
  /** The amount of debit in the customer's account. */
  public float  debit;
  
  /** The amount of credit in the customer's account. */
  public float  credit;

  /** Instantiates a new customer.
   * @param id the id
   * @param email the email
   * @param password the password
   * @param debit the debit
   * @param credit the credit */
  public Customer(int id, String email, String password, float debit, float credit) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.debit = debit;
    this.credit = credit;
  }

  /** Instantiates a new customer from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
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

  /** Adds the debit.
   * @param debit the debit */
  public void addDebit(float debit) {
    this.debit += debit;
  }

  public float getCredit() {
    return credit;
  }

  public void setCredit(float credit) {
    this.credit = credit;
  }

  /** Adds the credit.
   * @param credit the credit */
  public void addCredit(float credit) {
    this.credit += credit;
  }

  /** Create a new customer record in the database.
   * @param conn the SQL connection
   * @param email the email
   * @param password the password
   * @return the customer
   * @throws SQLException on error */
  public static Customer create(Connection conn, String email, String password) throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(Constants.SQL_CREATE_CUSTOMER, Statement.RETURN_GENERATED_KEYS);

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

  /** Find customer by ID.
   * @param conn the SQL connection
   * @param id the id
   * @return the customer
   * @throws SQLException on error */
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

  /** Find customer by ID, throw ServerException if not found.
   * @param conn the SQL connection
   * @param id the id
   * @return the customer
   * @throws SQLException on error
   * @throws ServerException if not found */
  public static Customer findByIDNotNull(Connection conn, int id) throws SQLException, ServerException {
    Customer result = findByID(conn, id);

    if (result == null) {
      throw new ServerException("Customer with id " + id + " does not exist");
    }

    return result;
  }

  /** Find customer by email.
   * @param conn the SQL connection
   * @param email the email
   * @return the customer
   * @throws SQLException on error */
  public static Customer findByEmail(Connection conn, String email) throws SQLException {
    Customer result = null;
    PreparedStatement st = conn.prepareStatement(Constants.SQL_FIND_CUSTOMER_BY_EMAIL);

    st.setString(1, email);
    ResultSet rs = st.executeQuery();

    if (rs.next()) {
      result = new Customer(rs);
    }

    st.close();
    rs.close();

    return result;
  }

  /** Find by email and password.
   * @param conn the SQL connection
   * @param email the email
   * @param password the password
   * @return the customer
   * @throws SQLException on error */
  public static Customer findByEmailAndPassword(Connection conn, String email, String password) throws SQLException {
    Customer result = null;
    PreparedStatement st = conn.prepareStatement(Constants.SQL_FIND_CUSTOMER_BY_EMAIL_AND_PASSWORD);

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

  /** Update the database record for this customer.
   * @param conn the SQL connection
   * @throws SQLException on error
   * @throws ServerException on error */
  public void update(Connection conn) throws SQLException, ServerException {
    PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_CUSTOMER);

    int index = 1;

    st.setString(index++, email);
    st.setString(index++, password);
    st.setFloat(index++, debit);
    st.setFloat(index++, credit);
    st.setInt(index++, id);

    st.executeUpdate();

    st.close();
  }

  /** Simulates payment. Add sum as a debit to the client's account and update the database record.
   * @param conn the SQL connection
   * @param sum the sum
   * @throws SQLException on error
   * @throws ServerException on error */
  public void pay(Connection conn, float sum) throws SQLException, ServerException {
    addDebit(sum);
    update(conn);
  }

  /** Simulates receiving refund. Add sum as a credit to the client's account and update the database record.
   * @param conn the SQL connection
   * @param sum the sum
   * @throws SQLException on error
   * @throws ServerException on error */
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

  /* (non-Javadoc)
   * @see cps.entities.people.User#canAccessDomain(int)
   */
  @Override
  public boolean canAccessDomain(int domain) {
    return false;
  }
}
