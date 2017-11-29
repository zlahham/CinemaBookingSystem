package application;

import java.util.Iterator;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Film {
	private String filmTitle;
	private String description;
	private String imageFilepath;
	private String ageRating;
	
	private ObservableList<Screening> screenings = FXCollections.observableArrayList();

	public Film(String filmTitle, String description, String imageFilepath, String ageRating, ObservableList<Screening> screenings) {
		this.filmTitle = filmTitle;
		this.description = description;
		this.imageFilepath = imageFilepath;
		this.ageRating = ageRating;
		this.screenings = screenings;
	}
	
	// TODO: Refactor this contructor with the others
	public Film(JSONObject filmJSON) {
		this(filmJSON.getString("filmTitle"), filmJSON.getString("description"), filmJSON.getString("imageFilePath"),
				filmJSON.getString("ageRating"), FXCollections.observableArrayList());
		JSONObject screeningsJSON = filmJSON.getJSONObject("screenings");
		JSONObject screeningI;
		Iterator<String> iterator = screeningsJSON.keys();
		String screeningKey = null;
		while (iterator.hasNext()) {
			screeningKey = iterator.next();
			screeningI = screeningsJSON.getJSONObject(screeningKey);
			this.screenings.add(new Screening(screeningI));
		}
	}
	
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
