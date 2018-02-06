package cps.server.testing.utilities;

import cps.server.ServerException;

public interface Actor {
  public void act(World world) throws ServerException;
}
