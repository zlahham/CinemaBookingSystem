package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.JSONObject;

public class Booking {
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private String username;
	private List seats;
	
	Booking (JSONObject bookingJSON) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.dateTime = LocalDateTime.parse(bookingJSON.getString("dateTime"), formatter);
		this.filmTitle = bookingJSON.getString("filmTitle");
		this.username = bookingJSON.getString("username");
		this.bookingID = dateTime.format(formatter) + " " + username;
		this.seats = bookingJSON.getJSONArray("seats").toList();
	}
	
	// to do: sort out these constructors
	
	Booking (String bookingID, String filmTitle, LocalDateTime dateTime) {
		this.bookingID = bookingID;
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
	}
	
	public String getBookingID() {
		return this.bookingID;
	}
	public String getFilmTitle() {
		return this.filmTitle;
	}
	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
}
