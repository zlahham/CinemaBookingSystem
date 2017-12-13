package application.controllers;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import application.models.Booking;
import application.models.Film;
import application.models.User;
import application.Main;

import org.apache.commons.codec.binary.StringUtils;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import application.services.Firebase;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class MainController {
	
	public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	
	public void logout() {
		Main.stage.setUserData(null);
		Parent view;
		try {
			view = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
			Scene scene = new Scene(view);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void transition(String viewName, String mode) {
		//transition debugging
		System.out.println("transition method; view: " + viewName);
		System.out.println("transition method; mode: " + mode);
		//
		if (mode.length() > 0) {
			if (mode.substring(0, 1).compareTo("F") == 0) {
				FilmController.mode = mode;
			} else if (mode.substring(0, 1).compareTo("B") == 0) {
				BookingController.mode = mode;
			} else if (mode.substring(0, 1).compareTo("C") == 0 || mode.substring(0, 1).compareTo("E") == 0) {
				UserController.mode = mode;
			}
		}
		try {
			Parent view;
			view = FXMLLoader.load(getClass().getResource("/views/"+ viewName +".fxml"));
			Scene scene = new Scene(view);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void buttonTransition(ActionEvent event) {
		String[] transitionDetails = ((Button)(event.getSource())).getId().split("_");
		switch (transitionDetails.length) {
			case 1:
				//transition debugging
				System.out.println("buttonTransition method; view: " + transitionDetails[0].replace("btnTo", ""));
				transition(transitionDetails[0].replace("btnTo", ""), "");
				break;
			case 2:
				//transition debugging
				System.out.println("buttonTransition method; view: " + transitionDetails[0].replace("btnTo", ""));
				System.out.println("buttonRransition method; mode: " + transitionDetails[1]);
				transition(transitionDetails[0].replaceAll("btnTo", ""), transitionDetails[1]);
				break;
			default:
				//transition debugging
				System.err.println("The button you pressed has been improperly named");
		}
	}

	public static void populateList(String type) {
		JSONObject json = null;
		try {
			json = Firebase.getList(type);
		} catch (UnirestException e) {
			e.printStackTrace();
		}

        assert json != null;
        Iterator<String> iterator = json.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (type.equals("films"))
				Main.filmList.add(new Film(json.getJSONObject(key)));
			else if(type.equals("bookings"))
				Main.bookingList.add(new Booking(json.getJSONObject(key)));
		}
	}

}
