/**
 * Project: a00967008_assignment01
 * File: CompareByLastName.java
 * Date: May 31, 2018
 * Time: 5:34:11 AM
 */
package a00967008.purchase.util;

import java.util.Comparator;
import java.util.LinkedList;

import a00967008.customer.Customer;
import a00967008.purchase.Purchase;

/**
 * @author Alan, A00967008
 *
 */
public class CompareByLastName implements Comparator<Purchase> {
	private String lastName;
	private String otherLastName;
	private LinkedList<Customer> customerList;
	private final int ZERO = 0;

	/**
	 * Main constructor
	 */
	public CompareByLastName(LinkedList<Customer> customerList) {
		this.customerList = customerList;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Purchase o1, Purchase o2) {
		for (int j = ZERO; j < customerList.size(); j++) {
			if (o1.getCustomerID().equals(customerList.get(j).getCustomerID())) {
				lastName = customerList.get(j).getLastName();
			}
		}

		for (int j = ZERO; j < customerList.size(); j++) {
			if (o2.getCustomerID().equals(customerList.get(j).getCustomerID())) {
				otherLastName = customerList.get(j).getLastName();
			}
		}
		return lastName.compareTo(otherLastName);
	}
}
