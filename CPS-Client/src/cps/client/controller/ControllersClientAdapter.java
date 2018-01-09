package cps.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import cps.client.main.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControllersClientAdapter {

  ClientApplication cpsClient;

  private static ControllersClientAdapter instance;

  private HashMap<String, ViewController> ctrlMapping;
  private HashMap<String, Scene>             sceneMapping;

  private ControllersClientAdapter() {
    this.ctrlMapping = new HashMap<String, ViewController>();
    this.sceneMapping = new HashMap<String, Scene>();
  }

  public static ControllersClientAdapter getInstance() {
    instance = instance == null ? new ControllersClientAdapter() : instance;
    return instance;
  }

  public static ViewController registerCtrl(ViewController ctrl, ControllerConstants.SceneCode code) {
    return getInstance().ctrlMapping.put(code.getCode(), ctrl);
  }

  public static ViewController fetchCtrl(ControllerConstants.SceneCode code) {
    return getInstance().ctrlMapping.get(code.getCode());
  }

  public static Scene registerScene(ControllerConstants.SceneCode code) throws IOException {

    URL url = getClient().getClass().getResource(code.myRelativePath);
    Pane pane;
    pane = FXMLLoader.load(url);
    Scene scene = new Scene(pane);
    getInstance().sceneMapping.put(code.getCode(), scene);
    return scene;
  }

  public static Scene fetchScene(ControllerConstants.SceneCode code) {
    return getInstance().sceneMapping.get(code.getCode());
  }

  public static ClientApplication registerClient(ClientApplication cpsClient) {
    getInstance().cpsClient = cpsClient;
    return getInstance().cpsClient = cpsClient;
  }

  public static ClientApplication getClient() {
    return getInstance().cpsClient;
  }

  public static void setStage(ControllerConstants.SceneCode code) {
    Scene scene = ControllersClientAdapter.fetchScene(code);
    ClientApplication clientApp = ControllersClientAdapter.getClient();
    Stage stage = clientApp.getPrimaryStage();
    stage.setScene(scene);
  }
}
