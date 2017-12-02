package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CustomerController extends MainController{
	public void viewBookings(ActionEvent event) {
		Parent viewBookings;
		try {
			BookingsController.mode = "view";
			viewBookings = FXMLLoader.load(getClass().getResource("/application/ViewBookings.fxml"));
			Scene scene = new Scene(viewBookings, 1000, 1000);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void newBooking(ActionEvent event) {
		Parent newBooking;
		try {
			BookingsController.mode = "new";
			newBooking = FXMLLoader.load(getClass().getResource("/application/NewBooking.fxml"));
			Scene scene = new Scene(newBooking, 1000, 1000);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void changeDetails(ActionEvent event) {
		Parent changeDetails;
		try {
			changeDetails = FXMLLoader.load(getClass().getResource("/application/Profile.fxml"));
			Scene scene = new Scene(changeDetails, 1000, 1000);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
