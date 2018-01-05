/**
 *
 */
package cps.client.alpha;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.apache.commons.cli.*;

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
  }

  public void loadKiosk() throws IOException {
    try {
      URL url = getClass().getResource("AlphaGUI_mainMenu.fxml");
      Pane pane;
      pane = FXMLLoader.load(url);
      Scene scene = new Scene(pane);
      ControllersClientAdapter.registerScene(scene);


      primaryStage.setScene(scene);
      primaryStage.setTitle("CPS Kiosk Client");
      primaryStage.show();

      url = getClass().getResource("AlphaGUI_2.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene(scene);

      url = getClass().getResource("AlphaGUI_3.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene(scene);

      url = getClass().getResource("AlphaGUI_4.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene(scene);

      url = getClass().getResource("AlphaGUI_5.fxml");
      pane = FXMLLoader.load(url);
      scene = new Scene(pane);
      ControllersClientAdapter.registerScene(scene);

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
  public void handleMessage(Object msg) {

  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }
}
