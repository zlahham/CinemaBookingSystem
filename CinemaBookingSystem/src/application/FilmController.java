package application;

import java.time.LocalDate;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilmController {
	
	// remove this and use some different way to accomplish things?
	public static Screening getScreeningForBooking(Booking booking) {
		for (Film f : Main.filmList) {
			for (Screening s : f.getScreenings()) {
				if (s.getDateTime().compareTo(booking.getDateTime()) == 0) {
					return s;
				}
			}
		}
		return null;
	}
	
	public static ObservableList<Screening> filterScreeningsByDate(LocalDate date) {
		ObservableList<Screening> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < Main.filmList.size(); i++) {
			for (int j = 0; j < Main.filmList.get(i).getScreenings().size(); j++) {
				if (date.equals(Main.filmList.get(i).getScreenings().get(j).getDateTime().toLocalDate())) {
					returnList.add(Main.filmList.get(i).getScreenings().get(j));
				}
			}
		}
		return returnList;
	}
	
}
