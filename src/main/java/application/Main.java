package application;

import application.controllers.MainController;
import application.models.Booking;
import application.models.Film;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    //Logging
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    // Main Stage to be used in the Application for all Scenes
    public static Stage stage = new Stage();

    // Initialize the Collections that will contain the local data for the Films and Bookings
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
        Main.LOGGER.info("INIT - FIREBASE - Retrieving Film & Bookings Data from Firebase...\n");
        MainController.populateList("films");
        MainController.populateList("bookings");
        Main.LOGGER.info("COMPLETED - FIREBASE - Data Retrieval from Firebase!\n");
        launch(args);
    }
}