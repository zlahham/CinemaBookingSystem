package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class Screening {
	private String screeningID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private HashMap<String, Boolean> seats = new HashMap<String, Boolean>(9);

	Screening(JSONObject screeningJSON) {
		this.screeningID = screeningJSON.getString("screeningID");
		this.filmTitle = screeningJSON.getString("filmTitle");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.dateTime = LocalDateTime.parse(screeningJSON.getString("dateTime"), formatter);
		
		// construct seats HashMap
		JSONObject seats = screeningJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			seats.put(seatKey, seats.getBoolean(seatKey));
		}
	}
	// To do: create another constructor; make the existing one refer to that

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

}
