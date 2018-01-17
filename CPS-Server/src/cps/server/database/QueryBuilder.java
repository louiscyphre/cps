package cps.server.database;

import java.sql.PreparedStatement;

public class QueryBuilder<T> {
  String command;
  PreparedStatement statement = null;
  
  public QueryBuilder(String command) {
    this.command = command; 
  }
}
