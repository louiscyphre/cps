package cps.client.controller;

import java.util.List;

import cps.api.response.ResponseHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public interface ViewController extends ResponseHandler {
  /**
   * @param formattedText
   */
  public void displayInfo(List<Text> formattedText); // finds the infoLabel
                                                     // element and writes the
                                                     // info text in info

  /**
   * @param simpleInfoMsg
   */
  public void displayInfo(String simpleInfoMsg); // finds the infoLabel element
                                                 // and writes the info text in
                                                 // info

  /**
   * @param formettedErrorMsg
   */
  public void displayError(List<Text> formettedErrorMsg); // finds the infoLabel
                                                          // element and writes
                                                          // the error message
                                                          // in info

  /**
   * @param simpleErrorMsg
   */
  public void displayError(String simpleErrorMsg);

  /**
   * 
   */
  public void turnProcessingStateOn(); // Makes spinner visible, display info
                                       // processing

  /**
   * 
   */
  public void turnProcessingStateOff(); // Makes spinner invisible, display
                                        // something or nothing

  /**
   * 
   */
  public void turnLoggedInStateOn();

  /**
   * 
   */
  public void turnLoggedInStateOff();

  /**
   * 
   */
  public void cleanCtrl();

  /**
   * @return
   */
  public BorderPane getRoot();

}
