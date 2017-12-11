package application.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;

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
