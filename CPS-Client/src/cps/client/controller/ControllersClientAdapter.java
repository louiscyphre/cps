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

/**
 * Singleton Class connecting controllers to the ClientApplication, Scenes and
 * Context to each other. Scene switching mechanisms are implemented here, as
 * well as most of the common data of the controllers.
 */
public class ControllersClientAdapter {

  /**
   * ClientApplication field.
   */
  private ClientApplication cpsClient;

  /**
   * Context of the customer.
   */
  private CustomerContext customerContext = new CustomerContextImpl();
  /**
   * Context of the employee.
   */
  private EmployeeContext employeeContext = new EmployeeContextImpl();

  /**
   * Singleton instance.
   */
  private static ControllersClientAdapter instance;

  /**
   * Current scene of the application.
   */
  private SceneCode currentScene;

  /**
   * Mapping of the Controllers to their SceneCodes names
   */
  private HashMap<String, ViewController> ctrlMapping;
  /**
   * Mapping of the Scenes to their SceneCodes names
   */
  private HashMap<String, Scene>          sceneMapping;

  /**
   * The lotId of the application
   */
  private int lotID = -1; // required : -1 if web-client

  /**
   * The default constructor, made private to implement Singleton.
   */
  private ControllersClientAdapter() {
    this.ctrlMapping = new HashMap<String, ViewController>();
    this.sceneMapping = new HashMap<String, Scene>();
  }

  /**
   * Singleton instance retrieval function. Creates new instance if there is no existing one.
   * @return
   */
  private static ControllersClientAdapter getInstance() {
    instance = (instance == null) ? (new ControllersClientAdapter()) : (instance);
    return instance;
  }

  /**
   * Registers the given ctrl to the SceneCode.
   * @param ctrl contoller instance
   * @param code the SceneCode
   * @see ControllerConstants.SceneCode
   */
  public static ViewController registerCtrl(ViewController ctrl, ControllerConstants.SceneCode code) {
    return getInstance().ctrlMapping.put(code.getCode(), ctrl);
  }

  /**
   * Retrieves the ctrl from the given SceneCode.
   * @param code
   * @return
   */
  public static ViewController fetchCtrl(ControllerConstants.SceneCode code) {
    return getInstance().ctrlMapping.get(code.getCode());
  }

  /**
   * Registers the given scene to the SceneCode, and returns newly loaded from FXML Scene.
   * @param code
   * @return loaded scene
   * @throws IOException
   */
  public static Scene registerScene(ControllerConstants.SceneCode code) throws IOException {
    URL url = getClient().getClass().getResource(code.myRelativePath);
    Pane pane;
    pane = FXMLLoader.load(url);
    Scene scene = new Scene(pane);
    getInstance().sceneMapping.put(code.getCode(), scene);
    return scene;
  }

  /**
   * Retrieves the scene from the given SceneCode.
   * @param code
   * @return priorly registered scene with given SceneCode 
   */
  public static Scene fetchScene(ControllerConstants.SceneCode code) {
    return getInstance().sceneMapping.get(code.getCode());
  }

  /**
   * Registers the client.
   * @param cpsClient
   * @return 
   */
  public static ClientApplication registerClient(ClientApplication cpsClient) {
    getInstance().cpsClient = cpsClient;
    return getInstance().cpsClient;
  }

  /**
   * Retrieves the prioly registered client.
   * @return
   */
  public static ClientApplication getClient() {
    return getInstance().cpsClient;
  }

  /**
   * Scene switching function, with custom transition duration. 
   * @param code
   * @param duration
   */
  public static void setStage(ControllerConstants.SceneCode code, Duration duration) {
    getInstance().currentScene = code; // indicate that this scene is current
                                       // for the future
    
    // pause transition instantiation 
    PauseTransition pauseTransition = new PauseTransition();
    pauseTransition.setDuration(duration);
    pauseTransition.play();
    pauseTransition.setOnFinished((ActionEvent e) -> {
      // scene switching mechanism using scene mapping
      Scene scene = ControllersClientAdapter.fetchScene(code);
      ClientApplication clientApp = ControllersClientAdapter.getClient();
      Stage stage = clientApp.getPrimaryStage();

      ViewController ctrl = fetchCtrl(code);
      stage.setScene(scene);
      // enforcing the full screen
      Screen screen = Screen.getPrimary();
      Rectangle2D bounds = screen.getVisualBounds();
      stage.setX(bounds.getMinX());
      stage.setY(bounds.getMinY());
      stage.setWidth(bounds.getWidth());
      stage.setHeight(bounds.getHeight());
      // controller clean, prior to the scene switch
      if (ctrl != null) {
        ctrl.cleanCtrl();
      }
    });
  }

  /**
   * Aux set stage
   * @param code
   * @param millis milliseconds for the scene transition
   */
  public static void setStage(ControllerConstants.SceneCode code, int millis) {
    setStage(code, Duration.millis(millis));
  }

  /**
   * Retrieves the customer context.
   * @return the context
   */
  public static CustomerContext getCustomerContext() {
    return getInstance().customerContext;
  }

  /**
   * Retrieves the employee context.
   * @return the context
   */
  public static EmployeeContext getEmployeeContext() {
    return getInstance().employeeContext;
  }

  /**
   * Retrieves Current scene.
   * @return current scene
   */
  public static SceneCode getCurrentScene() {
    return getInstance().currentScene;
  }

  /**
   * Retrieves Controller for the Current scene.
   * @return current controller
   */
  public static ViewController getCurrentCtrl() {
    return fetchCtrl(getCurrentScene());
  }

  /**
   * Transfers all the registered controllers to <tt>LoggedIn<tt> state.
   */
  public static void turnLoggedInStateOn() {
    getCustomerContext().setLoggedIn(true);
    getInstance().ctrlMapping.forEach((k, v) -> v.turnLoggedInStateOn());
  }

  /**
   * Transfers all the registered controllers to <tt>LoggedOut<tt> state.
   * Logs the context outs.
   */
  public static void turnLoggedInStateOff() {
    getCustomerContext().logContextOut();
    getEmployeeContext().logContextOut();
    getInstance().ctrlMapping.forEach((k, v) -> v.turnLoggedInStateOff());
  }

  /**
   * @return lotID
   */
  public static int getLotID() {
    return getInstance().lotID;
  }

  /**
   * @param lotID
   */
  public static void setLotID(int lotID) {
    getInstance().lotID = lotID;
  }

}
