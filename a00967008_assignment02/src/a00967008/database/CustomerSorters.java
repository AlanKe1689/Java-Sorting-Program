/**
 * Project: a00967008_lab09
 * File: Lab9.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.database;

import java.util.Comparator;

import a00967008.customer.Customer;

/**
 * @author Alan, A00967008
 *
 */
public class CustomerSorters {

	/**
	 * 
	 * @author Alan, A00967008
	 *
	 */
	public static class CompareByJoinedDate implements Comparator<Customer> {
		@Override
		public int compare(Customer customer1, Customer customer2) {
			return customer1.getJoinDate().compareTo(customer2.getJoinDate());
		}
	}
}
