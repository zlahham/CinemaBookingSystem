package application.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import application.*;
import application.models.Booking;
import application.models.Customer;
import application.models.Screening;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class BookingController extends MainController {

    //TODO: move variable definitions into initialisation methods?

    // variable for initialisation control;
    public static String mode = "";

	public static Booking chosenBooking = null;
	public static Booking existingBooking;
	public static Screening chosenScreening = null;
    private static HashMap<String, Boolean> seatsBooked;

    // booking view controls
    @FXML
    private Label lblFilmTitle;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblTime;
    @FXML
    private Label lblSeats;
    @FXML
    private ImageView image;
    
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

    // TODO: nicer icons; effects instead of new icons for booked and selected seats?
    // seats view controls
    @FXML
    private GridPane grdpnSeats = new GridPane();
    private ImageView[][] seatsArray;

    private Image unbooked = new Image(Objects.requireNonNull(getClass().getClassLoader().
            getResource("images/seat.png")).toExternalForm());
    private Image booked = new Image(Objects.requireNonNull(getClass().getClassLoader().
            getResource("images/bookedseat.png")).toExternalForm());
    private Image selected = new Image(Objects.requireNonNull(getClass().getClassLoader().
            getResource("images/selectedseat.png")).toExternalForm());
	@FXML
	private Label lblFailure;

	public void initialize() {

		switch (mode) {
			case "BCBookings":
				initializeBookings();
				break;
			case "BCBooking":
				initializeBooking();
				break;
			case "BCBookingSeats":
				initializeSeatPlan();
				break;
			case "BCScreening":
				initializeSeatPlan();
				break;
			default:
				System.err.println(mode);
				System.err.println("Something has gone horribly wrong (BookingController) and it's probably Aleksi's fault");
				break;
		}
	}

	// view bookings view initialisation
	private void initializeBookings() {
		
		tblBookings.getItems().addAll(getBookingsByCustomer((Customer)(Main.stage.getUserData())));
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
        tblBookings.setRowFactory(r -> {
            TableRow<Booking> row = new TableRow<>();
            row.setOnMouseClicked(rowClick -> {
                if (!row.isEmpty() && rowClick.getButton() == MouseButton.PRIMARY
                        && rowClick.getClickCount() == 1) {
                    chosenBooking = row.getItem();
                    chosenScreening = FilmController.getScreeningForBooking(chosenBooking);
                    transition("Booking", "BCBooking");
                }
            });
            return row;
        });
	}

	// booking view initialization
	private void initializeBooking() {
		lblFilmTitle.setText(chosenBooking.getFilmTitle());
		lblDate.setText(chosenBooking.getDateTime().format(dateFormatter));
		lblTime.setText(chosenBooking.getDateTime().format(timeFormatter));
		lblSeats.setText(chosenBooking.getSeats().keySet().toString());
	   // image.setImage(Main.bookingList.get(index) chosenBooking.getFilmTitle());
	    
	}
	
	// seats view and screening view initialisation
	private void initializeSeatPlan() {
		int dimensions[] = (chosenScreening.getTheatreDimensions());
		seatsArray = new ImageView[dimensions[0]][dimensions[1] + 1];
		seatsBooked = new HashMap<String, Boolean>();
		if (mode.compareTo("BCBookingSeats") == 0) {
			existingBooking = getCustomerBookingForScreening((Customer)(Main.stage.getUserData()), chosenScreening);
		}
		for (int i = 0; i < seatsArray.length; i++) {
			for (int j = 0; j < seatsArray[i].length; j++) {
				if (chosenScreening.checkSeat((char) ('a' + i) + "" + (j + 1))) {
					if (mode.compareTo("BCBookingSeats") == 0) {
						if (existingBooking != null) {
							if (existingBooking.checkSeat((char) ('a' + i) + "" + (j + 1))) {
								seatsArray[i][j] = new ImageView(selected);
								seatsBooked.put((char) ('a' + i) + "" + (j + 1), true);
							}
						}
					} else {
						seatsArray[i][j] = new ImageView(booked);
					}
				} else {
					seatsArray[i][j] = new ImageView(unbooked);
				}

				if (mode.compareTo("BCBookingSeats") == 0) {
					gridPaneClick(i, j);
				}

				GridPane.setConstraints(seatsArray[i][j], j, i);
				grdpnSeats.getChildren().add(seatsArray[i][j]);
			}
		}
	}
	
	//used in Booking view
	public void deleteBookingButtonPress(ActionEvent event) {
		deleteBooking(chosenBooking.getBookingID());
		transition("Bookings", "BCBookings");
	}
	
	//used in Screening view
	public void deleteScreeningButtonPress(ActionEvent event) {
		FilmController.deleteScreening(chosenScreening);
		transition("ScreeningsEmployee", "FCScreeningsEmployee");
	}
	
	// used in seats view
	public void gridPaneClick(int i, int j) {
		seatsArray[i][j].setOnMouseClicked(event -> {
			if (j != (chosenScreening.getTheatreDimensions()[1] + 1 ) / 2) { //divider in the middle
			}
			if (seatsArray[i][j].getImage().equals(unbooked)) {
				grdpnSeats.getChildren().remove(seatsArray[i][j]);
				seatsArray[i][j] = new ImageView(selected);
				grdpnSeats.add(seatsArray[i][j], j, i);
				seatsBooked.put(getSeatKeyInGridPane(i,j), true);
				gridPaneClick(i,j);
			} else if (seatsArray[i][j].getImage().equals(selected)) {
				grdpnSeats.getChildren().remove(seatsArray[i][j]);
				seatsArray[i][j] = new ImageView(unbooked);
				grdpnSeats.add(seatsArray[i][j], j, i);
				seatsBooked.put(getSeatKeyInGridPane(i,j), false);
				gridPaneClick(i,j);
			} else {
				// TODO:display error message (seat already booked)
			}
		});
	}

	//used in BookingSeats view
	private String getSeatKeyInGridPane(int i, int j) {
		if (j < (Screening.theatreDimensions[1] / 2) + 1) {
			return ((char) ('a' + i) + "" + (j + 1));
		} else {
			return ((char) ('a' + i) + "" + j);
		}
	}
	
	// used in seats view
	public void bookButtonPressed(ActionEvent event) {

		//debugging
		if (existingBooking != null) {
			System.out.println("Existing booking seats: " + existingBooking.getSeats());
		}
		System.out.println("SeatsBooked seats: " + seatsBooked);
		//
		//check if customer has a booking a for the screening:
		if (existingBooking != null) {
			//check if seats have been changed:
			if (!getFullSeatPlan(existingBooking.getSeats()).equals(getFullSeatPlan(seatsBooked))) {
				// TODO: give the customer appropriate messages about whether they are amending
				// a booking or creating one etc
				// amend customer's booking in chosenScreening:
				System.out.println("Amending existing");
				chosenBooking = existingBooking;
				updateBookingSeats(chosenBooking.getBookingID(), getFullSeatPlan(seatsBooked));
				seatsBooked = null;
				existingBooking = null;
				// TODO: check the logic with this (chosenScreening) and the back buttons etc
				// chosenScreening = null;
				transition("Booking", "BCBooking");
			} else {
				lblFailure.setText("You have not modified your existing booking. Please either change your seats or press back to keep the booking as is.");
			}
		} else {
			if (seatsBooked.containsValue(true)) {
				System.out.println("Creating new");
				chosenBooking = addBooking(chosenScreening, (Customer) (Main.stage.getUserData()), seatsBooked);
				seatsBooked = null;
				existingBooking = null;
				// TODO: check the logic with this (chosenScreening) and the back buttons etc
				// chosenScreening = null;
				transition("Booking", "BCBooking");
			} else {
				lblFailure.setText("Please select seats to create a booking.");
			}
		}
	}
	
	public static Booking getBooking(String bookingID) {
		for (Booking b : Main.bookingList) {
			if (b.getBookingID().compareTo(bookingID) == 0) {
				return b;
			}
		}
		return null;
	}
	
	public static ObservableList<Booking> getBookingsByCustomer(Customer customer) {
		ObservableList<Booking> returnList = FXCollections.observableArrayList();
		for (Booking b : Main.bookingList) {
			if (customer.getUsername().equals(b.getUsername())){
				returnList.add(b);
			}
		}/*
		for (int i = 0; i < Main.bookingList.size(); i++) {
			if (customer.getUsername().equals(Main.bookingList.get(i).getUsername())){
				returnList.add(Main.bookingList.get(i));
			}
		}*/
		return returnList;
	}
	
	public static ObservableList<Booking> getBookingsForScreening(Screening screening) {
		ObservableList<Booking> returnList = FXCollections.observableArrayList();
		for (Booking b : Main.bookingList) {
			if (b.getDateTime().compareTo(screening.getDateTime()) == 0){
				returnList.add(b);
			}
		}
		return returnList;
	}
	
	
	public static Booking getCustomerBookingForScreening(Customer customer, Screening screening) {
		ObservableList<Booking> returnList = getBookingsByCustomer(customer);
		returnList.retainAll(getBookingsForScreening(screening));
		//A customer should only have one booking per screening, so the list should contain at 
		//most one element
		if (returnList.size() == 1) {
			return returnList.get(0);
		} else {
			//TODO: print error message?
			return null;
		}
	}
	
	// returns a reference to the Booking for convenience
	public Booking addBooking(Screening screening, Customer customer, HashMap<String, Boolean> seats) {
		Booking booking = new Booking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);
		Main.bookingList.add(booking);
		screening.updateSeats(seats);
		return getBooking(booking.getBookingID());
	}
	
	public static HashMap<String, Boolean> getFullSeatPlan(HashMap<String, Boolean> seats) {
		HashMap<String, Boolean> returnPlan = FilmController.getEmptySeatPlan(Screening.theatreDimensions);
		returnPlan.putAll(seats);
		return returnPlan;
	}
	
	// TODO: remove this; or merge it with addSeats in Booking?
	// add seat removal functionality to the same method
	// make the interfaces uniform
	public void updateBookingSeats(String bookingID, HashMap<String, Boolean> seats) {
		Booking booking = getBooking(bookingID);
		booking.updateSeats(seats);
		FilmController.getScreeningForBooking(booking).updateSeats(seats);
	}
	
	public static void deleteBooking(String bookingID) {
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
