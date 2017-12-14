package application.models;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Film class containing fields and methods pertaining to
 *  <dd> a single film
 * 
 * 	<dt> Description:
 * 	<dd> Contains information about the film in the form of String fields,
 *  <dd> the image for the film, and a list of Screenings; along with
 *  <dd> setter/getter methods and methods for managing the list of Screenings
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Film extends SuperModel {
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
		this.image = new Image(imageFile.toURI().toString());
	}
	
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
	public void setImagePath(String imageFilepath) {
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
