package application;

import application.controllers.MainController;
import application.models.Booking;
import application.models.Film;
import application.models.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.Objects;

public class Main extends Application {
    public static Stage stage = new Stage();

    public static ObservableList<Film> filmList = FXCollections.observableArrayList();
    public static ObservableList<Booking> bookingList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        try {
            stage.setTitle("Cinema Booking System");
            stage.setResizable(false);

            Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainController.populateList("films");
        MainController.populateList("bookings");
        launch(args);
    }
}