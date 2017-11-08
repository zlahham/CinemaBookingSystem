package application;

import java.util.List;

import org.json.JSONObject;

import javafx.collections.ObservableList;

public abstract class User {
	private String username;
	private String password;
	private String role;

	User(JSONObject userJSON) {
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

	public boolean checkPassword(String passwordAttempt) {
		return (password == passwordAttempt);
	}

	public String getRole() {
		return role;
	}
	
	//abstract public ObservableList<Booking> getBookings();
}
