/**
 * Project: a00967008_lab09
 * File: Lab9.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Alan, A00967008
 *
 */
public abstract class Dao {
	private static Logger LOG = LogManager.getLogger();
	protected final Database database;
	protected final String tableName;

	/**
	 * Main constructor
	 * 
	 * @param database
	 *            - the database to set
	 * @param tableName
	 *            - the table name
	 */
	protected Dao(Database database, String tableName) {
		this.database = database;
		this.tableName = tableName;
	}

	/**
	 * Abstract method to create
	 * 
	 * @throws SQLException
	 */
	public abstract void create() throws SQLException;

	/**
	 * Delete the database table
	 * 
	 * @throws SQLException
	 */
	public void drop() throws SQLException {
		Statement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			if (Database.tableExists(tableName)) {
				LOG.debug("drop table " + tableName);
				statement.executeUpdate("drop table " + tableName);
			}
		} finally {
			close(statement);
		}
	}

	/**
	 * Tell the database we're shutting down.
	 */
	public void shutdown() {
		database.shutdown();
		LOG.debug("database shutdown");
	}

	/**
	 * Create connection
	 * 
	 * @param createStatement
	 *            - statement
	 * @throws SQLException
	 */
	protected void create(String createStatement) throws SQLException {
		LOG.debug(createStatement);
		Statement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(createStatement);
		} finally {
			close(statement);
		}
	}

	/**
	 * Method to execute
	 * 
	 * @param preparedStatementString
	 *            - SQL code
	 * @param args
	 *            - arguments
	 * @return if execute
	 * @throws SQLException
	 */
	protected boolean execute(String preparedStatementString, Object... args) throws SQLException {
		LOG.debug(preparedStatementString);
		boolean result = false;
		PreparedStatement statement = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.prepareStatement(preparedStatementString);
			int i = 1;
			for (Object object : args) {
				if (object instanceof String) {
					statement.setString(i, object.toString());
				} else if (object instanceof Boolean) {
					statement.setBoolean(i, (Boolean) object);
				} else if (object instanceof Integer) {
					statement.setInt(i, (Integer) object);
				} else if (object instanceof Long) {
					statement.setLong(i, (Long) object);
				} else if (object instanceof Float) {
					statement.setFloat(i, (Float) object);
				} else if (object instanceof Double) {
					statement.setDouble(i, (Double) object);
				} else if (object instanceof Byte) {
					statement.setByte(i, (Byte) object);
				} else if (object instanceof Timestamp) {
					statement.setTimestamp(i, (Timestamp) object);
				} else if (object instanceof LocalDateTime) {
					statement.setTimestamp(i, Timestamp.valueOf((LocalDateTime) object));
				} else {
					statement.setString(i, object.toString());
				}

				i++;
			}

			result = statement.execute();
		} finally {
			close(statement);
		}

		return result;
	}

	/**
	 * Method to close connection
	 * 
	 * @param statement
	 *            - statement
	 */
	protected void close(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			LOG.error("Failed to close statment" + e);
		}
	}

	/**
	 * Convert to Date
	 * 
	 * @param date
	 *            - the date to convert
	 * @return the date
	 */
	public static Date toDate(LocalDate date) {
		return Date.valueOf(date);
	}

	/**
	 * Convert to time stamp
	 * 
	 * @param date
	 *            - the date to convert
	 * @return time stamp
	 */
	public static Timestamp toTimestamp(LocalDate date) {
		return Timestamp.valueOf(LocalDateTime.of(date, LocalTime.now()));
	}

	/**
	 * Convert to time stamp
	 * 
	 * @param date
	 *            - the date to convert
	 * @return time stamp
	 */
	public static Timestamp toTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
}
