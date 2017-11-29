package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class Booking {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private String username;
	private HashMap<String, Boolean> seats = new HashMap<String, Boolean>(9);
	
	public Booking(String filmTitle, LocalDateTime dateTime, String username, HashMap<String, Boolean> seats) {
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.username = username;
		this.bookingID = dateTime.format(formatter) + " " + username;
		this.seats = seats;
	}
	
	// TODO: Refactor this contructor with the others
	public Booking(JSONObject bookingJSON) {
		this(bookingJSON.getString("filmTitle"), LocalDateTime.parse(bookingJSON.getString("dateTime"), formatter),
				bookingJSON.getString("username"), new HashMap<String, Boolean>(9));
		
		JSONObject seats = bookingJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			seats.put(seatKey, seats.getBoolean(seatKey));
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
