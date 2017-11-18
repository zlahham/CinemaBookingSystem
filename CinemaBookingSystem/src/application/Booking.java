package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Booking {
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private String username;
	private List<String> seats;
	
	Booking (JSONObject bookingJSON) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.dateTime = LocalDateTime.parse(bookingJSON.getString("dateTime"), formatter);
		this.filmTitle = bookingJSON.getString("filmTitle");
		this.username = bookingJSON.getString("username");
		this.bookingID = bookingJSON.getString("bookingID");
		this.seats = new ArrayList<String>();
		for (int i = 0; i < bookingJSON.getJSONArray("seats").length(); i++) {
			System.out.println(bookingJSON.getJSONArray("seats").get(i));
			this.seats.add(bookingJSON.getJSONArray("seats").getString(i));
		}
	}
	
	// to do: sort out these constructors
	
	Booking (String filmTitle, LocalDateTime dateTime, String username, ArrayList<String> seats) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.username = username;
		this.bookingID = dateTime.format(formatter) + " " + username;
		this.seats = new ArrayList<String>();
		this.seats.addAll(seats);
	}
	
	public String getBookingID() {
		return this.bookingID;
	}
	public String getFilmTitle() {
		return this.filmTitle;
	}
	public String getUsername() {
		return this.username;
	}
	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
}
