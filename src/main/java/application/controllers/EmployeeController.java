package application.controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class EmployeeController extends MainController {

	public void addFilms(ActionEvent event) {
		Parent addFilms;
		try {
			FilmController.mode = "addFilms";
			addFilms = FXMLLoader.load(getClass().getResource("/views/AddFilms.fxml"));
			Scene scene = new Scene(addFilms);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportFilms() {
	}
}
