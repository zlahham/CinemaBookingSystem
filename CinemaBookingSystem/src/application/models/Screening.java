package application.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

public class Screening {
	// used when a new Screening is created within the program;
	// Screenings from the database get their dimensions from the database
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
	
	// gets theatre dimensions for existing Screening objects
	// assumes the seat plan is a rectangle,
	// and that the row index is a single character
	public int[] getTheatreDimensions() {
		Iterator<String> iterator = this.getSeats().keySet().iterator();
		String key = "";
		ArrayList<String> rows = new ArrayList<String>();
		ArrayList<String> columns = new ArrayList<String>();
		while (iterator.hasNext()) {
			key = iterator.next().toString();
			if (!rows.contains(key.substring(0, 1))) {
				rows.add(key.substring(0, 1));
			}
			if (!columns.contains(key.substring(1))) {
				columns.add(key.substring(1));
			}
		}
		int dimensions[] = {rows.size(), columns.size()};
		return dimensions;
	}
}
