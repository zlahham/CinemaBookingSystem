package application.controllers;

import java.util.AbstractMap.SimpleEntry;

import application.models.Customer;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProfileController extends MainController {
	
	@FXML private Button btnBack;
	
	@FXML private TableView<SimpleEntry<String, String>> tblDetails;
	@FXML private TableColumn<SimpleEntry<String, String>, String> tblclmnDetail;
	@FXML private TableColumn<SimpleEntry<String, String>, String> tblclmnValue;
	
	public void initialize() {
		tblDetails.getItems().addAll(((Customer)(Main.user)).getDetails());
		tblclmnDetail.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getKey()));
		tblclmnValue.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getValue()));
	}
}
