package cps.client.alpha;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    // constructing our scene
    try {
      URL url = getClass().getResource("AlphaGUI_mainMenu.fxml");
      Pane pane;
      pane = FXMLLoader.load(url);
      Scene scene = new Scene(pane);
      // setting the stage
      primaryStage.setScene(scene);
      primaryStage.setTitle("CPS Kiosk Client");
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
