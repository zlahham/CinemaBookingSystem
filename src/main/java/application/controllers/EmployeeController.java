package application.controllers;

import application.Main;
import application.models.Film;
import application.models.Screening;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class EmployeeController extends UserController {
    public void exportFilms() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Export to TXT File");
        alert.setHeaderText("Please find the 'filmsExportList.txt' file under the root of the project directory!");
        alert.setContentText("Pressing OK will Export the following information for each Film: Title, Description, Age Rating, Screening Date & Time, Available & Booked Seats for each screening");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("filmsExportList.txt"))) {
                bw.write("AZ CINEMAS FILM LIST DETAILS:");
                bw.newLine();
                bw.write("=============================\n");
                bw.newLine();

                for (Film f : Main.filmList) {
                    bw.write("Title: " + f.getFilmTitle());
                    bw.newLine();
                    bw.write("Description: " + f.getDescription());
                    bw.newLine();
                    bw.write("Age Rating : " + f.getAgeRating());
                    bw.newLine();

                    ObservableList<Screening> screenings = f.getScreenings();
                    for (int i = 0; i < screenings.size(); i++) {
                        Screening s = screenings.get(i);

                        bw.write("Screening #" + (i + 1) + ": " + s.getDateTime().format(dateTimeFormatter));
                        bw.newLine();
                        int bookedSeats = FilmController.countBookedSeats(s)[0];
                        int availableSeats = FilmController.countBookedSeats(s)[1];
                        for(Boolean value: s.getSeats().values()) {
                            if (value.equals(true)) {
                                bookedSeats++;
                            }else{
                                availableSeats++;
                            }
                        }
                        bw.write("\tAvailable Seats: " + availableSeats);
                        bw.newLine();
                        bw.write("\tBooked Seats: " + bookedSeats);
                        bw.newLine();
                        bw.write("\n");
                    }

                    bw.newLine();
                    bw.write("----------------------------------------------");
                    bw.newLine();
                    bw.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}
