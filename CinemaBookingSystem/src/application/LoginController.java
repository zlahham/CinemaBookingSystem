package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import application.sevices.Firebase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends MainController {
	@FXML
	private Label lblTest;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtPassword;
	@FXML
	private PasswordField pwPassword;

	public void validateCredentials(ActionEvent event) throws UnirestException {
		
		if (txtUsername.getText().equals("") || pwPassword.getText().equals("")) {
			lblTest.setText("Username/Password cannot be empty");
			return;
		} else {
			JSONObject userJSON = findUser(txtUsername.getText(), pwPassword.getText());

			if (userJSON != null) {
				lblTest.setText("Success");

				User user = null;

				if (userJSON.getString("role").compareTo("employee") == 0) {
					user = new Employee(userJSON);
				} else if (userJSON.getString("role").compareTo("customer") == 0) {
					user = new Customer(userJSON);
				}
				Main.user = user;
				transitionToUserView(user);

			} else {
				lblTest.setText("Failure");
			}

		}

	}

	// TODO: Sort this out
	public void createUser(ActionEvent event) {
		System.out.println("kill me");
		String content = null;

	}

	private JSONObject findUser(String username, String password) throws UnirestException {
		JSONObject userJSON = null;

		try {
			userJSON = Firebase.getItem("users", username);
			if (userJSON.getString("password").compareTo(password) != 0)
				userJSON = null;

		} catch (JSONException e) {
			return userJSON;
		}

		return userJSON;
	}

}