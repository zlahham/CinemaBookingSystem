package application;

import java.time.LocalDate;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilmController {
	
	// maybe remove this
	// updating the seats in two places is painful;
	// is it more or less painful to always search one list?
	public static void updateScreeningSeats(String screeningID, HashMap<String, Boolean> seats) {
		// should screenings get their own list?
		for (Film f : Main.filmList) {
			for (Screening s : f.getScreenings()) {
				if (s.getScreeningID().compareTo(screeningID) == 0) {
					s.updateSeats(seats);
					return;
				}
			}
		}
	}
	
	// TODO move this to FilmsController once created
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
