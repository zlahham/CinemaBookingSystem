package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

public class NewBookingController {
	
	@FXML private DatePicker dtpckrDate;
	
	public void datePicked(ActionEvent event) {
		System.out.println(dtpckrDate.getValue());
		
	}
}
