package cps.common;

public interface Constants {
  public final int    DEFAULT_PORT    = 5555;
  public final String DEFAULT_HOST    = "127.0.0.1";
  public final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  public final int LICENSE_TYPE_ONETIME      = 1;
  public final int LICENSE_TYPE_SUBSCRIPTION = 2;
  public final int PARKING_TYPE_INCIDENTAL   = 1;
  public final int PARKING_TYPE_RESERVED     = 2;
  public final int SUBSCRIPTION_TYPE_REGULAR = 1;
  public final int SUBSCRIPTION_TYPE_FULL    = 2;

  public final int USER_TYPE_CUSTOMER                   = 1;
  public final int USER_TYPE_COMPANY_PERSON             = 2;
  public final int ACCESS_LEVEL_LOCAL_WORKER            = 10;
  public final int ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER = 20;
  public final int ACCESS_LEVEL_LOCAL_MANAGER           = 50;
  public final int ACCESS_LEVEL_GLOBAL_MANAGER          = 1000;

  // Bit flags
  public final int ACCESS_DOMAIN_PARKING_LOT      = 1;
  public final int ACCESS_DOMAIN_CUSTOMER_SERVICE = 2;
  public final int ACCESS_DOMAIN_GLOBAL           = 255;

  public final float  PRICE_PER_HOUR_RESERVED     = 4f;
  public final int    COMPLAINT_STATUS_PROCESSING = 1;
  public final int    COMPLAINT_STATUS_ACCEPTED   = 2;
  public final int    COMPLAINT_STATUS_REJECTED   = 3;
  public final String SPOT_IS_EMPTY               = "0";

  // SQL queries - OnetimeService
  public final String SQL_CREATE_ONETIME_SERVICE                    = "INSERT INTO onetime_service values(default, ?, ?, ?, ?, ?, ?, ?, ?)";
  public final String SQL_GET_ONETIME_SERVICE_BY_CUSTOMER_ID        = "SELECT * FROM onetime_service WHERE customer_id=? ORDER BY id";
  public final String SQL_GET_ONETIME_SERVICE_BY_CUSTID_CARID_LOTID = "SELECT * FROM onetime_service WHERE customer_id=? AND car_id=? AND lot_id=? ORDER BY id DESC LIMIT 1";
  public final String SQL_GET_ONETIME_SERVICE_BY_ID                 = "SELECT * FROM onetime_service WHERE id=?";
  public final String SQL_UPDATE_ONETIME_BY_ID                      = "UPDATE onetime_service SET parking_type = ?, customer_id=?, email=?, car_id=?, lot_id=?, planned_start_time=?, planned_end_time=?, canceled=? WHERE id=?";

  // SQL queries - CarTransportation
  public final String SQL_CREATE_CAR_TRANSPORTATION             = "INSERT INTO car_transportation values(?, ?, ?, ?, ?, default, default)";
  public final String SQL_FIND_CAR_TRANSPORTATION_BY_LOT_ID     = "SELECT * FROM car_transportation WHERE lot_id = ?";
  public final String SQL_UPDATE_REMOVED_AT                     = "UPDATE car_transportation SET removed_at=? WHERE customer_id=? AND car_id=? AND lot_id=? AND inserted_at=?";
  public final String SQL_FIND_CAR_TRANSPORTATION               = "SELECT * FROM car_transportation WHERE customer_id=? AND car_id=? AND lot_id=? AND removed_at IS NULL ORDER BY inserted_at DESC LIMIT 1";
  public final String SQL_FIND_CAR_TRANSPORTATION_BY_CAR_NUMBER = "SELECT * FROM car_transportation WHERE car_id=? AND lot_id=? AND removed_at IS NULL ORDER BY inserted_at DESC LIMIT 1";

  // SQL queries - SubscriptionService
  public final String SQL_CREATE_SUBSCRIPTION_SERVICE             = "INSERT INTO subscription_service values(default, ?, ?, ?, ?, ?, ?, ?, ?)";
  public final String SQL_GET_SUBSCRIPTION_SERVICE_BY_CUSTOMER_ID = "SELECT * FROM subscription_service WHERE customer_id=? ORDER BY id";
  public final String SQL_GET_SUBSCRIPTION_SERVICE_BY_ID          = "SELECT * FROM subscription_service WHERE id=? ORDER BY id";
  public final String SQL_GET_SUBSCRIPTION_BY_ID_CUSTOMER_CAR     = "SELECT * FROM subscription_service WHERE ID=? AND customer_id=? AND car_id=?";

  // SQL queries - DailyStatistics
  public final String SQL_CREATE_NEW_DAY          = "INSERT INTO daily_statistics values(? ,? ,default ,default ,default ,default)";
  public final String SQL_CHECK_DATE              = "SELECT * FROM daily_statistics DS WHERE day=?";
  public final String SQL_INCREASE_REALIZED_ORDER = "UPDATE daily_statistics SET realized_orders=? WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_CANCELED_ORDER = "UPDATE daily_statistics SET canceled_orders=? WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_LATE_ARRIVAL   = "UPDATE daily_statistics SET late_arrivals=? WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_INACTIVE_SLOTS = "UPDATE daily_statistics SET inactive_slots=? WHERE day=? AND lot_id=?";

  // SQL queries - ParkingLot
  public final String SQL_CREATE_PARKING_LOT    = "INSERT INTO parking_lot values(default, ?, ?, ?, ?, ?, default, ?,default)";
  public final String SQL_GET_LOT_BY_ID         = "SELECT * FROM parking_lot WHERE id=?";
  public final String SQL_FIND_ALL_PARKING_LOTS = "SELECT * FROM parking_lot ORDER BY id";
  public final String SQL_UPDATE_PARKING_LOT    = "UPDATE parking_lot SET street_address = ?, size=?, content=?, price1=?, price2=?, alternative_lots=?, robot_ip=?, lot_full=? WHERE id=?";

  // SQL queries - Customer
  public final String SQL_UPDATE_CUSTOMER     = "UPDATE customer SET email=?, password=?, debit=?, credit=? WHERE id=?";
  public final String SQL_CREATE_CUSTOMER     = "INSERT INTO customer values(default, ?, ?, default, default)";
  public final String SQL_FIND_CUSTOMER_BY_ID = "SELECT * FROM customer WHERE id=?";

  // SQL queries - Complaint
  public final String SQL_CREATE_COMPLAINT       = "INSERT INTO complaint values(default, ?, default, ?, ?, ?, ?, default)";
  public final String SQL_FIND_COMPLAINT_BY_ID   = "SELECT * FROM complaint WHERE id=?";
  public final String SQL_UPDATE_COMPLAINT_LIGHT = "UPDATE complaint SET customer_id = ?, employee_id = ?, status = ?, resolved_at = ?, refund_amount = ?";
  public final String SQL_UPDATE_COMPLAINT       = "UPDATE complaint SET customer_id = ?, employee_id = ?, status = ?, resolved_at = ?, refund_amount = ?, description = ?";
}
