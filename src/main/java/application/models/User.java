package application.models;

import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd>
 * 
 * 	<dt> Description:
 * 	<dd> 
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

	protected User(JSONObject userJSON) {
		this.username = userJSON.getString("username");
		this.password = userJSON.getString("password");
		this.role = userJSON.getString("role");
	}

	// setters
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// getters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}
	
	// other methods
	public boolean checkPassword(String passwordAttempt) {
		return (password == passwordAttempt);
	}
}
