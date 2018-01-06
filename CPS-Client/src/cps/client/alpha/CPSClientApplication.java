/**
 *
 */
package cps.client.alpha;

import java.io.IOException;

import cps.api.response.ServerResponse;
import cps.client.alpha.ControllersClientAdapter.SceneCode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CPSClientApplication extends Application implements ClientUIAlpha {

  private ClientControllerAlpha client;

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
  public CPSClientApplication() {
  }

  public void loadKiosk() throws IOException {
    try {
      Scene scene = ControllersClientAdapter.registerScene(SceneCode.MAIN_MENU, "AlphaGUI_mainMenu.fxml");

      primaryStage.setScene(scene);
      primaryStage.setTitle("CPS Alpha Client");
      primaryStage.show();
      primaryStage.setOnCloseRequest(e -> {
        Platform.exit();
        System.exit(0);
      });

      ControllersClientAdapter.registerScene(SceneCode.INCIDENTAL_PARKING, "AlphaGUI_2.fxml");

      ControllersClientAdapter.registerScene(SceneCode.VIEW_MY_REQUESTS, "AlphaGUI_3.fxml");

      ControllersClientAdapter.registerScene(SceneCode.REQUEST_PARKING_ENTRY, "AlphaGUI_4.fxml");

      ControllersClientAdapter.registerScene(SceneCode.INIT_PARKING_LOT, "AlphaGUI_5.fxml");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage primaryStage) {
    try {

      this.primaryStage = primaryStage;
      CmdParser parser = new CmdParser();
      parser.extract(getParameters().getRaw().toArray(new String[0]));

      this.client = new ClientControllerAlpha(parser.getHost(),
          parser.getPort(), this);

      ControllersClientAdapter.registerClient(this);

      switch (parser.getMode()) {
        case "webclient":
          // loadWebclient();
          break;
        case "service":
          // loadService();
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
