package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class Screening {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private String screeningID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private HashMap<String, Boolean> seats = new HashMap<String, Boolean>(9);

	public Screening(String screeningID, String filmTitle, LocalDateTime dateTime, HashMap<String, Boolean> seats) {
		this.screeningID = screeningID;
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.seats = seats;
		}
	
	public Screening(JSONObject screeningJSON) {
		this(screeningJSON.getString("screeningID"), screeningJSON.getString("filmTitle"),
				LocalDateTime.parse(screeningJSON.getString("dateTime"), formatter), new HashMap<String, Boolean>(9));
		// construct seats HashMap
		JSONObject seats = screeningJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			seats.put(seatKey, seats.getBoolean(seatKey));
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

}
