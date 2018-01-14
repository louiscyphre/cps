package cps.client.controller;

public class ControllerConstants {

  public enum SceneCode {

    TEST_SCENE("../view/testScene.fxml"),

    ALPHA_INCIDENTAL_PARKING("../view/AlphaGUI_2.fxml"),

    ALPHA_VIEW_MY_REQUESTS("../view/AlphaGUI_3.fxml"),

    ALPHA_REQUEST_PARKING_ENTRY("../view/AlphaGUI_4.fxml"),

    ALPHA_INIT_PARKING_LOT("../view/AlphaGUI_5.fxml"),

    ALPHA_MAIN_MENU("../view/AlphaGUI_mainMenu.fxml"),

    // customer views
    LOGIN("../view/customer/LoginScene.fxml"),

    CUSTOMER_INITIAL_MENU("../view/customer/CustomerInitialMenuScene.fxml"),

    CUSTOMER_LIST_SUBSCRIPTIONS("../view/customer/CustomerListSubscriptionsScene.fxml"),

    RESERVE_PARKING("../view/customer/ReserveParkingScene.fxml"),

    INCIDENTAL_PARKING("../view/customer/IncidentalParkingScene.fxml"),

    ENTER_PARKING("../view/customer/EnterParkingScene.fxml"),

    EXIT_PARKING("../view/customer/ExitParkingScene.fxml"),

    VIEW_MY_RESERVATION("../view/customer/ViewMyReservationsScene.fxml"),

    // service views
    SERVICE_ACTION_DISABLE_SLOT("../view/service/ServiceActionDisableSlot.fxml"),

    SERVICE_ACTION_INIT_LOT("../view/service/ServiceActionInitLotScene.fxml"),

    SERVICE_ACTION_LOT_IS_FULL("../view/service/ServiceActionLotIsFull.fxml"),

    SERVICE_ACTION_LOT_STATE("../view/service/ServiceActionLotState.fxml"),

    SERVICE_ACTION_MENU("../view/service/ServiceActionMenuScene.fxml"),

    SERVICE_ACTION_REFUND("../view/service/ServiceActionRefund.fxml"),

    SERVICE_ACTION_RESERVE_SLOT("../view/service/ServiceActionReserveSlot.fxml"),

    SERVICE_ACTION_UPDATE_PRICES("../view/service/ServiceActionUpdatePrices.fxml"),

    SERVICE_ACTION_LOGIN("../view/service/ServiceLoginScene.fxml"),

    SERVICE_ACTION_MAIN_MENU("../view/service/ServiceMainMenuScene.fxml"),;

    String myRelativePath;

    SceneCode(String relativePath) {
      this.myRelativePath = relativePath;
    }

    String getCode() {
      return this.name();
    }

  }

  public enum InputVerification {
    INPUT_OK(0, "The request is valid"), MISSING_USERID(0, "Missing or bad UserID"), MISSING_EMAIL(1,
        "Missing or bad Email"), MISSING_CARID(3, "Missing or bad CarID"), MISSING_LOTID(4,
            "Missing or bad LotID"), MISSING_PLANNEDENDTIME(5,
                "Missing or bad Planned End Time"), MISSING_PASSWORD(6, "Missing password"),;

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

  // TODO Title maybe unnecessary in 'fxml' files
  // serviceActionsMenuScene/serviceLoginScene/serviceMainMenuScene

}
