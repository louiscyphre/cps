package cps.client.alpha;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllersClientAdapter {

  CPSClientApplication cpsClient;

  private static ControllersClientAdapter instance;

  private HashMap<String, CPSViewController> ctrlMapping;
  private HashMap<String, Scene>             sceneMapping;

  private ControllersClientAdapter() {
    this.ctrlMapping = new HashMap<String, CPSViewController>();
    this.sceneMapping = new HashMap<String, Scene>();
  }

  public static ControllersClientAdapter getInstance() {
    instance = instance == null ? new ControllersClientAdapter() : instance;
    return instance;
  }

  static CPSViewController registerCtrl(CPSViewController ctrl, SceneCode code) {
    return getInstance().ctrlMapping.put(code.getCode(), ctrl);
  }

  static CPSViewController fetchCtrl(SceneCode code) {
    return getInstance().ctrlMapping.get(code.getCode());
  }

  static Scene registerScene(SceneCode code, String fxmlName) throws IOException {

    URL url = getClient().getClass().getResource(fxmlName);
    Pane pane;
    pane = FXMLLoader.load(url);
    Scene scene = new Scene(pane);
    getInstance().sceneMapping.put(code.getCode(), scene);
    return scene;
  }

  static Scene fetchScene(SceneCode code) {
    return getInstance().sceneMapping.get(code.getCode());
  }

  static CPSClientApplication registerClient(CPSClientApplication cpsClient) {
    getInstance().cpsClient = cpsClient;
    return getInstance().cpsClient = cpsClient;
  }

  static CPSClientApplication getClient() {
    return getInstance().cpsClient;
  }

  static void setStage(SceneCode code) {
    Scene scene = ControllersClientAdapter.fetchScene(code);
    CPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }

  public enum SceneCode {

    MAIN_MENU, INCIDENTAL_PARKING, VIEW_MY_REQUESTS, REQUEST_PARKING_ENTRY, INIT_PARKING_LOT;

    String getCode() {
      return this.name();
    }

  }
}
