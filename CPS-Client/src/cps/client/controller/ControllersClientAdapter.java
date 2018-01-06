package cps.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import cps.client.main.AlphaCPSClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllersClientAdapter {

  AlphaCPSClientApplication cpsClient;

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

  public static Scene registerScene(SceneCode code) throws IOException {

    URL url = getClient().getClass().getResource(code.myRelativePath);
    Pane pane;
    pane = FXMLLoader.load(url);
    Scene scene = new Scene(pane);
    getInstance().sceneMapping.put(code.getCode(), scene);
    return scene;
  }

  static Scene fetchScene(SceneCode code) {
    return getInstance().sceneMapping.get(code.getCode());
  }

  public static AlphaCPSClientApplication registerClient(AlphaCPSClientApplication cpsClient) {
    getInstance().cpsClient = cpsClient;
    return getInstance().cpsClient = cpsClient;
  }

  static AlphaCPSClientApplication getClient() {
    return getInstance().cpsClient;
  }

  static void setStage(SceneCode code) {
    Scene scene = ControllersClientAdapter.fetchScene(code);
    AlphaCPSClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }

  public enum SceneCode {

    MAIN_MENU("../view/AlphaGUI_mainMenu.fxml"),
    INCIDENTAL_PARKING("../view/AlphaGUI_2.fxml"),
    VIEW_MY_REQUESTS("../view/AlphaGUI_3.fxml"),
    REQUEST_PARKING_ENTRY("../view/AlphaGUI_4.fxml"),
    INIT_PARKING_LOT("../view/AlphaGUI_5.fxml");

    String myRelativePath;
    
    SceneCode(String relativePath){
      this.myRelativePath = relativePath;
    }
    
    String getCode() {
      return this.name();
    }

  }
}
