package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookingList {
	
	private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
	
	BookingList (String JSONfilepath) {
		String content = null;
	
		try {
			content = new Scanner(new File(JSONfilepath)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
		JSONObject bookingsJSON = new JSONObject(content);
		bookingsJSON = bookingsJSON.getJSONObject("bookings");
		JSONObject bookingI;
		Iterator<String> iterator = bookingsJSON.keys();
		String bookingKey = null;
		while (iterator.hasNext()) {
			bookingKey = iterator.next();
			bookingI = bookingsJSON.getJSONObject(bookingKey);
			bookingList.add(new Booking(bookingI));
		}
	}

}
