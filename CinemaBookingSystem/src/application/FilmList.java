package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Iterator;
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
		filmsJSON = filmsJSON.getJSONObject("films");
		JSONObject filmI;
		
		Iterator<String> iterator = filmsJSON.keys();
		String filmKey = null;
		while (iterator.hasNext()) {
			filmKey = iterator.next();
			filmI = filmsJSON.getJSONObject(filmKey);
			filmList.add(new Film(filmI));
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
