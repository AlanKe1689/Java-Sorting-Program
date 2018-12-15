/**
 * Project: Bcmc
 * File: ApplicationException.java
 * Date: Aug 18, 2016
 * Time: 1:59:25 PM
 */

package a00967008.book;

/**
 * @author Alan, A00967008
 *
 */
@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	/**
	 * Construct an ApplicationException
	 * 
	 * @param message
	 *            the exception message.
	 */
	public ApplicationException(String message) {
		super(message);
	}

	/**
	 * Construct an ApplicationException
	 * 
	 * @param throwable
	 *            a Throwable.
	 */
	public ApplicationException(Throwable throwable) {
		super(throwable);
	}
}
