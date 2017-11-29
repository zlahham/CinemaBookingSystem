package application;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class SeatsController {
	
	@FXML
	private GridPane grdpnSeats = new GridPane();
	private ImageView[][] seats = new ImageView[3][3];
	private Image unbooked  = new Image("file:seat.png");
	private Image booked  = new Image("file:bookedseat.png");
	
	public void initialize() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (NewBookingController.chosenScreening.checkSeat((char)('a' + i) + "" + (j+1))) {
					seats[i][j] = new ImageView(booked);
				} else {
					seats[i][j] = new ImageView(unbooked);
				}
				GridPane.setConstraints(seats[i][j], j, i);
				grdpnSeats.getChildren().add(seats[i][j]);
			}
		}
	}

}
