/**
 * Project: a00967008_assignment01
 * File: Customer.java
 * Date: May 1, 2018
 * Time: 11:27:08 PM
 */
package a00967008.customer;

import java.time.LocalDate;

/**
 * @author Alan, A00967008
 *
 */
public class Customer {
	private String customerID;
	private String firstName;
	private String lastName;
	private String streetName;
	private String city;
	private String postalCode;
	private String phone;
	private String emailAddress;
	private LocalDate joinDate;

	/**
	 * @author Alan, A00967008
	 *
	 */
	public static class Builder {
		private String customerID;
		private String phone;

		private String firstName;
		private String lastName;
		private String streetName;
		private String city;
		private String postalCode;
		private String emailAddress;
		private LocalDate joinDate;

		/**
		 * Builder constructor
		 * 
		 * @param customerID
		 *            - the customerID to set
		 * @param phone
		 *            - the phone to set
		 */
		public Builder(String customerID, String phone) {
			this.customerID = customerID;
			this.phone = phone;
		}

		/**
		 * Mutator for customer ID
		 * 
		 * @param value
		 *            - the customerID to set
		 */
		public Builder setCustomerID(String value) {
			customerID = value;
			return this;
		}

		/**
		 * Mutator for first name
		 * 
		 * @param value
		 *            - the firstName to set
		 */
		public Builder setFirstName(String value) {
			firstName = value;
			return this;
		}

		/**
		 * Mutator for last name
		 * 
		 * @param value
		 *            - the lastName to set
		 */
		public Builder setLastName(String value) {
			lastName = value;
			return this;
		}

		/**
		 * Mutator for street name
		 * 
		 * @param value
		 *            - the streetName to set
		 */
		public Builder setStreetName(String value) {
			streetName = value;
			return this;
		}

		/**
		 * Mutator for city
		 * 
		 * @param value
		 *            - the city to set
		 */
		public Builder setCity(String value) {
			city = value;
			return this;
		}

		/**
		 * Mutator for postal code
		 * 
		 * @param value
		 *            - the postalCode to set
		 */
		public Builder setPostalCode(String value) {
			postalCode = value;
			return this;
		}

		/**
		 * Mutator for phone
		 * 
		 * @param value
		 *            - the phone to set
		 */
		public Builder setPhone(String value) {
			phone = value;
			return this;
		}

		/**
		 * Mutator for email address
		 * 
		 * @param value
		 *            - the emailAddress to set
		 */
		public Builder setEmailAddress(String value) {
			emailAddress = value;
			return this;
		}

		/**
		 * Mutator for join date
		 * 
		 * @param value
		 *            - the joinDate to set
		 */
		public Builder setJoinDate(LocalDate value) {
			joinDate = value;
			return this;
		}

		/**
		 * Build method for Customer
		 * 
		 * @return the customer
		 */
		public Customer build() {
			return new Customer(this);
		}
	}

	/**
	 * Main constructor
	 * 
	 * @param builder
	 *            - the builder to set
	 */
	public Customer(Builder builder) {
		customerID = builder.customerID;
		firstName = builder.firstName;
		lastName = builder.lastName;
		streetName = builder.streetName;
		city = builder.city;
		postalCode = builder.postalCode;
		phone = builder.phone;
		emailAddress = builder.emailAddress;
		joinDate = builder.joinDate;
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
	 * Accessor for first name
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Accessor for last name
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Accessor for street name
	 * 
	 * @return the streetName
	 */
	public String getStreetName() {
		return streetName;
	}

	/**
	 * Accessor for city
	 * 
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Accessor for postal code
	 * 
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Accessor for phone
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Accessor for email address
	 * 
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Accessor for join date
	 * 
	 * @return the joinDate
	 */
	public LocalDate getJoinDate() {
		return joinDate;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", firstName=" + firstName + ", lastName=" + lastName + ", streetName=" + streetName + ", city="
				+ city + ", postalCode=" + postalCode + ", phone=" + phone + ", emailAddress=" + emailAddress + ", joinDate=" + joinDate + "]";
	}
}
