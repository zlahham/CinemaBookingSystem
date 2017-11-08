package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ViewBookingsController {
	@FXML private TableView<Booking> tblBookings;
	@FXML private TableColumn<Booking, String> tblclmnDate;
	@FXML private TableColumn<Booking, String> tblclmnTime;
	
	public void initialize() {
		System.out.println(((Customer)(Main.user)).getBookings().get(0).getFilmTitle());
		tblBookings.getItems().addAll(((Customer)(Main.user)).getBookings());
		tblclmnDate.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getDate()));
		tblclmnTime.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getTime()));
		//tblBookings.setItems(Main.user.getBookings());
	}
	
	{
		//tblBookings.setItems(Main.user.getBookings());
		//tblBookings.setItems((ObservableList<Booking>) Main.user.getBookings());
			//for (int i = 0; i < Main.user.getBookings().size(); i++) {
				//System.out.println(Main.user.getBookings().get(i).getFilmTitle());
	//	}
	}
	
	private void aaa(ActionEvent event) {
		System.out.println("aaa");
	}

}
