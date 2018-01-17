/*
 * 
 */
package cps.server.controllers;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

import cps.api.response.ServerResponse;
import cps.server.ServerConfig;
import cps.server.ServerException;
import cps.common.Utilities.VisitorWithException;
import cps.common.Utilities.VisitorWithExceptionAndReturnType;

/** Manages database-related operations. */
public class DatabaseController {

  /** The host address. */
  String host;

  /** Database name. */
  String dbName;

  /** Username. */
  String username;

  /** Password. */
  String password;

  /** Used to run a user-specified action with an SQL connection as parameter. */
  public interface DatabaseAction {

    /** Perform the action.
     * @param conn the SQL connection
     * @throws SQLException the SQL exception
     * @throws ServerException the server exception */
    void perform(Connection conn) throws SQLException, ServerException;
  }

  /** Used to return the result of a database query.
   * @param <T> the generic type */
  public interface DatabaseQuery<T> {

    /** Perform the query.
     * @param conn the SQL connection
     * @return the result object
     * @throws SQLException the SQL exception
     * @throws ServerException the server exception */
    T perform(Connection conn) throws SQLException, ServerException;
  }

  /** The Interface DatabaseQueryWithException.
   * @param <T> the generic type */
  public interface DatabaseQueryWithException<T> {

    /** Perform.
     * @param conn the SQL connection
     * @param response the response
     * @return the result object
     * @throws SQLException the SQL exception
     * @throws ServerException the server exception */
    T perform(Connection conn, T response) throws SQLException, ServerException;
  }

  /** The Interface EntityBuilder.
   * @param <T> the generic type */
  public interface EntityBuilder<T> {
    
    /** From result set.
     * @param result the result
     * @return the t
     * @throws SQLException the SQL exception */
    T fromResultSet(ResultSet result) throws SQLException;
  }

  /** The Interface StatementVisitor. */
  public interface StatementVisitor extends VisitorWithException<PreparedStatement, SQLException> {

  }

  /** The Interface ResultVisitor. */
  public interface ResultVisitor extends VisitorWithException<ResultSet, SQLException> {

  }

  /** Instantiates a new database controller.
   * @param host the host
   * @param dbName the db name
   * @param username the username
   * @param password the password
   * @throws Exception the exception */
  public DatabaseController(String host, String dbName, String username, String password) throws Exception {
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    this.host = host;
    this.dbName = dbName;
    this.username = username;
    this.password = password;
  }
  
  public DatabaseController(ServerConfig config) throws Exception {
    this(config.get("db.host"), config.get("db.name"), config.get("db.username"),
        config.get("db.password"));
  }

  /**
   * Gets the SQL connectionection.
   *
   * @return the SQL connectionection
   * @throws SQLException
   *           the SQL exception
   */
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, username, password);
  }

  /** Handle SQL exception.
   * @param ex the ex */
  public void handleSQLException(SQLException ex) {
    System.out.println("SQLException: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("VendorError: " + ex.getErrorCode());
    ex.printStackTrace();
  }

  /** Close connection.
   * @param conn the SQL connection */
  public void closeConnection(Connection conn) {
    if (conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /** Perform action.
   * @param action the action
   * @throws ServerException the server exception */
  public void performAction(DatabaseAction action) throws ServerException {
    Connection conn = null;

    try {
      conn = getConnection();
      action.perform(conn);
    } catch (SQLException ex) {
      handleSQLException(ex);
    } finally {
      closeConnection(conn);
    }
  }

  /** Perform query.
   * @param <T> the generic type
   * @param query the query
   * @return the t
   * @throws ServerException the server exception */
  public <T> T performQuery(DatabaseQuery<T> query) throws ServerException {
    Connection conn = null;
    T result = null;

    try {
      conn = getConnection();
      result = query.perform(conn);
    } catch (SQLException ex) {
      handleSQLException(ex);
    } finally {
      closeConnection(conn);
    }

    return result;
  }

  /** Perform query.
   * @param <T> the generic type
   * @param response the response
   * @param query the query
   * @return the t */
  public <T extends ServerResponse> T performQuery(T response, DatabaseQueryWithException<T> query) {
    Connection conn = null;
    T result = null;

    try {
      conn = getConnection();
      result = query.perform(conn, response);
    } catch (SQLException ex) {
      handleSQLException(ex);
    } catch (ServerException ex) {
      response.setError(ex.getMessage());
      return response;
    } finally {
      // This still gets called even if the previous block returns
      closeConnection(conn);
    }

    return result;
  }

  /** Truncate tables.
   * @throws ServerException the server exception */
  public void truncateTables() throws ServerException {
    performAction(conn -> {
      Collection<String> tables = getTables(conn);

      for (String table : tables) {
        // System.out.println("TRUNCATE " + table);
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("TRUNCATE " + table);
        stmt.close();
      }
    });
  }

  /** Gets the tables.
   * @param conn the SQL connection
   * @return the tables
   * @throws SQLException the SQL exception */
  public Collection<String> getTables(Connection conn) throws SQLException {
    LinkedList<String> results = new LinkedList<>();

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SHOW TABLES");

    while (rs.next()) {
      results.add(rs.getString(1));
    }

    rs.close();
    stmt.close();

    return results;
  }

  public Collection<String> getTables() throws ServerException {
    return performQuery(conn -> getTables(conn));
  }

  /** Count the number of rows in a table.
   * @param conn the SQL connection
   * @param table the table name
   * @return the number of rows
   * @throws SQLException the SQL exception */
  public int countEntities(Connection conn, String table) throws SQLException {
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table);

    int count = 0;

    if (rs.next()) {
      count = rs.getInt(1);
    }

    rs.close();
    stmt.close();

    return count;

  }

  /** Count the number of rows in a table.
   * @param table the table name
   * @return the number of rows
   * @throws ServerException the server exception */
  public int countEntities(String table) throws ServerException {
    return performQuery(conn -> countEntities(conn, table));
  }

  /** Find all entities of a given type (table name).
   * @param <T> the generic type
   * @param conn the SQL connection
   * @param table the table
   * @param orderBy which field to order by
   * @param builder the builder
   * @return the collection
   * @throws SQLException the SQL exception */
  public <T> Collection<T> findAll(Connection conn, String table, String orderBy, EntityBuilder<T> builder) throws SQLException {
    LinkedList<T> results = new LinkedList<T>();

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s ORDER BY %s", table, orderBy));

    while (rs.next()) {
      results.add(builder.fromResultSet(rs));
    }

    rs.close();
    stmt.close();

    return results;
  }

  /** Apply a user-specified action to each result of an SQL query.
   * @param conn the SQL connection
   * @param sqlCommand the sql command
   * @param withStatement the with statement
   * @param withResult the with result
   * @throws SQLException the SQL exception */
  public static void statementForEach(Connection conn, String sqlCommand, StatementVisitor withStatement, ResultVisitor withResult) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(sqlCommand);

    withStatement.apply(statement);
    ResultSet result = statement.executeQuery();

    while (result.next()) {
      withResult.apply(result);
    }

    statement.close();
    result.close();

  }

  /** Gets the (transformed) result of an SQL query.
   * @param <T> the generic type
   * @param conn the SQL connection
   * @param sqlCommand the SQL query to perform 
   * @param withStatement defines query fields substitution
   * @param withResult uses the query result to perfrom a custom action
   * @return the statement result
   * @throws SQLException the SQL exception */
  public static <T> T getStatementResult(Connection conn, String sqlCommand, VisitorWithException<PreparedStatement, SQLException> withStatement,
      VisitorWithExceptionAndReturnType<ResultSet, SQLException, T> withResult) throws SQLException {
    T item = null;
    PreparedStatement statement = conn.prepareStatement(sqlCommand);

    withStatement.apply(statement);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      item = withResult.apply(result);
    }

    statement.close();
    result.close();

    return item;

  }
}
