package application.models;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import application.Main;

import org.json.JSONObject;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Booking class containing fields and methods pertaining to
 *  <dd> a single booking
 * 
 * 	<dt> Description:
 * 	<dd> Contains information about the booking in the form of String fields,
 *  <dd> and a list of seats for the Booking (note: unlike the one in Screening,
 *  <dd> the seats list in Booking does not contain information about any
 *  <dd> unbooked seats; there are no false values in the HashMap
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Booking extends SuperModel {
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	private String username;
	// On seats: the key is a letter followed by a number (in the current
	// version, "a1" to "d6"), and the value is true iff the seat is booked.
	// Note: unlike the one in Screening, the seats list in Booking does
	// not contain information about any unbooked seats;
	// there are no false values in the HashMap
	private HashMap<String, Boolean> seats;

	// constructors
	/**
	 * Constructor
	 * Sets the Booking's film title, dateTime, seats, and username of the Customer
	 * that made the Booking from the parameters; constructs a bookingID from
	 * the dateTime and the username.
	 * @param filmTitle Film title String to be set
	 * @param dateTime LocalDateTime to be set
	 * @param username Customer's username to be set
	 * @param seats A Hashmap<String, Boolean>; the key is a letter followed
	 *        by a number (in the current  version, "a1" to "d6"),
	 *        and the value is always true; unbooked seats are not
	 *        included here
	 */
	public Booking(String filmTitle, LocalDateTime dateTime, String username, HashMap<String, Boolean> seats) {
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
		this.username = username;
		this.bookingID = dateTime.format(firebaseDateTimeFormatter) + " " + username;
		this.seats = new HashMap<>();
		updateSeats(seats);
	}
	/**
	 * Constructor
	 * Given a JSONObject, sets the fields of the Booking object based on the 
	 * corresponding key-value pairs in the JSONObject; parses the JSONObject
	 * and calls the constructor above.
	 * @param bookingJSON JSONObject for the booking containing the following keys:
	 *        filmTitle, dateTime, username, seats; the seats object should consist of
	 *        key(String)-value(boolean) pairs in the same fashion as the seats field
	 */
	public Booking(JSONObject bookingJSON) {
		this(bookingJSON.getString("filmTitle"), LocalDateTime.parse(bookingJSON.getString("dateTime"), firebaseDateTimeFormatter),
				bookingJSON.getString("username"), new HashMap<>());

		JSONObject seats = bookingJSON.getJSONObject("seats");
		Iterator<String> iterator = seats.keys();
		String seatKey = null;
		while (iterator.hasNext()) {
			seatKey = iterator.next();
			this.seats.put(seatKey, seats.getBoolean(seatKey));
		}
	}
	//getters
	/**
	 * Gets the booking ID of the Booking.
	 * @return The bookingID
	 */
	public String getBookingID() {
		return this.bookingID;
	}
	/**
	 * Gets the title of the film the Booking is for
	 * @return The film title
	 */
	public String getFilmTitle() {
		return this.filmTitle;
	}
	/**
	 * Gets username of the Customer that made the Booking
	 * @return The username
	 */
	public String getUsername() {
		return this.username;
	}
	/**
	 * Gets LocalDateTime of the Screening the Booking is for
	 * @return The LocalDateTime
	 */
	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
	/**
	 * Gets the list of seats for the booking, along with their
	 * booking status (always true; this list does not contain
	 * information about unbooked seats in the Screening)
	 * @return The seats HashMap
	 */
	public HashMap<String, Boolean> getSeats() {
		return this.seats;
	}
	/**
	 * Gets a list of the booked seats as a String wherein
	 * the seat names are capitalized; the list is also
	 * ordered alphabetically
	 * @return A String listing the booked seats
	 *         capitalized in alphabetic order
	 */
	public String getSeatsString() {
		String[] seatsToSort = this.getSeats().keySet().toString().replace("[", "").replace("]", "").replace(",", "")
				.toUpperCase().split(" ");
		Arrays.sort(seatsToSort);
		return String.join(" ", seatsToSort);

	}
	/**
	 * Checks if Booking contains a given seat
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
	 * Given a replacement list of seats for the Booking,
	 * replaces the existing list of seats with the new list
	 * @param seats The list of seats with which the current
	 *        list is to be replaced
	 */
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
	/**
	 * Checks if the DateTime of the Booking is in the past
	 * @return The String "Upcoming" if the Booking is in 
	 *         the future, and "Past" if it is in the past
	 */
	public String status() {
		if (this.dateTime.isAfter(LocalDateTime.now())) {
			return "Upcoming";
		}else {
			return "Past";
		}
	}
	/**
	 * Gets a reference to the Film object the Booking is for
	 * (from the filmList in Main)
	 * @return The Film object
	 */
	public Film getFilm() {
		for (Film f : Main.filmList) {
			if (f.getFilmTitle().compareTo(this.filmTitle) == 0) {
				return f;
			}
		}
		return null;
	}
}
