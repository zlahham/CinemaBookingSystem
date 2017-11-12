package application;


import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NewBookingController {
	
	//initialize films: move this somewhere else later
	FilmList filmList = new FilmList("assets/films.json");
	
	@FXML private DatePicker dtpckrDate;
	@FXML private TableView<Screening> tblFilms;
	@FXML private TableColumn<Screening, String> tblclmnFilmTitle;
	@FXML private TableColumn<Screening, String> tblclmnTime;
	// warning: this label is not set in the fxml yet
	@FXML private Label label = new Label("Select a date.");
	@FXML private Button btnBack;
	
	public void initialize() {
		tblFilms.setPlaceholder(label);
	}
	public void datePicked(ActionEvent event) {
		System.out.println(dtpckrDate.getValue());
		ObservableList<Screening> screeningList = filmList.screeningsOnDate(dtpckrDate.getValue());
		if (screeningList.size() > 0) {
			tblFilms.getItems().addAll(screeningList);
			tblclmnFilmTitle.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getFilmTitle()));
			tblclmnTime.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getTime()));
		} else {
			tblFilms.getItems().clear();
			label.setText("No screenings on this date.");
		}	
	}
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
