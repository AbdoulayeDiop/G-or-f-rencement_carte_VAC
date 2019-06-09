package cell;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Model;


/**
 * <p>
 * ClickableFileCell utilisée dans la construction des éléments de ListView du SearchBar de la
 * SecondPage.
 * </p>
 * 
 * <p>
 * Voir <a href=
 * "https://fabrice-bouye.developpez.com/tutoriels/javafx/customisation-controle-virtualise-api-cell-javafx/">Tutoriel
 * sur l'API Cell de JavaFX</a>.
 * </p>
 *
 */
public class ClickableFileCell extends FileCell {

  /**
   * Affiche le nom du fichier sans son extension, lors d'un clique set le currentFile de model avec
   * le fichier associé à la cell cliquée.
   * 
   * @param model Le modèle sur lequel agir pour changer le fichier courant.
   */
  public ClickableFileCell(Model model) {
    super();
    addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        if (fileOfCell != null) {
          model.getCurrentFile().set(fileOfCell.getName());
        }
      }
    });
  }

}
