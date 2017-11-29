package application;

import java.time.LocalDate;
import java.util.Iterator;

import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FilmList {
	
	private ObservableList<Film> filmList = FXCollections.observableArrayList();

	public FilmList () {
		JSONObject json = null;
		try { 
			json = FirebaseController.getList("films");
		} catch (UnirestException e) { 
			e.printStackTrace(); 
		}
		
		Iterator<String> iterator = json.keys();
		String filmKey = null;
		while (iterator.hasNext()) {
			filmKey = iterator.next();
			filmList.add(new Film(json.getJSONObject(filmKey)));
		}
	}
	
	public ObservableList<Screening> screeningsOnDate(LocalDate date) {
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
