package application.controllers;

import application.services.CSV;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;
import java.util.Optional;

/**
 * <dl>
 * 	<dt> Purpose:
 * 	<dd> Enable the Employee to export information outside the program.
 * 
 * 	<dt> Description:
 * 	<dd> When the Employee clicks the export button in the navbar,
 * 	<dd> this class creates an Alert prompting the Employee to choose
 * 	<dd> either a CSV or a formatted text file as their export option;
 * 	<dd> it then calls the relevant method in the CSV class.
 * </dl>
 * 
 * @author Zaid Al Lahham and Aleksi Anttila
 * @version $Date: 2017/12/14 16:00:00 $
 * 
 */
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
