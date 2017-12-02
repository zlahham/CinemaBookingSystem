package application;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

public class NewBookingController extends CustomerController {

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
	private Button btnBackToCustomerView;
	@FXML
	private Button btnBackToNewBookingView;
	@FXML
	private Button btnBook;
	
	// seats view controls
	@FXML
	private GridPane grdpnlSeats = new GridPane();
	private ImageView[][] seats = new ImageView[3][3];
	private Image unbooked  = new Image("file:seat.png");
	private Image booked  = new Image("file:bookedseat.png");
	private Image selected  = new Image("file:selectedseat.png");
	
	// used in seats view
	private static Screening chosenScreening = null;
	private static HashMap<String, Boolean> seatsBooked;
	
	public void initialize() {

		// new booking view
		initializeNewBooking();
		
		// seats view
		if (chosenScreening != null) {
			initializeSeatPlan();
		}

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
	public void datePicked(ActionEvent event) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		ObservableList<Screening> screeningList = filterScreeningsByDate(dtpckrDate.getValue());
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
				grdpnlSeats.getChildren().add(seats[i][j]);
			}
		}
	}
	
	// used in seats view
	public void gridPaneClick(int i, int j) {
		seats[i][j].setOnMouseClicked(event -> {
			if (seats[i][j].getImage().equals(unbooked)) {
				grdpnlSeats.getChildren().remove(seats[i][j]);
				seats[i][j] = new ImageView(selected);
				grdpnlSeats.add(seats[i][j], j, i);
				seatsBooked.put((char)('a' + i) + "" + (j+1), true);
				gridPaneClick(i,j);
			} else if (seats[i][j].getImage().equals(selected)) {
				grdpnlSeats.getChildren().remove(seats[i][j]);
				seats[i][j] = new ImageView(unbooked);
				grdpnlSeats.add(seats[i][j], j, i);
				gridPaneClick(i,j);
			}
		});
	}
	
	// used in seats view
	public void bookButtonPressed(ActionEvent event) {
		// so this is huge mess (the part amending an existing booking)
		// is why it would be good to have the Screening contain usernames in seats
		// other things also need to be changed
		// TODO: change the logic here
		ObservableList<Booking> customerBookings = ViewBookingsController.filterBookingsByCustomer((Customer)(Main.user));
		// check if customer has bookings:
		if (customerBookings != null) {
			for (int i = 0; i < customerBookings.size(); i++) {
				// check if customer has bookings in chosenScreening:
				if (customerBookings.get(i).getDateTime().compareTo(chosenScreening.getDateTime()) == 0) {
					// iterate through bookingList to find the right booking to amend:
					for (int j = 0; j < Main.bookingList.size(); j++) {
						if (Main.bookingList.get(j).getDateTime().compareTo(chosenScreening.getDateTime()) == 0) {
							// amend customer's booking in chosenScreening:
							Main.bookingList.get(j).addSeats(seatsBooked);
							seatsBooked = null;
							break;
						}
					}
					break;
				}
			} // add a new booking if customer has no bookings in chosenScreening:
			if (seatsBooked != null) { // this if is a silly hack to prevent duplicate bookings; should maybe rewrite logic
				addBooking(chosenScreening, (Customer)(Main.user), seatsBooked);
				seatsBooked = null;
			}
		} else { // add a new booking if customer has no bookings:
			addBooking(chosenScreening, (Customer)(Main.user), seatsBooked);
			seatsBooked = null;
		}
		chosenScreening = null;
		this.transitionToUserView(Main.user);
	}
	
	public ObservableList<Screening> filterScreeningsByDate(LocalDate date) {
		ObservableList<Screening> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < Main.filmList.size(); i++) {
			for (int j = 0; j < Main.filmList.get(i).getScreenings().size(); j++) {
				if (date.equals(Main.filmList.get(i).getScreenings().get(j).getDateTime().toLocalDate())) {
					returnList.add(Main.filmList.get(i).getScreenings().get(j));
				}
			}
		}
		return returnList;
	}
	
	public void addBooking(Screening screening, Customer customer, HashMap<String, Boolean> seats) {
		Booking booking = new Booking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);
		Main.bookingList.add(booking);
		chosenScreening.addSeats(seats);
	}
}
