package application;

public class Booking {
	private String bookingID;
	private String filmTitle;
	private String date;
	private String time;
	
	Booking (String bookingID, String filmTitle, String date, String time) {
		this.bookingID = bookingID;
		this.filmTitle = filmTitle;
		this.date = date;
		this.time = time;
	}
	
	public String getFilmTitle() {
		return this.filmTitle;
	}
	public String getDate() {
		return this.date;
	}
	public String getTime() {
		return this.time;
	}
}
