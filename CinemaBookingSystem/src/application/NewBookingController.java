package application;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Popup;
import javafx.util.Callback;

public class NewBookingController extends MainController {

	@FXML
	private DatePicker dtpckrDate;
	@FXML
	private TableView<Screening> tblFilms;
	@FXML
	private TableColumn<Screening, String> tblclmnFilmTitle;
	@FXML
	private TableColumn<Screening, String> tblclmnTime;
	@FXML
	private Label label = new Label("Select a date.");
	@FXML
	private Button btnBack;
	@FXML
	private TableColumn<Screening, String> tblclmnBook = new TableColumn<Screening, String>("Delete");
	public static Screening chosenScreening;

	public void initialize() {
		// set "select a date" label
		// causes NullPointerExceptions; replace
		// tblFilms.setPlaceholder(label);
		
		final Popup seatsWindow = new Popup();
		
		// set up screening table
		tblclmnBook.setCellValueFactory(new PropertyValueFactory<>("dummy"));
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
										chosenScreening =  getTableView().getItems().get(getIndex());
										try {
											seatsWindow.getContent().add((Parent)FXMLLoader.load(getClass().getResource("/application/Seats.fxml")));
										} catch(IOException e) {
											e.printStackTrace();
										}
										seatsWindow.show(Main.stage);
										ArrayList<String> x = new ArrayList<String>();
										x.add("a1");
										x.add("a2");
										Main.bookingList.addBooking(getTableView().getItems().get(getIndex()), (Customer)(Main.user), x);
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
		ObservableList<Screening> screeningList = Main.filmList.screeningsOnDate(dtpckrDate.getValue());
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
