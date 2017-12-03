package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import application.sevices.Firebase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends MainController {
	@FXML
	private Label lblSuccess;
	@FXML
	private Label lblFailure;
	@FXML
	private Label lblEmail;
	@FXML
	private Label lblUsername;
	@FXML
	private Label lblPassword;
	@FXML
	private Label lblFirstname;
	@FXML
	private Label lblLastname;

	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtFirstname;
	@FXML
	private TextField txtLastname;

	@FXML
	private PasswordField pwPassword;

	public void validateCredentials(ActionEvent event) throws UnirestException {

		if (txtUsername.getText().equals("") || pwPassword.getText().equals("")) {
			lblFailure.setText("Username/Password cannot be empty");
			return;
		} else {
			JSONObject userJSON = findUser(txtUsername.getText(), pwPassword.getText());

			if (userJSON != null) {
				User user = null;

				if (userJSON.getString("role").compareTo("employee") == 0) {
					user = new Employee(userJSON);
				} else if (userJSON.getString("role").compareTo("customer") == 0) {
					user = new Customer(userJSON);
				}
				Main.user = user;
				transitionToUserView(user);

			} else {
				lblFailure.setText("Failure");
			}

		}

	}

	public void transitionToRegistrationView(ActionEvent event) {

		try {
			Parent registrationView;

			registrationView = FXMLLoader.load(getClass().getResource("/application/Registration.fxml"));
			Scene scene = new Scene(registrationView, 750, 500);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createUser(ActionEvent event) throws UnirestException, InterruptedException {
		lblFailure.setText("");

		if (txtEmail.getText().equals("") || 
				pwPassword.getText().equals("") || 
				txtUsername.getText().equals("")	||
				txtFirstname.getText().equals("") ||
				txtLastname.getText().equals("")) {
			lblFailure.setText("Please fill in the entire form!");
			return;
		} else {
			try {
				Firebase.getItem("users", txtUsername.getText());
			} catch (JSONException e) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", txtUsername.getText());
				params.put("email", txtEmail.getText());
				params.put("firstname", txtFirstname.getText());
				params.put("lastname", txtLastname.getText());
				params.put("password", pwPassword.getText());

				Firebase.createItem("users", params);
				lblSuccess.setText("User created, please login!");
				transitionToLoginView();
			}

			lblFailure.setText("Username is taken, please choose another one");
		}

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