/**
 * Project: a00967008_assignment01
 * File: CustomerReader.java
 * Date: May 2, 2018
 * Time: 6:37:10 PM
 */
package a00967008.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.customer.ApplicationException;
import a00967008.customer.Customer;
import a00967008.customer.util.Validator;
import a00967008.database.CustomerDao;

/**
 * @author Alan, A00967008
 *
 */
public class CustomerReader {
	private LinkedList<Customer> customerList;

	private final static String CUSTOMER_FILE = "customers.dat";

	private final int ZERO = 0;
	private final int INDEX_ONE = 1;
	private final int INDEX_TWO = 2;
	private final int INDEX_THREE = 3;
	private final int INDEX_FOUR = 4;
	private final int INDEX_FIVE = 5;
	private final int INDEX_SIX = 6;
	private final int INDEX_SEVEN = 7;
	private final int INDEX_EIGHT = 8;
	private final int INFO_ARRAY_SIZE = 9;
	private final String SPLIT = "\\|";

	private static final Logger LOG = LogManager.getLogger(CustomerReader.class);

	/**
	 * Main constructor
	 */
	public CustomerReader() {

	}

	/**
	 * Accessor for customer list
	 * 
	 * @return the customerList
	 */
	public LinkedList<Customer> getCustomerList() {
		return customerList;
	}

	/**
	 * Mutator for customer list
	 * 
	 * @param customerList
	 *            - the customerList to set
	 */
	public void setInput(LinkedList<Customer> customerList) {
		this.customerList = customerList;
	}

	/**
	 * Method to create customer array
	 * 
	 * @return the customer list
	 * 
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public LinkedList<Customer> createCustomerList(File customerFile, CustomerDao dao) throws ApplicationException, FileNotFoundException {
		ArrayList<String> input = reader(customerFile);
		String[] inputArray = input.toArray(new String[INDEX_FIVE]);
		LocalDate joinDate = null;
		String date = null;
		customerList = new LinkedList<>();
		String[] infoArray = new String[INFO_ARRAY_SIZE];

		for (int i = ZERO; i < input.size(); i++) {
			LOG.info("Splitting customer information");
			infoArray = inputArray[i].split(SPLIT);
			LOG.info("Customer information: " + Arrays.toString(infoArray));

			LOG.info("Checking number of elements in array");
			if (infoArray.length != INFO_ARRAY_SIZE) {
				throw new ApplicationException("Expected 9 but got " + infoArray.length + ": " + Arrays.toString(infoArray));
			}

			date = Validator.validateJoinDate(infoArray[INDEX_EIGHT]);
			LOG.info("Validating date");
			int year = Integer.parseInt(date.substring(ZERO, INDEX_FOUR));
			int month = Integer.parseInt(date.substring(INDEX_FOUR, INDEX_SIX));
			int day = Integer.parseInt(date.substring(INDEX_SIX, INDEX_EIGHT));
			joinDate = LocalDate.of(year, month, day);

			LOG.info("Adding customer to list");
			Customer customer = new Customer.Builder(infoArray[ZERO], infoArray[INDEX_SIX]).setFirstName(infoArray[INDEX_ONE])
					.setLastName(infoArray[INDEX_TWO]).setStreetName(infoArray[INDEX_THREE]).setCity(infoArray[INDEX_FOUR])
					.setPostalCode(infoArray[INDEX_FIVE]).setEmailAddress(Validator.validateEmail(infoArray[INDEX_SEVEN])).setJoinDate(joinDate)
					.build();
			customerList.add(customer);

			try {
				dao.add(customer);
			} catch (SQLException e) {
				throw new ApplicationException(e);
			}
			LOG.info("Customer added to list");
		}
		return customerList;
	}

	/**
	 * Method to read from file
	 * 
	 * @return input from file
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String> reader(File customerFile) throws ApplicationException, FileNotFoundException {
		BufferedReader inputStream = null;
		ArrayList<String> inputArray = new ArrayList<>();

		try {
			LOG.info("Reading customer data from file");
			inputStream = new BufferedReader(new FileReader(customerFile));
			@SuppressWarnings("unused")
			String titleline = inputStream.readLine();
			String line = null;

			while ((line = inputStream.readLine()) != null) {
				inputArray.add(line);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + CUSTOMER_FILE);
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return inputArray;
	}
}
