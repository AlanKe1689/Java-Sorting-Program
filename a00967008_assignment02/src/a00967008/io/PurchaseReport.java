/**
 * Project: a00967008_assignment01
 * File: PurchaseReport.java
 * Date: May 31, 2018
 * Time: 1:26:40 AM
 */
package a00967008.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import a00967008.book.Book;
import a00967008.book.BookOptions;
import a00967008.customer.ApplicationException;
import a00967008.customer.Customer;
import a00967008.purchase.Purchase;

/**
 * @author Alan, A00967008
 *
 */
public class PurchaseReport {
	private double value;
	private final int ZERO = 0;
	private final double ROUND = 100.0;
	private final int TABLE_LENGTH = 113;
	private final String OUTPUT_FILE = "purchases_report.txt";
	public static final String HEADER_FORMAT = "%-24s %-80s %2s";
	public static final String PURCHASE_FORMAT = "%-24s %-80s $%.2f";

	/**
	 * Main constructor
	 */
	public PurchaseReport() {

	}

	/**
	 * Accessor for total value
	 * 
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Mutator for total value
	 * 
	 * @param value
	 *            - the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Method to write purchase report
	 * 
	 * @param customerList
	 *            - list of customers
	 * @param bookList
	 *            - list of books
	 * @param purchaseList
	 *            - list of purchases
	 * @throws ApplicationException
	 */
	public void writePurchaseReport(LinkedList<Customer> customerList, ArrayList<Book> bookList, ArrayList<Purchase> purchaseList)
			throws ApplicationException {
		PrintWriter output = null;
		String name = null;
		String title = null;

		try {
			output = new PrintWriter(new FileWriter(OUTPUT_FILE));

			System.out.println("");
			output.println("");
			System.out.println("Purchases Report");
			output.println("Purchases Report");

			for (int i = ZERO; i < TABLE_LENGTH; i++) {
				System.out.print("-");
				output.print("-");
			}
			System.out.println("");
			output.println("");

			System.out.format(HEADER_FORMAT, "Name", "Title", "Price");
			System.out.println("");
			output.println(String.format(HEADER_FORMAT, "Name", "Title", "Price"));

			for (int i = ZERO; i < TABLE_LENGTH; i++) {
				System.out.print("-");
				output.print("-");
			}
			System.out.println("");
			output.println("");

			if (BookOptions.getCustomerId() != null) {
				for (int i = ZERO; i < purchaseList.size(); i++) {
					if (purchaseList.get(i).getCustomerID().equals(BookOptions.getCustomerId())) {
						for (int j = ZERO; j < customerList.size(); j++) {

							if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
								name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
							}
						}

						for (int j = ZERO; j < bookList.size(); j++) {
							if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
								title = bookList.get(j).getOriginalTitle();
							}
						}
						System.out.format(PURCHASE_FORMAT, name, title, Double.parseDouble(purchaseList.get(i).getPrice()));
						System.out.println("");
						output.println(String.format(PURCHASE_FORMAT, name, title, Double.parseDouble(purchaseList.get(i).getPrice())));
						value += Double.parseDouble(purchaseList.get(i).getPrice());
					}
				}
			} else {
				for (int i = ZERO; i < purchaseList.size(); i++) {
					for (int j = ZERO; j < customerList.size(); j++) {
						if (purchaseList.get(i).getCustomerID().equals(customerList.get(j).getCustomerID())) {
							name = customerList.get(j).getFirstName() + " " + customerList.get(j).getLastName();
						}
					}

					for (int j = ZERO; j < bookList.size(); j++) {
						if (purchaseList.get(i).getBookID().equals(bookList.get(j).getBookID())) {
							title = bookList.get(j).getOriginalTitle();
						}
					}

					System.out.format(PURCHASE_FORMAT, name, title, Double.parseDouble(purchaseList.get(i).getPrice()));
					System.out.println("");
					output.println(String.format(PURCHASE_FORMAT, name, title, Double.parseDouble(purchaseList.get(i).getPrice())));
					value += Double.parseDouble(purchaseList.get(i).getPrice());
				}
			}

			if (BookOptions.isTotalOptionSet()) {
				System.out.println("");
				output.println("");
				System.out.println("Value of purchases: $" + Math.round(value * ROUND) / ROUND);
				output.println("Value of purchases: $" + Math.round(value * ROUND) / ROUND);
			}
			System.out.println("");
			output.close();
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}
