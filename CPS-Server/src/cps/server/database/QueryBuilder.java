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

/** Facilitates building and performing SQL queries by encapsulating common patterns.
 * @param <T> the generic type */
public class QueryBuilder<T> {
  private String command;
  private StatementVisitor filler = st -> {};
  
  /** Instantiates a new query builder.
   * @param command the command */
  public QueryBuilder(String command) {
    this.command = command;
  }
  
  /** Set a lambda function for filling the query fields.
   * The function accepts a PreparedStatement and is expected to fill the missing values in the statement.
   * @param fillFields the fill fields
   * @return the query builder */
  public QueryBuilder<T> withFields(StatementVisitor fillFields) {
    this.filler = fillFields;
    return this;
  }
  
  /** Perform the query and return the result.
   * @param conn the SQL connection
   * @param makeObject a lambda function that accepts an SQL ResultSet and returns an object of type T
   * @return the result object
   * @throws SQLException on error */
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
  
  /** Collect all query results in a list.
   * Used to find multiple records that match a condition.
   * @param conn the SQL connection
   * @param makeObject lambda function that instantiates an entity object of type T from a ResultSet
   * @return the list of results
   * @throws SQLException on error */
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
  
  /** Perform a query and count the number of results.
   * @param conn the SQL connection
   * @return the number of results
   * @throws SQLException on error */
  public int countResults(Connection conn) throws SQLException {
    int items = 0;
    
    PreparedStatement statement = conn.prepareStatement(command);
    filler.apply(statement);
    
    ResultSet result = statement.executeQuery();
    
    while (result.next()) {
      items++;
    }
    
    result.close();
    statement.close();
    
    return items;
  }
  
  /** Update a record in the database.
   * @param conn the SQL connection
   * @return the number of affected rows
   * @throws SQLException on error */
  public int update(Connection conn) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(command);
    filler.apply(statement);    
    int count = statement.executeUpdate();
    statement.close();
    return count;
  }

  /** Create a new record in the database.
   * @param conn the SQL connection
   * @return the ID of the new record
   * @throws SQLException on error */
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
