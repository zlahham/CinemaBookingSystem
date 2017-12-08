package application.controllers;

import java.io.IOException;
import java.util.Iterator;

import application.models.Booking;
import application.models.Film;
import application.Main;
import application.models.User;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.mashape.unirest.http.exceptions.UnirestException;

import application.services.Firebase;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainController {

	public void logout() {
		Main.stage.setUserData(null);
		transitionToLoginView();
	}
	
	public void transition(String viewName, String mode) {
		if (mode.substring(0, 1).compareTo("F") == 0) {
			FilmController.mode = mode;
		} else if (mode.substring(0, 1).compareTo("B") == 0) {
			BookingController.mode = mode;
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

	void transitionToUserView(User user) {
		transition(StringUtils.capitalize(user.getRole()), "");
	}

	public void backToUserView(ActionEvent event) {
		transitionToUserView((User) (Main.stage.getUserData()));
	}

	public void transitionToLoginView() {
		transition("Login", "");
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
