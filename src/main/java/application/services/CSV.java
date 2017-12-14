package application.services;

import application.Main;
import application.controllers.FilmController;
import application.models.Film;
import application.models.Screening;
import javafx.collections.ObservableList;

import java.io.*;

import static application.controllers.MainController.dateFormatter;
import static application.controllers.MainController.dateTimeFormatter;
import static application.controllers.MainController.timeFormatter;

public class CSV {
    public static void export() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("filmsExportList.csv"));
        StringBuilder sb = new StringBuilder();
        // Set the columns
        sb.append("Film Title,Date,Time,Total Seats,Booked Seats,Available Seats\n");

        // Loop through each Film and its associated Screenings
        for (Film f : Main.filmList) {
            ObservableList<Screening> screenings = f.getScreenings();
            for (int i = 0; i < screenings.size(); i++) {
                Screening s = screenings.get(i);
                int bookedSeats = FilmController.countBookedSeats(s)[0];
                int availableSeats = FilmController.countBookedSeats(s)[1];
                // Set the date for each row
                sb.append(f.getFilmTitle()).append(",").append(s.getDateTime().format(dateFormatter)).append(",")
                        .append(s.getDateTime().format(timeFormatter)).append(",")
                        .append("24").append(",").append(bookedSeats).append(",")
                        .append(availableSeats).append("\n");
            }
        }
        pw.write(sb.toString());
        pw.close();
    }

    public static void exportDetailed() {
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
                    int bookedSeats = FilmController.countBookedSeats(s)[0];
                    int availableSeats = FilmController.countBookedSeats(s)[1];
                    bw.write("Screening #" + (i + 1) + ": " + s.getDateTime().format(dateTimeFormatter));
                    bw.newLine();
                    for (Boolean value : s.getSeats().values()) {
                        if (value.equals(true)) {
                            bookedSeats++;
                        } else {
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
