package application;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ViewBookingsController {
	@FXML private TableView<Booking> tblBookings;
	@FXML private TableColumn<Booking, String> tblclmnFilmTitle;
	@FXML private TableColumn<Booking, String> tblclmnDate;
	@FXML private TableColumn<Booking, String> tblclmnTime;
	@FXML private Button btnBack;
	
	public void initialize() {
		// tblBookings.getItems() is an ObservableList<Booking>;
		// here we set it equal to the customer's bookings field
		tblBookings.getItems().addAll(((Customer)(Main.user)).getBookings());
		// c is a TableColumn.CellDataFeatures<Booking, String> object, this class
		// being a wrapper class for the cells in the TableView
		// where does c come from?
		// why does the lambda return a Callback, not a SimpleStringProperty?
		// alternative to lambdas: PropertyValueFactory
		// SimpleStringProperty is a Property wrapper for a String
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
		tblclmnFilmTitle.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getFilmTitle()));
		tblclmnDate.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getDate().format(formatter)));
		tblclmnTime.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getTime()));
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
	
	// To do: delete button(s)
       
}
