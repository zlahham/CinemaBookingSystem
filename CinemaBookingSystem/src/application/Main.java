package application;

import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static Stage stage = new Stage();
	public static User user;
	public static FilmList filmList = new FilmList("assets/cinemaBookingSystem.json");
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

	public static void main(String[] args) throws UnirestException {
		FirebaseController.get("users", "");
		// launch(args);
	}
}

// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
