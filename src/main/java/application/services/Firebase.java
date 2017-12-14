package application.services;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONObject;

import java.util.Map;

public class Firebase {
    private static String URL = "https://cinema-booking-app.firebaseio.com/";

    public static JSONObject getList(String className) throws UnirestException {
        String response = httpRequester(className, "");
        return new JSONObject(response);
    }

    public static JSONObject getItem(String className, String id) throws UnirestException {
        String response = httpRequester(className, id);
        return new JSONObject(response);
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

    // Params needed: dateTime, filmTitle
    public static void createScreening(Map<String, String> params) throws UnirestException {
        Unirest.patch(URL + "films/" + params.get("filmTitle") + "/screenings.json").header("accept", "application/json")
                .body("{\"" + params.get("dateTime") + "\":{\"dateTime\":\"" + params.get("dateTime") + "\","
                        + "\"screeningID\":\"" + params.get("dateTime") + "\"," + "\"filmTitle\":\""
                        + params.get("filmTitle") + "\"," +
                        "\"seats\":" + "{\"a1\" : false, \"a2\" : false, \"a3\" : false, \"a4\" : false, \"a5\" : false, \"a6\" : false," +
                                        "\"b1\" : false, \"b2\" : false, \"b3\" : false, \"b4\" : false, \"b5\" : false, \"b6\" : false," +
                                        "\"c1\" : false, \"c2\" : false, \"c3\" : false, \"c4\" : false, \"c5\" : false, \"c6\" : false," +
                                        "\"d1\" : false, \"d2\" : false, \"d3\" : false, \"d4\" : false, \"d5\" : false, \"d6\" : false}"
                        + "}}")
                .asString();
    }

    //works with incomplete seat map (with true/false configured correctly)
    public static void updateScreening(Map<String, String> params) throws UnirestException {
        String localURL = "films/" + params.get("filmTitle") + "/screenings/" + params.get("dateTime") + "/seats.json";
        String seats = params.get("seats").replace(":", " : ");

        Unirest.patch(URL + localURL).header("accept", "application/json")
                .body(seats)
                .asString();
    }


    // Params needed: dateTime, filmTitle, username, seats
    //works with incomplete seat map
    public static void createBooking(Map<String, String> params) throws UnirestException {
        String bookingID = params.get("dateTime") + " " + params.get("username");
        String seats = params.get("seats").replace(":", " : ");

        Unirest.patch(URL + "bookings.json").header("accept", "application/json")
                .body("{\"" + bookingID + "\":{\"dateTime\":\"" + params.get("dateTime") + "\","
                        + "\"bookingID\":\"" + bookingID + "\"," + "\"username\":\"" + params.get("username") + "\","
                        + "\"filmTitle\":\"" + params.get("filmTitle") + "\","
                        + "\"seats\":" + seats + "}}")
                .asString();
    }

//    public static void updateBooking(Map<String, String> params) throws UnirestException {
//        deleteBooking(params);
//    }

    public static void deleteBooking(String bookingID) throws UnirestException {
        Unirest.delete(URL + "bookings/" + bookingID +".json").asString();
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
