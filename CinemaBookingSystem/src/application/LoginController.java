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
		if (validateUser(txtUsername.getText(), pwPassword.getText())) {
			lblTest.setText("Success");
		} else {
			lblTest.setText("Failure");
		}
	}
	
	private boolean validateUser(String username, String password) {
		String content = null;
		
		try {
			content = new Scanner(new File("assets/users.json")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JSONObject obj = new JSONObject(content);
		JSONObject obj2;
		
		for (int i = 0 ; i < obj.getJSONArray("users").length(); i++) {
			obj2 = obj.getJSONArray("users").getJSONObject(i);
			if(obj2.getString("username").compareTo(username) == 0 &&
				obj2.getString("password").compareTo(password) == 0) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
	


}