package controller;

import cell.ClickableFileCell;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Model;

/**
 * Contrôleur associé au SearchBar
 * 
 * @author Nadir RANEM
 * @author Adrien LE ROHO
 * 
 * @version 2.0
 */
public class SearchBarController {

  /**
   * Référence à l'instance de la classe Model.
   */
  private Model model;

  /**
   * Référence à la ListView de la PrimaryPage. Vue de l'ensemble des fichiers PDF sélectionnés.
   */
  @FXML
  private ListView<File> fileListView;

  /**
   * Référence au TextField qui permet de rechercher une carte parmi celles sélectionnées dans la
   * PrimaryPage.
   */
  @FXML
  private TextField textSearch;

  /**
   * <p>
   * Initialise {@link #fileListView}.
   * </p>
   * 
   * <p>
   * Relie le {@link #textSearch} à la {@link #fileListView} pour pouvoir effectuer une recherche.
   * </p>
   */
  @FXML
  public void initialize() {
    model = Model.getInstance();
    FilteredList<File> filteredList = new FilteredList<>(model.getFileList());
    fileListView.setCellFactory((ListView<File> lv) -> new ClickableFileCell(model));
    fileListView.setItems(filteredList);

    textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(file -> {
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseSearch = newValue.toLowerCase();
        String filename = file.getName().replaceFirst("[.][^.]+$", "");
        return filename.toLowerCase().contains(lowerCaseSearch);
      });
    });

    model.getaExtraireManuellement()
        .addListener(new ChangeListener<HashMap<String, ArrayList<Integer>>>() {
          @Override
          public void changed(
              ObservableValue<? extends HashMap<String, ArrayList<Integer>>> observable,
              HashMap<String, ArrayList<Integer>> oldValue,
              HashMap<String, ArrayList<Integer>> newValue) {
            fileListView.setCellFactory((ListView<File> lv) -> new ClickableFileCell(model));
          }
        });

  }

  /**
   * Affiche la carte associée à la cell courante en changeant le currentFile de model.
   */
  public void showCurrentCell() {
    File currentCell = fileListView.getFocusModel().getFocusedItem();
    model.getCurrentFile().set(currentCell.getName());
  }

}
