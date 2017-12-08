package application.controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class CustomerController extends MainController {
	public void viewBookings(ActionEvent event) {
		transition("Login", "BCView");
	}
	public void newBooking(ActionEvent event) {
		transition("Login", "FCNew");
	}
	public void changeDetails(ActionEvent event) {
		transition("Profile", "");
	}
}
