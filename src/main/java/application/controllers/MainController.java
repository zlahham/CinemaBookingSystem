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

	void transitionToUserView(User user) {
		try {
			Parent userView;
			userView = FXMLLoader.load(getClass().getResource("/views/" + StringUtils.capitalize(user.getRole()) + ".fxml"));
			Scene scene = new Scene(userView);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void backToUserView(ActionEvent event) {
		transitionToUserView((User) (Main.stage.getUserData()));
	}

	public void transitionToLoginView() {
		try {
			Parent loginView;
			loginView = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
			Scene scene = new Scene(loginView);
			Main.stage.setScene(scene);
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
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
