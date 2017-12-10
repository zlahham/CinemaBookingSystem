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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserController  extends MainController{

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
    private TableColumn<Film, String> tblclmnFilmsDescription;
    @FXML
    private TableColumn<Film, String> tblclmnFilmsScreenings;


    public void initialize() {
        initializeDashboard();
        setName();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            lblDate.setText(dateFormat.format(date));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    private void initializeDashboard() {

        tblFilms.getItems().addAll(Main.filmList);
        tblclmnFilmsImage.setCellValueFactory(c -> new SimpleObjectProperty<ImageView>(new ImageView(c.getValue().getImage())));
        tblclmnFilmsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
        tblclmnFilmsDescription.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        tblclmnFilmsScreenings.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getScreenings().toString()));

        tblFilms.setRowFactory(r -> {
            TableRow<Film> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    Film selectedFilm = row.getItem();
                    transition("NewFilm", "FCNewFilm");
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
