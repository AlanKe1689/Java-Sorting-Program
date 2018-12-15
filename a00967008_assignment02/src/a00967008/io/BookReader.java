/**
 * Project: a00967008_assignment01
 * File: BookReader.java
 * Date: May 30, 2018
 * Time: 10:11:57 PM
 */
package a00967008.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.book.Book;
import a00967008.customer.ApplicationException;
import a00967008.database.BookDao;

/**
 * @author Alan, A00967008
 *
 */
public class BookReader {
	private ArrayList<Book> bookList;
	private String[] bookArray;

	private final int ZERO = 0;
	private final int INDEX_ONE = 1;
	private final int INDEX_TWO = 2;
	private final int INDEX_THREE = 3;
	private final int INDEX_FOUR = 4;
	private final int INDEX_FIVE = 5;
	private final int INDEX_SIX = 6;
	private final int INDEX_SEVEN = 7;
	private final int ARRAY_SIZE = 8;

	private final String BOOK_FILE = "books500.csv";
	private final String BOOK_ID = "book_id";
	private final String ISBN = "isbn";
	private final String AUTHORS = "authors";
	private final String OG_PUB_YEAR = "original_publication_year";
	private final String OG_TITLE = "original_title";
	private final String AVG_RATING = "average_rating";
	private final String RATINGS_COUNT = "ratings_count";
	private final String IMAGE_URL = "image_url";

	private static final Logger LOG = LogManager.getLogger(BookReader.class);

	/**
	 * Main constructor
	 */
	public BookReader() {

	}

	/**
	 * Accessor for book list
	 * 
	 * @return the bookList
	 */
	public ArrayList<Book> getBookList() {
		return bookList;
	}

	/**
	 * Mutator for book list
	 * 
	 * @param bookList
	 *            - the bookList to set
	 */
	public void setBookList(ArrayList<Book> bookList) {
		this.bookList = bookList;
	}

	/**
	 * Method to read from book list
	 * 
	 * @return the book list
	 * @throws ApplicationException
	 */
	public ArrayList<Book> readBookList(File bookFile, BookDao dao) throws ApplicationException {
		bookList = new ArrayList<>();
		bookArray = new String[ARRAY_SIZE];

		try {
			LOG.info("Reading book data from file");
			BufferedReader input = new BufferedReader(new FileReader(bookFile));
			CSVParser parse = new CSVParser(input, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			LOG.info("Creating book list");
			for (CSVRecord record : parse) {
				bookArray[ZERO] = record.get(BOOK_ID);
				bookArray[INDEX_ONE] = record.get(ISBN);
				bookArray[INDEX_TWO] = record.get(AUTHORS);
				bookArray[INDEX_THREE] = record.get(OG_PUB_YEAR);
				bookArray[INDEX_FOUR] = record.get(OG_TITLE);
				bookArray[INDEX_FIVE] = record.get(AVG_RATING);
				bookArray[INDEX_SIX] = record.get(RATINGS_COUNT);
				bookArray[INDEX_SEVEN] = record.get(IMAGE_URL);
				LOG.info("Book information: " + Arrays.toString(bookArray));

				Book book = new Book.Builder(bookArray[ZERO], bookArray[INDEX_ONE]).setAuthors(bookArray[INDEX_TWO])
						.setOriginalPublicationYear(bookArray[INDEX_THREE]).setOriginalTitle(bookArray[INDEX_FOUR])
						.setAverageRating(bookArray[INDEX_FIVE]).setRatingsCount(bookArray[INDEX_SIX]).setImageUrl(bookArray[INDEX_SEVEN]).build();

				bookList.add(book);

				bookArray = new String[ARRAY_SIZE];
				dao.add(book);
			}
			LOG.info("Book list created");
			parse.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + BOOK_FILE);
			throw new ApplicationException("Cannot find " + BOOK_FILE);
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} catch (SQLException e) {
			throw new ApplicationException(e.getMessage());
		}
		return bookList;
	}
}
