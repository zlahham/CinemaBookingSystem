package application;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.UnaryOperator;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.util.Callback;

public class BookingController extends CustomerController {
	
	//TODO: move variable definitions into initialisation methods?
	
	// variable for initialisation control;
	// TODO: come up with a better solution?
	public static String mode = "";
	
	// used in seats view
	private static Screening chosenScreening = null;
	private static HashMap<String, Boolean> seatsBooked;
	
	// view bookings view controls
	@FXML
	private TableView<Booking> tblBookings;
	@FXML
	private TableColumn<Booking, String> tblclmnBookingsFilmTitle;
	@FXML
	private TableColumn<Booking, String> tblclmnBookingsDate;
	@FXML
	private TableColumn<Booking, String> tblclmnBookingsTime;
	@FXML
	private TableColumn<Booking, String> tblclmnBookingsSeats;
	@FXML
	private TableColumn<Booking, String> tblclmnBookingsDelete = new TableColumn<Booking, String>("Delete");
	
	// new booking view controls
	@FXML
	private DatePicker dtpckrDate;
	@FXML
	private TableView<Screening> tblScreenings;
	@FXML
	private TableColumn<Screening, String> tblclmnScreeningsFilmTitle;
	@FXML
	private TableColumn<Screening, String> tblclmnScreeningsTime;
	@FXML
	private TableColumn<Screening, String> tblclmnScreeningsBook = new TableColumn<Screening, String>("Book");
	@FXML
	private Label lblDateInfo = new Label("Select a date.");
	@FXML
	private Button btnBook;
	
	// TODO: nicer icons; effects instead of new icons for booked and selected seats?
	// seats view controls
	@FXML
	private GridPane grdpnSeats = new GridPane();
	private ImageView[][] seats;
	private Image unbooked  = new Image("file:seat.png");
	private Image booked  = new Image("file:bookedseat.png");
	private Image selected  = new Image("file:selectedseat.png");
	@FXML
	private Label lblFailure;
	
	public void initialize() {

		switch (mode) {
			case "view":
				initializeViewBookings();
				break;
			case "new":
				initializeNewBooking();
				break;
			case "seats":
				initializeSeatPlan();
				break;
			default:
				System.err.println("Something has gone horribly wrong and it's probably Aleksi's fault");
				break;
		}
	}

	// view bookings view initialisation
	private void initializeViewBookings() {
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
		tblclmnBookingsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
		tblclmnBookingsDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateTime().format(dateFormatter)));
		tblclmnBookingsTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
		tblclmnBookingsSeats.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSeats().keySet().toString()));

		tblclmnBookingsDelete.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Booking, String>, TableCell<Booking, String>> cellFactory = 
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
		tblclmnBookingsDelete.setCellFactory(cellFactory);
	}
	
	// new booking view initialisation
	private void initializeNewBooking() {
		// set "select a date" label in NewBooking view
		// causes NullPointerExceptions; fix
		// tblFilms.setPlaceholder(label);
		
		// set up screening table
		tblclmnScreeningsBook.setCellValueFactory(new PropertyValueFactory<>("dummy"));
		Callback<TableColumn<Screening, String>, TableCell<Screening, String>> cellFactory = 
				new Callback<TableColumn<Screening, String>, TableCell<Screening, String>>() {
					@Override
					public TableCell<Screening, String> call(final TableColumn<Screening, String> param) {
						final TableCell<Screening, String> cell = new TableCell<Screening, String>() {

							final Button btnBook = new Button("Book");
							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btnBook.setOnAction(event -> {
										// note: this sets a REFERENCE to the screening; use it
										chosenScreening = getTableView().getItems().get(getIndex());
										try {
											mode = "seats";
											Parent seatsView = FXMLLoader.load(getClass().getResource("/application/Seats.fxml"));
											Scene scene = new Scene(seatsView, 750, 500);
											Main.stage.setScene(scene);
											Main.stage.show();
										} catch(IOException e) {
											e.printStackTrace();
										}
									});
									setGraphic(btnBook);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		tblclmnScreeningsBook.setCellFactory(cellFactory);
	}

	// used in new booking view
	public void showScreeningsOnSelectedDate(ActionEvent event) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		ObservableList<Screening> screeningList = FilmController.filterScreeningsByDate(dtpckrDate.getValue());
		if (screeningList.size() > 0) {
			tblScreenings.getItems().addAll(screeningList);
			tblclmnScreeningsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
			tblclmnScreeningsTime.setCellValueFactory(
					c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
		} else {
			tblScreenings.getItems().clear();
			lblDateInfo.setText("No screenings on this date.");
		}
	}
	
	// seats view initialisation
	private void initializeSeatPlan() {
		int dimensions[] = (chosenScreening.getTheatreDimensions());
		seats = new ImageView[dimensions[0]][dimensions[1]];
		seatsBooked = new HashMap<String, Boolean>();
		for (int i = 0; i < seats.length; i++) {
			for (int j = 0; j < seats[i].length; j++) {
				if (chosenScreening.checkSeat((char) ('a' + i) + "" + (j + 1))) {
					seats[i][j] = new ImageView(booked);
				} else {
					seats[i][j] = new ImageView(unbooked);
				}

				gridPaneClick(i, j);

				GridPane.setConstraints(seats[i][j], j, i);
				grdpnSeats.getChildren().add(seats[i][j]);
			}
		}
	}
	
	// used in seats view
	public void gridPaneClick(int i, int j) {
		seats[i][j].setOnMouseClicked(event -> {
			if (seats[i][j].getImage().equals(unbooked)) {
				grdpnSeats.getChildren().remove(seats[i][j]);
				seats[i][j] = new ImageView(selected);
				grdpnSeats.add(seats[i][j], j, i);
				seatsBooked.put((char)('a' + i) + "" + (j+1), true);
				gridPaneClick(i,j);
			} else if (seats[i][j].getImage().equals(selected)) {
				grdpnSeats.getChildren().remove(seats[i][j]);
				seats[i][j] = new ImageView(unbooked);
				grdpnSeats.add(seats[i][j], j, i);
				seatsBooked.remove((char)('a' + i) + "" + (j+1), true);
				gridPaneClick(i,j);
			} else {
				// display error message (seat already booked)
			}
		});
	}
	
	// used in seats view
	public void bookButtonPressed(ActionEvent event) {
		
		if (!seatsBooked.isEmpty()) {
			// so this is huge mess (the part amending an existing booking)
			// is why it would be good to have the Screening contain usernames in seats
			// other things also need to be changed
			// TODO: change the logic here, or change data structure
			ObservableList<Booking> customerBookings = filterBookingsByCustomer((Customer) (Main.user));
			// check if customer has bookings:
			if (customerBookings != null) {
				for (int i = 0; i < customerBookings.size(); i++) {
					// check if customer has bookings in chosenScreening:
					if (customerBookings.get(i).getDateTime().compareTo(chosenScreening.getDateTime()) == 0) {
						// amend customer's booking in chosenScreening:
						updateBookingSeats(customerBookings.get(i).getBookingID(), seatsBooked);
						seatsBooked = null;
						break;
					}
				} // add a new booking if customer has no bookings in chosenScreening:
				if (seatsBooked != null) { // this if is a silly hack to prevent duplicate bookings; should maybe
											// rewrite logic
					addBooking(chosenScreening, (Customer) (Main.user), seatsBooked);
					seatsBooked = null;
				}
			} else { // add a new booking if customer has no bookings:
				addBooking(chosenScreening, (Customer) (Main.user), seatsBooked);
				seatsBooked = null;
			}
			chosenScreening = null;
			this.transitionToUserView(Main.user);
		} else {
			lblFailure.setText("select some seats, man");
		}
	}
	
	public Booking getBooking(String bookingID) {
		for (Booking b : Main.bookingList) {
			if (b.getBookingID().compareTo(bookingID) == 0) {
				return b;
			}
		}
		return null;
	}
	
	public static ObservableList<Booking> filterBookingsByCustomer(Customer customer) {
		ObservableList<Booking> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < Main.bookingList.size(); i++) {
			if (customer.getUsername().equals(Main.bookingList.get(i).getUsername())){
				returnList.add(Main.bookingList.get(i));
			}
		}
		return returnList;
	}
	
	public void addBooking(Screening screening, Customer customer, HashMap<String, Boolean> seats) {
		Booking booking = new Booking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);
		Main.bookingList.add(booking);
		screening.updateSeats(seats);
	}
	
	// TODO: remove this; or merge it with addSeats in Booking?
	// add seat removal functionality to the same method
	// make the interfaces uniform
	public void updateBookingSeats(String bookingID, HashMap<String, Boolean> seats) {
		Booking booking = getBooking(bookingID);
		booking.addSeats(seats);
		FilmController.getScreeningForBooking(booking).updateSeats(seats);
	}

	public void deleteBooking(String bookingID) {
		//TODO change data structure to make this less horrendous
		//this first part removes the booked seats from the screening
		Booking b = getBooking(bookingID);
		Screening s = FilmController.getScreeningForBooking(b);
		// this is why we should convert from key-value pairs to strings
		// with the seats
		//TODO clean this up
		HashMap<String, Boolean> seatsToUnbook = new HashMap<String, Boolean>();
		Iterator<String> iterator = s.getSeats().keySet().iterator();
		String seatI = null;
		while (iterator.hasNext()) {
			seatI = iterator.next();
			if (b.getSeats().containsKey(seatI)) {
				seatsToUnbook.put(seatI, false);
			}
		}
		s.updateSeats(seatsToUnbook);				
		Main.bookingList.remove(b);
	}
}
