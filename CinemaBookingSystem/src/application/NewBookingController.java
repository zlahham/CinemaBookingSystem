package application;

import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class NewBookingController extends MainController {

	// initialize films: move this somewhere else later
	FilmList filmList = new FilmList("assets/films.json");

	@FXML
	private DatePicker dtpckrDate;
	@FXML
	private TableView<Screening> tblFilms;
	@FXML
	private TableColumn<Screening, String> tblclmnFilmTitle;
	@FXML
	private TableColumn<Screening, String> tblclmnTime;
	// warning: this label is not set in the fxml yet
	@FXML
	private Label label = new Label("Select a date.");
	@FXML
	private Button btnBack;
	@FXML
	private TableColumn<Screening, String> tblclmnBook = new TableColumn<Screening, String>("Delete");

	public void initialize() {
		tblFilms.setPlaceholder(label);

		tblclmnBook.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Screening, String>, TableCell<Screening, String>> cellFactory = //
				new Callback<TableColumn<Screening, String>, TableCell<Screening, String>>() {
					@Override
					public TableCell<Screening, String> call(final TableColumn<Screening, String> param) {
						final TableCell<Screening, String> cell = new TableCell<Screening, String>() {

							final Button btn = new Button("Book");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										((Customer) (Main.user)).addBooking(getTableView().getItems().get(getIndex()));
										getTableView().getItems().remove(getTableView().getItems().get(getIndex()));
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};

		tblclmnBook.setCellFactory(cellFactory);

	}

	public void datePicked(ActionEvent event) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		// test; remove later
		System.out.println(dtpckrDate.getValue());
		/////
		ObservableList<Screening> screeningList = filmList.screeningsOnDate(dtpckrDate.getValue());
		if (screeningList.size() > 0) {
			tblFilms.getItems().addAll(screeningList);
			tblclmnFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
			tblclmnTime.setCellValueFactory(
					c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
		} else {
			tblFilms.getItems().clear();
			label.setText("No screenings on this date.");
		}
	}
}
