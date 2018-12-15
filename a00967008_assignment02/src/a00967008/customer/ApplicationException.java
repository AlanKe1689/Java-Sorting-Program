/**
 * Project: a00967008_assignment01
 * File: ApplicationException.java
 * Date: May 8, 2018
 * Time: 1:02:05 AM
 */
package a00967008.customer;

/**
 * @author Alan, A00967008
 *
 */
@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	/**
	 * Default constructor
	 */
	public ApplicationException() {
		super();
	}

	/**
	 * Second constructor
	 * 
	 * @param arg0
	 *            - argument
	 * @param arg1
	 *            - argument
	 * @param arg2
	 *            - argument
	 * @param arg3
	 *            - argument
	 */
	public ApplicationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	/**
	 * Third constructor
	 * 
	 * @param arg0
	 *            - argument
	 * @param arg1
	 *            - argument
	 */
	public ApplicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Fourth constructor
	 * 
	 * @param arg0
	 *            - argument
	 */
	public ApplicationException(String arg0) {
		super(arg0);
	}

	/**
	 * Fifth constructor
	 * 
	 * @param arg0
	 *            - argument
	 */
	public ApplicationException(Throwable arg0) {
		super(arg0);
	}
}
