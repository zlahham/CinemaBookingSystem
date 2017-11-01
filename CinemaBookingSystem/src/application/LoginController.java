package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML private Label lblTest;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private PasswordField pwPassword;
	
	public void validateCredentials(ActionEvent event) {
		if (validateUsername(txtUsername.getText()) && validatePassword(pwPassword.getText())) {
			lblTest.setText("Success");
		} else {
			lblTest.setText("Failure");
		}
	}
	private boolean validateUsername(String username) {
		if(username.compareTo("username") == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean validatePassword(String password) {
		if(password.compareTo("password") == 0) {
			return true;
		} else {
			return false;
		}
	}

}