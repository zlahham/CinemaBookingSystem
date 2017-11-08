package application;

import org.json.JSONObject;

public abstract class User {
	private String username;
	private String password;
	private String role;
	
	User(JSONObject user) {
		this.username = user.getString(username);
		this.password = user.getString(password);
		this.role = user.getString(role);
	}
	
	// setters
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	// getters
	public String getUsername() {
		return username;
	}
	public boolean checkPassword(String passwordAttempt) {
		return (password == passwordAttempt);
	}
}

