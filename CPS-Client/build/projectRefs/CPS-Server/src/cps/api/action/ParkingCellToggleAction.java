package cps.api.action;

public abstract class ParkingCellToggleAction extends LotAction {
  private static final long serialVersionUID = 1L;
  private int               locationI;
  private int               locationJ;
  private int               locationK;
  private boolean           value;

  public ParkingCellToggleAction(int userID, int lotID, int locationI, int locationJ, int locationK, boolean value) {
    super(userID, lotID);
    this.locationI = locationI;
    this.locationJ = locationJ;
    this.locationK = locationK;
    this.value = value;
  }

  public ParkingCellToggleAction(int userID, int lotID, int locationI, int locationJ, int locationK) {
    super(userID, lotID);
    this.locationI = locationI;
    this.locationJ = locationJ;
    this.locationK = locationK;
    this.value = false;
  }

  public int getLocationI() {
    return locationI;
  }

  public void setLocationI(int locationI) {
    this.locationI = locationI;
  }

  public int getLocationJ() {
    return locationJ;
  }

  public void setLocationJ(int locationJ) {
    this.locationJ = locationJ;
  }

  public int getLocationK() {
    return locationK;
  }

  public void setLocationK(int locationK) {
    this.locationK = locationK;
  }

  public boolean getValue() {
    return value;
  }

  public void setValue(boolean enable) {
    this.value = enable;
  }
}
