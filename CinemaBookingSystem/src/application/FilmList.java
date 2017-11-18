package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilmList {
	
	private ObservableList<Film> filmList = FXCollections.observableArrayList();

	FilmList (String JSONfilepath) {
		String content = null;
	
		try {
			content = new Scanner(new File(JSONfilepath)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		JSONObject filmsJSON = new JSONObject(content);
		JSONObject filmI;
	
		for (int i = 0; i < filmsJSON.getJSONArray("films").length(); i++) {
			filmI = filmsJSON.getJSONArray("films").getJSONObject(i);
			Film film = new Film(filmI);
			filmList.add(film);
		}
	}
	
	public ObservableList<Screening> screeningsOnDate(LocalDate date) {
		System.out.println("screeningsondate running");
		ObservableList<Screening> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < filmList.size(); i++) {
			for (int j = 0; j < filmList.get(i).getScreenings().size(); j++) {
				if (date.equals(filmList.get(i).getScreenings().get(j).getDateTime().toLocalDate())) {
					returnList.add(filmList.get(i).getScreenings().get(j));
				}
			}
		}
		return returnList;
	}
}
