package application.sevices;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Firebase {
	private static String URL = "https://cinema-booking-app.firebaseio.com/";

	public static JSONObject getList(String className) throws UnirestException {
		String response = httpRequester(className, "");
		JSONObject json = new JSONObject(response);
		return json;
	}

	public static JSONObject getItem(String className, String id) throws UnirestException {
		String response = httpRequester(className, id);
		JSONObject json = new JSONObject(response);
		return json;
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

	private static String httpRequester(String className, String id) throws UnirestException {
		String response = null;

		if (id.equals(""))
			response = Unirest.get(URL + className + ".json").asString().getBody();
		else
			response = Unirest.get(URL + className + "/" + id + ".json").asString().getBody();

		return response;
	}
}
