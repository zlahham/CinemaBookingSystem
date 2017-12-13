package application.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import application.*;
import application.models.Booking;
import application.models.Customer;
import application.models.Screening;
import application.services.Firebase;
import com.jfoenix.controls.JFXButton;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;

public class BookingController extends MainController {

    //TODO: move variable definitions into initialisation methods?

    //variable for initialisation control
    public static String mode = "";
    public static String backFromSeats[];

    // used in: Booking
    // changed in: Bookings, Booking, BookingSeats
	public static Booking chosenBooking = null;
    // used in: BookingSeats, Screening
    // changed in: Bookings, Screening, Screenings, NewBooking
	public static Screening chosenScreening = null;

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
    private ImageView image, image1;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnToBookingSeats_BCBookingSeats;


    // view bookings view controls
    @FXML
    private TableView<Booking> tblBookings;
	@FXML
	private TableColumn<Booking, String> tblclmnBookingsFilmStatus;
    @FXML
    private TableColumn<Booking, String> tblclmnBookingsFilmTitle;
    @FXML
    private TableColumn<Booking, String> tblclmnBookingsDate;
    @FXML
    private TableColumn<Booking, String> tblclmnBookingsTime;
    @FXML
    private TableColumn<Booking, String> tblclmnBookingsSeats;

    // BookingSeats view controls
    @FXML
    private GridPane grdpnSeats = new GridPane();
	@FXML
	private Label lblFailure;
	@FXML
	private Label lblBookingSeatsTitle;
	@FXML
	private Button btnBook;
	
	//BookingSeats view variables
    private ImageView[][] seatsArray;
	private Booking existingBooking;
    private HashMap<String, Boolean> seatsBooked;
    // TODO: nicer icons; effects instead of new icons for booked and selected seats?
    private Image unbooked = new Image(Objects.requireNonNull(getClass().getClassLoader().
            getResource("images/seat.png")).toExternalForm());
    private Image booked = new Image(Objects.requireNonNull(getClass().getClassLoader().
            getResource("images/bookedseat.png")).toExternalForm());
    private Image selected = new Image(Objects.requireNonNull(getClass().getClassLoader().
            getResource("images/selectedseat.png")).toExternalForm());

	public void initialize() {

		switch (mode) {
			case "BCBookings": //Customer
				//access to Dashboard, Booking
				//accessed from Dashboard, Booking
				//uses:
				//changes: chosenBooking, chosenScreening
				chosenScreening = null;
				chosenBooking = null;
				initializeBookings();
				break;
			case "BCBooking": //Customer
				//access to Dashboard, Bookings (via back to Bookings and delete), BookingSeats (via edit)
				//accessed from Bookings, BookingSeats
				//uses: chosenBooking
				//changes:
				backFromSeats = new String[] {"Booking", "BCBooking"};
				initializeBooking();
				break;
			case "BCBookingSeats": //Customer
				//access to Dashboard, NewBooking (via back), Screenings (via back), Booking (via Book)
				//accessed from Booking, 
				//uses: chosenBooking, chosenScreening
				//changes: chosBooking
				initializeSeatPlan();
				break;
			case "BCScreening": //Employee
				//access to Dashboard, Screenings (via back and delete)
				//accessed from Screenings
				//uses: chosenScreening
				//changes: chosenScreening
				initializeSeatPlan();
				break;
			default:
				System.err.println(mode);
				System.err.println("Something has gone horribly wrong (BookingController) and it's probably Aleksi's fault");
				break;
		}
	}

	// bookings view initialisation
	private void initializeBookings() {
		
		tblBookings.getItems().addAll(getBookingsByCustomer((Customer)(Main.stage.getUserData())));
		tblclmnBookingsFilmStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().status()));
		tblclmnBookingsFilmTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFilmTitle()));
		tblclmnBookingsDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateTime().format(dateFormatter)));
		tblclmnBookingsTime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDateTime().format(timeFormatter)));
		tblclmnBookingsSeats.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSeatsString()));

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
        if (chosenBooking.getDateTime().isBefore(LocalDateTime.now())) {
            btnDelete.setDisable(true);
            btnToBookingSeats_BCBookingSeats.setDisable(true);
        }
		lblFilmTitle.setText(chosenBooking.getFilmTitle());
		lblDate.setText(chosenBooking.getDateTime().format(dateFormatter));
		lblTime.setText(chosenBooking.getDateTime().format(timeFormatter));
		lblSeats.setText(chosenBooking.getSeatsString());
	    image.setImage(chosenBooking.getFilm().getImage());
        image1.setImage(chosenBooking.getFilm().getImage());
	}
	
	//BookingSeats view and Screening view initialisation
	private void initializeSeatPlan() {
		int dimensions[] = (chosenScreening.getTheatreDimensions());
		seatsArray = new ImageView[dimensions[0]][dimensions[1] + 1];
		seatsBooked = new HashMap<String, Boolean>();
		existingBooking = null;
		if (mode.compareTo("BCBookingSeats") == 0) {
			existingBooking = getCustomerBookingForScreening((Customer)(Main.stage.getUserData()), chosenScreening);
			chosenBooking = existingBooking; //update for Booking view
		}
		
		if (existingBooking != null) {
			btnBook.setText("Confirm");
			lblBookingSeatsTitle.setText("Choose new seats!\n(Editing existing booking)");
		}
		
		for (int i = 0; i < seatsArray.length; i++) {
			for (int j = 0; j < seatsArray[i].length; j++) {
				//divider in the middle -> don't populate
				if (j != (chosenScreening.getTheatreDimensions()[1] + 1) / 2) {
					//seat free -> unbooked image
					if (chosenScreening.checkSeat(getSeatKeyInGridPane(i,j))) {
						//not BCBookingSeats mode (ie we are on BCScreening Mode
						//-> no selected images, just booked
						if (mode.compareTo("BCBookingSeats") == 0) {
							//no booking for this screening -> booked image
							if (existingBooking != null) {
								//for the seats in the existing booking by the customer
								//-> selected image
								if (existingBooking.checkSeat(getSeatKeyInGridPane(i,j))) {
									seatsArray[i][j] = new ImageView(selected);
									seatsBooked.put(getSeatKeyInGridPane(i,j), true);
								} else {
									//for seats that are booked by someone else
									//-> booked image
									seatsArray[i][j] = new ImageView(booked);
								}
							} else {
								seatsArray[i][j] = new ImageView(booked);
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

	// used in BookingSeats view
	public void gridPaneClick(int i, int j) {
		seatsArray[i][j].setOnMouseClicked(event -> {
			if (seatsArray[i][j].getImage().equals(unbooked)) {
				grdpnSeats.getChildren().remove(seatsArray[i][j]);
				seatsArray[i][j] = new ImageView(selected);
				grdpnSeats.add(seatsArray[i][j], j, i);
				seatsBooked.put(getSeatKeyInGridPane(i, j), true);
				gridPaneClick(i, j);
			} else if (seatsArray[i][j].getImage().equals(selected)) {
				grdpnSeats.getChildren().remove(seatsArray[i][j]);
				seatsArray[i][j] = new ImageView(unbooked);
				grdpnSeats.add(seatsArray[i][j], j, i);
				seatsBooked.put(getSeatKeyInGridPane(i, j), false);
				gridPaneClick(i, j);
			} else {
				// TODO:display error message (seat already booked)?
			}
		});
	}

	//used in BookingSeats view
	private String getSeatKeyInGridPane(int i, int j) {
		if (j < (chosenScreening.getTheatreDimensions()[1] + 1) / 2) {
			return ((char) ('d' - i) + "" + (j + 1));
		} else {
			return ((char) ('d' - i) + "" + j);
		}
	}
	
	// used in BookingSeats view
	public void bookButtonPressed(ActionEvent event) {
		
		if (seatsBooked.containsValue(true)) {
			// check if customer has a booking a for the screening:
			if (existingBooking != null) {
				// amend customer's booking in chosenScreening:
				chosenBooking = existingBooking;
				updateBookingSeats(chosenBooking.getBookingID(), seatsBooked);
				seatsBooked = null;
				existingBooking = null;
				transition("Booking", "BCBooking");
			} else {
				chosenBooking = addBooking(chosenScreening, (Customer) (Main.stage.getUserData()), seatsBooked);
				seatsBooked = null;
				transition("Booking", "BCBooking");
			}
		} else {
			if (existingBooking == null) {
				lblFailure.setText("Cannot create booking without any seats. Please select some seats.");
			} else {
				lblFailure.setText(
						"Cannot create booking without any seats. Please select some seats. If you wish to delete an existing booking, please do so on the Booking page.");
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
		}
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
	
	public void backFromSeats(ActionEvent event) {
		transition(backFromSeats[0],backFromSeats[1]);
	}
	
	// returns a reference to the Booking for convenience
	public Booking addBooking(Screening screening, Customer customer, HashMap<String, Boolean> seats) {
		Booking booking = new Booking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);
		Main.bookingList.add(booking);
		screening.updateSeats(seats);

		createFirebaseBooking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);


		return getBooking(booking.getBookingID());
	}
	
	//Gets a full seat plan after updating with parameters
	public static HashMap<String, Boolean> getFullSeatPlan(HashMap<String, Boolean>... seats) {
		HashMap<String, Boolean> returnPlan = FilmController.getEmptySeatPlan(Screening.theatreDimensions);
		for (HashMap<String, Boolean> s : seats) {
			returnPlan.putAll(s);
		}
		return returnPlan;
	}
	
	// TODO: remove this; or merge it with addSeats in Booking?
	// add seat removal functionality to the same method
	// make the interfaces uniform
	public void updateBookingSeats(String bookingID, HashMap<String, Boolean> seats) {
		Booking booking = getBooking(bookingID);
		booking.updateSeats(seats);
		Screening screening = FilmController.getScreeningForBooking(booking);
		screening.updateSeats(getFullSeatPlan(screening.getSeats(), seats));

        updateFirebaseBooking(screening.getFilmTitle(), screening.getDateTime(), booking.getUsername(), seats,bookingID);

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
        assert s != null;
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

        Map<String, String> params = new HashMap<>();
        params.put("filmTitle", b.getFilmTitle());
        params.put("dateTime", b.getDateTime().format(Screening.firebaseDateTimeFormatter));
        params.put("username", b.getUsername());
        JSONObject seatsObj = new JSONObject(seatsToUnbook);
        params.put("seats", seatsObj.toString());

        assert b != null;
        deleteFirebaseBooking(b.getBookingID());
        try {
            Firebase.updateScreening(params);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFirebaseBooking(String bookingID) {
        try {
            Firebase.deleteBooking(bookingID);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private void updateFirebaseBooking(String filmTitle, LocalDateTime dateTime, String username, HashMap<String, Boolean> seats, String bookingID) {
        Map<String, String> params = new HashMap<>();
        params.put("filmTitle", filmTitle);
        params.put("dateTime", dateTime.format(Screening.firebaseDateTimeFormatter));
        params.put("username", username);
        JSONObject seatsObj = new JSONObject(seats);
        params.put("seats", seatsObj.toString());

        Screening s = FilmController.getScreeningForBooking(getBooking(bookingID));
        HashMap<String, Boolean> seatsToUpdate = new HashMap<String, Boolean>();
        assert s != null;
        Iterator<String> iterator = s.getSeats().keySet().iterator();
        String seatI = null;
        while (iterator.hasNext()) {
            seatI = iterator.next();
            if (seats.containsKey(seatI)) {
                seatsToUpdate.put(seatI, false);
            }
        }

        try {
            Firebase.deleteBooking(bookingID);
            Firebase.createBooking(params);
            JSONObject smth = new JSONObject(seatsToUpdate);
            params.put("seats", smth.toString());
            Firebase.updateScreening(params);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

	private static void createFirebaseBooking(String filmTitle, LocalDateTime dateTime, String username, HashMap<String, Boolean> seats) {
		Map<String, String> params = new HashMap<>();
		params.put("filmTitle", filmTitle);
		params.put("dateTime", dateTime.format(Screening.firebaseDateTimeFormatter));
		params.put("username", username);
		JSONObject seatsObj = new JSONObject(seats);
		params.put("seats", seatsObj.toString());

		try {
			Firebase.createBooking(params);
			Firebase.updateScreening(params);
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}
}
