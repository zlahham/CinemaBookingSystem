package application;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class ChangeDetailsController {
	
	@FXML private Button btnBack;
	
	public void backToCustomerView(ActionEvent event) {
		try {
			Parent userView;
			userView = FXMLLoader
					.load(getClass().getResource("/application/" + StringUtils.capitalize("Customer.fxml")));
			Scene scene = new Scene(userView, 750, 500);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
