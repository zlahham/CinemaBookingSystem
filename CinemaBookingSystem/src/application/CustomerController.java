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
			viewBookings = FXMLLoader.load(getClass().getResource("/application/ViewBookings.fxml"));
			Scene scene = new Scene(viewBookings, 1000, 1000);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void newBooking() {
	}
	public void changeDetails() {
	}
	
}
