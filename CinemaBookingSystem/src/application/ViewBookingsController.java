package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ViewBookingsController {
	@FXML private TableView<Booking> tblBookings;
	@FXML private TableColumn<Booking, String> tblclmnFilmTitle;
	@FXML private TableColumn<Booking, String> tblclmnDate;
	@FXML private TableColumn<Booking, String> tblclmnTime;
	
	
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
		tblclmnFilmTitle.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getFilmTitle()));
		tblclmnDate.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getDate()));
		tblclmnTime.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getTime()));
	}
	
	// To do: delete button(s)
       
}
