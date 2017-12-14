package application.models;

import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Employee model
 * 
 * 	<dt> Description:
 * 	<dd> 
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Employee extends User {

	public Employee(JSONObject user) {
		super(user);
	}

}
