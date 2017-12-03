package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class FilmController extends MainController {

	public static final ObservableList<String> AGE_RATINGS = FXCollections
			.observableArrayList(Arrays.asList("U", "PG", "12A", "12", "15", "18", "R18"));
	
	private static ObservableList<LocalDateTime> screeningDatesToAdd = FXCollections.observableArrayList();
	private Image image;

	// AddFilm view controls
	@FXML
	private TextField txtFilmTitle;
	@FXML
	private TextField txtDescription;
	@FXML
	private ComboBox<String> cbxAgeRating;
	@FXML
	private Button btnUploadImage;
	@FXML
	private Label lblError;
	
	public void initialize() {
		initializeAddFilm();
	}
	
	// initialize addFilm view
	private void initializeAddFilm() {
		lblError.setText("");
		cbxAgeRating.getItems().addAll(AGE_RATINGS);
	}
	
	// used in AddFilm view
	public void addFilmButtonPressed(ActionEvent event) {
		if (txtFilmTitle.getText().trim().isEmpty()) {
			lblError.setText(lblError.getText()+"Film title is missing");
		}
		if (txtDescription.getText().trim().isEmpty()) {
			if (!lblError.getText().trim().isEmpty()){
				lblError.setText(lblError.getText()+"\n");
			}
			lblError.setText(lblError.getText()+"Description is missing");
		}
		if (cbxAgeRating.getValue() == null) {
			if (!lblError.getText().trim().isEmpty()){
				lblError.setText(lblError.getText()+"\n");
			}
			lblError.setText(lblError.getText()+"Age Rating is missing");
		}
		/*if (image == null) {
			if (!lblError.getText().trim().isEmpty()){
				lblError.setText(lblError.getText()+"\n");
			}
			lblError.setText(lblError.getText()+"Image is missing");
		}*/
		if (lblError.getText().trim().isEmpty()) {
			// first create film with no screenings (Film object required to create Screening objects)
			Film film = new Film(txtFilmTitle.getText(), txtDescription.getText(), "", cbxAgeRating.getValue(), FXCollections.observableArrayList());
			ArrayList<Screening> screeningsToAdd = new ArrayList<Screening>();
			screeningDatesToAdd.add(LocalDateTime.now()); // for testing
			for (LocalDateTime dt : screeningDatesToAdd) {
				screeningsToAdd.add(new Screening(film.getFilmTitle(), dt, emptySeatPlan()));
			}
			film.addScreenings(screeningsToAdd);
			Main.filmList.add(film);
			txtFilmTitle.clear();
			txtDescription.clear();
			cbxAgeRating.setValue("Choose Age Rating");
			screeningDatesToAdd.clear();
		}
	}
	
	// 
	public void addScreeningsButtonPressed(ActionEvent event) {
	}
	
	// remove this and use some different way to accomplish things?
	public static Screening getScreeningForBooking(Booking booking) {
		for (Film f : Main.filmList) {
			for (Screening s : f.getScreenings()) {
				if (s.getDateTime().compareTo(booking.getDateTime()) == 0) {
					return s;
				}
			}
		}
		return null;
	}
	
	public static ObservableList<Screening> filterScreeningsByDate(LocalDate date) {
		ObservableList<Screening> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < Main.filmList.size(); i++) {
			for (int j = 0; j < Main.filmList.get(i).getScreenings().size(); j++) {
				if (date.equals(Main.filmList.get(i).getScreenings().get(j).getDateTime().toLocalDate())) {
					returnList.add(Main.filmList.get(i).getScreenings().get(j));
				}
			}
		}
		return returnList;
	}

	public static HashMap<String, Boolean> emptySeatPlan() {
		HashMap<String, Boolean> seats = new HashMap<String, Boolean>();
		for (int i = 0; i < 9; i++) {
			seats.put((char)('a' + i/3) + "" + (i%3 + 1), false);
		}
		return seats;
	}
	
	
}
