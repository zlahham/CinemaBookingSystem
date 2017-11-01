package application;
	
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	static List<User> users = new ArrayList<User>();
	@Override
	public void start(Stage primaryStage) {
		try {
			// BorderPane root = new BorderPane();
			Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
			Scene scene = new Scene(root,400,400);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		// JSONObject myString = new JSONObject().put("username", "Hello, World!");
		// System.out.println(myString);
	}
}