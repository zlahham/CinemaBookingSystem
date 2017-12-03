package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.util.Callback;

public class FilmController extends MainController {

	public static final ObservableList<String> AGE_RATINGS = FXCollections
			.observableArrayList(Arrays.asList("U", "PG", "12A", "12", "15", "18", "R18"));
	
	public static String mode = "";
	
	private static ObservableList<LocalDateTime> screeningDateTimesToAdd = FXCollections.observableArrayList();
	private Image image;

	// AddFilms view controls
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
	
	// AddScreenings view controls
	@FXML
	private DatePicker dtpckrDate;
	@FXML
	private TableView<LocalTime> tblTimes;
	@FXML
	private TableColumn<LocalTime, String> tblclmnTimesTime;
	@FXML
	private TableColumn<LocalTime, String> tblclmnTimesAdd = new TableColumn<LocalTime, String>("Add");
	@FXML
	private Label lblDateInfo = new Label("Select a date.");
	@FXML
	private Button btnAdd;
	@FXML
	private TableView<LocalTime> tblTimes2;
	@FXML
	private TableColumn<LocalTime, String> tblclmnTimes2Time;
	@FXML
	private TableColumn<LocalTime, String> tblclmnTimes2Remove = new TableColumn<LocalTime, String>("Remove");
	@FXML
	private Button btnRemove;
	
	
	public void initialize() {
		
		switch (mode) {
		case "addFilms":
			initializeAddFilms();
			break;
		case "addScreenings":
			initializeAddScreenings();
			break;
		default:
			System.err.println("Something has gone horribly wrong and it's probably Aleksi's fault");
			break;
		}
	}
	
	// initialize addFilm view
	private void initializeAddFilms() {
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
			screeningDateTimesToAdd.add(LocalDateTime.now()); // for testing
			for (LocalDateTime dt : screeningDateTimesToAdd) {
				screeningsToAdd.add(new Screening(film.getFilmTitle(), dt, emptySeatPlan()));
			}
			film.addScreenings(screeningsToAdd);
			Main.filmList.add(film);
			txtFilmTitle.clear();
			txtDescription.clear();
			cbxAgeRating.setValue("Choose Age Rating");
			screeningDateTimesToAdd.clear();
		}
	}
	
	public void transitionToAddScreeningsView(ActionEvent event) {
		try {
			mode = "addScreenings";
			Parent addScreeningsView = FXMLLoader.load(getClass().getResource("/application/AddScreenings.fxml"));
			Scene scene = new Scene(addScreeningsView, 750, 500);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// initialize addScreenings view
	private void initializeAddScreenings() {
		ObservableList<LocalTime> screeningTimesToAdd = FXCollections.observableArrayList();
		tblclmnTimesAdd.setCellValueFactory(new PropertyValueFactory<>("dummy"));
		Callback<TableColumn<LocalTime, String>, TableCell<LocalTime, String>> cellFactory = 
				new Callback<TableColumn<LocalTime, String>, TableCell<LocalTime, String>>() {
					@Override
					public TableCell<LocalTime, String> call(final TableColumn<LocalTime, String> param) {
						final TableCell<LocalTime, String> cell = new TableCell<LocalTime, String>() {

							final Button btnAdd = new Button("Add");
							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btnAdd.setOnAction(event -> {
										screeningTimesToAdd.add(getTableView().getItems().get(getIndex()));
										getTableView().getItems().remove(getTableView().getItems().get(getIndex()));
									});
									setGraphic(btnAdd);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		tblclmnTimesAdd.setCellFactory(cellFactory);
	}
	
	// used in addScreenings view
	public void showAvailableTimesOnSelectedDate(ActionEvent event) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		ObservableList<LocalTime> timesList = getAvailableTimesByDate(dtpckrDate.getValue());
		if (timesList.size() > 0) {
			tblTimes.getItems().addAll(timesList);
			tblclmnTimesTime.setCellValueFactory(
					c -> new SimpleStringProperty(c.getValue().format(timeFormatter)));
		} else {
			tblTimes.getItems().clear();
			lblDateInfo.setText("No available times on this date.");
		}
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

	public static ObservableList<LocalTime> getAvailableTimesByDate(LocalDate date) {
		ObservableList<LocalTime> returnList = FXCollections.observableArrayList();
		for (int i = 12; i < 24; i++) {
			returnList.add(LocalTime.parse(i + ":00"));
		}
		for (Film f : Main.filmList) {
			for (Screening s : f.getScreenings() ) {
				if (s.getDateTime().toLocalDate().compareTo(date) == 0) {
					returnList.remove(s.getDateTime().toLocalTime());
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
