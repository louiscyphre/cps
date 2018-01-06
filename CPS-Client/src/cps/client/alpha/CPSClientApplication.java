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

/**
 * @author student
 *
 */
public class CPSClientApplication extends Application implements ClientUIAlpha {

  private ClientControllerAlpha client;

  private Stage primaryStage;

  /**
   *
   */
  public CPSClientApplication() {
    try {
      this.client = new ClientControllerAlpha(cps.common.Constants.DEFAULT_HOST, cps.common.Constants.DEFAULT_PORT,
          this);
      ControllersClientAdapter.registerClient(this);
    } catch (IOException e) {
      System.out.println("Error: Can't setup connection! Terminating client.");
      System.exit(1);
    }
  }

  @Override
  public void start(Stage primaryStage) {
    try {
      Scene scene = ControllersClientAdapter.registerScene(SceneCode.MAIN_MENU, "AlphaGUI_mainMenu.fxml");

      this.primaryStage = primaryStage;

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
