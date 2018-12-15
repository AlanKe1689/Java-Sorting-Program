/**
 * Project: a00967008_assignment01
 * File: CustomerReport.java
 * Date: May 2, 2018
 * Time: 6:36:10 PM
 */
package a00967008.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import a00967008.customer.ApplicationException;
import a00967008.customer.Customer;

/**
 * @author Alan, A00967008
 *
 */
public class CustomerReport {
	private LinkedList<Customer> customerList;

	private final int ZERO = 0;
	private final int INITIAL_COUNTER = 1;
	private final int TABLE_LENGTH = 187;
	private final String OUTPUT_FILE = "customers_report.txt";
	public static final String HEADER_FORMAT = "%4s %-6s %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-12s";
	public static final String CUSTOMER_FORMAT = "%3s. %-6s %-12s %-12s %-40s %-25s %-12s %-15s %-40s %-2tb %-2td %-2tY";

	/**
	 * Main constructor
	 */
	public CustomerReport() {

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
	public void setCustomerList(LinkedList<Customer> customerList) {
		this.customerList = customerList;
	}

	/**
	 * Method to write output
	 * 
	 * @param customerList
	 *            - the customer list to write
	 * @throws ApplicationException
	 */
	public void writeOutput(LinkedList<Customer> customerList) throws ApplicationException {
		PrintWriter outputStream = null;

		try {
			outputStream = new PrintWriter(new FileWriter(OUTPUT_FILE));

			System.out.println("");
			outputStream.println("");
			System.out.println("Customer Report");
			outputStream.println("Customer Report");

			for (int i = ZERO; i < TABLE_LENGTH; i++) {
				System.out.print("-");
				outputStream.print("-");
			}
			System.out.println("");
			outputStream.println("");

			System.out.format(HEADER_FORMAT, "#.", "ID", "First name", "Last name", "Street", "City", "Postal Code", "Phone", "Email", "Join Date");
			System.out.println("");
			outputStream.println(String.format(HEADER_FORMAT, "#.", "ID", "First name", "Last name", "Street", "City", "Postal Code", "Phone",
					"Email", "Join Date"));

			for (int i = ZERO; i < TABLE_LENGTH; i++) {
				System.out.print("-");
				outputStream.print("-");
			}
			System.out.println("");
			outputStream.println("");
			int counter = INITIAL_COUNTER;

			for (int i = ZERO; i < customerList.size(); i++) {

				System.out.format(CUSTOMER_FORMAT, Integer.toString(counter), customerList.get(i).getCustomerID(), customerList.get(i).getFirstName(),
						customerList.get(i).getLastName(), customerList.get(i).getStreetName(), customerList.get(i).getCity(),
						customerList.get(i).getPostalCode(), customerList.get(i).getPhone(), customerList.get(i).getEmailAddress(),
						customerList.get(i).getJoinDate(), customerList.get(i).getJoinDate(), customerList.get(i).getJoinDate());
				System.out.println("");
				outputStream.println(String.format(CUSTOMER_FORMAT, Integer.toString(counter), customerList.get(i).getCustomerID(),
						customerList.get(i).getFirstName(), customerList.get(i).getLastName(), customerList.get(i).getStreetName(),
						customerList.get(i).getCity(), customerList.get(i).getPostalCode(), customerList.get(i).getPhone(),
						customerList.get(i).getEmailAddress(), customerList.get(i).getJoinDate(), customerList.get(i).getJoinDate(),
						customerList.get(i).getJoinDate()));
				counter++;
			}
			System.out.println("");
			outputStream.close();
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}
