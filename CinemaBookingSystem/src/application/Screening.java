package application;

import java.time.LocalDateTime;

public class Screening {
	private String filmTitle;
	private LocalDateTime dateTime;
	private Booking[][] seats;
	
	Screening (String filmTitle, LocalDateTime dateTime) {
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
