package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	private Label lblTest;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtPassword;
	@FXML
	private PasswordField pwPassword;

	public void validateCredentials(ActionEvent event) {
		JSONObject userJSON = validateUser(txtUsername.getText(), pwPassword.getText());
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

	public void transitionToUserView(User user) {
		try {
			Parent userView;
			userView = FXMLLoader
					.load(getClass().getResource("/application/" + StringUtils.capitalize(user.getRole()) + ".fxml"));
			Scene scene = new Scene(userView, 750, 500);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// TODO: Sort this out
	public void createUser(ActionEvent event) {
		System.out.println("kill me");
		String content = null;

	}

	private JSONObject validateUser(String username, String password) {
		String content = null;

		try {
			content = new Scanner(new File("assets/users.json")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		JSONObject usersJSON = new JSONObject(content);
		JSONObject userI;

		for (int i = 0; i < usersJSON.getJSONArray("users").length(); i++) {
			userI = usersJSON.getJSONArray("users").getJSONObject(i);
			if (userI.getString("username").compareTo(username) == 0
					&& userI.getString("password").compareTo(password) == 0) {
				return userI;
			} else {
				continue;
			}
		}
		return null;
	}

}