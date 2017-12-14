package application.models;

import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Employee model
 * 
 * 	<dt> Description:
 * 	<dd> Extends user; used to identify when a cinema employee
 * 	<dd> (as opposed to a customer) is logged in
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Employee extends User {

	/**
	 * Constructor
	 * Given a JSONObject, calls User's JSON constructor to set username,
	 * password and role.
	 * @param userJSON JSONObject for the user with the following keys:
	 * username, password, role
	 */
	public Employee(JSONObject user) {
		super(user);
	}

}
