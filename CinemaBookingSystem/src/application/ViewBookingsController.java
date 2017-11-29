package application;

import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ViewBookingsController extends MainController {
	@FXML
	private TableView<Booking> tblBookings;
	@FXML
	private TableColumn<Booking, String> tblclmnFilmTitle;
	@FXML
	private TableColumn<Booking, String> tblclmnDate;
	@FXML
	private TableColumn<Booking, String> tblclmnTime;
	@FXML
	private TableColumn<Booking, String> tblclmnSeats;
	@FXML
	private Button btnBack;
	@FXML
	private TableColumn<Booking, String> tblclmnDelete = new TableColumn<Booking, String>("Delete");

	public void initialize() {
		// tblBookings.getItems() is an ObservableList<Booking>;
		// here we set it equal to the customer's bookings field
		
		tblBookings.getItems().addAll(filterBookingsByCustomer((Customer)(Main.user)));
		
		// c is a TableColumn.CellDataFeatures<Booking, String> object, this class
		// being a wrapper class for the cells in the TableView
		// where does c come from?
		// why does the lambda return a Callback, not a SimpleStringProperty?
		// alternative to lambdas: PropertyValueFactory
		// SimpleStringProperty is a Property wrapper for a String
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		tblclmnFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
		tblclmnDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateTime().format(dateFormatter)));
		tblclmnTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
		tblclmnSeats.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSeats()));

		tblclmnDelete.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Booking, String>, TableCell<Booking, String>> cellFactory = //
				new Callback<TableColumn<Booking, String>, TableCell<Booking, String>>() {
					@Override
					public TableCell<Booking, String> call(final TableColumn<Booking, String> param) {
						final TableCell<Booking, String> cell = new TableCell<Booking, String>() {

							final Button btn = new Button("Delete");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										deleteBooking(getTableView().getItems().get(getIndex()).getBookingID());
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
		tblclmnDelete.setCellFactory(cellFactory);
	}
	

	public ObservableList<Booking> filterBookingsByCustomer(Customer customer) {
		ObservableList<Booking> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < Main.bookingList.size(); i++) {
			if (customer.getUsername().equals(Main.bookingList.get(i).getUsername())){
				returnList.add(Main.bookingList.get(i));
			}
			

		}
		return returnList;
	}
	
	// TODO: Move to BookingController when it is created
	public void deleteBooking(String bookingID) {
		for (Booking i : Main.bookingList) {
			if (i.getBookingID().compareTo(bookingID) == 0) {
				Main.bookingList.remove(i);
				return;
			}
		}
	}

}
