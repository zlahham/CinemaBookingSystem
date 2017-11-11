package application;

import java.time.LocalDate;

public class Screening {
	private String filmTitle;
	private LocalDate date;
	private String time;
	private Customer[][] seats;
	
	Screening (String filmTitle, LocalDate date, String time) {
		this.filmTitle = filmTitle;
		this.date = date;
		this.time = time;
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
