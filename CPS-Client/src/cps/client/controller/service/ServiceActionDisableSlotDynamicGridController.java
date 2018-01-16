package cps.client.controller.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cps.api.response.RequestLotStateResponse;
import cps.client.controller.ControllerConstants;
import cps.client.controller.ControllersClientAdapter;
import cps.entities.models.ParkingCell;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import sun.print.DocumentPropertiesUI;

// TODO stub - need to implement the dynamic grid creation and the view initialization
public class ServiceActionDisableSlotDynamicGridController extends ServiceActionControllerBase {

  @FXML
  private Label levelIndicator;

  @FXML
  private GridPane carsGrid;

  // used to save currently selected slot
  private Integer[] selection;

  @FXML
  void handleBackButton(ActionEvent event) {

  }

  @FXML
  void handleOkButton(ActionEvent event) {

  }

  @FXML
  void upperPressed(ActionEvent event) {

  }

  @FXML
  void lowerPressed(ActionEvent event) {

  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert carsGrid != null : "fx:id=\"carsGrid\" was not injected: check your FXML file 'ServiceActionDisableSlotDynamicGrid.fxml'.";
    ControllersClientAdapter.registerCtrl(this, ControllerConstants.SceneCode.SERVICE_ACTION_DISABLE_SLOT);

  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
  }

  @Override
  void validateAndSend() {
    // TODO Auto-generated method stub

  }

  // TODO testing button
  @FXML
  void addDummyData(ActionEvent event) {
    RequestLotStateResponse response = new RequestLotStateResponse();
    response.getContent();

    ParkingCell[][][] content = new ParkingCell[10][3][10];
    Rectangle[][][] rects = new Rectangle[3][10][10];

    Button[][][] buttons = new Button[3][10][10];

    Random random = new Random();

    AnchorPane parent = (AnchorPane) carsGrid.getParent();

    double vgap = 10;
    double hgap = 10;

    // i - width , j - floor , k - depth
    for (int j = 0; j < content[0].length; j++) {
      for (int i = 0; i < content.length; i++) {
        for (int k = 0; k < content[0][0].length; k++) {
          content[i][j][k] = new ParkingCell(1, i, j, k,
              (random.nextInt(2) == 1) ? ("carid" + i + "" + j + "" + k) : null, null,
              false, false);
          if(content[i][j][k].isFree()) {
            if((random.nextInt(10) == 1)) {
              content[i][j][k].setDisabled(true);
            }
            else if((random.nextInt(4) == 1)) {
              content[i][j][k].setReserved(true);
            }
          }
            
          content[i][j][k].isDisabled();
          double width = (parent.getWidth() - vgap) / content[0][0].length - vgap;
          double height = (parent.getHeight() - hgap) / content.length - hgap;
          if (content[i][j][k].isFree()) {
            rects[j][i][k] = new Rectangle();
            rects[j][i][k].setFill(Paint.valueOf("BLUE"));
            rects[j][i][k].setWidth(width);
            rects[j][i][k].setHeight(height);
            // rects[j][i][k].prefWidth(Double.MAX_VALUE);
            // rects[j][i][k].prefHeight(Double.MAX_VALUE);
            // rects[j][i][k].maxWidth(Double.MAX_VALUE);
            // rects[j][i][k].maxHeight(Double.MAX_VALUE);
            // buttons[j][i][k] = new Button("disable");
            // buttons[j][i][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            // buttons[j][i][k].getStyleClass().add("infoLabel");
          } else if (content[i][j][k].isDisabled()) {
            rects[j][i][k] = new Rectangle();
            rects[j][i][k].setFill(Paint.valueOf("RED"));
            rects[j][i][k].setWidth(width);
            rects[j][i][k].setHeight(height);
          } else if (content[i][j][k].isReserved()) {
            rects[j][i][k] = new Rectangle();
            rects[j][i][k].setFill(Paint.valueOf("GREEN"));
            rects[j][i][k].setWidth(width);
            rects[j][i][k].setHeight(height);
          }

        }
      }
    }

    parent.getChildren().remove(carsGrid);

    // int columns = rects.length;
    // int rows = rects[0][0].length;
    carsGrid = new GridPane();

    for (int level = 0; level < rects.length; level++) {
      for (int i = 0; i < rects[0].length; i++) {
        for (int k = 0; k < rects[0][0].length; k++) {
          if (rects[level][i][k] != null) {
            carsGrid.add(rects[level][i][k], i, k);
            // carsGrid.add(buttons[level][i][k], i, k);
          }
        }
      }
    }

    carsGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    carsGrid.setPrefWidth(Double.MAX_VALUE);
    carsGrid.setPrefHeight(Double.MAX_VALUE);

    carsGrid.setVgap(10);
    carsGrid.setHgap(10);
    carsGrid.setPadding(new Insets(10));
    carsGrid.requestLayout();
    parent.getChildren().setAll(carsGrid);
  }
}
