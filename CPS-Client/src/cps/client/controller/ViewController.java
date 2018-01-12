package cps.client.controller;

import java.util.List;

import javafx.scene.text.Text;

public interface ViewController {
  public void displayInfo(List<Text> formattedText); // finds the infoLabel
                                                     // element and writes the
                                                     // info text in info

  public void displayInfo(String simpleInfoMsg); // finds the infoLabel element
                                                 // and writes the info text in
                                                 // info

  public void displayError(List<Text> formettedErrorMsg); // finds the infoLabel
                                                          // element and writes
                                                          // the error message
                                                          // in info

  public void displayError(String simpleErrorMsg);

  public void turnProcessingStateOn(); // Makes spinner visible, display info
                                       // processing

  public void turnProcessingStateOff(); // Makes spinner invisible, display TODO
                                        // something or nothing
}
