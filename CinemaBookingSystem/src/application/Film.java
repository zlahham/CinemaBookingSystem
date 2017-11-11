package application;

import java.time.LocalDate;

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

	{
		
	}
	Film(JSONObject filmJSON) {
		this.filmTitle = filmJSON.getString("filmTitle");
		this.description = filmJSON.getString("description");
		this.imageFilepath = filmJSON.getString("imageFilePath");
		this.ageRating = filmJSON.getString("ageRating");
		JSONArray screeningsJSON = filmJSON.getJSONArray("screenings");
		JSONObject screeningI;
		for (int i = 0; i < screeningsJSON.length(); i++) {
			screeningI = screeningsJSON.getJSONObject(i);
			this.screenings.add(new Screening(screeningI.getString("filmTitle"),
					LocalDate.parse(screeningI.getString("date")), screeningI.getString("time")));
			// remove these later
			System.out.println("Test: adding screening for " + screenings.get(i).getFilmTitle());
			System.out.println("Test: Date: " + LocalDate.parse(screeningI.getString("date")));
			/////////////////
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
