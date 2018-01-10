package cps.client.controller;

public interface ViewController {
  public void displayInfo(String infoMsg); // finds the infoLabel element and writes the info text in info
  public void displayError(String errorMsg); // finds the infoLabel element and writes the error message in info
  public void turnProcessingStateOn(); // Makes spinner visible, display info processing
  public void turnProcessingStateOff(); // Makes spinner invisible, display TODO something or nothing
}
