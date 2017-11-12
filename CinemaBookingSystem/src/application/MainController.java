package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainController {

	public void logout() {
		try {
			Main.user = null;
			
			Parent loginView;
			loginView = FXMLLoader
					.load(getClass().getResource("/application/Login.fxml"));
			Scene scene = new Scene(loginView, 750, 500);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void backToCustomerView(ActionEvent event) {
		try {
			Parent userView;
			userView = FXMLLoader
					.load(getClass().getResource("/application/Customer.fxml"));
			Scene scene = new Scene(userView, 750, 500);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
