package application.controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CustomerController extends MainController {
	public void viewBookings(ActionEvent event) {
		Parent viewBookings;
		try {
			BookingController.mode = "view";
			viewBookings = FXMLLoader.load(getClass().getResource("/views/ViewBookings.fxml"));
			Scene scene = new Scene(viewBookings);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void newBooking(ActionEvent event) {
		Parent newBooking;
		try {
			FilmController.mode = "new";
			newBooking = FXMLLoader.load(getClass().getResource("/views/NewBooking.fxml"));
			Scene scene = new Scene(newBooking);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void changeDetails(ActionEvent event) {
		Parent changeDetails;
		try {
			changeDetails = FXMLLoader.load(getClass().getResource("/views/Profile.fxml"));
			Scene scene = new Scene(changeDetails);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
