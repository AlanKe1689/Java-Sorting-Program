/**
 * Project: a00967008_assignment01
 * File: Validator.java
 * Date: May 2, 2018
 * Time: 7:03:34 PM
 */
package a00967008.customer.util;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a00967008.customer.ApplicationException;

/**
 * @author Alan, A00967008
 *
 */
public class Validator {
	private static final int INDEX_ZERO = 0;
	private static final int INDEX_FOUR = 4;
	private static final int INDEX_SIX = 6;
	private static final int INDEX_EIGHT = 8;
	private static final int JANUARY = 1;
	private static final int MARCH = 3;
	private static final int APRIL = 4;
	private static final int MAY = 5;
	private static final int JUNE = 6;
	private static final int JULY = 7;
	private static final int AUGUST = 8;
	private static final int SEPTEMBER = 9;
	private static final int OCTOBER = 10;
	private static final int NOVEMBER = 11;
	private static final int DECEMBER = 12;
	private static final int DAY_ONE = 1;
	private static final int DAY_TWENTY_EIGHT = 28;
	private static final int DAY_THIRTY = 30;
	private static final int DAY_THIRTY_ONE = 31;
	private static final int MIN_YEAR = 1900;
	private static final int MAX_YEAR = LocalDate.now().getYear();

	private static final Logger LOG = LogManager.getLogger(Validator.class);

	/**
	 * Method to validate postal code
	 * 
	 * @param postalCode
	 *            - the postal code to validate
	 * @return the postal code
	 * @throws ApplicationException
	 */
	public static String validatePostalCode(String postalCode) throws ApplicationException {
		LOG.info("Validating postal code");
		if (postalCode.matches("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$")) {
			return postalCode;
		} else {
			throw new ApplicationException("Invalid postal code: " + postalCode);
		}
	}

	/**
	 * Method to validate phone number
	 * 
	 * @param phone
	 *            - the phone number to validate
	 * @return the phone number
	 * @throws ApplicationException
	 */
	public static String validatePhone(String phone) throws ApplicationException {
		LOG.info("Validating phone number");
		if (phone.matches("^([(][0-9][0-9][0-9][)])\\s([0-9][0-9][0-9][-][0-9][0-9][0-9][0-9])$")) {
			return phone;
		} else {
			throw new ApplicationException("Invalid phone number: " + phone);
		}
	}

	/**
	 * Method to validate email address
	 * 
	 * @param emailAddress
	 *            - the email to validate
	 * @return the email
	 * @throws ApplicationException
	 */
	public static String validateEmail(String emailAddress) throws ApplicationException {
		LOG.info("Validating email address");
		if (emailAddress.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			return emailAddress;
		} else {
			throw new ApplicationException("Invalid email: " + emailAddress);
		}
	}

	/**
	 * Method to validate join date
	 * 
	 * @param joinDate
	 *            - the join date to validate
	 * @return the join date
	 * @throws ApplicationException
	 */
	public static String validateJoinDate(String joinDate) throws ApplicationException {
		if (joinDate.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
			int year = Integer.parseInt(joinDate.substring(INDEX_ZERO, INDEX_FOUR));
			int month = Integer.parseInt(joinDate.substring(INDEX_FOUR, INDEX_SIX));
			int day = Integer.parseInt(joinDate.substring(INDEX_SIX, INDEX_EIGHT));

			if (year < MIN_YEAR || year > MAX_YEAR) {
				throw new ApplicationException("Invalid value for Year (valid values 1900 - 2018): " + year);
			}

			if (month < JANUARY || month > DECEMBER) {
				throw new ApplicationException("Invalid value for Month (valid values 1 - 12): " + month);
			}

			if (month == JANUARY || month == MARCH || month == MAY || month == JULY || month == AUGUST || month == OCTOBER || month == DECEMBER) {
				if (day < DAY_ONE || day > DAY_THIRTY_ONE) {
					throw new ApplicationException("Invalid value for DayOfMonth (valid values 1 - 31): " + day);
				}
			} else if (month == APRIL || month == JUNE || month == SEPTEMBER || month == NOVEMBER) {
				if (day < DAY_ONE || day > DAY_THIRTY) {
					throw new ApplicationException("Invalid value for DayOfMonth (valid values 1 - 30): " + day);
				}
			} else {
				if (day < DAY_ONE || day > DAY_TWENTY_EIGHT) {
					throw new ApplicationException("Invalid value for DayOfMonth (valid values 1 - 28): " + day);
				}
			}
			return joinDate;
		} else {
			throw new ApplicationException("Invalid date format: " + joinDate);
		}
	}
}
