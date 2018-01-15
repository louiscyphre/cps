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

    // i - width , j - floor , k - depth
    for (int j = 0; j < content[0].length; j++) {
      for (int i = 0; i < content.length; i++) {
        for (int k = 0; k < content[0][0].length; k++) {
          content[i][j][k] = new ParkingCell(1, i, j, k,
              (random.nextInt(2) == 1) ? ("carid" + i + "" + j + "" + k) : null, null, false, false);
          if (content[i][j][k].isFree()) {
             rects[j][i][k] = new Rectangle();
             rects[j][i][k].setFill(Paint.valueOf("BLUE"));
             rects[j][i][k].setWidth(12);
             rects[j][i][k].setHeight(12);
             rects[j][i][k].prefWidth(Double.MAX_VALUE);
             rects[j][i][k].prefHeight(Double.MAX_VALUE);

//            buttons[j][i][k] = new Button("disable");
//            buttons[j][i][k].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            // buttons[j][i][k].getStyleClass().add("infoLabel");
          }

        }
      }
    }

    AnchorPane parent = (AnchorPane) carsGrid.getParent();

    parent.getChildren().clear();

    // int columns = rects.length;
    // int rows = rects[0][0].length;
    carsGrid = new GridPane();

    for (int level = 0; level < rects.length; level++) {
      for (int i = 0; i < rects[0].length; i++) {
        for (int k = 0; k < rects[0][0].length; k++) {
          if (rects[level][i][k] != null) {
             carsGrid.add(rects[level][i][k], i, k);
//            carsGrid.add(buttons[level][i][k], i, k);
          }
        }
      }
    }

    carsGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//    carsGrid.setVgap(10);
//    carsGrid.setHgap(10);
//    carsGrid.setPadding(new Insets(10));
//    carsGrid.requestLayout();
    parent.getChildren().setAll(carsGrid);
  }
}
