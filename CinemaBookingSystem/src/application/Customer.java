package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

public class Customer extends User {
	private String firstName;
	private String lastName;
	private String email;
	// Do we need to use FXCollections.observableList here instead?
	// maybe first define an ArrayList and pass it to the above?
	private ObservableList<Booking> bookings = FXCollections.observableArrayList();

	Customer(JSONObject userJSON) {
		super(userJSON);
		this.firstName = userJSON.getString("firstName");
		this.lastName = userJSON.getString("lastName");
		this.email = userJSON.getString("email");
		JSONArray bookingsJSON = userJSON.getJSONArray("bookings");
		JSONObject bookingI;
		for (int i = 0; i < bookingsJSON.length(); i++) {
			bookingI = bookingsJSON.getJSONObject(i);
			this.bookings.add(new Booking(bookingI.getString("bookingID"), bookingI.getString("filmTitle"),
					LocalDate.parse(bookingI.getString("date")), bookingI.getString("time")));
			System.out.println("Test: adding booking for " + bookings.get(i).getFilmTitle());
		}
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
	
	public ObservableList<Booking> getBookings() {
		return bookings;
	}

}
