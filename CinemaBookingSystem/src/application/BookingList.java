package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
		
		System.out.println("bookingList:");
		for (Booking i : bookingList) {
			System.out.println(i.getBookingID());
		}
	}
	
	public ObservableList<Booking> bookingsByCustomer(Customer customer) {
		System.out.println("bookingsByCustomer running");
		ObservableList<Booking> returnList = FXCollections.observableArrayList();
		for (int i = 0; i < bookingList.size(); i++) {
			if (customer.getUsername().equals(bookingList.get(i).getUsername())){
				returnList.add(bookingList.get(i));
				System.out.println(bookingList.get(i).getBookingID());
			}
		}
		return returnList;
	}
	
	public void deleteBooking(String bookingID) {
		for (Booking i : bookingList) {
			if (i.getBookingID().compareTo(bookingID) == 0) {
				bookingList.remove(i);
				return;
			}
		}
	}
	
	public void addBooking(Screening screening, Customer customer, ArrayList<String> seats) {
		Booking booking = new Booking(screening.getFilmTitle(), screening.getDateTime(), customer.getUsername(), seats);
		bookingList.add(booking);
	}

}
