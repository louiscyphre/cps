package cps.client.controller;

import java.util.List;

import cps.api.response.ResponseHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * Public interface implemented by all the application's Controllers. Extends
 * the Response Handler - idea is let specific controller to handle specific
 * request.
 * 
 * @see ResponseHandler
 */
public interface ViewController extends ResponseHandler {
  /**
   * Displays the formatted list of JavaFX text.
   * 
   * @param formattedText
   *          - List consisted of Text element
   * @see Text
   */
  public void displayInfo(List<Text> formattedText);

  /**
   * Displays the message given as a parameter.
   * 
   * @param simpleInfoMsg
   *          - string representing the message to be displayed
   */
  public void displayInfo(String simpleInfoMsg);

  /**
   * Displays the formatted list of JavaFX text, as an error.
   * 
   * @param formettedErrorMsg
   *          - List consisted of Text element
   */
  public void displayError(List<Text> formettedErrorMsg);

  /**
   * Displays the message given as a parameter, as an error.
   * 
   * @param simpleErrorMsg
   *          - string representing the message to be displayed
   */
  public void displayError(String simpleErrorMsg);

  /**
   * Calling this function transfers ViewController to <tt>Processing<tt> state,
   * allowing controller send 1 request at a time, and handle the response.
   * 
   * @see ViewController#turnProcessingStateOff()
   */
  public void turnProcessingStateOn();

  /**
   * Calling this method transfers ViewController tp <tt>Not Processing<tt>
   * state, meaning that there are not request that Controller is waiting to
   * receive response from.
   * 
   * @see ViewController#turnProcessingStateOn()
   */
  public void turnProcessingStateOff();

  /**
   * Calling this method transfers ViewController to <tt>LoggedIn<tt> state,
   * indicating that View may need to change, accordingly.
   */
  public void turnLoggedInStateOn();

  /**
   * Calling this function transfers ViewController to <tt>LoggedOut<tt> state,
   * indicating that View may need to change, accordingly.
   */
  public void turnLoggedInStateOff();

  /**
   * Calling this method tells the controller to clear its input and output
   * fields / structures, cleaning the input fields and output made by the
   * controller.
   */
  public void cleanCtrl();

  /**
   * Returns the root element of the scene bound to the controller.
   * @return root element of the scene
   */
  public BorderPane getRoot();

}
