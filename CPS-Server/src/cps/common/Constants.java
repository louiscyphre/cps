package cps.common;

public interface Constants {
  // TODO disable on release
  // public final boolean DEBUG_MODE = true;
  public final boolean DEBUG_MODE = false;

  public final int    DEFAULT_LOT_NUMBER = 0;
  public final int    DEFAULT_PORT       = 5555;
  public final String DEFAULT_HOST       = "127.0.0.1";
  public final String DATETIME_FORMAT    = "yyyy-MM-dd HH:mm:ss.SSS";

  public final int LICENSE_TYPE_ONETIME      = 1;
  public final int LICENSE_TYPE_SUBSCRIPTION = 2;
  public final int PARKING_TYPE_INCIDENTAL   = 1;
  public final int PARKING_TYPE_RESERVED     = 2;
  public final int SUBSCRIPTION_TYPE_REGULAR = 1;
  public final int SUBSCRIPTION_TYPE_FULL    = 2;

  public final int SUBSCRIPTION_TYPE_REGULAR_ONE_CAR_HOURS      = 60;
  public final int SUBSCRIPTION_TYPE_REGULAR_MULTIPLE_CAR_HOURS = 54;
  public final int SUBSCRIPTION_TYPE_FULL_HOURS                 = 72;

  public final int USER_TYPE_CUSTOMER                   = 1;
  public final int USER_TYPE_COMPANY_PERSON             = 2;
  public final int ACCESS_LEVEL_LOCAL_WORKER            = 10;
  public final int ACCESS_LEVEL_CUSTOMER_SERVICE_WORKER = 20;
  public final int ACCESS_LEVEL_LOCAL_MANAGER           = 50;
  public final int ACCESS_LEVEL_GLOBAL_MANAGER          = 1000;

  // Bit flags
  public final int ACCESS_DOMAIN_PARKING_LOT      = 1;
  public final int ACCESS_DOMAIN_CUSTOMER_SERVICE = 2;
  public final int ACCESS_DOMAIN_EVERYTHING       = 255;

  public final float  PRICE_PER_HOUR_RESERVED     = 4f;
  public final int    COMPLAINT_STATUS_PROCESSING = 1;
  public final int    COMPLAINT_STATUS_ACCEPTED   = 2;
  public final int    COMPLAINT_STATUS_REJECTED   = 3;
  public final String SPOT_IS_EMPTY               = "0";

  public final int LOT_HEIGHT = 3;
  public final int LOT_DEPTH  = 3;

  // SQL queries - OnetimeService
  public final String SQL_CREATE_ONETIME_SERVICE             = "INSERT INTO onetime_service values(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public final String SQL_GET_ONETIME_SERVICE_BY_CUSTOMER_ID = "SELECT * FROM onetime_service WHERE customer_id=? ORDER BY id";
  public final String SQL_FIND_ONETIME_SERVICE_FOR_ENTRY     = "SELECT * FROM onetime_service WHERE customer_id=? AND car_id=? AND lot_id=? AND not canceled AND not completed ORDER BY id DESC LIMIT 1";
  public final String SQL_GET_ONETIME_SERVICE_BY_ID          = "SELECT * FROM onetime_service WHERE id=?";
  public final String SQL_OVERLAPPING_ONETIME_SERVICE_CLAUSE = "car_id=? AND ((planned_start_time < ? AND ? < planned_end_time) OR (? < planned_start_time AND planned_start_time < ?)) AND not canceled AND not completed";
  public final String SQL_FIND_OVERLAPPING_ONETIME_SERVICE   = "SELECT * FROM onetime_service WHERE " + SQL_OVERLAPPING_ONETIME_SERVICE_CLAUSE;
  public final String SQL_COUNT_OVERLAPPING_ONETIME_SERVICE  = "SELECT count(*) FROM onetime_service WHERE " + SQL_OVERLAPPING_ONETIME_SERVICE_CLAUSE;
  public final String SQL_UPDATE_ONETIME_BY_ID               = "UPDATE onetime_service SET parking_type=?, customer_id=?, email=?, car_id=?, lot_id=?, planned_start_time=?, planned_end_time=?, parked=?, completed=?, canceled=?, warned=? WHERE id=?";

  // SQL queries - SubscriptionService
  // TODO: add `parked` field to `subscription_service` table
  public final String SQL_CREATE_SUBSCRIPTION_SERVICE             = "INSERT INTO subscription_service values(default, ?, ?, ?, ?, ?, ?, ?, ?)";
  public final String SQL_GET_SUBSCRIPTION_SERVICE_BY_CUSTOMER_ID = "SELECT * FROM subscription_service WHERE customer_id=? ORDER BY id";
  public final String SQL_GET_SUBSCRIPTION_SERVICE_BY_ID          = "SELECT * FROM subscription_service WHERE id=? ORDER BY id";
  public final String SQL_GET_SUBSCRIPTION_BY_ID_CUSTOMER_CAR     = "SELECT * FROM subscription_service WHERE ID=? AND customer_id=? AND car_id=?";
  public final String SQL_UPDATE_SUBSCRIPTION_BY_ID               = "UPDATE subscription_service SET subs_type=?, customer_id=?, email=?, car_id=?, lot_id=?, start_date=?, end_date=?, daily_exit_time=? WHERE id=?";

  // SQL queries - CarTransportation
  public final String SQL_CREATE_CAR_TRANSPORTATION             = "INSERT INTO car_transportation values(?, ?, ?, ?, ?, default, default)";
  public final String SQL_FIND_CAR_TRANSPORTATION_BY_LOT_ID     = "SELECT * FROM car_transportation WHERE lot_id=?";
  public final String SQL_UPDATE_REMOVED_AT                     = "UPDATE car_transportation SET removed_at=? WHERE customer_id=? AND car_id=? AND lot_id=? AND inserted_at=?";
  public final String SQL_FIND_CAR_TRANSPORTATION_FOR_ENTRY     = "SELECT * FROM car_transportation WHERE customer_id=? AND car_id=? AND lot_id=? LIMIT 1";
  public final String SQL_FIND_CAR_TRANSPORTATION_FOR_EXIT      = "SELECT * FROM car_transportation WHERE customer_id=? AND car_id=? AND lot_id=? AND removed_at IS NULL ORDER BY inserted_at DESC LIMIT 1";
  public final String SQL_FIND_CAR_TRANSPORTATION_BY_CAR_NUMBER = "SELECT * FROM car_transportation WHERE car_id=? AND lot_id=? AND removed_at IS NULL ORDER BY inserted_at DESC LIMIT 1";

  // SQL queries - DailyStatistics
  public final String SQL_CREATE_NEW_DAY          = "INSERT INTO daily_statistics values(? ,? ,default ,default ,default ,default,default)";
  public final String SQL_CHECK_DATE              = "SELECT * FROM daily_statistics WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_REALIZED_ORDER = "UPDATE daily_statistics SET realized_orders=? WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_CANCELED_ORDER = "UPDATE daily_statistics SET canceled_orders=? WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_LATE_ARRIVAL   = "UPDATE daily_statistics SET late_arrivals=? WHERE day=? AND lot_id=?";
  public final String SQL_INCREASE_INACTIVE_SLOTS = "UPDATE daily_statistics SET inactive_slots=? WHERE day=? AND lot_id=?";

  // SQL queries - Weekly statistics
  public final String SQL_CREATE_WEEKLY_STATISTICS = "INSERT INTO weekly_statistics values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  public final String SQL_FIND_WEEKLY_STATISTICS   = "SELECT * FROM weekly_statistics WHERE start = ? AND lot_id = ?";
  public final String SQL_UPDATE_WEEKLY_STATISTICS = "UPDATE weekly_statistics SET start = ?, lot_id = ?, realized_orders_mean = ?, canceled_orders_mean = ?, late_arrivals_mean = ?, realized_orders_median = ?, canceled_orders_median = ?, late_arrivals_median = ?, realized_orders_dist = ?, canceled_orders_dist = ?, late_arrivals_dist = ?";

  // SQL queries - ParkingLot
  public final String SQL_CREATE_PARKING_LOT    = "INSERT INTO parking_lot values(default, ?, ?, ?, ?, default, ?,default)";
  public final String SQL_GET_LOT_BY_ID         = "SELECT * FROM parking_lot WHERE id=?";
  public final String SQL_FIND_ALL_PARKING_LOTS = "SELECT * FROM parking_lot ORDER BY id";
  public final String SQL_UPDATE_PARKING_LOT    = "UPDATE parking_lot SET street_address=?, size=?, price1=?, price2=?, alternative_lots=?, robot_ip=?, lot_full=? WHERE id=?";

  // SQL queries - ParkingCell
  public final String SQL_CREATE_PARKING_CELL         = "INSERT INTO parking_cell values(?, ?, ?, ?, ?, ?, ?, ?)";
  public final String SQL_FIND_PARKING_CELL           = "SELECT * FROM parking_cell WHERE lot_id=? and i=? and j=? and k=?";
  public final String SQL_UPDATE_PARKING_CELL         = "UPDATE parking_cell SET car_id=?, planned_end_time=?, reserved=?, disabled=? where lot_id=? AND i=? AND j=? AND k=?";
  public final String SQL_FIND_PARKING_CELL_BY_LOT_ID = "SELECT * FROM parking_cell WHERE lot_id=?";
  public final String SQL_COUNT_FREE_PARKING_CELLS    = "SELECT count(*) FROM parking_cell WHERE lot_id=? AND car_id IS NULL AND NOT reserved AND NOT disabled";

  // SQL queries - Customer
  public final String SQL_UPDATE_CUSTOMER                     = "UPDATE customer SET email=?, password=?, debit=?, credit=? WHERE id=?";
  public final String SQL_CREATE_CUSTOMER                     = "INSERT INTO customer values(default, ?, ?, default, default)";
  public final String SQL_FIND_CUSTOMER_BY_ID                 = "SELECT * FROM customer WHERE id=?";
  public final String SQL_FIND_CUSTOMER_BY_EMAIL              = "SELECT * FROM customer WHERE email=?";
  public final String SQL_FIND_CUSTOMER_BY_EMAIL_AND_PASSWORD = "SELECT * FROM customer WHERE email=? AND password=?";

  // SQL queries - Complaint
  public final String SQL_CREATE_COMPLAINT       = "INSERT INTO complaint values(default, ?, default, ?, ?, ?, ?, default)";
  public final String SQL_FIND_COMPLAINT_BY_ID   = "SELECT * FROM complaint WHERE id=?";
  public final String SQL_UPDATE_COMPLAINT_LIGHT = "UPDATE complaint SET customer_id=?, employee_id=?, status=?, resolved_at=?, refund_amount=?";
  public final String SQL_UPDATE_COMPLAINT       = "UPDATE complaint SET customer_id=?, employee_id=?, status=?, resolved_at=?, refund_amount=?, description=?";
}
