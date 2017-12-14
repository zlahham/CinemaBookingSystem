package application.models;

import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Customer model
 * 
 * 	<dt> Description:
 * 	<dd> Extends User, includes customer details and setter/getter methods
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Customer extends User {
	private String firstName;
	private String lastName;
	private String email;
	
	// constructors
	
	/**
	 * Constructor
	 * Given a JSONObject, calls User's JSON constructor to set username,
	 * password and role, and sets the firstName, lastName, and email based
	 * on the corresponding  key(string)-value(string) pairs in the JSONObject.
	 * @param userJSON JSONObject for the user with the following keys:
	 * username, password, role, firstName, lastName, email
	 */
	public Customer(JSONObject userJSON) {
		super(userJSON);
		this.firstName = userJSON.getString("firstName");
		this.lastName = userJSON.getString("lastName");
		this.email = userJSON.getString("email");
	}
	// setters
	/**
	 * Sets Customer's first name.
	 * @param firstName New first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * Sets Customer's last name.
	 * @param lastName New last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * Sets Customer's email address.
	 * @param email New email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	// getters
	/**
	 * Gets Customer's first name.
	 * @return Customer's first name
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * Gets Customer's last name.
	 * @return Customer's last name
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * Gets Customer's email address.
	 * @return Customer's email address
	 */
	public String getEmail() {
		return email;
	}
}
