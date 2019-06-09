package controller;

import cell.FileCell;
import java.io.File;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Main;
import model.Model;
import view.SecondPageView;

/**
 * Contrôleur associé à la PrimaryPageView
 * 
 * @author Adrien LE ROHO
 * 
 * @version 2.0
 */
public class PrimaryPageController {

  /**
   * Référence à l'instance de la classe Model.
   */
  private Model model;

  /**
   * Référence à l'instance de la classe Main.
   */
  private Main application;

  /**
   * Référence à la ListView de la PrimaryPage. Vue de l'ensemble des fichiers PDF sélectionnés.
   */
  @FXML
  private ListView<File> fileListView;

  /**
   * Bouton pour sélectionner le dossier de destination de l'ensemble des fichiers générés.
   */
  @FXML
  private Button directoryButton;

  /**
   * Set model.
   * 
   * @param model model to set
   */
  public void setModel(Model model) {
    this.model = model;
  }

  /**
   * Set application.
   * 
   * @param application application to set
   */
  public void setApplication(Main application) {
    this.application = application;
  }

  /**
   * Après un clique sur le bouton "ajouter", affiche une fenêtre permettant de choisir les fichiers
   * PDF à traiter. Puis, met à jour le modèle.
   * 
   * @see model.Model#fileList
   * 
   * @param event l'évènement lié au clique
   */
  @FXML
  public void handleAddFileChooserButtonAction(MouseEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select PDF Files");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF Files", "*.pdf"));
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

    List<File> files;
    if ((files = fileChooser.showOpenMultipleDialog(application.getPrimaryStage())) != null) {
      model.addFiles(files);
    }
  }

  /**
   * Après un clique sur le bouton "supprimer", supprime les fichiers PDF, sélectionnés dans la
   * <a href="#fileListView">fileListView</a>, de la liste de fichier du modèle.
   * 
   * @see model.Model#fileList
   * 
   * @param event l'évènement lié au clique
   */
  @FXML
  public void handleRemoveFileChooserButtonAction(MouseEvent event) {
    ObservableList<File> selectedItems = fileListView.getSelectionModel().getSelectedItems();
    model.getFileList().removeAll(selectedItems);
  }

  /**
   * Après un clique sur le bouton "sélectionner dossier", affiche une fenêtre permettant de
   * sélectionner le dossier de destination. Puis, met à jour le modèle.
   * 
   * @see model.Model#outputDirectory
   * 
   * @param event l'évènement lié au clique
   */
  @FXML
  public void handleDirectoryChooserButtonAction(MouseEvent event) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select a Directory");
    directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    File directory;
    if ((directory = directoryChooser.showDialog(application.getPrimaryStage())) != null) {
      model.setOutputDirectory(directory);
    }
  }

  /**
   * Après un clique sur le bouton "start", vérifie si des fichiers et un dossier de destination ont
   * bien été choisis. Puis, affiche la {@link view.SecondPageView SecondPageView}.
   * 
   * @param event l'évènement lié au clique
   * @throws Exception IOException dans la construction de SecondPageView (jamais atteint)
   */
  @FXML
  public void handleStartButtonAction(MouseEvent event) throws Exception {
    if (model.getFileList().size() > 0 && model.getOutputDirectory().get() != null) {
      SecondPageView secondPageView = new SecondPageView(application.getPrimaryStage(), model);
      application.getPrimaryStage().setScene(secondPageView.getScene());
    } else {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setContentText(
          "Sélectionnez des fichiers PDF et un dossier de destination avant de continuer.");
      alert.showAndWait();
    }
  }

  /**
   * <p>
   * Initialise {@link #model model} avec {@link model.Model#instance Model.instance} et
   * {@link #fileListView fileListView} avec {@link model.Model#fileList Model.fileList}.
   * </p>
   * 
   * <p>
   * Ajoute une CellFactory {@link cell.FileCell FileCell} à {@link #fileListView fileListView}.
   * </p>
   * 
   * <p>
   * Ajoute un écouteur d'évènement sur {@link model.Model#getOutputDirectory()
   * Model.getOutputDirectory()} pour mettre à jour le texte du {@link #directoryButton
   * directoryButton}.
   * </p>
   */
  @FXML
  public void initialize() {
    setModel(Model.getInstance());
    fileListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    fileListView.setCellFactory((ListView<File> lv) -> new FileCell());
    fileListView.setItems(model.getFileList());

    model.getOutputDirectory().addListener(new ChangeListener<File>() {

      @Override
      public void changed(ObservableValue<? extends File> observable, File oldValue,
          File newValue) {
        directoryButton.setText(newValue.getAbsolutePath());
      }

    });
  }

}
