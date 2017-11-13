package application;


import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ChangeDetailsController extends MainController{
	
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
