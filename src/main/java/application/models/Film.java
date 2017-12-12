package application.models;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Film {
	private String filmTitle;
	private String description;
	private String imageFilepath;
	private String ageRating;
	private Image image;
	
	private ObservableList<Screening> screenings = FXCollections.observableArrayList();

	public Film(String filmTitle, String description, String imageFileName, String ageRating, ObservableList<Screening> screenings) {
		this.filmTitle = filmTitle;
		this.description = description;
		this.imageFilepath = imageFileName;
		this.ageRating = ageRating;
		this.screenings = screenings;
		File imageFile = new File("src/main/resources/images/films/" + imageFileName);
		// TODO: don't resize image here, resize it when fetched
		this.image = new Image(imageFile.toURI().toString(), 100, 100, true, true);
		//this.image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(imageFilepath)).toExternalForm(), 200, 200, true, true);
	}
	
	// TODO: Refactor this constructor with the others
	public Film(JSONObject filmJSON) {
		this(filmJSON.getString("filmTitle"), filmJSON.getString("description"), filmJSON.getString("imageFileName"),
				filmJSON.getString("ageRating"), FXCollections.<Screening>observableArrayList());
		if(filmJSON.has("screenings")) {
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
	}
	
	// setters
	public void setFilmTitle(String filmTitle) {
		this.filmTitle = filmTitle;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setImage(String imageFilepath) {
		this.imageFilepath = imageFilepath;
		//this.image = new Image("File:" + imageFilepath, 200, 200, true, true);
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
	public Image getImage() {
		return this.image;
	}
	public String getAgeRating() {
		return ageRating;
	}
	public ObservableList<Screening> getScreenings() {
		return screenings;
	}
	public void addScreenings(List<Screening> screenings) {
		this.screenings.addAll(screenings);
	}
	
	//TODO change to use a List?
	public void removeScreening(Screening screening) {
		Iterator<Screening> iterator = this.screenings.iterator();
		while (iterator.hasNext()) {
			Screening s = iterator.next();
			if (s.getScreeningID().compareTo(screening.getScreeningID()) == 0) {
				iterator.remove();
				return;
			}
		}
		//TODO: print error message?
	}
	
	
}
