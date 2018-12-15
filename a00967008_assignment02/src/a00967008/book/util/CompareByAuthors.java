/**
 * Project: a00967008_assignment01
 * File: CompareByAuthors.java
 * Date: May 31, 2018
 * Time: 5:23:15 AM
 */
package a00967008.book.util;

import java.util.Comparator;

import a00967008.book.Book;

/**
 * @author Alan, A00967008
 *
 */
public class CompareByAuthors implements Comparator<Book> {

	/**
	 * Main constructor
	 */
	public CompareByAuthors() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Book o1, Book o2) {
		return o1.getAuthors().compareTo(o2.getAuthors());
	}
}
