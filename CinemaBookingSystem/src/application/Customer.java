package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer extends User {
	private String firstName;
	private String lastName;
	private String email;
	// Do we need to use FXCollections.observableList here instead?
	// maybe first define an ArrayList and pass it to the above?
	private ObservableList<Booking> bookings = FXCollections.observableArrayList();
	// list of customer details for TableView
	private ObservableList<SimpleEntry<String, String>> details = FXCollections.observableArrayList();

	Customer(JSONObject userJSON) {
		super(userJSON);
		this.firstName = userJSON.getString("firstName");
		this.lastName = userJSON.getString("lastName");
		this.email = userJSON.getString("email");
		// construct bookings list
		JSONArray bookingsJSON = userJSON.getJSONArray("bookings");
		JSONObject bookingI;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		for (int i = 0; i < bookingsJSON.length(); i++) {
			bookingI = bookingsJSON.getJSONObject(i);
			this.bookings.add(new Booking(bookingI.getString("bookingID"), bookingI.getString("filmTitle"),
					LocalDateTime.parse(bookingI.getString("dateTime"), formatter) ));
			System.out.println("Test: adding booking for " + bookings.get(i).getFilmTitle());
		}
		// construct details list
		details.add(new SimpleEntry<String, String>("Username", username));
		details.add(new SimpleEntry<String, String>("Password", password.replaceAll(".", "*")));
		details.add(new SimpleEntry<String, String>("First name", firstName));
		details.add(new SimpleEntry<String, String>("Last name", lastName));
		details.add(new SimpleEntry<String, String>("Email address", email));
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
	
	public ObservableList<SimpleEntry<String, String>> getDetails() {
		return details;
	}
	
	// other methods
	public void deleteBooking(String bookingID) {
		for (Booking i : bookings) {
			if (i.getBookingID().compareTo(bookingID) == 0) {
				bookings.remove(i);
				return;
			}
		}
	}
	
	public void addBooking(Screening screening) {
		Booking booking = new Booking("XXX", screening.getFilmTitle(), screening.getDateTime());
		bookings.add(booking);
	}
}
