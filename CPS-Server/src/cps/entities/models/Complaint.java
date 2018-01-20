package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Constants;
import cps.server.ServerException;
import cps.server.database.DatabaseController;

/** Represents a customer complaint as a data object that can be sent through the network and persisted in the database. */
public class Complaint implements Serializable {
  private static final long serialVersionUID = 1L;
  private int               id;
  private int               customerID;
  private int               employeeID;
  private int               lotID;
  private int               status;
  private String            description;
  private String            reason;
  private Timestamp         createdAt;
  private Timestamp         resolvedAt;
  private float             refundAmount;

  /** Instantiates a new complaint.
   * @param id the id
   * @param customerID the customer ID
   * @param employeeID the employee ID
   * @param status the status
   * @param description the description
   * @param createdAt the created at
   * @param resolvedAt the resolved at
   * @param refundAmount the refund amount */
  public Complaint(int id, int customerID, int employeeID, int lotID, int status, String description, String reason, Timestamp createdAt, Timestamp resolvedAt, float refundAmount) {
    this.id = id;
    this.customerID = customerID;
    this.employeeID = employeeID;
    this.lotID = lotID;
    this.status = status;
    this.description = description;
    this.reason = reason;
    this.createdAt = createdAt;
    this.resolvedAt = resolvedAt;
    this.refundAmount = refundAmount;
  }

  /** Instantiates a new complaint from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException on error */
  public Complaint(ResultSet rs) throws SQLException {
    int index = 1;
    this.id = rs.getInt(index++);
    this.customerID = rs.getInt(index++);
    this.employeeID = rs.getInt(index++);
    this.lotID = rs.getInt(index++);
    this.status = rs.getInt(index++);
    this.description = rs.getString(index++);
    this.reason = rs.getString(index++);
    this.createdAt = rs.getTimestamp(index++);
    this.resolvedAt = rs.getTimestamp(index++);
    this.refundAmount = rs.getFloat(index++);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public void setEmployeeID(int employeeID) {
    this.employeeID = employeeID;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(Timestamp resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  public float getRefundAmount() {
    return refundAmount;
  }

  public void setRefundAmount(float refundAmount) {
    this.refundAmount = refundAmount;
  }

  /** Creates a new complaint entry in the database.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param description the description
   * @param createdAt the created at
   * @param resolvedAt the resolved at
   * @return the complaint
   * @throws SQLException on error */
  
  public static Complaint create(Connection conn, int customerID, int employeeID, int lotID, int status, String description, String reason, Timestamp createdAt, Timestamp resolvedAt, float refundAmount) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_COMPLAINT, Statement.RETURN_GENERATED_KEYS);

    int field = 1;
    statement.setInt(field++, customerID);
    statement.setInt(field++, employeeID);
    statement.setInt(field++, lotID);
    statement.setInt(field++, status);
    statement.setString(field++, description);
    statement.setString(field++, reason);
    statement.setTimestamp(field++, createdAt);
    statement.setTimestamp(field++, resolvedAt);
    statement.setFloat(field++, refundAmount);
    statement.executeUpdate();

    ResultSet keys = statement.getGeneratedKeys();
    int newID = 0;

    if (keys != null && keys.next()) {
      newID = keys.getInt(1);
      keys.close();
    }

    statement.close();

    return new Complaint(newID, customerID, employeeID, lotID, status, description, reason, createdAt, resolvedAt, refundAmount);
  }
  
  /** Short version of create for creating a new complaint.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param lotID the lot ID
   * @param description the description
   * @param createdAt the created at
   * @param resolvedAt the resolved at
   * @return the complaint
   * @throws SQLException on error */
  public static Complaint create(Connection conn, int customerID, int lotID, String description, Timestamp createdAt, Timestamp resolvedAt) throws SQLException {
    return create(conn, customerID, 0, lotID, Constants.COMPLAINT_STATUS_PROCESSING, description, null, createdAt, resolvedAt, 0);
  }

  /** Find a complaint entry by its `id` field.
   * @param conn the SQL connection
   * @param id the id
   * @return the complaint
   * @throws SQLException on error */
  public static Complaint findByID(Connection conn, int id) throws SQLException {
    Complaint item = null;
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_COMPLAINT_BY_ID);

    statement.setInt(1, id);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = new Complaint(result);
    }

    statement.close();
    result.close();

    return item;
  }

  /** Find a complaint entry by its `id` field.
   * Throw an exception if not found.
   * @param conn the SQL connection
   * @param id the id
   * @return the complaint
   * @throws SQLException on error
   * @throws ServerException if not found */
  public static Complaint findByIDNotNull(Connection conn, int id) throws SQLException, ServerException {
    Complaint result = findByID(conn, id);

    if (result == null) {
      throw new ServerException("Complaint with id " + id + " does not exist");
    }

    return result;
  }

  /** Find all complaints filed by a specific customer, identified by their customer ID.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @return a list of the customer's complaints
   * @throws SQLException on error */
  public static Collection<Complaint> findByCustomerID(Connection conn, int customerID) throws SQLException {
    Collection<Complaint> items = new LinkedList<>();
    DatabaseController.statementForEach(conn, "SELECT * FROM complaint where customer_id = ?", statement -> statement.setInt(1, customerID),
        result -> items.add(new Complaint(result)));
    return items;
  }

  /** Find all complaints in the system.
   * @param conn the SQL connection
   * @return a list of all complaints in the system
   * @throws SQLException on error */
  public static Collection<Complaint> findAll(Connection conn) throws SQLException {
    Collection<Complaint> items = new LinkedList<>();
    DatabaseController.statementForEach(conn, "SELECT * FROM complaint", statement -> {
    }, result -> items.add(new Complaint(result)));
    return items;
  }

  /** Update (save to database) an existing complaint entry.
   * Light update - write all fields except complaint description
   * Full update - write all fields, including description
   * @param conn the SQL connection
   * @param light is it a light update, or a full update
   * @throws SQLException on error */
  public void update(Connection conn, boolean light) throws SQLException {
    String queryString = light ? Constants.SQL_UPDATE_COMPLAINT_LIGHT : Constants.SQL_UPDATE_COMPLAINT;
    PreparedStatement st = conn.prepareStatement(queryString);

    int field = 1;
    st.setInt(field++, customerID);
    st.setInt(field++, employeeID);
    st.setInt(field++, lotID);
    st.setInt(field++, status);

    if (!light) {
      // The description field is 2000 chars, which is why we avoid updating it if not necessary
      st.setString(field++, description);
    }
    
    st.setString(field++, reason);
    st.setTimestamp(field++, createdAt);
    st.setTimestamp(field++, resolvedAt);
    st.setFloat(field++, refundAmount);

    st.setInt(field++, id);
    st.executeUpdate();
    st.close();
  }

  /** Update (save to database) an existing complaint entry.
   * Shortcut method for a full update.
   * @param conn the SQL connection
   * @throws SQLException on error */
  // Default - full update
  public void update(Connection conn) throws SQLException {
    update(conn, false);
  }

  /** Represent the complaint status (an integer) in textual form.
   * @return the status text */
  public String getStatusText() {
    switch (status) {
      case Constants.COMPLAINT_STATUS_PROCESSING:
        return "Processing";
      case Constants.COMPLAINT_STATUS_ACCEPTED:
        return "Accepted";
      case Constants.COMPLAINT_STATUS_REJECTED:
        return "Rejected";
      default:
        return String.format("<Unknown status %d>", status);
    }
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

}
