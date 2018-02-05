package cps.server.testing.utilities;

import java.time.Duration;
import java.time.LocalDateTime;

import cps.api.request.Request;
import cps.api.response.ServerResponse;
import cps.server.ServerController;
import cps.server.session.SessionHolder;

public interface World {
  LocalDateTime getTime();
  public Duration getTimeslice();
  public int getNumberOfParkingLots();
  public ServerController getServerController();
  public <T extends ServerResponse> T sendRequest(Request request, SessionHolder context, Class<T> type);
  public String formatObject(Object object);
}
