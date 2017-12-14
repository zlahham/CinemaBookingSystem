package application.models;

import java.util.AbstractMap.SimpleEntry;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Customer model
 * 
 * 	<dt> Description:
 * 	<dd> Extends User, includes customer details and setter/getter methods
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Customer extends User {
	private String firstName;
	private String lastName;
	private String email;
	// list of customer details for TableView
	private ObservableList<SimpleEntry<String, String>> details = FXCollections.observableArrayList();
	
	public Customer(JSONObject userJSON) {
		super(userJSON);
		this.firstName = userJSON.getString("firstName");
		this.lastName = userJSON.getString("lastName");
		this.email = userJSON.getString("email");
		
		// construct details list
		details.add(new SimpleEntry<String, String>("Username", this.getUsername()));
		details.add(new SimpleEntry<String, String>("Password", "********"));
		//details.add(new SimpleEntry<String, String>("Password", password.replaceAll(".", "*"))); maybe not the best idea
		details.add(new SimpleEntry<String, String>("First name", this.getFirstName()));
		details.add(new SimpleEntry<String, String>("Last name", this.getLastName()));
		details.add(new SimpleEntry<String, String>("Email address", this.getEmail()));
	}
	// setters
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// getters
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}
	
	public ObservableList<SimpleEntry<String, String>> getDetails() {
		return details;
	}
}
