package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public class Screening {
	public static final int[] theatreDimensions = {3, 3};	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private String screeningID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private HashMap<String, Boolean> seats;

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
				LocalDateTime.parse(screeningJSON.getString("dateTime"), formatter), new HashMap<String, Boolean>());
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
	
	// TODO: take another look at this and changing the theatre dimensions!
	public boolean checkSeat(String seat) {
		if (this.seats.containsKey(seat)) {
			return seats.get(seat);
		} else {
			return false;
		}
	}
	
	public void updateSeats(HashMap<String, Boolean> seats) {
		this.seats.putAll(seats);
	}
}
