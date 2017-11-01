package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML private Label lblTest;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	
	public void validateCredentials(ActionEvent event) {
		validateUsername(txtUsername.getText());
		validatePassword(txtPassword.getText());
	}
	
	private void validateUsername(String username) {
		if(username.compareTo("username") == 0) {
			lblTest.setText("Success!");
		} else {
			lblTest.setText("Try Again!");
		}
	}
	
	private void validatePassword(String password) {
		if(password.compareTo("password") == 0) {
			lblTest.setText("Success!");
		} else {
			lblTest.setText("Try Again!");
		}
	}
}