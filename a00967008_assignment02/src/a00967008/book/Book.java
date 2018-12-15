/**
 * Project: a00967008_assignment01
 * File: Book.java
 * Date: May 30, 2018
 * Time: 4:27:16 AM
 */
package a00967008.book;

/**
 * @author Alan, A00967008
 *
 */
public class Book {
	private String bookID;
	private String isbn;
	private String authors;
	private String originalPublicationYear;
	private String originalTitle;
	private String averageRating;
	private String ratingsCount;
	private String imageUrl;

	/**
	 * @author Alan, A00967008
	 *
	 */
	public static class Builder {
		private String bookID;
		private String isbn;
		private String authors;
		private String originalPublicationYear;
		private String originalTitle;
		private String averageRating;
		private String ratingsCount;
		private String imageUrl;

		/**
		 * Builder constructor
		 * 
		 * @param bookID
		 *            - the book ID to set
		 * @param isbn
		 *            - the ISBN to set
		 */
		public Builder(String bookID, String isbn) {
			this.bookID = bookID;
			this.isbn = isbn;
		}

		/**
		 * Mutator for book ID
		 * 
		 * @param bookID
		 *            - the bookID to set
		 */
		public Builder setBookID(String bookID) {
			this.bookID = bookID;
			return this;
		}

		/**
		 * Mutator for ISBN
		 * 
		 * @param isbn
		 *            - the isbn to set
		 */
		public Builder setIsbn(String isbn) {
			this.isbn = isbn;
			return this;
		}

		/**
		 * Mutator for author
		 * 
		 * @param authors
		 *            - the authors to set
		 */
		public Builder setAuthors(String authors) {
			this.authors = authors;
			return this;
		}

		/**
		 * Mutator for year
		 * 
		 * @param originalPublicationYear
		 *            - the originalPublicationYear to set
		 */
		public Builder setOriginalPublicationYear(String originalPublicationYear) {
			this.originalPublicationYear = originalPublicationYear;
			return this;
		}

		/**
		 * Mutator for title
		 * 
		 * @param originalTitle
		 *            - the originalTitle to set
		 */
		public Builder setOriginalTitle(String originalTitle) {
			this.originalTitle = originalTitle;
			return this;
		}

		/**
		 * Mutator for rating
		 * 
		 * @param averageRating
		 *            - the averageRating to set
		 */
		public Builder setAverageRating(String averageRating) {
			this.averageRating = averageRating;
			return this;
		}

		/**
		 * Mutator for rating count
		 * 
		 * @param ratingsCount
		 *            - the ratingsCount to set
		 */
		public Builder setRatingsCount(String ratingsCount) {
			this.ratingsCount = ratingsCount;
			return this;
		}

		/**
		 * Mutator for image URL
		 * 
		 * @param imageUrl
		 *            - the imageUrl to set
		 */
		public Builder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		/**
		 * Build method for book
		 * 
		 * @return the book
		 */
		public Book build() {
			return new Book(this);
		}
	}

	/**
	 * Main constructor
	 * 
	 * @param builder
	 *            - the builder
	 */
	public Book(Builder builder) {
		bookID = builder.bookID;
		isbn = builder.isbn;
		authors = builder.authors;
		originalPublicationYear = builder.originalPublicationYear;
		originalTitle = builder.originalTitle;
		averageRating = builder.averageRating;
		ratingsCount = builder.ratingsCount;
		imageUrl = builder.imageUrl;
	}

	/**
	 * Accessor for book ID
	 * 
	 * @return the bookID
	 */
	public String getBookID() {
		return bookID;
	}

	/**
	 * Accessor for ISBN
	 * 
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * Accessor for authors
	 * 
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * Accessor for year
	 * 
	 * @return the originalPublicationYear
	 */
	public String getOriginalPublicationYear() {
		return originalPublicationYear;
	}

	/**
	 * Accessor for title
	 * 
	 * @return the originalTitle
	 */
	public String getOriginalTitle() {
		return originalTitle;
	}

	/**
	 * Accessor for rating
	 * 
	 * @return the averageRating
	 */
	public String getAverageRating() {
		return averageRating;
	}

	/**
	 * Accessor for ratings count
	 * 
	 * @return the ratingsCount
	 */
	public String getRatingsCount() {
		return ratingsCount;
	}

	/**
	 * Accessor for image URL
	 * 
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Book [bookID=" + bookID + ", isbn=" + isbn + ", authors=" + authors + ", originalPublicationYear=" + originalPublicationYear
				+ ", originalTitle=" + originalTitle + ", averageRating=" + averageRating + ", ratingsCount=" + ratingsCount + ", imageUrl="
				+ imageUrl + "]";
	}
}
