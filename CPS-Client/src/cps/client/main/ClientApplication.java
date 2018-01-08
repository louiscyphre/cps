/**
 *
 */
package cps.client.main;

import java.io.IOException;

import org.apache.commons.cli.ParseException;

import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.network.CPSNetworkClient;
import cps.client.network.INetworkClient;
import cps.client.utils.CmdParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ClientApplication extends Application implements INetworkClient {

  private CPSNetworkClient client;

  private Stage primaryStage;

  private int lotID; // required : -1 if web-client
  
  public int getLotID() {
    return lotID;
  }

  private void setLotID(int lotID) {
    this.lotID = lotID;
  }

  /**
   *
   */
  public ClientApplication() {
  }

  public void loadKiosk() throws IOException {
    try {
      Scene scene = ControllersClientAdapter.registerScene(ControllerConstants.SceneCode.MAIN_MENU);
      primaryStage.setScene(scene);
      primaryStage.setTitle("CPS Alpha Client");
      primaryStage.show();
      primaryStage.setOnCloseRequest(e -> {
        Platform.exit();
        System.exit(0);
      });
      ControllersClientAdapter.registerScene(ControllerConstants.SceneCode.INCIDENTAL_PARKING);
      ControllersClientAdapter.registerScene(ControllerConstants.SceneCode.VIEW_MY_REQUESTS);
      ControllersClientAdapter.registerScene(ControllerConstants.SceneCode.REQUEST_PARKING_ENTRY);
      ControllersClientAdapter.registerScene(ControllerConstants.SceneCode.INIT_PARKING_LOT);

    } catch (IOException e) {
      e.printStackTrace();
    }
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

      this.client = new CPSNetworkClient(parser.getHost(),
          parser.getPort(), this);

      ControllersClientAdapter.registerClient(this);

      switch (parser.getMode()) {
        case "webclient":
          // loadWebclient();
          break;
        case "service":
          loadService();
          break;
        default:
          loadKiosk();
      }
      
      setLotID(parser.getLotId());

    } catch (IOException e) {
      System.out.println("Error: Can't setup connection! Terminating client.");
      System.exit(1);
    } catch (NumberFormatException e) {
      System.out.println("Error: Wrong port or parking lot id");
      System.exit(1);
    }
  }

  private void loadService() {

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
    if (resp instanceof ServerResponse) {
      ServerResponse srvrResp = (ServerResponse) resp;
      if (srvrResp.getStatus() == ServerResponse.STATUS_OK) {
        Platform.runLater(() -> {
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Success");
          alert.setHeaderText("The operation was successful");
          alert.setContentText(srvrResp.getDescription());
          alert.showAndWait();
        });
 }
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }
}
