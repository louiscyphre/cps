package cps.client.alpha;

import java.util.HashMap;

import javafx.scene.Scene;

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
    return instance == null ? new ControllersClientAdapter() : instance;
  }

  static CPSViewController registerCtrl(CPSViewController ctrl) {
    return getInstance().ctrlMapping.put(ctrl.getCtrlId(), ctrl);
  }

  static CPSViewController fetchCtrl(String name) {
    return getInstance().ctrlMapping.get(name);
  }

  static Scene registerScene(Scene ctrl) {
    return getInstance().sceneMapping.put(Scene.class.getName(), ctrl);
  }

  static Scene fetchScene(String name) {
    return getInstance().sceneMapping.get(name);
  }

  static CPSClientApplication registerClient(CPSClientApplication cpsClient) {
    getInstance().cpsClient = cpsClient;
    return getInstance().cpsClient = cpsClient;
  }

  static CPSClientApplication getClient() {
    return getInstance().cpsClient;
  }

}
