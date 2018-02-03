/*
 * 
 */
package cps.server.database;

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

  String host;
  String dbName;
  String username;
  String password;

  /** Used to run a user-specified action with an SQL connection as parameter. */
  public interface DatabaseAction {

    /** Perform the action.
     * @param conn the SQL connection
     * @throws SQLException on error
     * @throws ServerException on error */
    void perform(Connection conn) throws SQLException, ServerException;
  }

  /** Used to receive a database query as a lambda function
   * The query runs some code (specified in the body of the lambda function) and then returns a result of the generic type T.
   * @param <T> the return type of the query */
  public interface DatabaseQuery<T> {

    /** Perform the query.
     * @param conn the SQL connection
     * @return the result object
     * @throws SQLException on error
     * @throws ServerException on error */
    T perform(Connection conn) throws SQLException, ServerException;
  }

  /** Used to receive a database query as a lambda function
   * The query runs some code (specified in the body of the lambda function) and then returns a result of the generic type T.
   * Receives a response of generic type T (typically a ServerResponse subclass) and returns it (perhaps modified).
   * @param <T> the return type of the query */
  public interface DatabaseQueryWithResponse<T> {

    /** Perform.
     * @param conn the SQL connection
     * @param response the response
     * @return the result object
     * @throws SQLException on error
     * @throws ServerException on error */
    T perform(Connection conn, T response) throws SQLException, ServerException;
  }

  /** Is used to receive a lambda function that fills the fields of a PreparedStatement. */
  public interface StatementVisitor extends VisitorWithException<PreparedStatement, SQLException> {

  }

  /** Is used to receive a lambda function that creates an entity object or returns some result from an SQL ResultSet as parameter.
   * @param <R> the return type of the lambda function - the type of the created entity */
  public interface ResultVisitor<R> extends VisitorWithExceptionAndReturnType<ResultSet, SQLException, R> {

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
  
  /** Instantiates a new database controller.
   * @param config the config
   * @throws Exception the exception */
  public DatabaseController(ServerConfig config) throws Exception {
    this(config.get("db.host"), config.get("db.name"), config.get("db.username"),
        config.get("db.password"));
  }

  /**
   * Opens and returns a new SQL connection.
   *
   * @return the SQL connection
   * @throws SQLException
   *           on error
   */
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:mysql://" + host + "/" + dbName, username, password);
  }

  /** Handle SQL exception.
   * @param ex the exception */
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

  /** Perform an action (SQL query that doesn't return results) on the database.
   * @param action a lambda function that performs the action
   * @throws ServerException on error */
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

  /** Perform a query on the database and return a result object.
   * Is used to facilitate database access in request handlers.
   * @param <T> the result type
   * @param query a lambda function that contains the code of the query
   * @return the query result
   * @throws ServerException on error */
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

  /** Perform a query on the database and return a result object.
   * Is used to facilitate database access in request handlers.
   * Automatically handles server exceptions, making it possible to write handler code in a stream-lined way.
   * @param <T> the generic type
   * @param response the response
   * @param query the query
   * @return the t */
  public <T extends ServerResponse> T performQuery(T response, DatabaseQueryWithResponse<T> query) {
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

  /** Truncate all tables in the database.
   * @throws ServerException on error */
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

  Collection<String> getTables(Connection conn) throws SQLException {
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

  Collection<String> getTables() throws ServerException {
    return performQuery(conn -> getTables(conn));
  }

  /** Count the number of rows in a table.
   * @param conn the SQL connection
   * @param table the table name
   * @return the number of rows
   * @throws SQLException on error */
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
   * @throws ServerException on error */
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
   * @throws SQLException on error */
  public <T> Collection<T> findAll(Connection conn, String table, String orderBy, ResultVisitor<T> builder) throws SQLException {
    LinkedList<T> results = new LinkedList<T>();

    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s ORDER BY %s", table, orderBy));

    while (rs.next()) {
      results.add(builder.apply(rs));
    }

    rs.close();
    stmt.close();

    return results;
  }

  /** Apply a user-specified action to each result of an SQL query.
   * @param <R> the returned object type (not used here)
   * @param conn the SQL connection
   * @param sqlCommand the SQL command
   * @param withStatement the with statement
   * @param withResult the with result
   * @throws SQLException on error */
  public static <R> void statementForEach(Connection conn, String sqlCommand, StatementVisitor withStatement, ResultVisitor<R> withResult) throws SQLException {
    PreparedStatement statement = conn.prepareStatement(sqlCommand);

    withStatement.apply(statement);
    ResultSet result = statement.executeQuery();

    while (result.next()) {
      withResult.apply(result);
    }

    statement.close();
    result.close();

  }

  /** Get the (transformed) result of an SQL query.
   * @param <T> the generic type
   * @param conn the SQL connection
   * @param sqlCommand the SQL query to perform 
   * @param withStatement defines query fields substitution
   * @param withResult uses the query result to perform a custom action
   * @return the statement result
   * @throws SQLException on error */
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
