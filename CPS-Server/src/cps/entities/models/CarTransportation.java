package cps.entities.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

import cps.common.Constants;
import cps.server.ServerException;
import cps.server.database.QueryBuilder;

/** Represents instances of transporting a car to or from the internal storage (Parking Cell array) of a Parking Lot.
 * Is a data object that can be sent through the network and persisted in the database. */
public class CarTransportation implements Serializable {

  private static final long serialVersionUID = 1L;
  /* key - customer id + car id + lot id + inserted at */
  private int    customerID;
  private String carID;
  private int    authType;
  private int    authID;
  private int    lotID;

  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public String getCarID() {
    return carID;
  }

  public void setCarID(String carID) {
    this.carID = carID;
  }

  public int getAuthType() {
    return authType;
  }

  public void setAuthType(int authType) {
    this.authType = authType;
  }

  public int getAuthID() {
    return authID;
  }

  public void setAuthID(int authID) {
    this.authID = authID;
  }

  public int getLotID() {
    return lotID;
  }

  public void setLotID(int lotID) {
    this.lotID = lotID;
  }

  public Timestamp getInsertedAt() {
    return insertedAt;
  }

  public void setInsertedAt(Timestamp insertedAt) {
    this.insertedAt = insertedAt;
  }

  public Timestamp getRemovedAt() {
    return removedAt;
  }

  public void setRemovedAt(Timestamp removedAt) {
    this.removedAt = removedAt;
  }

  /** Instantiates a new car transportation.
   * @param customerID the customer ID
   * @param carID the car ID
   * @param authType the auth type
   * @param authID the auth ID
   * @param lotID the lot ID
   * @param insertedAt the inserted at
   * @param removedAt the removed at */
  public CarTransportation(int customerID, String carID, int authType, int authID, int lotID, Timestamp insertedAt, Timestamp removedAt) {
    this.customerID = customerID;
    this.carID = carID;
    this.authType = authType;
    this.authID = authID;
    this.lotID = lotID;
    this.insertedAt = insertedAt;
    this.removedAt = removedAt;
  }

  private Timestamp insertedAt;
  private Timestamp removedAt;

  /** Instantiates a new car transportation from an SQL ResultSet.
   * @param rs the SQL ResultSet
   * @throws SQLException the SQL exception */
  public CarTransportation(ResultSet rs) throws SQLException {
    this(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getTimestamp(6), rs.getTimestamp(7));
  }

  /** Creates the.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param carID the car ID
   * @param authType the auth type
   * @param authID the auth ID
   * @param lotID the lot ID
   * @return the car transportation
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  // Creates a new CarTransportation entry in the SQL table
  public static CarTransportation create(Connection conn, int customerID, String carID, int authType, int authID, int lotID)
      throws SQLException, ServerException {
    PreparedStatement statement = conn.prepareStatement(Constants.SQL_CREATE_CAR_TRANSPORTATION, Statement.RETURN_GENERATED_KEYS);

    int field = 1;
    statement.setInt(field++, customerID);
    statement.setString(field++, carID);
    statement.setInt(field++, authType);
    statement.setInt(field++, authID);
    statement.setInt(field++, lotID);

    if (statement.executeUpdate() < 1) {
      throw new ServerException("Failed to create CarTransportation");
    }

    ResultSet keys = statement.getGeneratedKeys();
    Timestamp insertedAt = null;
    Timestamp removedAt = null;

    if (keys != null && keys.next()) {
      insertedAt = keys.getTimestamp(1);
      removedAt = keys.getTimestamp(2);
      keys.close();
    }

    statement.close();

    return new CarTransportation(customerID, carID, authType, authID, lotID, insertedAt, removedAt);
  }

  /** Find a car of the customer when he wants to exit parking.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param carID the car ID
   * @param lotID the lot ID
   * @return the car transportation
   * @throws SQLException the SQL exception */
  public static CarTransportation findForExit(Connection conn, int customerID, String carID, int lotID) throws SQLException {
    // First - find the insertion of the car

    CarTransportation result = null;
    PreparedStatement query = conn.prepareStatement(Constants.SQL_FIND_CAR_TRANSPORTATION_FOR_EXIT);

    int index = 1;
    query.setInt(index++, customerID);
    query.setString(index++, carID);
    query.setInt(index++, lotID);

    ResultSet insertionSet = query.executeQuery();

    // if exists - return the object
    if (insertionSet.next()) {
      result = new CarTransportation(insertionSet);
    }
    // else return null;

    insertionSet.close();
    query.close();
    return result;
  }

  /** Find for entry.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param carID the car ID
   * @param lotID the lot ID
   * @return the car transportation
   * @throws SQLException the SQL exception */
  public static CarTransportation findForEntry(Connection conn, int customerID, String carID, int lotID) throws SQLException {
    return new QueryBuilder<CarTransportation>(Constants.SQL_FIND_CAR_TRANSPORTATION_FOR_ENTRY).withFields(statement -> {
      statement.setInt(1, customerID);
      statement.setString(2, carID);
      statement.setInt(3, lotID);
    }).fetchResult(conn, result -> new CarTransportation(result));
  }

  /** Update removed at.
   * @param conn the SQL connection
   * @param removedAt the removed at
   * @throws SQLException the SQL exception */
  public void updateRemovedAt(Connection conn, Timestamp removedAt) throws SQLException {
    PreparedStatement st = conn.prepareStatement(Constants.SQL_UPDATE_REMOVED_AT);

    this.removedAt = removedAt;
    int index = 1;

    st.setTimestamp(index++, removedAt);
    st.setInt(index++, this.customerID);
    st.setString(index++, this.carID);
    st.setInt(index++, this.lotID);
    st.setTimestamp(index++, this.insertedAt);

    st.executeUpdate();

    st.close();
  }

  /** Find by car id.
   * @param conn the SQL connection
   * @param carID the car ID
   * @param lotID the lot ID
   * @return the car transportation
   * @throws SQLException the SQL exception */
  public static CarTransportation findByCarId(Connection conn, String carID, int lotID) throws SQLException {
    // First - find the insertion of the car

    CarTransportation result = null;
    PreparedStatement query = conn.prepareStatement(Constants.SQL_FIND_CAR_TRANSPORTATION_BY_CAR_NUMBER);

    int index = 1;
    query.setString(index++, carID);
    query.setInt(index++, lotID);

    ResultSet resultSet = query.executeQuery();

    // if exists - return the object
    if (resultSet.next()) {
      result = new CarTransportation(resultSet);
    }
    // else return null;

    resultSet.close();
    query.close();
    return result;
  }

  /** Find by lot ID.
   * @param conn the SQL connection
   * @param lotID the lot ID
   * @return the collection
   * @throws SQLException the SQL exception */
  public static Collection<CarTransportation> findByLotID(Connection conn, int lotID) throws SQLException {
    LinkedList<CarTransportation> items = new LinkedList<CarTransportation>();

    PreparedStatement statement = conn.prepareStatement(Constants.SQL_FIND_CAR_TRANSPORTATION_BY_LOT_ID);
    statement.setInt(1, lotID);
    ResultSet result = statement.executeQuery();

    while (result.next()) {
      items.add(new CarTransportation(result));
    }

    result.close();
    statement.close();

    return items;
  }

  /** Gets the parking lot.
   * @param conn the SQL connection
   * @return the parking lot
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public ParkingLot getParkingLot(Connection conn) throws SQLException, ServerException {
    return ParkingLot.findByIDNotNull(conn, lotID);
  }

  /** Gets the parking service.
   * @param conn the SQL connection
   * @return the parking service
   * @throws SQLException the SQL exception
   * @throws ServerException the server exception */
  public ParkingService getParkingService(Connection conn) throws SQLException, ServerException {
    if (authType == Constants.LICENSE_TYPE_ONETIME) {
      return OnetimeService.findByIDNotNull(conn, authID);
    } else if (authType == Constants.LICENSE_TYPE_SUBSCRIPTION) {
      return SubscriptionService.findByIDNotNull(conn, authID);
    } else {
      throw new ServerException("Invalid license type " + authType);
    }
  }

  /** Find completed subscription entry by date.
   * @param conn the SQL connection
   * @param customerID the customer ID
   * @param carID the car ID
   * @param lotID the lot ID
   * @param subsID the subs ID
   * @param day the day
   * @return the car transportation
   * @throws SQLException the SQL exception */
  public static CarTransportation findCompletedSubscriptionEntryByDate(Connection conn, int customerID, String carID, int lotID, int subsID, LocalDate day) throws SQLException {
    return new QueryBuilder<CarTransportation>(
      "SELECT * FROM car_transportation WHERE customer_id = ? AND car_id = ? AND lot_id = ? AND auth_type = ? AND auth_id = ? AND date(inserted_at) = ? AND removed_at IS NOT NULL"
      ).withFields(statement -> {
        int index = 1;
        statement.setInt(index++, customerID);
        statement.setString(index++, carID);
        statement.setInt(index++, lotID);
        statement.setInt(index++, Constants.LICENSE_TYPE_SUBSCRIPTION);
        statement.setInt(index++, subsID);
        statement.setDate(index++, Date.valueOf(day));
      }).fetchResult(conn, result -> new CarTransportation(result));
  }
}
