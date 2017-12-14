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
 *  <dd> getter methods and methods for managing the list of Screenings
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class Film extends SuperModel {
	private String filmTitle;
	private String description;
	private String imageFileName;
	//The British Board of Film Classification age rating for the film
	//Should be one of the following:
	//U, PG, 12A, 12, 15, 18, R18
	private String ageRating;
	private Image image;
	private ObservableList<Screening> screenings = FXCollections.observableArrayList();

	// constructors
	/**
	 * Constructor
	 * Sets the Film's film title, description, image file name,
	 * BBFC age rating, and list of Screenings from the parameters,
	 * and constructs the film image
	 * @param filmTitle Film title String to be set
	 * @param description Film description to be set
	 * @param imageFileName The film's image file name
	 * @param ageRating The film's BBFC age rating;
	 *        should be one of U, PG, 12A, 12, 15, 18, R18
	 * @param screenings A list of Screenings for the Film
	 */
	public Film(String filmTitle, String description, String imageFileName, String ageRating, ObservableList<Screening> screenings) {
		this.filmTitle = filmTitle;
		this.description = description;
		this.imageFileName = imageFileName;
		this.ageRating = ageRating;
		this.screenings = screenings;
		File imageFile = new File("src/main/resources/images/films/" + imageFileName);
		this.image = new Image(imageFile.toURI().toString());
	}
	/**
	 * Constructor
	 * Given a JSONObject, sets the fields of the Film object based on the 
	 * corresponding key-value pairs in the JSONOBject; parses the JSONObject
	 * and calls the constructor above.
	 * @param filmJSON JSONObject for the film containing the following keys:
	 *        filmTitle, description, imageFileName, ageRating, screenings;
	 *        the screeningsJSON should consist of key(String)-value(ScreeningJSON)
	 *        pairs, where the keys are screeningIDs and the values are screeningJSONs
	 *        of the form taken by the JSON constructor in Screening
	 */
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
	// getters
	/**
	 * Gets the title of the Film.
	 * @return The film title
	 */
	public String getFilmTitle() {
		return filmTitle;
	}
	/**
	 * Gets the description of the Film.
	 * @return The description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Gets the image file name of the film
	 * @return The image file name
	 */
	public String getImageFileName() {
		return imageFileName;
	}
	/**
	 * Gets the image saved for the film
	 * @return The image
	 */
	public Image getImage() {
		return this.image;
	}
	/**
	 * Gets the BBFC age rating for the film
	 * @return The age rating
	 */
	public String getAgeRating() {
		return ageRating;
	}
	/**
	 * Gets the list of Screening objects for the film
	 * @return The list of Screenings
	 */
	public ObservableList<Screening> getScreenings() {
		return screenings;
	}
	/**
	 * Adds a list of Screenings to the list of Screenings
	 * for the film
	 * @param screenings The list of Screenings to be added
	 */
	public void addScreenings(List<Screening> screenings) {
		this.screenings.addAll(screenings);
	}
	/**
	 * Remove a Screening from the list of Screenings for
	 * the Film
	 * @param screenings The Screening to be removed
	 */
	public void removeScreening(Screening screening) {
		Iterator<Screening> iterator = this.screenings.iterator();
		while (iterator.hasNext()) {
			Screening s = iterator.next();
			if (s.getScreeningID().compareTo(screening.getScreeningID()) == 0) {
				iterator.remove();
				return;
			}
		}
	}
}
