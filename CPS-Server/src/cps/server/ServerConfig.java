package cps.server;

import java.util.HashMap;

/** Encapsulates configuration parameters for the server application. */
@SuppressWarnings("serial")
public class ServerConfig extends HashMap<String, String> {
  interface Init {
    void visit(ServerConfig config);
  }

  static class InitLocal implements Init {

    /*
     * (non-Javadoc)
     * @see cps.server.ServerConfig.Init#visit(cps.server.ServerConfig)
     */
    public void visit(ServerConfig config) {
      config.put("db.host", "localhost:3306");
      config.put("db.name", "cps");
      config.put("db.username", "cps");
      config.put("db.password", "project");
    }
  }

  static class InitRemote implements Init {

    /*
     * (non-Javadoc)
     * @see cps.server.ServerConfig.Init#visit(cps.server.ServerConfig)
     */
    public void visit(ServerConfig config) {
      config.put("db.host", "softengproject.cspvcqknb3vj.eu-central-1.rds.amazonaws.com:3306");
      config.put("db.name", "kiwi_schema");
      config.put("db.username", "kiwi_admin");
      config.put("db.password", "*:_Tuu44\\3SJpH)f");
    }
  }

  static class InitTesting implements Init {
    public void visit(ServerConfig config) {
      config.put("db.host", "localhost:3306");
      config.put("db.name", "cps_test");
      config.put("db.username", "cps");
      config.put("db.password", "project");
    }
  }

  private ServerConfig(Init init) {
    init.visit(this);
  }

  /** Get the configuration settings to run the server with a local database.
   * @return the configuration settings */
  public static ServerConfig local() {
    return new ServerConfig(new InitLocal());
  }

  /** Get the configuration settings to run the server with a remote database.
   * @return the configuration settings */
  public static ServerConfig remote() {
    return new ServerConfig(new InitRemote());
  }


  /** Get the configuration settings to run the server with a testing database.
   * @return the configuration settings */
  public static ServerConfig testing() {
    return new ServerConfig(new InitTesting());
  }
}
