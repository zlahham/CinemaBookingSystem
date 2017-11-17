package application;

import org.json.JSONObject;

public abstract class User {
	protected JSONObject userJSON;
	protected String username;
	protected String password;
	protected String role;

	User(JSONObject userJSON) {
		this.userJSON = userJSON;
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

	public String getRole() {
		return role;
	}
	
	// other methods
	public boolean checkPassword(String passwordAttempt) {
		return (password == passwordAttempt);
	}
	//abstract public ObservableList<Booking> getBookings();
}
