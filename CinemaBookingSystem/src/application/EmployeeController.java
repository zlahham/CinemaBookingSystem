package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class EmployeeController extends MainController{

	public void addFilms(ActionEvent event) {
		Parent addFilms;
		try {
			addFilms = FXMLLoader.load(getClass().getResource("/application/AddFilms.fxml"));
			Scene scene = new Scene(addFilms, 1000, 1000);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportFilms() {
	}
}
