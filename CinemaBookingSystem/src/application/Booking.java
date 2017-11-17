package application;

import java.time.LocalDateTime;

public class Booking {
	private String bookingID;
	private String filmTitle;
	private LocalDateTime dateTime;
	
	Booking (String bookingID, String filmTitle, LocalDateTime dateTime) {
		this.bookingID = bookingID;
		this.filmTitle = filmTitle;
		this.dateTime = dateTime;
	}
	
	public String getBookingID() {
		return this.bookingID;
	}
	public String getFilmTitle() {
		return this.filmTitle;
	}
	public LocalDateTime getDateTime() {
		return this.dateTime;
	}
}
