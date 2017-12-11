package application.controllers;

import application.Main;
import application.models.Film;
import application.models.Screening;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController extends UserController {
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private PieChart chartBookings;


    public void initialize() {
        super.initialize();
        PieChart.Data slice1 = new PieChart.Data("Desktop", 213);
        PieChart.Data slice2 = new PieChart.Data("Phone"  , 67);
        PieChart.Data slice3 = new PieChart.Data("Tablet" , 36);

        chartBookings.getData().add(slice1);
        chartBookings.getData().add(slice2);
        chartBookings.getData().add(slice3);
        hamburgerInitializer();
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
}
