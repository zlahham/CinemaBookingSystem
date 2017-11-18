package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Film {
	private String filmTitle;
	private String description;
	private String imageFilepath;
	private String ageRating;
	
	private ObservableList<Screening> screenings = FXCollections.observableArrayList();


	Film(JSONObject filmJSON) {
		this.filmTitle = filmJSON.getString("filmTitle");
		this.description = filmJSON.getString("description");
		this.imageFilepath = filmJSON.getString("imageFilePath");
		this.ageRating = filmJSON.getString("ageRating");
		JSONObject screeningsJSON = filmJSON.getJSONObject("screenings");
		JSONObject screeningI;
		Iterator<String> iterator = screeningsJSON.keys();
		String screeningKey = null;
		while (iterator.hasNext()) {
			screeningKey = iterator.next();
			screeningI = screeningsJSON.getJSONObject(screeningKey);
			this.screenings.add(new Screening(this, screeningKey, screeningI));
		}
	}
	// To do: create another constructor; make the existing one refer to that
	
	// setters
	public void setFilmTitle(String filmTitle) {
		this.filmTitle = filmTitle;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImageFilepath(String imageFilepath) {
		this.imageFilepath = imageFilepath;
	}
	public void setAgeRating(String ageRating) {
		this.ageRating = ageRating;
	}
	
	// getters
	public String getFilmTitle() {
		return filmTitle;
	}
	public String getDescription() {
		return description;
	}
	public String getImageFilePath() {
		return imageFilepath;
	}
	public String getAgeRating() {
		return ageRating;
	}
	public ObservableList<Screening> getScreenings() {
		return screenings;
	}
	
	
}
