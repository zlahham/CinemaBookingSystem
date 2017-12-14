package application.controllers;

import application.Main;
import application.services.CSV;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;
import java.util.Optional;

public class NavbarController extends MainController {

    public void exportFilms() throws FileNotFoundException {
        ButtonType buttonTypeOne = new ButtonType("CSV");
        ButtonType buttonTypeTwo = new ButtonType("Detailed TXT");
        ButtonType buttonTypeCancel = ButtonType.CANCEL;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please choose which data format you would like to export to:",
                buttonTypeOne, buttonTypeTwo, buttonTypeCancel);
        alert.setTitle("Export Data");
        alert.setHeaderText("Please find the filmsExportList.txt file under the root of the project directory!");

        Optional<ButtonType> result = alert.showAndWait();


        if(result.isPresent()) {
            loggerBegin("EXPORT", result.get().getText());
            if (result.get() == buttonTypeOne) {
                CSV.export();
            } else if (result.get() == buttonTypeTwo) {
                CSV.exportDetailed();
            }
            loggerComplete("EXPORT", result.get().getText() + " can be found at $ProjectRoot/filmExportList");
        }
    }
}
