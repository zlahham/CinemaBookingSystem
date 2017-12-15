package application.controllers;

import application.Main;
import application.models.Customer;
import application.models.Employee;
import application.models.User;
import application.services.Firebase;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Control logging in to the program, and for setting up
 * 	<dd> a User and their environment
 * 
 * 	<dt> Description:
 * 	<dd> Checks the user's username and password (via the Login view) and
 * 	<dd> enables creation of Customer's (via the Registration view).
 * 	<dd> Sends the User to the appropriate Dashboard (that for the Customer
 * 	<dd> or that for the Employee) when they log in.
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class LoginController extends MainController {
    @FXML
    private Label lblSuccess;
    @FXML
    private Label lblFailure;
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

                String mode = "";
                if (userJSON.getString("role").compareTo("employee") == 0) {
                    user = new Employee(userJSON);
                    mode = "ECDashboard";
                } else if (userJSON.getString("role").compareTo("customer") == 0) {
                    user = new Customer(userJSON);
                    mode = "CCDashboard";
                }
                Main.stage.setUserData(user);

                assert user != null;
                transition(StringUtils.capitalize(StringUtils.capitalize(user.getRole())), mode);
            } else {
                lblFailure.setText("Failure");
            }

        }

    }

    public void createUser(ActionEvent event) throws UnirestException {
        lblFailure.setText("");

        if (txtEmail.getText().equals("") ||
                pwPassword.getText().equals("") ||
                txtUsername.getText().equals("") ||
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

                Firebase.createUser(params);
                lblSuccess.setText("User created, please login!");
                transition("Login", "");
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