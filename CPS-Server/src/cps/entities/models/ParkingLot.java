package cps.entities.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingLot extends Entity {
  private static final long serialVersionUID = 1L;
  // `id` int(11) NOT NULL AUTO_INCREMENT,
  // `street_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  // `size` int(11) DEFAULT NULL,
  // `content` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  // `price1` float DEFAULT NULL,
  // `price2` float DEFAULT NULL,

  private int id;

  private String streetAddress;
  private int    size;
  private String content;
  private float  price1;
  private float  price2;

  // `id` int(11) NOT NULL AUTO_INCREMENT,
  // `street_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  // `size` int(11) DEFAULT NULL,
  // `content` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  // `price1` float DEFAULT NULL,
  // `price2` float DEFAULT NULL,
  
  public ParkingLot(int id, String streetAddress, int size, String content, float price1, float price2) {
    super();
    this.id = id;
    this.streetAddress = streetAddress;
    this.size = size;
    this.content = content;
    this.price1 = price1;
    this.price2 = price2;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public float getPrice1() {
    return price1;
  }

  public void setPrice1(float price1) {
    this.price1 = price1;
  }

  public float getPrice2() {
    return price2;
  }

  public void setPrice2(float price2) {
    this.price2 = price2;
  }

  public static ParkingLot buildFromQueryResult(ResultSet rs) throws SQLException {
    return new ParkingLot(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getFloat(5), rs.getFloat(6));
  }
  
}
