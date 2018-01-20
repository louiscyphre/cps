package cps.server.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

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
    
    result.close();
    statement.close();
    
    return object;
  }
  
  public Collection<T> collectResults(Connection conn, ResultVisitor<T> makeObject) throws SQLException {
    LinkedList<T> items = new LinkedList<>();
    
    PreparedStatement statement = conn.prepareStatement(command);
    filler.apply(statement);
    
    ResultSet result = statement.executeQuery();
    
    while (result.next()) {
      items.add(makeObject.apply(result));
    }
    
    result.close();
    statement.close();
    
    return items;
  }
  
  public int update(Connection conn) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(command);
    filler.apply(statement);    
    int count = statement.executeUpdate();
    statement.close();
    return count;
  }

  public int create(Connection conn) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
    filler.apply(statement);    
    statement.executeUpdate();
    
    ResultSet keys = statement.getGeneratedKeys();
    int newID = 0;

    if (keys.next()) {
      newID = keys.getInt(1);
    }
    
    if (keys != null) {
      keys.close();      
    }
    
    statement.close();
    
    return newID;
  }
}
