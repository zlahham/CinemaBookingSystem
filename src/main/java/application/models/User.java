package application.models;

import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Abstract User model (extended by Customer and Employee)
 * 
 * 	<dt> Description:
 * 	<dd> Contains details and setter/getter methods common to both
 *  <dd> Customers and Employees
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public abstract class User extends SuperModel{
	private String username;
	private String password;
	private String role;

	/**
	 * Constructor
	 * Given a JSONObject, sets the username, password, and role based
	 * on the corresponding  key(string)-value(string) pairs in the JSONObject
	 * @param userJSON JSONObject for the user with at least the following keys:
	 * username, password, role
	 */
	protected User(JSONObject userJSON) {
		this.username = userJSON.getString("username");
		this.password = userJSON.getString("password");
		this.role = userJSON.getString("role");
	}

	// setters
	/**
	 * Sets User's username (used to identify Users)
	 * @param username Username to be set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Sets User's password
	 * @param password Password to be set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * Sets User's role (Customer or Employee)
	 * @param role The User's role as a String ("customer" or "employee")
	 */
	public void setRole(String role) {
		this.role = role;
	}

	// getters
	/**
	 * Gets User's username
	 * @return The User's username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Gets User's password
	 * @return The User's password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Gets User's role (Customer or Employee)
	 * @return The User's role as a String ("customer" or "employee")
	 */
	public String getRole() {
		return role;
	}
}
