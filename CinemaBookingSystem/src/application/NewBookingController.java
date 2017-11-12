package application;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class NewBookingController extends MainController{
	
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
}
