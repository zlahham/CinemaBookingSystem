package application.controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class EmployeeController extends MainController {

	public void addFilms(ActionEvent event) {
		transition("AddFilms", "FCAddFilms");
	}

	public void exportFilms() {
	}
}
