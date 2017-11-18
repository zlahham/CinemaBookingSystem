package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;

public class Screening {
	private String filmTitle;
	private LocalDateTime dateTime;
	private HashMap<String, Boolean> seats = new HashMap<String, Boolean>(9);

	// Note: the screeningJSON itself does not contain the date or the time anymore,
	// which is why they key is passed as well. An array of ScreeningJSON objects
	// would make this constructor nicer, but we would lose the ability to fetch
	// screenings directly using keys (would have to iterate through the array
	// instead).
	Screening(Film film, String key, JSONObject screeningJSON) {
		this.filmTitle = film.getFilmTitle();
		String[] dateTime = key.split(" ");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		this.dateTime = LocalDateTime.parse(dateTime[0].concat(" " + dateTime[1]), dateFormatter);
		
		// construct seats HashMap
		JSONObject seats = screeningJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			seats.put(seatKey, seats.getBoolean(seatKey));
		}
	}

	Screening(String filmTitle, LocalDateTime dateTime) {
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
	}

	public String getFilmTitle() {
		return this.filmTitle;
	}

	public LocalDateTime getDateTime() {
		return this.dateTime;
	}

}
