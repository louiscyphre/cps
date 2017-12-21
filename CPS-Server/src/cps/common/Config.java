package cps.common;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Config extends HashMap<String, String> {	
	private static final Config local = new Config(new InitLocal());
	private static final Config remote = new Config(new InitRemote());
	
	interface Init {
		void visit(Config config);
	}
	
	static class InitLocal implements Init {
		public void visit(Config config) {
			config.put("db.host", "localhost:3306");
			config.put("db.name", "cps");
			config.put("db.username", "cps");
			config.put("db.password", "project");			
		}
	}
	
	static class InitRemote implements Init {
		public void visit(Config config) {
			config.put("db.host", "softengproject.cspvcqknb3vj.eu-central-1.rds.amazonaws.com:3306");
			config.put("db.name", "kiwi_schema");
			config.put("db.username", "kiwi_admin");
			config.put("db.password", "*:_Tuu44\\3SJpH)f");			
		}		
	}
	
	public static Config getLocal() {
		return local;
	}
	
	public static Config getRemote() {
		return remote;
	}

	private Config(Init init) {
		init.visit(this);
	}
}
