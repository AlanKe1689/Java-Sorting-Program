/**
 * Project: a00967008_assignment02
 * File: BookDao.java
 * Date: Jun 25, 2018
 * Time: 10:46:28 PM
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

import a00967008.book.Book;
import a00967008.customer.ApplicationException;
import a00967008.io.BookReader;

/**
 * @author Alan, A00967008
 *
 */
public class BookDao extends Dao {

	public static final String TABLE_NAME = DbConstants.TABLE_ROOT + "Books";

	private static final String BOOKS_DATA_FILENAME = "books500.csv";
	private static Logger LOG = LogManager.getLogger();

	/**
	 * Main constructor
	 * 
	 * @param database
	 *            - the database to set
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public BookDao(Database database) throws ApplicationException, FileNotFoundException {
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
		File bookDataFile = new File(BOOKS_DATA_FILENAME);
		try {
			if (!Database.tableExists(BookDao.TABLE_NAME) || Database.dbTableDropRequested()) {
				if (Database.tableExists(BookDao.TABLE_NAME) && Database.dbTableDropRequested()) {
					drop();
				}

				create();

				LOG.info("Inserting the books");

				if (!bookDataFile.exists()) {
					throw new ApplicationException(String.format("Required '%s' is missing.", BOOKS_DATA_FILENAME));
				}

				BookReader br = new BookReader();
				br.readBookList(bookDataFile, this);
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
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "%s VARCHAR(%d), " //
				+ "PRIMARY KEY (%s))", // ID
				TABLE_NAME, //
				Column.ID.name, Column.ID.length, //
				Column.ISBN.name, Column.ISBN.length, //
				Column.AUTHORS.name, Column.AUTHORS.length, //
				Column.PUBLICATION_YEAR.name, Column.PUBLICATION_YEAR.length, //
				Column.TITLE.name, Column.TITLE.length, //
				Column.AVERAGE_RATING.name, Column.AVERAGE_RATING.length, //
				Column.RATINGS_COUNT.name, Column.RATINGS_COUNT.length, //
				Column.IMAGE_URL.name, Column.IMAGE_URL.length, //
				Column.ID.name);

		super.create(sqlString);
	}

	/**
	 * Method to add
	 * 
	 * @param book
	 *            - the book to add
	 * @throws SQLException
	 */
	public void add(Book book) throws SQLException {
		String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?)", TABLE_NAME);
		boolean result = execute(sqlString, //
				book.getBookID(), //
				book.getIsbn(), //
				book.getAuthors(), //
				book.getOriginalPublicationYear(), //
				book.getOriginalTitle(), //
				book.getAverageRating(), //
				book.getRatingsCount(), //
				book.getImageUrl());
		LOG.info(String.format("Adding %s was %s", book, !result ? "successful" : "unsuccessful"));
	}

	/**
	 * Update the book.
	 * 
	 * @param book
	 *            - the book to update
	 * @throws SQLException
	 */
	public void update(Book book) throws SQLException {
		String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", TABLE_NAME, //
				Column.ISBN.name, //
				Column.AUTHORS.name, //
				Column.PUBLICATION_YEAR.name, //
				Column.TITLE.name, //
				Column.AVERAGE_RATING.name, //
				Column.RATINGS_COUNT.name, //
				Column.IMAGE_URL.name, //
				Column.ID.name);
		LOG.info("Update statment: " + sqlString);

		boolean result = execute(sqlString, book.getBookID(), book.getIsbn(), book.getAuthors(), book.getOriginalPublicationYear(),
				book.getOriginalTitle(), book.getAverageRating(), book.getRatingsCount(), book.getImageUrl());
		LOG.info(String.format("Updating %s was %s", book, result ? "successful" : "unsuccessful"));
	}

	/**
	 * Delete the book from the database.
	 * 
	 * @param book
	 *            - the book to delete
	 * @throws SQLException
	 */
	public void delete(Book book) throws SQLException {
		Connection connection;
		Statement statement = null;
		try {
			connection = Database.getConnection();
			statement = connection.createStatement();

			String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", TABLE_NAME, Column.ID.name, book.getBookID());
			LOG.info(sqlString);
			int rowcount = statement.executeUpdate(sqlString);
			LOG.info(String.format("Deleted %d rows", rowcount));
		} finally {
			close(statement);
		}
	}

	/**
	 * Retrieve all the book IDs from the database
	 * 
	 * @return the list of book IDs
	 * @throws SQLException
	 */
	public List<Long> getBookIds() throws SQLException {
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

		LOG.info(String.format("Loaded %d book IDs from the database", ids.size()));

		return ids;
	}

	/**
	 * Method to get book by id
	 * 
	 * @param bookId
	 *            - the book id to set
	 * @return the book
	 * @throws Exception
	 */
	public Book getBook(Long bookId) throws Exception {
		String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_NAME, Column.ID.name, bookId);
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

				Book book = new Book.Builder(resultSet.getString(Column.ID.name), resultSet.getString(Column.ISBN.name))
						.setAuthors(resultSet.getString(Column.AUTHORS.name))
						.setOriginalPublicationYear(resultSet.getString(Column.PUBLICATION_YEAR.name))
						.setOriginalTitle(resultSet.getString(Column.TITLE.name)).setAverageRating(resultSet.getString(Column.AVERAGE_RATING.name))
						.setRatingsCount(resultSet.getString(Column.RATINGS_COUNT.name)).setImageUrl(resultSet.getString(Column.IMAGE_URL.name))
						.build();

				return book;
			}
		} finally {
			close(statement);
		}
		return null;
	}

	/**
	 * Method to count books
	 * 
	 * @return number of books
	 * @throws Exception
	 */
	public int countAllBooks() throws Exception {
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
		ISBN("isbn", 90), //
		AUTHORS("authors", 90), //
		PUBLICATION_YEAR("originalPublicationYear", 90), //
		TITLE("originalTitle", 90), //
		AVERAGE_RATING("averageRating", 90), //
		RATINGS_COUNT("ratingsCount", 90), //
		IMAGE_URL("imageUrl", 90); //

		String name;
		int length;

		private Column(String name, int length) {
			this.name = name;
			this.length = length;
		}
	}
}
