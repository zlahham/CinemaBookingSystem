package application.controllers;

import application.Main;
import application.models.Booking;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class FilmController extends MainController {

    private static final ObservableList<String> AGE_RATINGS = FXCollections
            .observableArrayList(Arrays.asList("U", "PG", "12A", "12", "15", "18", "R18"));

    public static String mode;
    public static Film selectedFilm = null;
    private static ObservableList<LocalDateTime> screeningDateTimesToAdd;
    private static File filePicked;
    private static ArrayList<String> errors = new ArrayList<String>();

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
    private Button btnUploadImage;
    @FXML
    private Label lblError;
    @FXML
    private Label lblImageError;

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

    public void initialize() {
        switch (mode) {
            case "FCFilmEmployee":
                initializeFilm();
                break;
            case "FCFilmCustomer":
                initializeFilm();
                break;
            case "FCNewFilm":
                initializeNewFilm();
                break;
            case "FCNewScreening":
                initializeNewScreening();
                break;
            case "FCNewBooking":
                initializeNewBooking();
                break;
            case "FCScreeningsEmployee":
                initializeScreenings();
                break;
            case "FCScreeningsCustomer":
                initializeScreenings();
                break;
            default:
                System.err.println(mode);
                System.err.println("Something has gone horribly wrong (FilmController) and it's probably Aleksi's fault");
                break;
        }
    }

    // used in Screenings views and export
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
                    f.removeScreening(screening);
                    return;
                }
            }
        }
    }

    //initialize Film views
    private void initializeFilm() {
        lblFilmTitle.setText(selectedFilm.getFilmTitle());
        lblFilmDescription.setText(selectedFilm.getDescription());
        lblFilmAge.setText(selectedFilm.getAgeRating());
        image.setImage(selectedFilm.getImage());
        image1.setImage(selectedFilm.getImage());
    }

    private void initializeNewFilm() {
        image = new ImageView();
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
                Files.copy(filePicked.toPath(), Paths.get("src/main/resources/images/films/" + txtFilmTitle.getText().trim()));
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
            filePicked = null;
            image = null;
            transition("Employee", "");
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

    // used in NewFilm view
    public void pickImage() {
        lblImageError.setText("");
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        filePicked = fileChooser.showOpenDialog(Main.stage);
        if (filePicked != null) {

            Tika tika = new Tika();
            try {
                String mimeType = tika.detect(filePicked).split("/")[0];
                if (mimeType.compareTo("image") == 0) {
                    Image imagePicked = new Image(filePicked.toURI().toString());
                    image.setPreserveRatio(true);
                    //set in fxml instead?
                    image.setFitHeight(200);
                    image.setFitWidth(200);
                    image.setImage(imagePicked);
                    image.setEffect(new DropShadow(10, 10, 10, Color.rgb(0, 0, 0)));
                } else {
                    lblImageError.setText("The file you chose does not seem to be an image.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // initialize Screenings views
    private void initializeScreenings() {
        if (selectedFilm.getScreenings().size() > 0) {
            tblScreenings.getItems().addAll(selectedFilm.getScreenings());
            tblclmnScreeningsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
            tblclmnScreeningsDate.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getDateTime().format(dateFormatter)));
            tblclmnScreeningsTime.setCellValueFactory(
                    c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
            if (mode.compareTo("ScreeningsEmployee") == 0) {
                tblclmnScreeningsSeats.setText("Seats booked");
                tblclmnScreeningsSeats
                        .setCellValueFactory(c -> new SimpleStringProperty(countBookedSeats(c.getValue())[0] + "/"
                                + (countBookedSeats(c.getValue())[0] + countBookedSeats(c.getValue())[1])
                                + " seats booked"));
            } else if (mode.compareTo("FCScreeningsCustomer") == 0) {
                tblclmnScreeningsSeats.setText("Seats available");
                tblclmnScreeningsSeats.setCellValueFactory(
                        c -> new SimpleStringProperty(countBookedSeats(c.getValue())[1] + " seats available"));
            } else {
                // TODO: print error message?
            }

            tblScreenings.setRowFactory(r -> {
                TableRow<Screening> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        BookingController.chosenScreening = row.getItem();
                        if (mode.compareTo("FCScreeningsEmployee") == 0) {
                            transition("Screening", "BCScreening");
                        } else if (mode.compareTo("FCScreeningsCustomer") == 0) {
                            transition("BookingSeats", "BCBookingSeats");
                        } else {
                            //TODO: print error message?
                        }
                    }
                });
                return row;
            });
        } else {
            lblTableInfo = new Label("This film has no screenings.");
            tblScreenings.setPlaceholder(lblTableInfo);
        }
    }

    // new booking view initialisation
    private void initializeNewBooking() {
        lblTableInfo = new Label("Select a date.");
        tblScreenings.setPlaceholder(lblTableInfo);
        disableDatesInThePast();
    }

    // used in new booking view
    public void showScreeningsOnSelectedDate(ActionEvent event) {
        tblScreenings.getItems().clear();
        ObservableList<Screening> screeningList = filterScreeningsByDate(dtpckrDate.getValue());
        if (screeningList.size() > 0) {
            tblScreenings.getItems().addAll(screeningList);
            tblclmnScreeningsFilmImage.setCellValueFactory(c -> {
                ImageView iv = new ImageView();
                iv.setFitHeight(75);
                iv.setFitWidth(75);
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
                    c -> new SimpleStringProperty(countBookedSeats(c.getValue())[1] + " seats available"));

            tblScreenings.setRowFactory(r -> {
                TableRow<Screening> row = new TableRow<>();
                row.setOnMouseClicked(rowClick -> {
                    if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                            && rowClick.getClickCount() == 1) {
                        BookingController.chosenScreening = row.getItem();
                        transition("BookingSeats", "BCBookingSeats");
                    }
                });
                return row;
            });
        } else {
            tblScreenings.getItems().clear();
            lblTableInfo.setText("No screenings on this date.");
        }
    }

    // initializeaddScreenings view
    private void initializeNewScreening() {
        screeningDateTimesToAdd = FXCollections.observableArrayList();
    }

    // used in addScreenings view
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
            Screening s = new Screening(selectedFilm.getFilmTitle(), dt, getEmptySeatPlan(Screening.theatreDimensions));
            screeningsToAdd.add(s);
        }
        selectedFilm.addScreenings(screeningsToAdd);
        screeningDateTimesToAdd.clear();
        transition("ScreeningsEmployee", "FCScreeningsEmployee");
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
}
