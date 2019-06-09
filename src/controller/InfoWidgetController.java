package controller;

import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Model;
import utils.CarteVAC;
import utils.MetaDonnees;

/**
 * Contrôleur associé à la CentralView.
 * 
 * @author Abdoulaye DIOP
 * @author Adrien LE ROHO
 * 
 * @version 2.0
 */
public class InfoWidgetController {

  /**
   * Texte d'erreur lorsqu'aucun code OACI n'a été fourni.
   */
  public final String confirmationIdOACI = "Aucun code OACI n'a été fourni pour cette carte.\n";

  /**
   * Texte d'erreur lorsque les coordonnées de référence n'ont pas été fournies.
   */
  public final String absenceEchelles = "Les coordonnées de référence n'ont pas été fournies.\n";

  /**
   * Référence à l'instance de la classe Model.
   */
  private Model model;

  /**
   * Champ pour saisir le code OACI de la carte VAC courante.
   */
  @FXML
  private TextField idOACITextField;

  /**
   * Menu pour sélectionner le type de carte VAC.
   */
  @FXML
  private MenuButton typesMenuButton;

  /**
   * Bouton pour exporter les informations.
   */
  @FXML
  private Button saveButton;

  /**
   * Set model, et écoute les changements de CurrentFile pour mettre à jour idOACITextField.
   * 
   * @param model the model to set
   */
  public void setModel(Model model) {
    this.model = model;
    this.model.getCurrentFile().addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        String text = newValue.split("\\.")[0].toUpperCase();
        idOACITextField.setText(text);
      }
    });
  }

  /**
   * Permet d'avoir un retour visuel sur ce qui est rentré dans le champ idOACITextField puisqu'on
   * ne doit avoir que 4 lettres.
   * 
   * @param e l'évènement du clavier
   */
  @FXML
  public void handleIdOACIOnKeyReleased(KeyEvent e) {
    CarteVAC carteVAC = model.getCurrentVAC().get();
    String text = ((TextField) e.getSource()).getText();
    if (text.length() == 4) {
      carteVAC.getMetaDonnees().setIdOACI(text);
      idOACITextField.setStyle("-fx-background-color: #F0FFEE; -fx-border-color: #22FF00;");
    } else {
      idOACITextField.setStyle("-fx-background-color: #FFEEEE; -fx-border-color: #FF0000;");
    }

  }

  @FXML
  public void handleModeAutoClicked(MouseEvent e) {
    model.extraireCarteVAC();
  }

  /**
   * Change le texte de typesMenuButton pour suivre l'élément sélectionné.
   * 
   * @param e l'action lors de la sélection d'un menu item
   */
  @FXML
  public void handleType1OnAction(ActionEvent e) {
    String text = ((MenuItem) e.getSource()).getText();
    typesMenuButton.setText(text);
  }

  /**
   * Change le texte de typesMenuButton pour suivre l'élément sélectionné.
   * 
   * @param e l'action lors de la sélection d'un menu item
   */
  @FXML
  public void handleType2OnAction(ActionEvent e) {
    String text = ((MenuItem) e.getSource()).getText();
    typesMenuButton.setText(text);
  }

  /**
   * Change le texte de typesMenuButton pour suivre l'élément sélectionné.
   * 
   * @param e l'action lors de la sélection d'un menu item
   */
  @FXML
  public void handleType3OnAction(ActionEvent e) {
    String text = ((MenuItem) e.getSource()).getText();
    typesMenuButton.setText(text);
  }

  /**
   * Change le texte de typesMenuButton pour suivre l'élément sélectionné.
   * 
   * @param e l'action lors de la sélection d'un menu item
   */
  @FXML
  public void handleType4OnAction(ActionEvent e) {
    String text = ((MenuItem) e.getSource()).getText();
    typesMenuButton.setText(text);
  }

  /**
   * Change le texte de typesMenuButton pour suivre l'élément sélectionné.
   * 
   * @param e l'action lors de la sélection d'un menu item
   */
  @FXML
  public void handleSaveButtonOnClicked(MouseEvent e) {
    CarteVAC carteVAC = model.getCurrentVAC().get();
    String idOACI = idOACITextField.getText();
    String type = typesMenuButton.getText();
    if (carteVAC != null) {
      MetaDonnees metadonnees = carteVAC.getMetaDonnees();
      metadonnees.setType(type);
      if (idOACI.length() == 0 || metadonnees.getCoordBasDroite().getX() == 100) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText("");
        if (idOACI.length() == 0) {
          alert.setHeaderText(alert.getContentText() + confirmationIdOACI);
        }
        if (metadonnees.getCoordBasDroite().getX() == 100) {
          alert.setHeaderText(alert.getContentText() + absenceEchelles);
        }
        alert.setContentText("Voulez-vous continuer ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
          metadonnees.setIdOACI(idOACI);
          model.getCurrentVAC().get().save(model.getOutputDirectory().get());
          saveButton.setStyle("-fx-background-color: #F0FFEE; -fx-border-color: #22FF00;");
        }
      } else {
        metadonnees.setIdOACI(idOACI);
        model.getCurrentVAC().get().save(model.getOutputDirectory().get());
        saveButton.setStyle("-fx-background-color: #F0FFEE; -fx-border-color: #22FF00;");
      }
    } else {
      saveButton.setStyle("-fx-background-color: #FFEEEE; -fx-border-color: #FF0000;");
      Alert alert = new Alert(AlertType.ERROR);
      alert.setHeaderText("Impossible de sauvegarder");
      alert.setContentText("Veillez sélectionner une carte VAC");
      alert.showAndWait();
    }
  }

}
