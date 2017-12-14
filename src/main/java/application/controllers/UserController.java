package application.controllers;

import application.Main;
import application.models.Film;
import application.models.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

import com.sun.prism.paint.Color;

public class UserController  extends MainController{

    public static String mode = "";

    @FXML
    private Label lblDate;
    @FXML
    private Label lblWelcome;

    @FXML
    private TableView<Film> tblFilms;
    @FXML
    private TableColumn<Film, ImageView> tblclmnFilmsImage;
    @FXML
    private TableColumn<Film, String> tblclmnFilmsFilmTitle;
    @FXML
    private TableColumn<Film, String> tblclmnFilmsAgeRating;
    @FXML
    private TableColumn<Film, String> tblclmnFilmsScreenings;


    public void initialize() {
    	BookingController.chosenBooking = null;
    	BookingController.chosenScreening = null;
    	FilmController.chosenFilm = null;
    	FilmController.chosenDate = null;
    	FilmController.backFromFilmEmployee = new String[] {"Employee", ""};
    	
        initializeFilmTable();
        setName();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            lblDate.setText(dateFormat.format(date));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void initializeFilmTable() {
        tblFilms.getItems().addAll(Main.filmList);
        tblclmnFilmsImage.setCellValueFactory(c -> {
            ImageView iv = new ImageView();
            iv.setFitWidth(120);
            iv.setPreserveRatio(true);
            iv.setImage(c.getValue().getImage());
            return new SimpleObjectProperty<>(iv);
        });
        tblclmnFilmsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
        tblclmnFilmsAgeRating.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAgeRating()));
        tblclmnFilmsScreenings.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getScreenings()
                .stream().map(s -> s.getDateTime().format(dateTimeFormatter)).collect(Collectors.joining("\n"))));


        tblFilms.setRowFactory(r -> {
            TableRow<Film> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    FilmController.chosenFilm = row.getItem();
                    if (((User)(Main.stage.getUserData())).getRole().compareTo("employee") == 0) {
                    	transition("FilmEmployee", "FCFilmEmployee");
                    } else {
                    	transition("FilmCustomer", "FCFilmCustomer");
                    }
                }
            });
            return row;
        });
    }

    private void setName(){
        User user = (User) Main.stage.getUserData();
        lblWelcome.setText(user.getUsername());

    }
}
