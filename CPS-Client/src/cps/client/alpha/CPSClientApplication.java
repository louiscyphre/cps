/**
 *
 */
package cps.client.alpha;

import java.io.IOException;
import java.net.URL;

import cps.api.response.ServerResponse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CPSClientApplication extends Application implements ClientUIAlpha {

  private ClientControllerAlpha client;

  private Stage primaryStage;

  /**
   *
   */
  public CPSClientApplication() {
  }

  public void loadKiosk() throws IOException {
    try {
      URL url = getClass().getResource("AlphaGUI_mainMenu.fxml");
      Pane pane;
      pane = FXMLLoader.load(url);
      Scene scene = new Scene(pane);
      ControllersClientAdapter.registerScene("alphaMain", scene);

      primaryStage.setScene(scene);
      primaryStage.setTitle("CPS Kiosk Client");
      primaryStage.show();
      primaryStage.setOnCloseRequest(e -> {
        Platform.exit();
        System.exit(0);
      });

      url = getClass().getResource("AlphaGUI_2.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene("alpha2", scene);

      url = getClass().getResource("AlphaGUI_3.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene("alpha3", scene);

      url = getClass().getResource("AlphaGUI_4.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene("alpha4", scene);

      url = getClass().getResource("AlphaGUI_5.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene("alpha5", scene);

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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Operation was successful");
        alert.showAndWait();
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
