package application;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class FirebaseController {
	private static String URL = "https://cinema-booking-app.firebaseio.com/";
	
	public static JSONObject getList(String className) throws UnirestException {
		String response = Unirest.get(URL + className + ".json").asString().getBody();
		JSONObject listJSON = new JSONObject(response);
		return listJSON;
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
