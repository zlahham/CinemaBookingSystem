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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.util.Callback;

public class NewBookingController extends CustomerController {

	// new booking view
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
	private Button btnBackToCustomerView;
	@FXML
	private Button btnBackToNewBookingView;
	@FXML
	private Button btnBook;
	@FXML
	private TableColumn<Screening, String> tblclmnBook = new TableColumn<Screening, String>("Delete");
	//
	
	// seats view
	@FXML
	private GridPane grdpnlSeats = new GridPane();
	private ImageView[][] seats = new ImageView[3][3];
	private Image unbooked  = new Image("file:seat.png");
	private Image booked  = new Image("file:bookedseat.png");
	//
	
	private static Screening chosenScreening = null;
	private static HashMap<String, Boolean> seatsBooked = new HashMap<String, Boolean>();

	
	
	public void initialize() {
		// set "select a date" label
		// causes NullPointerExceptions; replace
		// tblFilms.setPlaceholder(label);
		
		// set up screening table
	
		
		
		initializeNewBooking();
		
		if (chosenScreening != null) {
			initializeSeatPlan();
		}

	}

	private Screening initializeNewBooking() {
		tblclmnBook.setCellValueFactory(new PropertyValueFactory<>("dummy"));
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
		tblclmnBook.setCellFactory(cellFactory);
		return null;
	}

	private void initializeSeatPlan() {
		for (int i = 0; i < seats.length; i++) {
			for (int j = 0; j < seats[i].length; j++) {
				if (chosenScreening.checkSeat((char) ('a' + i) + "" + (j + 1))) {
					seats[i][j] = new ImageView(booked);
				} else {
					seats[i][j] = new ImageView(unbooked);
					// horrible hack; do something about it
					final int ix = i;
					final int jx = j;
					//
					seats[i][j].setOnMouseClicked(event -> {
						System.out.println("Muahaha");
						grdpnlSeats.getChildren().remove(seats[ix][jx]);
						seats[ix][jx] = new ImageView(booked);
						grdpnlSeats.add(seats[ix][jx], jx, ix);
						seatsBooked.put((char)('a' + ix) + "" + (jx+1), true);
					});
				}
				GridPane.setConstraints(seats[i][j], j, i);
				grdpnlSeats.getChildren().add(seats[i][j]);
			}
		}
	}
	
	public void datePicked(ActionEvent event) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		ObservableList<Screening> screeningList = filterScreeningsByDate(dtpckrDate.getValue());
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
	
	public void bookButtonPressed(ActionEvent event) {
		addBooking(chosenScreening, (Customer)(Main.user), seatsBooked);
	}
	// TODO: Move to BookingController when it is created
	public void addBooking(Screening screening, Customer customer, HashMap<String, Boolean> seats) {
		Booking booking = new Booking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);
		Main.bookingList.add(booking);
	}
}
