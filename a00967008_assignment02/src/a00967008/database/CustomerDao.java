/**
 * Project: a00967008_lab09
 * File: Lab9.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.customer.ApplicationException;
import a00967008.customer.Customer;
import a00967008.io.CustomerReader;

/**
 * @author Alan, A00967008
 *
 */
public class CustomerDao extends Dao {

	public static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Customers";

	private static final String CUSTOMERS_DATA_FILENAME = "customers.dat";
	private static Logger LOG = LogManager.getLogger();

	/**
	 * Main constructor
	 * 
	 * @param database
	 *            - the database to set
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public CustomerDao(Database database) throws ApplicationException, FileNotFoundException {
		super(database, TABLE_NAME);

		load();
	}

	/**
	 * Method to load
	 * 
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	public void load() throws ApplicationException, FileNotFoundException {
		File customerDataFile = new File(CUSTOMERS_DATA_FILENAME);
		try {
			if (!Database.tableExists(CustomerDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(CustomerDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}

				create();

				LOG.info("Inserting the customers");

				if (!customerDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", CUSTOMERS_DATA_FILENAME));
				}

				CustomerReader cr = new CustomerReader();
				cr.createCustomerList(customerDataFile, this);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}

	/**
	 * Create method
	 */
	@Override
	public void create() throws SQLException {
		LOG.info("Creating database table " + TABLE_NAME);

		// With MS SQL Server, JOINED_DATE needs to be a DATETIME type.
		String sqlString = String.format("CREATE TABLE %s(" //
				+ "%s VARCHAR(%d), " // ID
				+ "%s VARCHAR(%d), " // FIRST_NAME
				+ "%s VARCHAR(%d), " // LAST_NAME
				+ "%s VARCHAR(%d), " // STREET
				+ "%s VARCHAR(%d), " // CITY
				+ "%s VARCHAR(%d), " // POSTAL_CODE
				+ "%s VARCHAR(%d), " // PHONE
				+ "%s VARCHAR(%d), " // EMAIL_ADDRESS
				+ "%s DATETIME, " // JOINED_DATE
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, Column.ID.length, //
				Column.FIRST_NAME.name, Column.FIRST_NAME.length, //
				Column.LAST_NAME.name, Column.LAST_NAME.length, //
				Column.STREET.name, Column.STREET.length, //
				Column.CITY.name, Column.CITY.length, //
				Column.POSTAL_CODE.name, Column.POSTAL_CODE.length, //
				Column.PHONE.name, Column.PHONE.length, //
				Column.EMAIL_ADDRESS.name, Column.EMAIL_ADDRESS.length, //
				Column.JOINED_DATE.name, //
				Column.ID.name);

		super.create(sqlString);
	}

	/**
	 * Method to add
	 * 
	 * @param customer
	 *            - the customer to add
	 * @throws SQLException
	 */
	public void add(Customer customer) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				customer.getCustomerID(), //
				customer.getFirstName(), //
				customer.getLastName(), //
				customer.getStreetName(), //
				customer.getCity(), //
				customer.getPostalCode(), //
				customer.getPhone(), //
				customer.getEmailAddress(), //
				toTimestamp(customer.getJoinDate()));
		LOG.info(String.format("Adding %s was %s", customer, !result ? "successful" : "unsuccessful"));
	}

	/**
	 * Method to update customer
	 * 
	 * @param customer
	 * @throws SQLException
	 */
	public void update(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sql = String.format("UPDATE %s set %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s', %s='%s' WHERE %s='%s'",
					tableName, //
					Column.ID.name, customer.getCustomerID(), //
					Column.FIRST_NAME.name, customer.getFirstName(), //
					Column.LAST_NAME.name, customer.getLastName(), //
					Column.STREET.name, customer.getStreetName(), //
					Column.CITY.name, customer.getCity(), //
					Column.POSTAL_CODE.name, customer.getPostalCode(), //
					Column.PHONE.name, customer.getPhone(), //
					Column.EMAIL_ADDRESS.name, customer.getEmailAddress(), //
					Column.JOINED_DATE.name, customer.getJoinDate(), //
					Column.ID.name, customer.getCustomerID());
			LOG.debug(sql);
			int rowcount = statement.executeUpdate(sql);
			System.out.println(String.format("Updated %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Delete the customer from the database.
	 * 
	 * @param customer
	 *            - the customer to delete
	 * @throws SQLException
	 */
	public void delete(Customer customer) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, customer.getCustomerID());
			LOG.info(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.info(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the customer IDs from the database
	 * 
	 * @return the list of customer IDs
	 * @throws SQLException
	 */
	public List<Long> getCustomerIds() throws SQLException {
		List<Long> ids = new ArrayList<>();

		String selectString = String.format("SELECT %s FROM %s", Column.ID.name, TABLE_NAME);
		LOG.info(selectString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(selectString);

			while (resultSet.next()) {
				ids.add(resultSet.getLong(Column.ID.name));
			}

		} finally {
			close(statement);
		}

		LOG.info(String.format("Loaded %d customer IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * Method to get customer by id
	 * 
	 * @param customerId
	 *            - the customer id to set
	 * @return the customer
	 * @throws Exception
	 */
	public Customer getCustomer(Long customerId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, customerId);
		LOG.info(sqlString);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlString);

			int count = 0;
			while (resultSet.next()) {
				count++;
				if (count > 1) {
					throw new ApplicationException(String.format("Expected one result, got %d", count));
				}

				Timestamp timestamp = resultSet.getTimestamp(Column.JOINED_DATE.name);
				LocalDate date = timestamp.toLocalDateTime().toLocalDate();

				Customer customer = new Customer.Builder(resultSet.getString(Column.ID.name), resultSet.getString(Column.PHONE.name)) //
						.setFirstName(resultSet.getString(Column.FIRST_NAME.name)) //
						.setLastName(resultSet.getString(Column.LAST_NAME.name)) //
						.setStreetName(resultSet.getString(Column.STREET.name)) //
						.setCity(resultSet.getString(Column.CITY.name)) //
						.setPostalCode(resultSet.getString(Column.POSTAL_CODE.name)) //
						.setEmailAddress(resultSet.getString(Column.EMAIL_ADDRESS.name)) //
						.setJoinDate(date).build();

				return customer;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	/**
	 * Method to count customers
	 * 
	 * @return number of customers
	 * @throws Exception
	 */
	public int countAllCustomers() throws Exception {
		Statement statement = null;
		int count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} finally {
			close(statement);
		}
		return count;
	}

	/**
	 * 
	 * @author Alan, A00967008
	 *
	 */
	public enum Column {
		ID("id", 90), //
		FIRST_NAME("firstName", 90), //
		LAST_NAME("lastName", 90), //
		STREET("street", 90), //
		CITY("city", 90), //
		POSTAL_CODE("postalCode", 90), //
		PHONE("phone", 90), //
		EMAIL_ADDRESS("emailAddress", 90), //
		JOINED_DATE("joinedDate", 90); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}
}
