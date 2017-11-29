package application;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class FirebaseController {
	private static String URL = "https://cinema-booking-app.firebaseio.com/";

	public static void get(String className, String id) throws UnirestException {
		String response = Unirest.get(URL + className + ".json").asString().getBody();
		System.out.println(response);
	}

	public static boolean post() {
		return false;

	}

	public static boolean patch() {
		return false;

	}

	public static boolean destroy() {
		return false;

	}
}
