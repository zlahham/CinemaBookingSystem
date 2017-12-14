package application.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import application.Main;
import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd>
 * 
 * 	<dt> Description:
 * 	<dd> 
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Screening extends SuperModel{
	// used when a new Screening is created within the program;
	// Screenings from the database get their dimensions from the database
	public static final int[] theatreDimensions = {4, 6};	
	//TODO: put this somewhere else?
	private String screeningID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private HashMap<String, Boolean> seats;

	public Screening(String filmTitle, LocalDateTime dateTime, HashMap<String, Boolean> seats) {
		this.screeningID = dateTime.format(firebaseDateTimeFormatter);
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.seats = seats;
	}
	
	// TODO: Refactor this constructor with the others
	public Screening(JSONObject screeningJSON) {
		this(screeningJSON.getString("filmTitle"),
				LocalDateTime.parse(screeningJSON.getString("dateTime"), firebaseDateTimeFormatter ), new HashMap<String, Boolean>());
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

	public Film getFilm() {
		for (Film f : Main.filmList) {
			if (this.filmTitle.compareTo(f.getFilmTitle()) == 0) {
				return f;
			}
		}
		return null;
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
