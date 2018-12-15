/**
 * Project: a00967008_assignment02
 * File: PurchaseDao.java
 * Date: Jun 25, 2018
 * Time: 10:46:45 PM
 */
package a00967008.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.customer.ApplicationException;
import a00967008.io.PurchaseReader;
import a00967008.purchase.Purchase;

/**
 * @author Alan, A00967008
 *
 */
public class PurchaseDao extends Dao {

	public static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Purchases";

	private static final String PURCHASE_DATA_FILENAME = "purchases.csv";
	private static Logger LOG = LogManager.getLogger();

	/**
	 * Main constructor
	 * 
	 * @param database
	 *            - the database to set
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public PurchaseDao(Database database) throws ApplicationException, FileNotFoundException {
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
		File purchaseDataFile = new File(PURCHASE_DATA_FILENAME);
		try {
			if (!Database.tableExists(PurchaseDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(PurchaseDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}

				create();

				LOG.info("Inserting the purchases");

				if (!purchaseDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", PURCHASE_DATA_FILENAME));
				}

				PurchaseReader pr = new PurchaseReader();
				pr.readPurchaseList(purchaseDataFile, this);
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

		String sqlString = String.format("CREATE TABLE %s(" //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s FLOAT, " //
				+ "PRIMARY KEY (%s))", //
				TABLE_NAME, //
				Column.ID.name, Column.ID.length, //
				Column.CUSTOMER_ID.name, Column.CUSTOMER_ID.length, //
				Column.BOOK_ID.name, Column.BOOK_ID.length, //
				Column.PRICE.name, //
				Column.ID.name);

		super.create(sqlString);
	}

	/**
	 * Method to add
	 * 
	 * @param purchase
	 *            - the purchase to add
	 * @throws SQLException
	 */
	public void add(Purchase purchase) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				purchase.getPurchaseID(), //
				purchase.getCustomerID(), //
				purchase.getBookID(), //
				Double.parseDouble(purchase.getPrice()));

		LOG.info(String.format("Adding %s was %s", purchase, !result ? "successful" : "unsuccessful"));
	}

	/**
	 * Update the purchase.
	 * 
	 * @param purchase
	 *            - the purchase to update
	 * @throws SQLException
	 */
	public void update(Purchase purchase) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.CUSTOMER_ID.name, //
				Column.BOOK_ID.name, //
				Column.PRICE.name, //
				Column.ID.name);
		LOG.info("Update statment: " + sqlString);

		boolean result = execute(sqlString, purchase.getPurchaseID(), purchase.getCustomerID(), purchase.getBookID(), purchase.getPrice());
		LOG.info(String.format("Updating %s was %s", purchase, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Delete the purchase from the database.
	 * 
	 * @param purchase
	 *            - the purchase to delete
	 * @throws SQLException
	 */
	public void delete(Purchase purchase) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, purchase.getPurchaseID());
			LOG.info(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.info(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the purchase IDs from the database
	 * 
	 * @return the list of purchase IDs
	 * @throws SQLException
	 */
	public List<Long> getPurchaseIds() throws SQLException {
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

		LOG.info(String.format("Loaded %d purchase IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * Method to get purchase by id
	 * 
	 * @param purchaseId
	 *            - the purchase id to set
	 * @return the purchase
	 * @throws Exception
	 */
	public Purchase getPurchase(Long purchaseId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, purchaseId);
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

				Purchase purchase = new Purchase.Builder(resultSet.getString(Column.ID.name), resultSet.getString(Column.CUSTOMER_ID.name),
						resultSet.getString(Column.BOOK_ID.name)).setPrice(resultSet.getString(Column.PRICE.name)).build();

				return purchase;
			}
		} finally {
			close(statement);
		}

		return null;
	}

	/**
	 * Method to count cost purchases
	 * 
	 * @return cost of customers
	 * @throws Exception
	 */
	public double countAllPurchases() throws Exception {
		Statement statement = null;
		double count = 0;
		try {
			Connection connection = Database.getConnection();
			statement = connection.createStatement();
			// Execute a statement
			String sqlString = String.format("SELECT SUM(PRICE) as sum FROM %s", tableName);
			ResultSet resultSet = statement.executeQuery(sqlString);
			if (resultSet.next()) {
				count += resultSet.getDouble("sum");
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
		ID("id", 40), //
		CUSTOMER_ID("customerId", 40), //
		BOOK_ID("lastName", 40), //
		PRICE("price", 20); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}
}
