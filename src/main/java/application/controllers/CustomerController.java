package application.controllers;

import application.Main;
import application.models.Customer;
import application.services.Firebase;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Control basic Customer functionality.
 * 
 * 	<dt> Description:
 * 	<dd> Controls initialisation of the Customer Dashboard and Profile 
 * 	<dd> (calling UserController to initialize the Dashboard), and
* 	<dd> enabling a Customer to edit their details.
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class CustomerController extends UserController {
    private Customer customer = (Customer) (Main.stage.getUserData());

    @FXML
    private Label lblFailure;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField pwPassword;
    @FXML
    private TextField txtFirstname;
    @FXML
    private TextField txtLastname;

    public void initialize() {
        switch (mode) {
            case "CCDashboard":
                super.initialize();
                break;
            case "CCProfile":
                fillProfileForm();
                break;
            default:
                super.initialize();
        }
    }

    public void editUser() {
        lblFailure.setText("");

        if (emptyFieldsExist()) {
            lblFailure.setText("Please fill in the entire form!");
        } else {
            Map<String, String> params = paramsForFirebase();
            updateUserDataLocally();

            Main.stage.setUserData(null);
            Main.stage.setUserData(customer);

            try {
                Firebase.createUser(params);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            transition("Customer", "CCDashboard");
        }
    }

    private void fillProfileForm() {
        txtEmail.setText(customer.getEmail());
        pwPassword.setText(customer.getPassword());
        txtFirstname.setText(customer.getFirstName());
        txtLastname.setText(customer.getLastName());
    }

    private boolean emptyFieldsExist() {
        return txtEmail.getText().equals("") || pwPassword.getText().equals("") ||
                txtFirstname.getText().equals("") || txtLastname.getText().equals("");
    }

    private void updateUserDataLocally() {
        customer.setEmail(txtEmail.getText());
        customer.setFirstName(txtFirstname.getText());
        customer.setLastName(txtLastname.getText());
        customer.setPassword(pwPassword.getText());
    }

    private Map<String, String> paramsForFirebase() {
        Map<String, String> params = new HashMap<>();
        params.put("username", customer.getUsername());
        params.put("email", txtEmail.getText());
        params.put("firstname", txtFirstname.getText());
        params.put("lastname", txtLastname.getText());
        params.put("password", pwPassword.getText());
        return params;
    }
}
