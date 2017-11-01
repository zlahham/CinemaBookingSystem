package application;

public abstract class User {
	private String username;
	private String password;
	
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

