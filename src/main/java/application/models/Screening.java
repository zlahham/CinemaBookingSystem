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
 * 	<dd> Screening class containing fields and methods pertaining to
 *  <dd> a single screening
 * 
 * 	<dt> Description:
 * 	<dd> Contains information about the screening in the form of String fields,
 *  <dd> a list of seats in the screening (with information on whether or not
 *  <dd> each seat is booked); along with setter/getter methods and methods
 *  <dd> for managing the list of seats.
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Screening extends SuperModel{
	// theatreDimensions used when a new Screening is created within the program;
	// Screenings from the database get their dimensions from the database
	public static final int[] theatreDimensions = {4, 6};	
	// The screeningID is the dateTime as a formatted String
	private String screeningID;
	private String filmTitle;
	private LocalDateTime dateTime;
	// The key is a letter followed by a number (in the current
	//  version, "a1" to "d6"), and the value is true iff the seat is booked.
	private HashMap<String, Boolean> seats;

	// constructors
	/**
	 * Constructor
	 * Sets the Screening's film title, datetime and seats list from the
	 * parameters, and constructs a screeningID from them
	 * @param filmTitle Film title String to be set
	 * @param dateTime LocalDateTime to be set
	 * @param seats A Hashmap<String, Boolean>; the key is a letter followed
	 * by a number (in the current  version, "a1" to "d6"),
	 * and the value is true iff the seat is booked.
	 */
	public Screening(String filmTitle, LocalDateTime dateTime, HashMap<String, Boolean> seats) {
		this.screeningID = dateTime.format(firebaseDateTimeFormatter);
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.seats = seats;
	}
	/**
	 * Constructor
	 * Given a JSONObject, sets the fields of the Screening object based on the 
	 * corresponding key-value pairs in the JSONOBject; parses the JSONObject
	 * and calls the constructor above.
	 * @param screeningJSON JSONObject for the screening the following keys:
	 * filmTitle, dateTime, seats; the seats object should consist of
	 * key(String)-value(boolean) pairs in the same fashion as the seats field
	 */
	public Screening(JSONObject screeningJSON) {
		this(screeningJSON.getString("filmTitle"),
				LocalDateTime.parse(screeningJSON.getString("dateTime"), firebaseDateTimeFormatter), new HashMap<String, Boolean>());
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
	/**
	 * Gets the ScreeningID of the Screening.
	 * @return The screeningID
	 */
	public String getScreeningID() {
		return this.screeningID;
	}
	/**
	 * Gets the film title of the film the Screening is for.
	 * @return The film title
	 */
	public String getFilmTitle() {
		return this.filmTitle;
	}
	/**
	 * Gets a reference to the Film object the Screening is for
	 * (from the filmList in Main)
	 * @return The Film object
	 */
	public Film getFilm() {
		for (Film f : Main.filmList) {
			if (this.filmTitle.compareTo(f.getFilmTitle()) == 0) {
				return f;
			}
		}
		return null;
	}
	/**
	 * Gets the dateTime of the Screening (LocalDateTime)
	 * @return The dateTime
	 */
	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
	/**
	 * Gets the list of seats for the screening, along with their
	 * booking status (true iff booked)
	 * @return The seats HashMap
	 */
	public HashMap<String, Boolean> getSeats() {
		return this.seats;
	}
	/**
	 * Checks if a given seat in the Screening is booked.
	 * @param seat The key for the seat to be checked
	 * @return True iff the seat is booked
	 */
	public boolean checkSeat(String seat) {
		if (this.seats.containsKey(seat)) {
			return seats.get(seat);
		} else {
			return false;
		}
	}
	/**
	 * Given a list of seats in the Screening that are to be changed,
	 * updates the Screening's list of seats using the new list
	 * (all values corresponding to keys that are shared between the
	 * Screening's list and the new list are overwritten by the values
	 * in the new list, and all other values are left as they are.)
	 * @param seats The seats list updating the Screening's seat list
	 */
	public void updateSeats(HashMap<String, Boolean> seats) {
		this.seats.putAll(seats);
	}
	/**
	 * Gets the theatre dimensions for this Screening object
	 * as an integer array where the first element is the number
	 * of seat rows in the theatre, and the second is the number
	 * of seat columns. Assumes that the seat plan is a rectangle,
	 * and that the row index is a single character.
	 * @return A two-element integer array; the first element is the number
	 * of seat rows in the theatre, and the second is the number
	 * of seat columns.
	 */
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
