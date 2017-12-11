package application.services;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.Map;

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

    public static void createUser(Map<String, String> params) throws UnirestException {
        Unirest.patch(URL + "users.json").header("accept", "application/json")
                .body("{\"" + params.get("username") + "\":{\"email\":\"" + params.get("email") + "\","
                        + "\"firstName\":\"" + params.get("firstname") + "\"," + "\"lastName\":\""
                        + params.get("lastname") + "\"," + "\"password\":\"" + params.get("password") + "\","
                        + "\"role\":\"customer\"," + "\"username\":\"" + params.get("username") + "\"}}")
                .asString();
    }

    public static void createFilm(Map<String, String> params) throws UnirestException {
        Unirest.patch(URL + "films.json").header("accept", "application/json")
                .body("{\"" + params.get("filmID") + "\":{\"ageRating\":\"" + params.get("ageRating") + "\","
                        + "\"description\":\"" + params.get("description") + "\"," + "\"filmTitle\":\""
                        + params.get("filmTitle") + "\"," + "\"imageFileName\":\"" + params.get("imageFileName") + "\"}}")
                .asString();
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
