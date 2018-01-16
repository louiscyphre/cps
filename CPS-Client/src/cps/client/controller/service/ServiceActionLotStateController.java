package cps.client.controller.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import cps.api.action.RequestLotStateAction;
import cps.api.request.ListParkingLotsRequest;
import cps.api.response.RequestLotStateResponse;
import cps.api.response.ServerResponse;
import cps.client.controller.ControllerConstants.SceneCode;
import cps.client.controller.ControllersClientAdapter;
import cps.client.controller.ParkingLotsController;
import cps.common.Constants;
import cps.entities.models.ParkingCell;
import cps.entities.models.ParkingLot;
import cps.entities.people.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ServiceActionLotStateController extends ServiceActionControllerBase implements ParkingLotsController {

  @FXML // ResourceBundle that was given to the FXMLLoader
  private ResourceBundle resources;

  @FXML // URL location of the FXML file that was given to the FXMLLoader
  private URL location;

  @FXML // fx:id="gridAnchor"
  private AnchorPane gridAnchor; // Value injected by FXMLLoader

  @FXML // fx:id="parkingLotsList"
  private ComboBox<String> parkingLotsList; // Value injected by FXMLLoader

  @FXML // fx:id="levelIndicator"
  private Label levelIndicator; // Value injected by FXMLLoader

  private ArrayList<GridPane> carsGrids;

  HashMap<String, ParkingLot> parkingLotsMap = new HashMap<String, ParkingLot>();

  // matrix representing 3D array
  ArrayList<ArrayList<ArrayList<ParkingCell>>> parkingCell;

  @FXML
  void addDummyData(ActionEvent event) {

  }

  @FXML
  void upperPressed(ActionEvent event) {
    if (!processing) {
      int levelInt = Integer.parseInt(levelIndicator.getText());
      if (levelInt < 3) {
        levelIndicator.setText(Integer.toString(levelInt + 1));
        updateView();
      }
    }
  }

  @FXML
  void lowerPressed(ActionEvent event) {
    if (!processing) {
      int levelInt = Integer.parseInt(levelIndicator.getText());
      if (levelInt > 1) {
        levelIndicator.setText(Integer.toString(levelInt - 1));
        updateView();
      }
    }
  }

  private void updateView() {
    gridAnchor.getChildren().setAll(carsGrids.get(getCurrentLevelIndex()));
    carsGrids.get(getCurrentLevelIndex()).requestFocus();
  }

  @FXML
  void handleRefreshButton(ActionEvent event) {
    if (!processing) {
      validateAndSend();
    }
  }

  @FXML // This method is called by the FXMLLoader when initialization is
        // complete
  void initialize() {
    super.baseInitialize();
    assert parkingLotsList != null : "fx:id=\"parkingLotsList\" was not injected: check your FXML file 'ServiceActionLotState.fxml'.";
    assert levelIndicator != null : "fx:id=\"levelIndicator\" was not injected: check your FXML file 'ServiceActionLotState.fxml'.";
    initParkingCells();
    initCarsGrids();
    ControllersClientAdapter.registerCtrl(this, SceneCode.SERVICE_ACTION_LOT_STATE);
  }

  @Override
  public void cleanCtrl() {
    super.cleanCtrl();
//    gridAnchor.getChildren().setAll(new Rectangle(gridAnchor.getWidth(),gridAnchor.getHeight(), Paint.valueOf("WHITE")));
    clearCarsGrids();
    parkingLotsList.getItems().clear();
    parkingLotsList.setDisable(false);
    parkingLotsMap.clear();
    // Get the list of parking lots
    turnProcessingStateOn();
    sendRequest(new ListParkingLotsRequest());
  }

  private void clearParkingCells() {
    parkingCell.forEach(inner -> {
      inner.forEach(inner2 -> {
        inner2.clear();
      });
      inner.clear();
    });
    parkingCell.clear();
  }

  private void initParkingCells() {
    parkingCell = new ArrayList<ArrayList<ArrayList<ParkingCell>>>(Constants.LOT_HEIGHT);
    for (int i = 0; i < Constants.LOT_DEPTH; i++) {
      parkingCell.add(i, new ArrayList<ArrayList<ParkingCell>>(0));
      for(int j = 0 ; j < Constants.LOT_DEPTH; j++) {
        parkingCell.get(i).add(new ArrayList<ParkingCell>());
      }
    }
  }

  private void clearCarsGrids() {
    carsGrids.forEach(carsGrid -> {
      carsGrid.getChildren().clear();
    });
    
    gridAnchor.getChildren().clear();
    
  }

  private void initCarsGrids() {
    carsGrids = new ArrayList<GridPane>(Constants.LOT_HEIGHT);
    for (int i = 0; i < Constants.LOT_DEPTH; i++) {
      GridPane carsGrid = new GridPane();
      carsGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
      carsGrid.setPrefWidth(Double.MAX_VALUE);
      carsGrid.setPrefHeight(Double.MAX_VALUE);
      carsGrid.setVgap(10);
      carsGrid.setHgap(10);
      carsGrid.setPadding(new Insets(10));
      carsGrids.add(i, carsGrid);
    }
  }

  private int getCurrentLevelIndex() {
    return Integer.parseInt(levelIndicator.getText()) - 1;
  }

  @Override
  void validateAndSend() {
    try {
      User user = requireLoggedInUser();
      ParkingLot lot = parkingLotsMap.get(parkingLotsList.getValue());
      errorIfNull(lot, "Please choose a parking lot");
      turnProcessingStateOn();
      sendRequest(new RequestLotStateAction(user.getId(), lot.getId()));
    } catch (Exception e) {
      displayError(e.getMessage());
    }
  }

  @Override
  public void setParkingLots(Collection<ParkingLot> list) {
    List<String> tmp = new ArrayList<String>();

    parkingLotsMap.clear();

    for (ParkingLot i : list) {
      String address = new String(i.getStreetAddress());
      tmp.add(address);
      parkingLotsMap.put(address, i);
    }

    ObservableList<String> addresses = FXCollections.observableList(tmp);
    fillComboBoxItems(addresses);
  }

  private void fillComboBoxItems(ObservableList<String> addresses) {
    parkingLotsList.getItems().addAll(addresses);
    parkingLotsList.setDisable(false);
  }

  @Override
  public ServerResponse handle(RequestLotStateResponse response) {

    if (response.success()) {

      ParkingCell[][][] content = response.getContent();

      clearParkingCells();
      initParkingCells();
      
      double vgap = carsGrids.get(0).getVgap();
      double hgap = carsGrids.get(0).getHgap();

      Rectangle[][][] rects = new Rectangle[content[0].length][content[0][0].length][content.length];

      double rectWidth = (gridAnchor.getWidth() - vgap) / content[0][0].length - vgap;
      double rectHeight = (gridAnchor.getHeight() - hgap) / content.length - hgap;
      Paint rectPaint;

      for (int level = 0; level < content[0].length; level++) {
        for (int depth = 0; depth < content[0][0].length; depth++) {
          for (int width = 0; width < content.length; width++) {
            ParkingCell currentParkingCell = content[width][level][depth];
            parkingCell.get(level).get(depth).add(width, currentParkingCell);
            if (currentParkingCell.isFree() || currentParkingCell == null) {
              rectPaint = (Paint.valueOf("WHITE"));
            } else if (currentParkingCell.isDisabled()) {
              rectPaint = (Paint.valueOf("RED"));
            } else if (currentParkingCell.isReserved()) {
              rectPaint = (Paint.valueOf("GREEN"));
            } else {
              rectPaint = (Paint.valueOf("BLUE"));
            }
            Rectangle currentRectangle = new Rectangle(rectWidth, rectHeight, rectPaint);
            currentRectangle.setArcHeight(rectWidth*0.2);
            currentRectangle.setArcWidth(rectHeight*0.2);
            currentRectangle.setOnMouseEntered(evt -> {
              int rowInd = GridPane.getRowIndex(currentRectangle);
              int colInd = GridPane.getColumnIndex(currentRectangle);
              int levelInd = getCurrentLevelIndex();
              ParkingCell correspondingCell = parkingCell.get(levelInd).get(colInd).get(rowInd);
              displayInfo(correspondingCell.getCarID() != null ? correspondingCell.getCarID() : "");
            });
            currentRectangle.setOnMouseExited(evt -> {
              displayInfo("");
            });
            rects[level][depth][width] = currentRectangle;
          }
        }
      }
      for (int level = 0; level < rects.length; level++) {
        GridPane carsGrid = carsGrids.get(level);
        for (int depth = 0; depth < rects[0].length; depth++) {
          for (int width = 0; width < rects[0][0].length; width++) {
            if (rects[level][depth][width] != null) {
              carsGrid.add(rects[level][depth][width], depth, width);
            }
          }
        }
      }
      updateView();
      parkingLotsList.setDisable(true);
      turnProcessingStateOff();
      displayInfo(response.getDescription());
    } else {
      turnProcessingStateOff();
      displayError(response.getDescription());
    }
    return null;
  }
}
