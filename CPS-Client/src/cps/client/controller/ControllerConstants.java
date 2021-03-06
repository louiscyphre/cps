package cps.client.controller;

/**
 * Class to hold all the constants needed for the application operation. 
 */
public class ControllerConstants {

  /**
   *  SceneCodes hold relative paths to fxml
   */
  public enum SceneCode {

    TEST_SCENE("view/service/ServiceActionDisableSlotDynamicGrid.fxml"),

    // customer views
    LOGIN("view/customer/LoginScene.fxml"),

    CUSTOMER_INITIAL_MENU("view/customer/CustomerInitialMenuScene.fxml"),

    CUSTOMER_LIST_SUBSCRIPTIONS("view/customer/CustomerListSubscriptionsScene.fxml"),

    RESERVE_PARKING("view/customer/ReserveParkingScene.fxml"),

    INCIDENTAL_PARKING("view/customer/IncidentalParkingScene.fxml"),

    ENTER_PARKING("view/customer/EnterParkingScene.fxml"),

    EXIT_PARKING("view/customer/ExitParkingScene.fxml"),

    VIEW_MY_RESERVATION("view/customer/ViewMyReservationsScene.fxml"),

    REGULAR_SUBSCRIPTION("view/customer/RegularSubscriptionScene.fxml"),

    FULL_SUBSCRIPTION("view/customer/FullSubscriptionScene.fxml"),

    FILE_COMPLAINT("view/customer/FileComplaintScene.fxml"),

    // service views
    SERVICE_ACTION_INIT_LOT("view/service/ServiceActionInitLotScene.fxml"),

    SERVICE_ACTION_LOT_IS_FULL("view/service/ServiceActionLotIsFull.fxml"),

    SERVICE_ACTION_LOT_STATE("view/service/ServiceActionLotState.fxml"),

    SERVICE_ACTION_MENU("view/service/ServiceActionMenuScene.fxml"),

    SERVICE_ACTION_REFUND("view/service/ServiceActionRefund.fxml"),

    SERVICE_ACTION_UPDATE_PRICES("view/service/ServiceActionUpdatePrices.fxml"),

    SERVICE_ACTION_LOGIN("view/service/ServiceLoginScene.fxml"),

    SERVICE_ACTION_MANAGE_LOT("view/service/ServiceActionManageLot.fxml"),
    
    SERVICE_STATISTICS_CHOICE("view/service/ServiceStatisticsChoice.fxml"),
    
    SERVICE_STATISTICS_QUARTERLY("view/service/ServiceStaticticsQuarterly.fxml"),
    
    SERVICE_STATISTICS_ACTIVITY("view/service/ServiceStaticticsActivity.fxml"),
    
    SERVICE_STATISTICS_PERFORMANCE("view/service/ServiceStaticticsPerformance.fxml"),
    ;

    String myRelativePath;

    SceneCode(String relativePath) {
      this.myRelativePath = relativePath;
    }

    String getCode() {
      return this.name();
    }

  }

  /**
   * Enum for error message handling, and default error messages.
   */
  public enum InputVerification {
    INPUT_OK(0, "The request is valid"),

    MISSING_USERID(0, "Missing or bad UserID"),

    MISSING_EMAIL(1, "Missing or bad Email"),

    MISSING_CARID(3, "Missing or bad CarID"),

    MISSING_LOTID(4, "Missing or bad LotID"),

    MISSING_PLANNEDENDTIME(5, "Missing or bad Planned End Time"),

    MISSING_PASSWORD(6, "Missing password"),;

    private final int    id;
    private final String msg;

    InputVerification(int id, String msg) {
      this.id = id;
      this.msg = msg;
    }

    public int getId() {
      return this.id;
    }

    public String getMsg() {
      return this.msg;
    }
  }
}
