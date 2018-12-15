/**
 * Project: a00967008_lab09
 * File: Lab9.java
 * Date: May 6, 2018
 * Time: 6:21:28 PM
 */
package a00967008.database;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.customer.Customer;

/**
 * @author Alan, A00967008
 *
 */
public class CustomerDaoTester {

	private static Logger LOG = LogManager.getLogger();
	private CustomerDao customerDao;

	/**
	 * Main constructor
	 * 
	 * @param customerDao
	 *            - customerDao to set
	 */
	public CustomerDaoTester(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	/**
	 * Method to test customerDao
	 */
	public void test() {
		try {
			LOG.info("Getting the customer IDs");
			List<Long> ids = customerDao.getCustomerIds();
			LOG.info("Customer IDs: " + Arrays.toString(ids.toArray()));
			for (Long id : ids) {
				LOG.info(id);
				Customer customer = customerDao.getCustomer(id);
				LOG.info(customer);
			}
			long count = customerDao.countAllCustomers();
			LOG.info(count);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
