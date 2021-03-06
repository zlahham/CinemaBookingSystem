package application.controllers;

import application.Main;
import application.models.Booking;
import application.models.Customer;
import application.models.Film;
import application.models.Screening;
import application.services.Firebase;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Display Film and Screening information, and 
 * 	<dd> and manipulate Film and Screening objects
 * 
 * 	<dt> Description:
 * 	<dd> Contains all methods manipulating films and screenings,
 * 	<dd> and sets up the views primarily governed with film and 
 * 	<dd> screening tables and manipulation (FilmEmployee and
 * 	<dd> FilmCustomer (displaying information about a given film),
 * 	<dd> NewFilm (for adding new films), NewScreening (for adding new
 * 	<dd> screenings), NewBooking (for displaying a screenings table and
 * 	<dd> choosing one for a new booking), and ScreeningsCustomer and 
 * 	<dd> ScreeningsEmployee (for displaying a list of screenings for 
* 	<dd> a given film)).
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
public class FilmController extends MainController {

    private static final ObservableList<String> AGE_RATINGS = FXCollections
            .observableArrayList(Arrays.asList("U", "PG", "12A", "12", "15", "18", "R18"));

    //variable for initialisation control
    public static String mode;
    public static String backFromFilmEmployee[];
    
    //used in: Film views, Screening views, NewScreening view
    //changed in: Dashboard views
    public static Film chosenFilm = null;
    public static LocalDate chosenDate;

    //NewFilm view controls
    @FXML
    private ImageView image;
    @FXML
    private ImageView image1;
    @FXML
    private DatePicker dtpckrDate;
    @FXML
    private Label lblFilmDescription;
    @FXML
    private Label lblFilmTitle;
    @FXML
    private Label lblFilmAge;
    @FXML
    private TextField txtFilmTitle;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<String> cbxAgeRating;
    @FXML
    private Label lblError;
	@FXML
	private Label lblImageError;
	
	//NewFilm view variables
	private File chosenFile;
	private ArrayList<String> errors;

	//Screenings views controls
    @FXML
    private TableView<Screening> tblScreenings;
    @FXML
    private TableColumn<Screening, ImageView> tblclmnScreeningsFilmImage;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsFilmTitle;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsTime;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsFilmAge;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsSeats;
    @FXML
    private TableColumn<Screening, String> tblclmnScreeningsDate;

    private Label lblTableInfo;

    //NewScreening view controls
    @FXML
    private TableView<LocalTime> tblTimes;
    @FXML
    private TableColumn<LocalTime, String> tblclmnTimesTime;
    @FXML
    private TableView<LocalDateTime> tblDateTimesToAdd;
    @FXML
    private TableColumn<LocalDateTime, String> tblclmnDateTimesToAddDate;
    @FXML
    private TableColumn<LocalDateTime, String> tblclmnDateTimesToAddTime;
    
    //NewScreening view variables
    private ObservableList<LocalDateTime> screeningDateTimesToAdd;

    public void initialize() {
        switch (mode) {
            case "FCFilmEmployee":
                initializeFilm();
                break;
            case "FCFilmCustomer":
                initializeFilm();
                break;
            case "FCNewFilm":
				backFromFilmEmployee = new String[] {"NewFilm", "FCNewFilm"};
                initializeNewFilm();
                break;
            case "FCNewScreening":
                initializeNewScreening();
                break;
            case "FCNewBooking":
            	BookingController.chosenScreening = null;
				BookingController.chosenBooking = null;
            	BookingController.backFromBookingSeats = new String[] {"NewBooking", "FCNewBooking"};
                initializeNewBooking();
                break;
            case "FCScreeningsEmployee":
            	BookingController.chosenScreening = null;
                initializeScreenings();
                break;
            case "FCScreeningsCustomer":
            	BookingController.chosenScreening = null;
				BookingController.chosenBooking = null;
            	BookingController.backFromBookingSeats = new String[] {"ScreeningsCustomer", "FCScreeningsCustomer"};
                initializeScreenings();
                break;
            default:
                System.err.println(mode);
                System.err.println("FilmController mode error");
                break;
        }
    }

    //initialize Film views
    private void initializeFilm() {
        lblFilmTitle.setText(chosenFilm.getFilmTitle());
        lblFilmDescription.setText(chosenFilm.getDescription());
        lblFilmAge.setText(chosenFilm.getAgeRating());
        image.setImage(chosenFilm.getImage());
        image1.setImage(chosenFilm.getImage());
    }

	public void backFromFilmEmployee(ActionEvent event) {
		transition(backFromFilmEmployee[0],backFromFilmEmployee[1]);
	}
    
    //initialize NewFilm view
    private void initializeNewFilm() {
        cbxAgeRating.getItems().addAll(AGE_RATINGS);
        errors = new ArrayList<>();
    }

    //used in NewFilm view
    public void addFilmButtonPressed(ActionEvent event) {
        errors.clear();
        lblError.setText("");
        if (txtFilmTitle.getText().trim().isEmpty()) {
            errors.add("* Film title is missing");
        }
        for (Film f : Main.filmList) {
            if (f.getFilmTitle().compareTo(txtFilmTitle.getText().trim()) == 0) {
                errors.add("* A film with that title already exists. Please enter another title.");
                break;
            }
        }
        if (txtDescription.getText().trim().isEmpty()) {
            errors.add("* Description is missing");
        }
        if (cbxAgeRating.getValue() == null) {
            errors.add("* Age Rating is missing");
        }
        if (image.getImage() == null) {
            errors.add("* Image is missing");
        }
        if (errors.isEmpty()) {


            File destinationFile = new File(System.getProperty("user.dir") + "/images/" + txtFilmTitle.getText().trim());
            try {
                //Copies the image file from the specified source to the destination
                Files.copy(chosenFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, String> params = new HashMap<>();
            params.put("filmID", txtFilmTitle.getText().trim());
            params.put("ageRating", cbxAgeRating.getValue());
            params.put("description", txtDescription.getText().replace("\n", " ").replace("\r", " "));
            params.put("filmTitle", txtFilmTitle.getText());
            params.put("imageFileName", txtFilmTitle.getText().trim());
            try {
                Firebase.createFilm(params);
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            Film film = new Film(txtFilmTitle.getText().trim(), txtDescription.getText().trim(), txtFilmTitle.getText().trim(), cbxAgeRating.getValue(), FXCollections.observableArrayList());
            Main.filmList.add(film);
            chosenFilm = film;
            chosenFile = null;
            image = null;
            transition("FilmEmployee", "FCFilmEmployee");
        } else {
            for (String error : errors) {
                if (lblError.getText().trim().isEmpty()) {
                    lblError.setText(error);
                } else {
                    lblError.setText(lblError.getText() + "\n" + error);
                }
            }
        }
    }

    //used in NewFilm view
    public void pickImage() {
        lblImageError.setText("");
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        chosenFile = fileChooser.showOpenDialog(Main.stage);
//        if (chosenFile != null) {
//
//            Tika tika = new Tika();
//            try {
//                String mimeType = tika.detect(chosenFile).split("/")[0];
//                if (mimeType.compareTo("image") == 0) {
        String imagePath = null;
        try {
            imagePath = chosenFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Image imagePicked = new Image(imagePath, 500,500,true,true,true);
                    image.setImage(imagePicked);
//                } else {
//                    lblImageError.setText("* The file you chose does not seem to be an image.");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    //initialize Screenings views
    private void initializeScreenings() {
        if (chosenFilm.getScreenings().size() > 0) {
            tblScreenings.getItems().addAll(chosenFilm.getScreenings());
            tblclmnScreeningsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
            tblclmnScreeningsDate.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getDateTime().format(dateFormatter)));
            tblclmnScreeningsTime.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
            if (mode.compareTo("FCScreeningsEmployee") == 0) {
                tblclmnScreeningsSeats.setText("Seats Booked");
				tblclmnScreeningsSeats
						.setCellValueFactory(c -> new SimpleStringProperty(countBookedSeats(c.getValue())[0] + "/"
								+ (countBookedSeats(c.getValue())[0] + countBookedSeats(c.getValue())[1]) + ""));
            } else if (mode.compareTo("FCScreeningsCustomer") == 0) {
                tblclmnScreeningsSeats.setText("Seats Available");
                tblclmnScreeningsSeats.setCellValueFactory(c -> new SimpleStringProperty(countBookedSeats(c.getValue())[1] +""));
            } 

            //clicking a row takes a customer to seats selection; an employee to Screening view
            tblScreenings.setRowFactory(r -> {
                TableRow<Screening> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        BookingController.chosenScreening = row.getItem();
                        if (mode.compareTo("FCScreeningsEmployee") == 0) {
                            transition("Screening", "BCScreening");
                        } else if (mode.compareTo("FCScreeningsCustomer") == 0) {
                        	//customer can only click on date if it is the future
                        	if (row.getItem().getDateTime().isAfter(LocalDateTime.now())) {
                        		transition("BookingSeats", "BCBookingSeats");
                        	}
                        } else {
                            //TODO: print error message?
                        }
                    }
                });
                return row;
            });
            
            //change row background colour if customer has bookings in the screening
            tblclmnScreeningsSeats.setCellFactory(column -> {
                return new TableCell<Screening, String>() {
                    protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty ? "" : getItem().toString());
						setGraphic(null);
						TableRow<Screening> row = getTableRow();
						if (!isEmpty()) {
							// default colour: green
							row.setStyle("-fx-background-color:lightgreen");
							if (mode.compareTo("FCScreeningsCustomer") == 0) {
								Customer customer = (Customer) (Main.stage.getUserData());
								Booking booking = BookingController.getCustomerBookingForScreening(customer,
										row.getItem());
								if (booking != null) {
									//customer has bookings: blue
									row.setStyle("-fx-background-color:lightblue");
									setText(empty ? ""
											: getItem().toString() + "; Your seat(s): " + booking.getSeatsString());
								} 
							}
							//date is in the past: red
							if (row.getItem().getDateTime().isBefore(LocalDateTime.now())) {
								row.setStyle("-fx-background-color:lightcoral");
							}
						}
					}
				};
            });
            
        } else {
            lblTableInfo = new Label("This film has no screenings.");
            tblScreenings.setPlaceholder(lblTableInfo);
        }
    }

    //initialize NewBooking view
    private void initializeNewBooking() {
        lblTableInfo = new Label("Select a date.");
        tblScreenings.setPlaceholder(lblTableInfo);
        disableDatesInThePast();
        if (chosenDate != null) {
        	showScreeningsOnSelectedDate(chosenDate);
        }
    }

    public void datePickerChoose(ActionEvent event) {
    	showScreeningsOnSelectedDate(dtpckrDate.getValue());
    }
    
    // used in NewBooking view
    public void showScreeningsOnSelectedDate(LocalDate date) {
        tblScreenings.getItems().clear();
        ObservableList<Screening> screeningList = getScreeningsByDate(date);
        if (screeningList.size() > 0) {
            tblScreenings.getItems().addAll(screeningList);
            tblclmnScreeningsFilmImage.setCellValueFactory(c -> {
                ImageView iv = new ImageView();
                iv.setFitWidth(120);
                iv.setPreserveRatio(true);
                try {
                    iv.setImage(c.getValue().getFilm().getImage());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return new SimpleObjectProperty<>(iv);
            });
            tblclmnScreeningsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
            tblclmnScreeningsTime.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
            tblclmnScreeningsFilmAge.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getFilm().getAgeRating()));
            tblclmnScreeningsSeats.setCellValueFactory(
                    c -> new SimpleStringProperty(countBookedSeats(c.getValue())[1] + ""));

            tblScreenings.setRowFactory(r -> {
                TableRow<Screening> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        BookingController.chosenScreening = row.getItem();
                        chosenDate = date;
                        if (row.getItem().getDateTime().isAfter(LocalDateTime.now())) {
                        	transition("BookingSeats", "BCBookingSeats");
                        }
                    }
                });
                return row;
            });
			// change row background colour if customer has bookings in the screening
			tblclmnScreeningsSeats.setCellFactory(column -> {
				return new TableCell<Screening, String>() {
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty ? "" : getItem().toString());
						setGraphic(null);
						TableRow<Screening> row = getTableRow();
						if (row.getItem() != null) {
							// default colour: green
							row.setStyle("-fx-background-color:lightgreen");
							Customer customer = (Customer) (Main.stage.getUserData());
							Booking booking = BookingController.getCustomerBookingForScreening(customer, row.getItem());
							if (booking != null) {
								// customer has bookings: blue
								row.setStyle("-fx-background-color:lightblue");
								setText(empty ? ""
										: getItem().toString() + "; Your seat(s): " + booking.getSeatsString());
							}
							//date is in the past: red
							if (row.getItem().getDateTime().isBefore(LocalDateTime.now())) {
								row.setStyle("-fx-background-color:lightcoral");
							}
						}
					}
				};
			});

		} else {
            tblScreenings.getItems().clear();
            lblTableInfo.setText("No screenings on this date.");
        }
    }

    //initialize addScreenings view
    private void initializeNewScreening() {
        screeningDateTimesToAdd = FXCollections.observableArrayList();
        disableDatesInThePast();
    }

    //used in addScreenings view
    public void showAvailableTimesOnSelectedDate(ActionEvent event) {
        tblTimes.getItems().clear();
        ObservableList<LocalTime> timesList = getAvailableTimesByDate(dtpckrDate.getValue());
        if (timesList.size() > 0) {
            tblTimes.getItems().addAll(timesList);
            tblclmnTimesTime.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().format(timeFormatter)));
            tblTimes.setRowFactory(r -> {
                TableRow<LocalTime> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        screeningDateTimesToAdd.add(LocalDateTime.of(dtpckrDate.getValue(), row.getItem()));
                        tblDateTimesToAdd.getItems().add(LocalDateTime.of(dtpckrDate.getValue(), row.getItem()));
                        tblclmnDateTimesToAddDate.setCellValueFactory(
                                c -> new SimpleStringProperty(c.getValue().format(dateFormatter)));
                        tblclmnDateTimesToAddTime.setCellValueFactory(
                                c -> new SimpleStringProperty(c.getValue().format(timeFormatter)));
                        tblTimes.getItems().remove(row.getIndex());
                    }
                });
                return row;
            });

            tblDateTimesToAdd.setRowFactory(r -> {
                TableRow<LocalDateTime> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        screeningDateTimesToAdd.remove(row.getItem());
                        if (dtpckrDate.getValue().compareTo(row.getItem().toLocalDate()) == 0) {
                            tblTimes.getItems().add(row.getItem().toLocalTime());
                        }
                        tblDateTimesToAdd.getItems().remove(row.getItem());
                    }
                });
                return row;
            });
        } else {
            lblTableInfo = new Label("No available times on selected date.");
            tblTimes.setPlaceholder(lblTableInfo);
        }
    }

    //used in AddScreenings view
    public void addButtonPress(ActionEvent event) {
        ArrayList<Screening> screeningsToAdd = new ArrayList<Screening>();
        for (LocalDateTime dt : screeningDateTimesToAdd) {
            Screening s = new Screening(chosenFilm.getFilmTitle(), dt, getEmptySeatPlan(Screening.theatreDimensions));
            screeningsToAdd.add(s);
            createFirebaseScreening(chosenFilm.getFilmTitle(), dt.format(Screening.firebaseDateTimeFormatter));
        }
        chosenFilm.addScreenings(screeningsToAdd);
        screeningDateTimesToAdd.clear();
        transition("ScreeningsEmployee", "FCScreeningsEmployee");
    }

    public static int[] countBookedSeats(Screening s) {
        int[] bookedAvailable = {0, 0};
        for (Boolean value : s.getSeats().values()) {
            if (value.equals(true)) {
                bookedAvailable[0]++;
            } else {
                bookedAvailable[1]++;
            }
        }
        return bookedAvailable;
    }

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

    public static ObservableList<Screening> getScreeningsByDate(LocalDate date) {
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

    public static HashMap<String, Boolean> getEmptySeatPlan(int[] dimensions) {
        HashMap<String, Boolean> seats = new HashMap<String, Boolean>();
        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                seats.put((char) ('a' + i) + "" + (j + 1), false);
            }
        }
        return seats;
    }

    public static void deleteScreening(Screening screening) {
        for (Film f : Main.filmList) {
            Iterator<Screening> iterator = f.getScreenings().iterator();
            while (iterator.hasNext()) {
                Screening s = iterator.next();
                if (s.getScreeningID().compareTo(screening.getScreeningID()) == 0) {
                    // This needs to come first (before removing the Screening)
                    // because deleteBooking uses getScreeningForBooking, which uses the
                    // FilmList screening
                    for (Booking b : BookingController.getBookingsForScreening(screening)) {
                        BookingController.deleteBooking(b.getBookingID());
                    }
                    deleteFirebaseScreening(screening);
                    f.removeScreening(screening);
                    return;
                }
            }
        }
    }
    
    private void disableDatesInThePast() {
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        dtpckrDate.setDayCellFactory(dayCellFactory);
    }

    private static void createFirebaseScreening(String filmTitle, String dateTime) {
        Map<String, String> params = new HashMap<>();
        params.put("filmTitle", filmTitle);
        params.put("dateTime", dateTime);
        try {
            Firebase.createScreening(params);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
    
    private static void deleteFirebaseScreening(Screening screening) {
       	//deleteBooking already calls deleteFirebaseBooking, so no need to 
    	//delete Firebase bookings here
        try {
            Firebase.deleteScreening(screening.getScreeningID(), screening.getFilmTitle());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
