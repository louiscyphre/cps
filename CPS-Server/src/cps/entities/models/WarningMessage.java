package cps.entities.models;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Class WarningMessage.
 */
public class WarningMessage {
  private int       customerId;
  private String    email;
  private String    carid;
  private int       lotId;
  private Timestamp plannedStartTime;
  private Timestamp plannedEndTime;

  /**
   * Instantiates a new warning message.
   *
   * @param customerid
   *          the customerid
   * @param email
   *          the email
   * @param carid
   *          the carid
   * @param lotid
   *          the lotid
   * @param startTime
   *          the start time
   * @param endTime
   *          the end time
   */
  public WarningMessage(int customerid, String email, String carid, int lotid, Timestamp startTime, Timestamp endTime) {
    this.customerId = customerid;
    this.email = email;
    this.carid = carid;
    this.lotId = lotid;
    this.plannedStartTime = startTime;
    this.plannedEndTime = endTime;
  }

  /**
   * Instantiates a new warning message.
   *
   * @param rs
   *          the rs
   * @throws SQLException
   *           the SQL exception
   */
  public WarningMessage(ResultSet rs) throws SQLException {
    this.customerId = rs.getInt("customer_id");
    this.email = rs.getString("email");
    this.email = rs.getString("car_id");
    this.lotId = rs.getInt("lot_id");
    this.plannedStartTime = rs.getTimestamp("planned_start_time");
    this.plannedEndTime = rs.getTimestamp("planned_end_time");
  }

  public boolean setsend(Connection conn) throws SQLException {
    // TODO mark current message as sent in the DB
    // TODO test this
    String helper = "";
    helper += "UPDATE onetime_service SET warned=true WHERE warned=false AND lot_id=? AND ";
    helper += "car_id=? AND customer_id=? AND planned_start_time=? AND planned_end_time=?";
    PreparedStatement st = conn.prepareStatement(helper);
    int i = 1;
    st.setInt(i++, this.lotId);
    st.setString(i++, this.carid);
    st.setInt(i++, this.customerId);
    st.setTimestamp(i++, this.plannedStartTime);
    st.setTimestamp(i++, this.plannedEndTime);
    int count = st.executeUpdate();
    return count == 1;
  }

  public void warn(Connection conn) {
    // TODO optional - send an email to email address
    /*
     * I've tried to set up mailing, but unfortunately, Gmail does not allow non
     * registered applications to use it's smtp anymore. So screw it
     */
    System.out.printf(
        "\nSending message to customer %d, to email %s.\n We are waiting for your car #%s, at %d, at %s\n",
        this.customerId, this.email, this.carid, this.lotId, this.plannedStartTime.toString());
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCarid() {
    return carid;
  }

  public void setCarid(String carid) {
    this.carid = carid;
  }

  public int getLotId() {
    return lotId;
  }

  public void setLotId(int lotId) {
    this.lotId = lotId;
  }

  public Timestamp getPlannedStartTime() {
    return plannedStartTime;
  }

  public void setPlannedStartTime(Timestamp plannedStartTime) {
    this.plannedStartTime = plannedStartTime;
  }

  public Timestamp getPlannedEndTime() {
    return plannedEndTime;
  }

  public void setPlannedEndTime(Timestamp plannedEndTime) {
    this.plannedEndTime = plannedEndTime;
  }

}
