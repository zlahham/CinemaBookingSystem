package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONObject;

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
		return searchForUsername(username);

	}
	
	private boolean validatePassword(String password) {
		if(password.compareTo("password") == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean searchForUsername(String username) {
		String content = null;
		
		try {
			content = new Scanner(new File("assets/users.json")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(content);
		
		for (int i = 0 ; i < obj.getJSONArray("users").length(); i++) {
			if(obj.getJSONArray("users").getJSONObject(i).getString("username").compareTo(username) == 0) {
				return true;
			} else {
				continue;
			}
			
		}
		return false;
	}

}