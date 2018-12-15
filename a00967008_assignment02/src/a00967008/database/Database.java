/**
 * Project: a00967008_lab09
 * File: Lab9.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Alan, A00967008
 *
 */
public class Database {

	public static final String DB_DRIVER_KEY = "db.driver";
	public static final String DB_URL_KEY = "db.url";
	public static final String DB_USER_KEY = "db.user";
	public static final String DB_PASSWORD_KEY = "db.password";

	private static final Logger LOG = LogManager.getLogger();

	private static Connection connection;
	private static Properties properties;
	private static boolean dbTableDropRequested;

	/**
	 * Main constructor
	 * 
	 * @param properties
	 *            - properties of object
	 */
	public Database(Properties properties) {
		LOG.debug("Loading database properties from db.properties");
		Database.properties = properties;
	}

	/**
	 * Method to get connection
	 * 
	 * @return the connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (connection != null) {
			return connection;
		}

		try {
			connect();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}

		return connection;
	}

	/**
	 * Method to connect
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void connect() throws ClassNotFoundException, SQLException {
		String dbDriver = properties.getProperty(DB_DRIVER_KEY);
		LOG.debug(dbDriver);
		Class.forName(dbDriver);
		LOG.debug("Driver loaded");
		String dbUrl = properties.getProperty(DB_URL_KEY);
		LOG.debug("DB URL=" + dbUrl);
		String dbUser = properties.getProperty(DB_USER_KEY);
		LOG.debug("DB USER=" + dbUser);
		String dbPassword = properties.getProperty(DB_PASSWORD_KEY);
		LOG.debug("DB URL=" + dbPassword);
		connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		LOG.debug("Database connected");
	}

	/**
	 * Close the connections to the database
	 */
	public void shutdown() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
	}

	/**
	 * Determine if the database table exists.
	 * 
	 * @param tableName
	 *            - the table name
	 * @return true is the table exists, false otherwise
	 * @throws SQLException
	 */
	public static boolean tableExists(String targetTableName) throws SQLException {
		DatabaseMetaData databaseMetaData = getConnection().getMetaData();
		ResultSet resultSet = null;
		String tableName = null;

		try {
			resultSet = databaseMetaData.getTables(connection.getCatalog(), "%", "%", null);
			while (resultSet.next()) {
				tableName = resultSet.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(targetTableName)) {
					LOG.debug("Found the target table named: " + targetTableName);
					return true;
				}
			}
		} finally {
			resultSet.close();
		}

		return false;
	}

	/**
	 * Request table drop
	 */
	public static void requestDbTableDrop() {
		dbTableDropRequested = true;
	}

	/**
	 * Check if table drop requested
	 * 
	 * @return if requested
	 */
	public static boolean dbTableDropRequested() {
		return dbTableDropRequested;
	}

	/**
	 * Check if it is MS SQL server
	 * 
	 * @return if it is correct server
	 */
	public static boolean isMsSqlServer() {
		return Database.properties.get(DB_DRIVER_KEY).toString().contains("sqlserver");
	}
}
