package cps.client.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import cps.client.context.CustomerContext;
import cps.client.context.CustomerContextImpl;
import cps.client.context.EmployeeContext;
import cps.client.context.EmployeeContextImpl;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.main.ClientApplication;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ControllersClientAdapter {

  private ClientApplication cpsClient;

  private CustomerContext customerContext = new CustomerContextImpl();
  private EmployeeContext employeeContext = new EmployeeContextImpl();

  private static ControllersClientAdapter instance;

  private SceneCode currentScene;

  private HashMap<String, ViewController> ctrlMapping;
  private HashMap<String, Scene>          sceneMapping;

  private int lotID = -1; // required : -1 if web-client

  private ControllersClientAdapter() {
    this.ctrlMapping = new HashMap<String, ViewController>();
    this.sceneMapping = new HashMap<String, Scene>();
  }

  private static ControllersClientAdapter getInstance() {
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

  public static void setStage(ControllerConstants.SceneCode code, Duration duration) {
    getInstance().currentScene = code; // indicate that this scene is current
                                       // for the future

//    FadeTransition fadeOut = new FadeTransition();
//    fadeOut.setDuration(duration);
//    fadeOut.setNode(getCurrentCtrl().getRoot());
//    fadeOut.setFromValue(1.0);
//    fadeOut.setToValue(0.0);
//    fadeOut.play();
    
    PauseTransition pauseTransition = new PauseTransition();
    pauseTransition.setDuration(duration);
    pauseTransition.play();
    
    pauseTransition.setOnFinished((ActionEvent e) -> {
      Scene scene = ControllersClientAdapter.fetchScene(code);
      ClientApplication clientApp = ControllersClientAdapter.getClient();
      Stage stage = clientApp.getPrimaryStage();

      ViewController ctrl = fetchCtrl(code);

      stage.setScene(scene);
      Screen screen = Screen.getPrimary();
      Rectangle2D bounds = screen.getVisualBounds();
      stage.setX(bounds.getMinX());
      stage.setY(bounds.getMinY());
      stage.setWidth(bounds.getWidth());
      stage.setHeight(bounds.getHeight());

      if (ctrl != null) {
        ctrl.cleanCtrl();
      }
    });
    
//    fadeOut.setOnFinished((ActionEvent e) -> {
//      Scene scene = ControllersClientAdapter.fetchScene(code);
//      ClientApplication clientApp = ControllersClientAdapter.getClient();
//      Stage stage = clientApp.getPrimaryStage();
//
//      ViewController ctrl = fetchCtrl(code);
//      ctrl.getRoot().setOpacity(0);
//      
//      FadeTransition fadeIn = new FadeTransition();
//      fadeIn.setDuration(Duration.millis(500));
//      fadeIn.setNode(ctrl.getRoot());
//      fadeIn.setFromValue(0.0);
//      fadeIn.setToValue(1.0);
//      fadeIn.play();
//
//      stage.setScene(scene);
//      Screen screen = Screen.getPrimary();
//      Rectangle2D bounds = screen.getVisualBounds();
//      stage.setX(bounds.getMinX());
//      stage.setY(bounds.getMinY());
//      stage.setWidth(bounds.getWidth());
//      stage.setHeight(bounds.getHeight());
//
//      if (ctrl != null) {
//        ctrl.cleanCtrl();
//      }
//    });
  }

  
  public static void setStage(ControllerConstants.SceneCode code, int millis) {
    setStage(code, Duration.millis(millis));
  }

  public static CustomerContext getCustomerContext() {
    return getInstance().customerContext;
  }

  public static EmployeeContext getEmployeeContext() {
    return getInstance().employeeContext;
  }

  public static SceneCode getCurrentScene() {
    return getInstance().currentScene;
  }

  public static ViewController getCurrentCtrl() {
    return fetchCtrl(getCurrentScene());
  }

  public static void turnLoggedInStateOn() {
    getCustomerContext().setLoggedIn(true);
    getInstance().ctrlMapping.forEach((k, v) -> v.turnLoggedInStateOn());
  }

  public static void turnLoggedInStateOff() {
    getCustomerContext().logContextOut();
    getEmployeeContext().logContextOut();
    getInstance().ctrlMapping.forEach((k, v) -> v.turnLoggedInStateOff());
  }

  public static int getLotID() {
    return getInstance().lotID;
  }

  public static void setLotID(int lotID) {
    getInstance().lotID = lotID;
  }

}
