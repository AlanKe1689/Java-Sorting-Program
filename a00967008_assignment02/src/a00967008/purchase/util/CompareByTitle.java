/**
 * Project: a00967008_assignment01
 * File: CompareByTitle.java
 * Date: May 31, 2018
 * Time: 4:30:54 PM
 */
package a00967008.purchase.util;

import java.util.ArrayList;
import java.util.Comparator;

import a00967008.book.Book;
import a00967008.purchase.Purchase;

/**
 * @author Alan, A00967008
 *
 */
public class CompareByTitle implements Comparator<Purchase> {
	private ArrayList<Book> bookList;
	private String title;
	private String otherTitle;

	private final int ZERO = 0;

	/**
	 * Main constructor
	 * 
	 * @param bookList
	 *            - the book list to compare
	 */
	public CompareByTitle(ArrayList<Book> bookList) {
		this.bookList = bookList;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Purchase o1, Purchase o2) {

		for (int j = ZERO; j < bookList.size(); j++) {
			if (o1.getBookID().equals(bookList.get(j).getBookID())) {
				title = bookList.get(j).getOriginalTitle();
			}
		}

		for (int j = ZERO; j < bookList.size(); j++) {
			if (o2.getBookID().equals(bookList.get(j).getBookID())) {
				otherTitle = bookList.get(j).getOriginalTitle();
			}
		}
		return title.compareTo(otherTitle);
	}
}
