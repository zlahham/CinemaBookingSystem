package application;

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
	public static ObservableList<Booking> bookingList = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {

		try {
			stage.setTitle("Cinema Booking System");

			Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
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