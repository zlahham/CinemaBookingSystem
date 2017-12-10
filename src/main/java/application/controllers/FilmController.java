package application.controllers;

import application.Main;
import application.models.Booking;
import application.models.Film;
import application.models.Screening;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;

public class FilmController extends EmployeeController {
	
    public static final ObservableList<String> AGE_RATINGS = FXCollections
            .observableArrayList(Arrays.asList("U", "PG", "12A", "12", "15", "18", "R18"));

    public static String mode;

    private static ObservableList<LocalDateTime> screeningDateTimesToAdd = FXCollections.observableArrayList();
    private static Film selectedFilm = null;
	private static File filePicked;
	private static ArrayList<String> errors =  new ArrayList<String>();

    // NewFilms and EditFilms view controls
    @FXML
    private Label lblViewTitle;
    @FXML
    private TextField txtFilmTitle;
    @FXML
    private TextField txtDescription;
    @FXML
    private ComboBox<String> cbxAgeRating;
    @FXML
    private ImageView image = new ImageView();
    @FXML
    private Button btnUploadImage;
    @FXML
    private Label lblError;
	@FXML
	private Label lblImageError;

    // EditFilms view controls (not in NewFilm)
    @FXML
    private Label lblFilmTitle;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblAgeRating;

    // AddScreenings and SelectScreening view controls
    @FXML
    private DatePicker dtpckrDate;
    @FXML
    private TableView<Screening> tblScreenings;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsFilmTitle;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsTime;
    @FXML
    private Label lblDateInfo = new Label("Select a date.");

    // AddScreening view controls (not in SelectScreenings)
    @FXML
    private TableView<LocalTime> tblTimes;
    @FXML
    private TableColumn<LocalTime, String> tblclmnTimesTime;
    @FXML
    private TableColumn<LocalTime, String> tblclmnTimesAdd = new TableColumn<LocalTime, String>("Add");
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
            case "FCNewFilm":
                initializeNewFilm();
                break;
            case "FCAddScreenings":
                initializeAddScreenings();
                break;
            case "FCNewBooking":
                initializeNewBooking();
                break;
            default:
                System.err.println(mode);
                System.err.println("Something has gone horribly wrong (FilmController) and it's probably Aleksi's fault");
                break;
        }
    }

	// initialize NewFilm view
	private void initializeNewFilm() {
		cbxAgeRating.getItems().addAll(AGE_RATINGS);
	}

	// used in NewFilm view
	public void addFilmButtonPressed(ActionEvent event) {
		errors.clear();
		lblError.setText("");
		if (txtFilmTitle.getText().trim().isEmpty()) {
			errors.add("Film title is missing");
		}
		for (Film f : Main.filmList) {
			if (f.getFilmTitle().compareTo(txtFilmTitle.getText().trim()) == 0) {
				errors.add("A film with that title already exists. Please enter another title.");
				break;
			}
		}
		if (txtDescription.getText().trim().isEmpty()) {
			errors.add("Description is missing");
		}
		if (cbxAgeRating.getValue() == null) {
			errors.add("Age Rating is missing");
		}
		if (image.getImage() == null) {
			errors.add("Image is missing");
		}
		if (errors.isEmpty()) {
			
			try {
				Files.copy(filePicked.toPath(), Paths.get("src/main/resources/images/" + txtFilmTitle.getText().trim()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			Film film = new Film(txtFilmTitle.getText().trim(), txtDescription.getText().trim(), "images/" + txtFilmTitle.getText().trim(), cbxAgeRating.getValue(), FXCollections.observableArrayList());
			Main.filmList.add(film);
			// needed?
			txtFilmTitle.clear();
			txtDescription.clear();
			cbxAgeRating.setValue("Choose Age Rating");
			filePicked = null;
			image = null;	
			transition("Employee", "");
		} else {
			for (String error : errors) {
				if (lblError.getText().trim().isEmpty()){
					lblError.setText(error);
				} else {
					lblError.setText(lblError.getText() + "\n" + error);
				}
			}
		}
	}
	
	public void pickImage() {
		lblImageError.setText("");
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose image");
		filePicked = fileChooser.showOpenDialog(Main.stage);
		if (filePicked != null) {
	        //try {
				//String[] type = Files.probeContentType(filePicked.toPath()).split("/");
				//if (type[0].compareTo("image") == 0) {
					Image imagePicked = new Image(filePicked.toURI().toString());
					image.setPreserveRatio(true);
					image.setFitHeight(200);
					image.setFitWidth(200);
					image.setImage(imagePicked);
				//} else {
				//	lblImageError.setText("The file you chose does not seem to be an image.");
				//}
		//	} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
		}
	}
	
	// screening stuff
	/* get this later
	ArrayList<Screening> screeningsToAdd = new ArrayList<Screening>();
	screeningDateTimesToAdd.add(LocalDateTime.now()); // for testing
	for (LocalDateTime dt : screeningDateTimesToAdd) {
		screeningsToAdd.add(new Screening(film.getFilmTitle(), dt, emptySeatPlan(Screening.theatreDimensions)));
	}
	film.addScreenings(screeningsToAdd);
	screeningDateTimesToAdd.clear(); (after other stuff)*/
	
	// film view stuff
	/*if (selectedFilm != null) {
	(reference these in the fxml to get them to work)
	lblViewTitle.setText("Edit film details");
	lblFilmTitle.setText(selectedFilm.getFilmTitle());
	lblDescription.setText(selectedFilm.getDescription());
	lblFilmTitle.setText(selectedFilm.getFilmTitle());
	lblAgeRating.setText(selectedFilm.getAgeRating());
	image.setImage(selectedFilm.getImage());
	}*/

    // new booking view initialisation
    private void initializeNewBooking() {
        // set "select a date" label in NewBooking view
        // causes NullPointerExceptions; fix
        // tblFilms.setPlaceholder(label);
    }

    // used in select screening view
    public void showScreeningsOnSelectedDate(ActionEvent event) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        ObservableList<Screening> screeningList = FilmController.filterScreeningsByDate(dtpckrDate.getValue());
        if (screeningList.size() > 0) {
            tblScreenings.getItems().addAll(screeningList);
            tblclmnScreeningsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
            tblclmnScreeningsTime.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));

            tblScreenings.setRowFactory(r -> {
                TableRow<Screening> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        BookingController.chosenScreening = row.getItem();
                        transition("Seats", "BCSeats");
                    }
                });
                return row;
            });
        } else {
            tblScreenings.getItems().clear();
            lblDateInfo.setText("No screenings on this date.");
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
            for (Screening s : f.getScreenings()) {
                if (s.getDateTime().toLocalDate().compareTo(date) == 0) {
                    returnList.remove(s.getDateTime().toLocalTime());
                }
            }
        }
        return returnList;
    }

    public static HashMap<String, Boolean> emptySeatPlan(int[] dimensions) {
        HashMap<String, Boolean> seats = new HashMap<String, Boolean>();
        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; i < dimensions[1]; j++) {
                seats.put((char) ('a' + i / 3) + "" + (j % 3 + 1), false);
            }
        }
        return seats;
    }

}
