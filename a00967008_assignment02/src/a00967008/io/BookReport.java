/**
 * Project: a00967008_assignment01
 * File: BookReport.java
 * Date: May 30, 2018
 * Time: 11:49:22 PM
 */
package a00967008.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import a00967008.book.Book;
import a00967008.customer.ApplicationException;

/**
 * @author Alan, A00967008
 *
 */
public class BookReport {
	private final int ZERO = 0;
	private final int TABLE_LENGTH = 189;
	private final int LENGTH_CUTOFF = 37;
	private final int MAX_LENGTH = 40;
	private final String OUTPUT_FILE = "book_report.txt";
	public static final String HEADER_FORMAT = "%-8s %-12s %-40s %-40s %-4s %-6s %-13s %-60s";
	public static final String BOOK_FORMAT = "%08d %-12s %-40s %-40s %4d %6.3f %13d %-60s";

	/**
	 * Main constructor
	 */
	public BookReport() {

	}

	/**
	 * Method to write book report
	 * 
	 * @param bookList
	 *            - the book list to report
	 * @throws ApplicationException
	 */
	public void writeBookReport(ArrayList<Book> bookList) throws ApplicationException {
		PrintWriter output = null;

		try {
			output = new PrintWriter(new FileWriter(OUTPUT_FILE));

			System.out.println("");
			output.println("");
			System.out.println("Books Report");
			output.println("Books Report");

			for (int i = ZERO; i < TABLE_LENGTH; i++) {
				System.out.print("-");
				output.print("-");
			}
			System.out.println("");
			output.println("");

			System.out.format(HEADER_FORMAT, "ID", "ISBN", "Authors", "Title", "Year", "Rating", "Ratings Count", "Image URL");
			output.println(String.format(HEADER_FORMAT, "ID", "ISBN", "Authors", "Title", "Year", "Rating", "Ratings Count", "Image URL"));
			System.out.println("");

			for (int i = ZERO; i < TABLE_LENGTH; i++) {
				System.out.print("-");
				output.print("-");
			}
			System.out.println("");
			output.println("");

			String authors = null;
			String title = null;
			String imageUrl = null;

			for (int i = ZERO; i < bookList.size(); i++) {

				if (bookList.get(i).getAuthors().length() > MAX_LENGTH) {
					authors = bookList.get(i).getAuthors().trim().substring(ZERO, LENGTH_CUTOFF) + "...";
				} else {
					authors = bookList.get(i).getAuthors().trim();
				}

				if (bookList.get(i).getOriginalTitle().length() > MAX_LENGTH) {
					title = bookList.get(i).getOriginalTitle().trim().substring(ZERO, LENGTH_CUTOFF) + "...";
				} else {
					title = bookList.get(i).getOriginalTitle().trim();
				}

				if (bookList.get(i).getImageUrl().length() > 60) {
					imageUrl = bookList.get(i).getImageUrl().trim().substring(ZERO, 57) + "...";
				} else {
					imageUrl = bookList.get(i).getImageUrl().trim();
				}

				System.out.format(BOOK_FORMAT, Integer.parseInt(bookList.get(i).getBookID()), bookList.get(i).getIsbn(), authors, title,
						Integer.parseInt(bookList.get(i).getOriginalPublicationYear()), Double.parseDouble(bookList.get(i).getAverageRating()),
						Integer.parseInt(bookList.get(i).getRatingsCount()), imageUrl);
				System.out.println("");
				output.println(String.format(BOOK_FORMAT, Integer.parseInt(bookList.get(i).getBookID()), bookList.get(i).getIsbn(), authors, title,
						Integer.parseInt(bookList.get(i).getOriginalPublicationYear()), Double.parseDouble(bookList.get(i).getAverageRating()),
						Integer.parseInt(bookList.get(i).getRatingsCount()), imageUrl));
			}
			System.out.println("");
			output.close();
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		}
	}
}
