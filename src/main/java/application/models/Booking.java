package application.models;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import application.Main;

import org.json.JSONObject;

public class Booking extends SuperModel {
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private String username;
	private HashMap<String, Boolean> seats;

	public Booking(String filmTitle, LocalDateTime dateTime, String username, HashMap<String, Boolean> seats) {
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.username = username;
		this.bookingID = dateTime.format(firebaseDateTimeFormatter) + " " + username;
		this.seats = new HashMap<>();
		updateSeats(seats);
	}

	// TODO: Refactor this constructor with the others
	public Booking(JSONObject bookingJSON) {
		this(bookingJSON.getString("filmTitle"), LocalDateTime.parse(bookingJSON.getString("dateTime"), firebaseDateTimeFormatter),
				bookingJSON.getString("username"), new HashMap<>(9));

		JSONObject seats = bookingJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			this.seats.put(seatKey, seats.getBoolean(seatKey));
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
	public HashMap<String, Boolean> getSeats() {
		return this.seats;
	}

	public String getSeatsString() {
		String[] seatsToSort = this.getSeats().keySet().toString().replace("[", "").replace("]", "").replace(",", "")
				.toUpperCase().split(" ");
		Arrays.sort(seatsToSort);
		return String.join(" ", seatsToSort);

	}
	public boolean checkSeat(String seat) {
		if (this.seats.containsKey(seat)) {
			return seats.get(seat);
		} else {
			return false;
		}
	}
	public void updateSeats(HashMap<String, Boolean> seats) {
		Iterator<Entry<String, Boolean>> iterator = seats.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Boolean> seat = iterator.next();
			if (seat.getValue()) {
				this.seats.put(seat.getKey(), seat.getValue());
			} else {
				if (this.seats.containsKey(seat.getKey())) {
					this.seats.remove(seat.getKey());
				}
			}
		}
	}
	public String status() {
		if (this.dateTime.isAfter(LocalDateTime.now())) {
			return "Upcoming";
		}else {
			return "Past";
		}
	}

	public Film getFilm() {
		for (Film f : Main.filmList) {
			if (f.getFilmTitle().compareTo(this.filmTitle) == 0) {
				return f;
			}
		}
		return null;
	}
}
