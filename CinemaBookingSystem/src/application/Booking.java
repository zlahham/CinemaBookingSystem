package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class Booking {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private String username;
	private List<String> seats;
	
	public Booking(String filmTitle, LocalDateTime dateTime, String username, ArrayList<String> seats) {
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.username = username;
		this.bookingID = dateTime.format(formatter) + " " + username;
		this.seats = new ArrayList<String>();
		this.seats.addAll(seats);
	}
	
	public Booking(JSONObject bookingJSON) {
		this(bookingJSON.getString("filmTitle"), LocalDateTime.parse(bookingJSON.getString("dateTime"), formatter),
				bookingJSON.getString("username"), new ArrayList<String>());
		for (int i = 0; i < bookingJSON.getJSONArray("seats").length(); i++) {
			this.seats.add(bookingJSON.getJSONArray("seats").getString(i));
		}
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
	public String getSeats() {
		return this.seats.toString();
	}
}
