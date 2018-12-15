/**
 * Project: a00967008_assignment01
 * File: Purchase.java
 * Date: May 30, 2018
 * Time: 5:17:01 AM
 */
package a00967008.purchase;

/**
 * @author Alan, A00967008
 *
 */
public class Purchase {
	private String purchaseID;
	private String customerID;
	private String bookID;
	private String price;

	/**
	 * @author Alan, A00967008
	 *
	 */
	public static class Builder {
		private String purchaseID;
		private String customerID;
		private String bookID;
		private String price;

		/**
		 * Builder constructor
		 * 
		 * @param purchaseID
		 *            - the purchase ID to set
		 * @param customerID
		 *            - the customer ID to set
		 * @param bookID
		 *            - the book ID to set
		 */
		public Builder(String purchaseID, String customerID, String bookID) {
			this.purchaseID = purchaseID;
			this.customerID = customerID;
			this.bookID = bookID;
		}

		/**
		 * Mutator for purchase ID
		 * 
		 * @param purchaseID
		 *            - the purchaseID to set
		 */
		public Builder setPurchaseID(String purchaseID) {
			this.purchaseID = purchaseID;
			return this;
		}

		/**
		 * Mutator for customer ID
		 * 
		 * @param customerID
		 *            - the customerID to set
		 */
		public Builder setCustomerID(String customerID) {
			this.customerID = customerID;
			return this;
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
		 * Mutator for price
		 * 
		 * @param price
		 *            - the price to set
		 */
		public Builder setPrice(String price) {
			this.price = price;
			return this;
		}

		/**
		 * Build method for purchase
		 * 
		 * @return the purchase
		 */
		public Purchase build() {
			return new Purchase(this);
		}
	}

	/**
	 * Main constructor
	 * 
	 * @param builder
	 *            - the builder to set
	 */
	public Purchase(Builder builder) {
		purchaseID = builder.purchaseID;
		customerID = builder.customerID;
		bookID = builder.bookID;
		price = builder.price;
	}

	/**
	 * Accessor for purchase ID
	 * 
	 * @return the purchaseID
	 */
	public String getPurchaseID() {
		return purchaseID;
	}

	/**
	 * Accessor for customer ID
	 * 
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
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
	 * Accessor for price
	 * 
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Purchase [purchaseID=" + purchaseID + ", customerID=" + customerID + ", bookID=" + bookID + ", price=" + price + "]";
	}
}
