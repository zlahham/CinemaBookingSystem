package application;

import java.util.Iterator;

import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static Stage stage = new Stage();
	public static User user;

	public static ObservableList<Film> filmList = FXCollections.observableArrayList();
	public static BookingList bookingList = new BookingList("assets/cinemaBookingSystem.json");

	@Override
	public void start(Stage primaryStage) {

		try {
			stage.setTitle("Cinema Booking System");

			Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
			Scene scene = new Scene(root, 750, 500);

			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		populateFilmList();
		// populateBookingList();
		// System.out.println(FirebaseController.getList("users"));
		launch(args);
	}

	private static void populateFilmList() {
		JSONObject json = null;
		try {
			json = FirebaseController.getList("films");
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String filmKey = iterator.next();
			filmList.add(new Film(json.getJSONObject(filmKey)));
		}
	}
}

// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
