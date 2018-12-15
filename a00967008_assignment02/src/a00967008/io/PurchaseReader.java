/**
 * Project: a00967008_assignment01
 * File: PurchaseReader.java
 * Date: May 31, 2018
 * Time: 12:43:39 AM
 */
package a00967008.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.customer.ApplicationException;
import a00967008.database.PurchaseDao;
import a00967008.purchase.Purchase;

/**
 * @author Alan, A00967008
 *
 */
public class PurchaseReader {
	private ArrayList<Purchase> purchaseList;
	private ArrayList<String> purchaseArray;

	private final int ZERO = 0;
	private final int INDEX_ONE = 1;
	private final int INDEX_TWO = 2;
	private final int INDEX_THREE = 3;
	private final String PURCHASE_FILE = "purchases.csv";
	private final String PURCHASE_ID = "id";
	private final String CUSTOMER_ID = "customer_id";
	private final String BOOK_ID = "book_id";
	private final String PRICE = "price";

	private static final Logger LOG = LogManager.getLogger(PurchaseReader.class);

	/**
	 * Main constructor
	 */
	public PurchaseReader() {

	}

	/**
	 * Accessor for purchase list
	 * 
	 * @return the purchaseList
	 */
	public ArrayList<Purchase> getPurchaseList() {
		return purchaseList;
	}

	/**
	 * Mutator for purchase list
	 * 
	 * @param purchaseList
	 *            - the purchaseList to set
	 */
	public void setPurchaseList(ArrayList<Purchase> purchaseList) {
		this.purchaseList = purchaseList;
	}

	/**
	 * Method to read purchase list
	 * 
	 * @return the purchase list
	 * @throws ApplicationException
	 */
	public ArrayList<Purchase> readPurchaseList(File purchaseFile, PurchaseDao dao) throws ApplicationException {
		purchaseList = new ArrayList<>();
		purchaseArray = new ArrayList<>();

		try {
			LOG.info("Reading purchase data from file");
			BufferedReader input = new BufferedReader(new FileReader(purchaseFile));
			CSVParser parse = new CSVParser(input, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			LOG.info("Creating purchase list");
			for (CSVRecord record : parse) {
				purchaseArray.add(record.get(PURCHASE_ID));
				purchaseArray.add(record.get(CUSTOMER_ID));
				purchaseArray.add(record.get(BOOK_ID));
				purchaseArray.add(record.get(PRICE));
				LOG.info("Purchase information: " + purchaseArray);

				Purchase purchase = new Purchase.Builder(purchaseArray.get(ZERO), purchaseArray.get(INDEX_ONE), purchaseArray.get(INDEX_TWO))
						.setPrice(purchaseArray.get(INDEX_THREE)).build();

				purchaseList.add(purchase);
				dao.add(purchase);

				purchaseArray = new ArrayList<>();
			}
			LOG.info("Purchase list created");
			parse.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find " + PURCHASE_FILE);
			throw new ApplicationException("Cannot find " + PURCHASE_FILE);
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		} catch (SQLException e) {
			throw new ApplicationException(e.getMessage());
		}
		return purchaseList;
	}
}
