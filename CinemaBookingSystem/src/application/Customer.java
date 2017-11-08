package application;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Customer extends User {
	private String firstName;
	private String lastName;
	private String email;
	private List<Booking> bookings = new ArrayList<Booking>();

	Customer(JSONObject userJSON) {
		super(userJSON);
		this.firstName = userJSON.getString("first name");
		this.lastName = userJSON.getString("last name");
		this.email = userJSON.getString("email");
		JSONArray bookingsJSON = userJSON.getJSONArray("bookings");
		JSONObject bookingI;
		for (int i = 0; i < bookingsJSON.length(); i++) {
			bookingI = bookingsJSON.getJSONObject(i);
			this.bookings.add(new Booking(bookingI.getString("film title"), bookingI.getString("date"), bookingI.getString("time")));
			System.out.println("Test: adding booking for " + bookingI.getString("film title"));
		}
	}
}
