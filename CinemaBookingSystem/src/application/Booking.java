package application;

import java.time.LocalDate;

public class Booking {
	private String bookingID;
	private String filmTitle;
	private LocalDate date;
	private String time;
	
	Booking (String bookingID, String filmTitle, LocalDate date, String time) {
		this.bookingID = bookingID;
		this.filmTitle = filmTitle;
		this.date = date;
		this.time = time;
	}
	
	public String getBookingID() {
		return this.bookingID;
	}
	public String getFilmTitle() {
		return this.filmTitle;
	}
	public LocalDate getDate() {
		return this.date;
	}
	public String getTime() {
		return this.time;
	}
}
