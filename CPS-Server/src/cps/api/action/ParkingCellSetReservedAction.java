package cps.api.action;

import cps.api.request.RequestHandler;
import cps.api.response.ServerResponse;

public class ParkingCellSetReservedAction extends ParkingCellToggleAction {
  private static final long serialVersionUID = 1L;
  
  public ParkingCellSetReservedAction(int userID, int lotID, int locationI, int locationJ, int locationK, boolean value) {
    super(userID, lotID, locationI, locationJ, locationK, value);
  }
  
  public ParkingCellSetReservedAction(int userID, int lotID, int locationI, int locationJ, int locationK) {
    super(userID, lotID, locationI, locationJ, locationK);
  }

  @Override
  public <T> ServerResponse handle(RequestHandler<T> handler, T session) {
    return handler.handle(this, session);
  }
}
