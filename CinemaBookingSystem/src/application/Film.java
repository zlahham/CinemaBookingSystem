package application;

import java.util.List;

public class Film {
	private String title;
	private String description;
	private String imageFilepath;
	private String ageRating;
	
	private List<Screening> screenings;

	// setters
	public void setTitle(String title) {
		this.title = title;
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
	public String getTitle() {
		return title;
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
	
	
	
}
