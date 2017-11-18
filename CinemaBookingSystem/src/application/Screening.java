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
	
	
	/* old
	// Note: the screeningJSON itself does not contain the date or the time anymore,
	// which is why they key is passed as well. An array of ScreeningJSON objects
	// would make this constructor nicer, but we would lose the ability to fetch
	// screenings directly using keys (would have to iterate through the array
	// instead). Alternatively, just include everything in the screeningJSON
	// (so include filmTitle and dateTime, also for similarity with bookings)
	Screening(Film film, String key, JSONObject screeningJSON) {
		this.screeningID = key;
		this.filmTitle = film.getFilmTitle();
		String[] dateTime = key.split(" ");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.dateTime = LocalDateTime.parse(dateTime[0].concat(" " + dateTime[1]), formatter);
		
		// construct seats HashMap
		JSONObject seats = screeningJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			seats.put(seatKey, seats.getBoolean(seatKey));
		}
	}
	*/
	
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
