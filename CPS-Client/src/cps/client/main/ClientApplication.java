/**
 *
 */
package cps.client.main;

import java.io.IOException;
import java.util.Locale;

import org.apache.commons.cli.ParseException;

import cps.api.response.Response;
import cps.api.response.ResponseHandler;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.responsehandler.ResponseHandlerImpl;
import cps.client.network.CPSNetworkClient;
import cps.client.network.INetworkClient;
import cps.client.utils.CmdParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ClientApplication extends Application implements INetworkClient {

  private CPSNetworkClient client;

  private Stage primaryStage;

  private ResponseHandler responseHandler = new ResponseHandlerImpl();

  /**
   *
   */
  public ClientApplication() {
  }

  private void loadKiosk() throws IOException {
    try {
      ControllersClientAdapter.registerScene(SceneCode.CUSTOMER_INITIAL_MENU);
      ControllersClientAdapter.registerScene(SceneCode.LOGIN);
      ControllersClientAdapter.registerScene(SceneCode.ENTER_PARKING);
      ControllersClientAdapter.registerScene(SceneCode.EXIT_PARKING);
      ControllersClientAdapter.registerScene(SceneCode.CUSTOMER_LIST_SUBSCRIPTIONS);
      ControllersClientAdapter.registerScene(SceneCode.FULL_SUBSCRIPTION);
      ControllersClientAdapter.registerScene(SceneCode.REGULAR_SUBSCRIPTION);
      ControllersClientAdapter.registerScene(SceneCode.RESERVE_PARKING);
      ControllersClientAdapter.registerScene(SceneCode.INCIDENTAL_PARKING);
      ControllersClientAdapter.registerScene(SceneCode.VIEW_MY_RESERVATION);
      ControllersClientAdapter.registerScene(SceneCode.FILE_COMPLAINT);
      ControllersClientAdapter.turnLoggedInStateOff();
      initializeStage(SceneCode.CUSTOMER_INITIAL_MENU, "CPS Kiosk Client");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadService() {
    try {
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_LOGIN);
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_INIT_LOT);
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_MENU);
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_LOT_IS_FULL);
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_MANAGE_LOT); 
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_REFUND);
      ControllersClientAdapter.registerScene(SceneCode.SERVICE_ACTION_UPDATE_PRICES);
      ControllersClientAdapter.turnLoggedInStateOff();
      initializeStage(SceneCode.SERVICE_ACTION_LOGIN, "CPS Service Client");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadTest() throws IOException {
    ControllersClientAdapter.registerScene(SceneCode.TEST_SCENE);
    initializeStage(SceneCode.TEST_SCENE, "CPS Tests");
  }

  @Override
  public void start(Stage primaryStage) {
    try {

      this.primaryStage = primaryStage;
      CmdParser parser = new CmdParser();
      try {
        parser.extract(getParameters().getRaw().toArray(new String[0]));
      } catch (ParseException e) {
        System.exit(1);
      }

      Locale.setDefault(Locale.Category.FORMAT, Locale.ENGLISH);
      
      this.client = new CPSNetworkClient(parser.getHost(), parser.getPort(), this);

      ControllersClientAdapter.registerClient(this);

      switch (parser.getMode()) {
        case "webclient":
          // loadWebclient();
          break;
        case "service":
          loadService();
          break;
        case "test":
          loadTest(); // TODO kill it with fire before release (useful now)
          break;
        case "kiosk":
          loadKiosk();
          break;
        default:
          loadKiosk();
      }

      ControllersClientAdapter.setLotID(parser.getLotId());

    } catch (IOException e) {
      System.out.println("Error: Can't setup connection! Terminating client.");
      System.exit(1);
    } catch (NumberFormatException e) {
      System.out.println("Error: Wrong port or parking lot id");
      System.exit(1);
    }
  }

  private void initializeStage(SceneCode code, String title) {
    ControllersClientAdapter.getClient().getPrimaryStage().setTitle(title);
    ControllersClientAdapter.setStage(code, 10);
    primaryStage.show();
    primaryStage.setOnCloseRequest(e -> {
      Platform.exit();
      System.exit(0);
    });

  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void sendRequest(Object rqst) {
    client.handleMessageFromClientUI(rqst);
  }

  @Override
  public void receiveResponse(Object resp) {
    responseHandler.dispatch((Response) resp);
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }
}
