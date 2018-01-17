package cps.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cps.server.database.DatabaseController.StatementVisitor;
import cps.server.database.DatabaseController.ResultVisitor;

public class QueryBuilder<T> {
  String command;
  StatementVisitor filler = null;
  
  public QueryBuilder(String command) {
    this.command = command;
  }
  
  public QueryBuilder<T> withFields(StatementVisitor fillFields) {
    this.filler = fillFields;
    return this;
  }
  
  public T fetchResult(Connection conn, ResultVisitor<T> makeObject) throws SQLException {
    T object = null;
    PreparedStatement statement = conn.prepareStatement(command);
    filler.apply(statement);
    
    ResultSet result = statement.executeQuery();
    
    if (result.next()) {
      object = makeObject.apply(result);
    }
    
    return object;
  }
}
