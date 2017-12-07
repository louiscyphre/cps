package cps.server;

public class DatabaseController {
	String host;
	String dbName;
	String username;
	String password;

	DatabaseController(String host, String dbName, String username, String password) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		this.host = host;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}
}
