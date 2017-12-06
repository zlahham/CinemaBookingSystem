package application.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public class Screening {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private String screeningID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private HashMap<String, Boolean> seats = new HashMap<String, Boolean>(9);

	public Screening(String filmTitle, LocalDateTime dateTime, HashMap<String, Boolean> seats) {
		// old version:
		// this.screeningID = screeningID;
		// the new version makes constructing screenings easier (no formatter needed)
		this.screeningID = dateTime.format(formatter).toString() + " " + filmTitle;
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
			this.seats = seats;
		}
	
	// TODO: Refactor this constructor with the others
	public Screening(JSONObject screeningJSON) {
		this(screeningJSON.getString("filmTitle"),
				LocalDateTime.parse(screeningJSON.getString("dateTime"), formatter), new HashMap<String, Boolean>(9));
		// construct seats HashMap
		JSONObject seats = screeningJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			this.seats.put(seatKey, seats.getBoolean(seatKey));
		}
	}

	// getters
	public String getScreeningID() {
		return this.screeningID;
	}
	
	public String getFilmTitle() {
		return this.filmTitle;
	}

	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
	
	public HashMap<String, Boolean> getSeats() {
		return this.seats;
	}
	
	public boolean checkSeat(String seat) {
		return seats.get(seat);
	}
	
	public void updateSeats(HashMap<String, Boolean> seats) {
		this.seats.putAll(seats);
	}
}
