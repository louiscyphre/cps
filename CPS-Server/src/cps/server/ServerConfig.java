package cps.server;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ServerConfig extends HashMap<String, String> {	
	private static final ServerConfig local = new ServerConfig(new InitLocal());
	private static final ServerConfig remote = new ServerConfig(new InitRemote());
	
	interface Init {
		void visit(ServerConfig config);
	}
	
	static class InitLocal implements Init {
		public void visit(ServerConfig config) {
			config.put("db.host", "localhost:3306");
			config.put("db.name", "cps");
			config.put("db.username", "cps");
			config.put("db.password", "project");			
		}
	}
	
	static class InitRemote implements Init {
		public void visit(ServerConfig config) {
			config.put("db.host", "softengproject.cspvcqknb3vj.eu-central-1.rds.amazonaws.com:3306");
			config.put("db.name", "kiwi_schema");
			config.put("db.username", "kiwi_admin");
			config.put("db.password", "*:_Tuu44\\3SJpH)f");			
		}		
	}
	
	public static ServerConfig getLocal() {
		return local;
	}
	
	public static ServerConfig getRemote() {
		return remote;
	}

	private ServerConfig(Init init) {
		init.visit(this);
	}
}
