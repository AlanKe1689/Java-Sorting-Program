package a00967008;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a00967008.book.ApplicationException;
import a00967008.book.Book;
import a00967008.book.BookOptions;
import a00967008.book.util.CompareByAuthors;
import a00967008.customer.Customer;
import a00967008.customer.util.CompareByJoinedDate;
import a00967008.database.BookDao;
import a00967008.database.CustomerDao;
import a00967008.database.Database;
import a00967008.database.PurchaseDao;
import a00967008.io.BookReader;
import a00967008.io.BookReport;
import a00967008.io.CustomerReader;
import a00967008.io.CustomerReport;
import a00967008.io.PurchaseReader;
import a00967008.io.PurchaseReport;
import a00967008.purchase.Purchase;
import a00967008.purchase.util.CompareByLastName;
import a00967008.purchase.util.CompareByTitle;
import a00967008.ui.MainFrame;

/**
 * Project: Book
 * File: BookStore.java
 * Date: October, 2017
 * Time: 1:22:25 PM
 */

/**
 * @author Alan, A00967008
 *
 */
public class BookStore {
	private static final String DB_PROPERTIES_FILENAME = "db.properties";
	private static final String CUSTOMERS_DATA_FILENAME = "customers.dat";
	private static final String BOOKS_DATA_FILENAME = "books500.csv";
	private static final String PURCHASE_DATA_FILENAME = "purchases.csv";

	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	static {
		configureLogging();
	}
	private static final Logger LOG = LogManager.getLogger();

	/**
	 * Bcmc Constructor. Processes the commandline arguments
	 * ex. -inventory -make=honda -by_count -desc -total -service
	 * 
	 * @throws ApplicationException
	 * @throws ParseException
	 */
	public BookStore(String[] args) throws ApplicationException, ParseException {
		LOG.info("Created Bcmc");

		BookOptions.process(args);
	}

	/**
	 * Entry point to GIS
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		LOG.info("Starting Books");
		LOG.info(startTime);

		// start the Book System
		try {
			BookStore book = new BookStore(args);
			if (BookOptions.isHelpOptionSet()) {
				BookOptions.Value[] values = BookOptions.Value.values();
				System.out.format("%-5s %-15s %-10s %s%n", "Option", "Long Option", "Has Value", "Description");
				for (BookOptions.Value value : values) {
					System.out.format("-%-5s %-15s %-10s %s%n", value.getOption(), ("-" + value.getLongOption()), value.isHasArg(),
							value.getDescription());
				}

				return;
			}

			book.run();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug(e.getMessage());
		} finally {
			Instant endTime = Instant.now();
			LOG.info(endTime);
			LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
		}
	}

	/**
	 * Configures log4j2 from the external configuration file specified in LOG4J_CONFIG_FILENAME.
	 * If the configuration file isn't found then log4j2's DefaultConfiguration is used.
	 */
	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format("WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
					LOG4J_CONFIG_FILENAME));
			Configurator.initialize(new DefaultConfiguration());
		}
	}

	/**
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 * @throws a00967008.customer.ApplicationException
	 * 
	 */
	private void run() throws ApplicationException, FileNotFoundException, a00967008.customer.ApplicationException {
		LOG.info("run()");

		try {
			LOG.info("Connecting to database");
			Database db = connect();
			LOG.info("Loading customers");
			CustomerDao customerDao = loadCustomers(db);
			LOG.info("Loading books");
			BookDao bookDao = new BookDao(db);
			LOG.info("Loading purchases");
			PurchaseDao purchaseDao = new PurchaseDao(db);

			// new CustomerDaoTester(customerDao).test();

			createUI(bookDao, customerDao, purchaseDao);
			// generateReports(customerDao, bookDao, purchaseDao);

		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Method to create UI
	 * 
	 * @param bookDao
	 *            - the bookDao to set
	 * @param customerDao
	 *            - the customerDao to set
	 * @param purchaseDao
	 *            - the purchaseDao to set
	 */
	public static void createUI(BookDao bookDao, CustomerDao customerDao, PurchaseDao purchaseDao) {
		LOG.info("Creating user interface");
		// set the Nimbus look and feel...
		// create and show the user interface
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				LOG.info("Setting Nimbus");
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}

					LOG.info("Creating main frame");
					MainFrame mainFrame = new MainFrame(bookDao, customerDao, purchaseDao);
					mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Load the customers
	 * 
	 * @param db
	 *            - database
	 * @throws IOException
	 * @throws SQLException
	 * @throws ApplicationException
	 * @throws a00967008.customer.ApplicationException
	 * @throws FileNotFoundException
	 */
	private CustomerDao loadCustomers(Database db) throws ApplicationException, FileNotFoundException, a00967008.customer.ApplicationException {
		CustomerDao customerDao = new CustomerDao(db);
		return customerDao;
	}

	/**
	 * Connection method
	 * 
	 * @return the database
	 * @throws IOException
	 * @throws SQLException
	 * @throws ApplicationException
	 */
	private Database connect() throws IOException, SQLException, ApplicationException {
		Properties dbProperties = new Properties();
		dbProperties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
		Database db = new Database(dbProperties);

		return db;
	}

	/**
	 * Generate the reports from the input data
	 * 
	 * @throws FileNotFoundException
	 * @throws a00967008.customer.ApplicationException
	 */
	@SuppressWarnings("unused")
	private void generateReports(CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao)
			throws FileNotFoundException, a00967008.customer.ApplicationException {
		LOG.info("generating the reports");

		CustomerReader customerRead = new CustomerReader();
		LinkedList<Customer> list = customerRead.createCustomerList(new File(CUSTOMERS_DATA_FILENAME), customerDao);
		BookReader bookRead = new BookReader();
		ArrayList<Book> bookList = bookRead.readBookList(new File(BOOKS_DATA_FILENAME), bookDao);
		PurchaseReader purchaseRead = new PurchaseReader();
		ArrayList<Purchase> purchaseList = purchaseRead.readPurchaseList(new File(PURCHASE_DATA_FILENAME), purchaseDao);

		if (BookOptions.isCustomersOptionSet()) {
			LOG.info("generating the customer report");
			// for program args: -c -J -d
			System.out.println("Customer Report: " + BookOptions.isCustomersOptionSet());
			System.out.println("Customer Join Date: " + BookOptions.isByJoinDateOptionSet());
			System.out.println("Customer Join Date DESC: " + BookOptions.isDescendingOptionSet());

			if (BookOptions.isByJoinDateOptionSet()) {
				LOG.info("Sorting customers by join date");
				if (BookOptions.isDescendingOptionSet()) {
					Collections.sort(list, Collections.reverseOrder(new CompareByJoinedDate()));
				} else {
					Collections.sort(list, new CompareByJoinedDate());
				}
				LOG.info("Sorted customers by join date");
			}

			CustomerReport report = new CustomerReport();
			report.writeOutput(list);
			LOG.info("Customer report generated");

		}

		if (BookOptions.isBooksOptionSet()) {
			LOG.info("generating the book report");
			System.out.println("Book Report: " + BookOptions.isBooksOptionSet());
			System.out.println("Book Author: " + BookOptions.isByAuthorOptionSet());
			System.out.println("Book Author DESC: " + BookOptions.isDescendingOptionSet());

			if (BookOptions.isByAuthorOptionSet()) {
				LOG.info("Sorting books by author");
				if (BookOptions.isDescendingOptionSet()) {
					Collections.sort(bookList, Collections.reverseOrder(new CompareByAuthors()));
				} else {
					Collections.sort(bookList, new CompareByAuthors());
				}
				LOG.info("Sorted books by author");
			}

			BookReport bookReport = new BookReport();
			bookReport.writeBookReport(bookList);
			LOG.info("Book report generated");
		}

		if (BookOptions.isPurchasesOptionSet()) {
			LOG.info("generating the inventory report");
			System.out.println("Purchase Report: " + BookOptions.isPurchasesOptionSet());
			System.out.println("Purchase Last Name: " + BookOptions.isByLastnameOptionSet());
			System.out.println("Purchase Title: " + BookOptions.isByTitleOptionSet());
			System.out.println("Purchase DESC: " + BookOptions.isDescendingOptionSet());

			if (BookOptions.isByLastnameOptionSet()) {
				LOG.info("Sorting purchases by last name");
				if (BookOptions.isDescendingOptionSet()) {
					Collections.sort(purchaseList, Collections.reverseOrder(new CompareByLastName(list)));
				} else {
					Collections.sort(purchaseList, new CompareByLastName(list));
				}
				LOG.info("Sorted purchases by last name");
			}

			if (BookOptions.isByTitleOptionSet()) {
				LOG.info("Sorting purchases by title");
				if (BookOptions.isDescendingOptionSet()) {
					Collections.sort(purchaseList, Collections.reverseOrder(new CompareByTitle(bookList)));
				} else {
					Collections.sort(purchaseList, new CompareByTitle(bookList));
				}
				LOG.info("Sorted purchases by title");
			}

			PurchaseReport purchaseReport = new PurchaseReport();
			purchaseReport.writePurchaseReport(list, bookList, purchaseList);
			LOG.info("Purchase report generated");
		}
		LOG.info("Books has ended");
	}
}
