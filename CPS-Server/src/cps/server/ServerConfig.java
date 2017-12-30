package cps.server;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerConfig.
 */
@SuppressWarnings("serial")
public class ServerConfig extends HashMap<String, String> {	
	
	/** The Constant local. */
	private static final ServerConfig local = new ServerConfig(new InitLocal());
	
	/** The Constant remote. */
	private static final ServerConfig remote = new ServerConfig(new InitRemote());
	
	/**
	 * The Interface Init.
	 */
	interface Init {
		
		/**
		 * Visit.
		 *
		 * @param config the config
		 */
		void visit(ServerConfig config);
	}
	
	/**
	 * The Class InitLocal.
	 */
	static class InitLocal implements Init {
		
		/* (non-Javadoc)
		 * @see cps.server.ServerConfig.Init#visit(cps.server.ServerConfig)
		 */
		public void visit(ServerConfig config) {
			config.put("db.host", "localhost:3306");
			config.put("db.name", "cps");
			config.put("db.username", "cps");
			config.put("db.password", "project");			
		}
	}
	
	/**
	 * The Class InitRemote.
	 */
	static class InitRemote implements Init {
		
		/* (non-Javadoc)
		 * @see cps.server.ServerConfig.Init#visit(cps.server.ServerConfig)
		 */
		public void visit(ServerConfig config) {
			config.put("db.host", "softengproject.cspvcqknb3vj.eu-central-1.rds.amazonaws.com:3306");
			config.put("db.name", "kiwi_schema");
			config.put("db.username", "kiwi_admin");
			config.put("db.password", "*:_Tuu44\\3SJpH)f");			
		}		
	}
	
	/**
	 * Gets the local.
	 *
	 * @return the local
	 */
	public static ServerConfig getLocal() {
		return local;
	}
	
	/**
	 * Gets the remote.
	 *
	 * @return the remote
	 */
	public static ServerConfig getRemote() {
		return remote;
	}

	/**
	 * Instantiates a new server config.
	 *
	 * @param init the init
	 */
	private ServerConfig(Init init) {
		init.visit(this);
	}
}
