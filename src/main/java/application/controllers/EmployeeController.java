package application.controllers;

import application.Main;
import application.services.Firebase;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.IOException;

public class EmployeeController extends UserController {
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private BarChart<?, ?> chart;
    @FXML
    private CategoryAxis x;
    @FXML
    private NumberAxis y;


    public void initialize() {
        super.initialize();
        hamburgerInitializer();
        chartInitializer();

    }

    private void hamburgerInitializer() {
        try {
            VBox box = FXMLLoader.load(getClass().getResource("/views/EmployeeNavbar.fxml"));
            drawer.setSidePane(box);
            HamburgerBackArrowBasicTransition task = new HamburgerBackArrowBasicTransition(hamburger);
            task.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                task.setRate(task.getRate() * -1);
                task.play();
                if (drawer.isShown()){
                    drawer.close();
                } else {
                    drawer.open();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chartInitializer(){
        XYChart.Series set = new XYChart.Series<>();
        final int[] screenings = {0};
        Main.filmList.forEach((f)-> screenings[0] = screenings[0] + f.getScreenings().size());
        try {
            set.getData().add(new XYChart.Data("Customers", Firebase.getList("users").length()));
            set.getData().add(new XYChart.Data("Films", Main.filmList.size()));
            set.getData().add(new XYChart.Data("Bookings", Main.bookingList.size()));
            set.getData().add(new XYChart.Data("Screenings", screenings[0]));
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        chart.getData().addAll(set);

    }
}
