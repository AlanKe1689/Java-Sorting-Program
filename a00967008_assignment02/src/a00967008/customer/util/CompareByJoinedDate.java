/**
 * Project: a00967008_assignment01
 * File: CompareByJoinedDate.java
 * Date: May 15, 2018
 * Time: 5:14:35 PM
 */
package a00967008.customer.util;

import java.util.Comparator;

import a00967008.customer.Customer;

/**
 * @author Alan, A00967008
 *
 */
public class CompareByJoinedDate implements Comparator<Customer> {
	/**
	 * Default constructor
	 */
	public CompareByJoinedDate() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Customer o1, Customer o2) {
		return o1.getJoinDate().compareTo(o2.getJoinDate());
	}
}
