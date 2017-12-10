package application.controllers;

import application.Main;
import application.models.Film;
import application.models.Screening;
import javafx.collections.ObservableList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeController extends MainController {

    public void exportFilms() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("filmsExportList.txt"))) {
            bw.write("AZ CINEMAS FILM LIST DETAILS:");
            bw.newLine();
            bw.write("=============================");
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
                    int bookedSeats = 0;
                    int availableSeats = 0;

                    bw.write("Screening #" + (i + 1) + ": " + s.getDateTime());
                    bw.newLine();
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
