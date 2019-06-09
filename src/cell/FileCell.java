package cell;

import java.io.File;
import javafx.scene.control.ListCell;
import model.Model;

/**
 * <p>
 * FileCell utilisée dans la construction des éléments de ListView de la PrimaryPage.
 * </p>
 * 
 * <p>
 * Affiche le nom du fichier sans son extension.
 * </p>
 * 
 * <p>
 * Voir <a href=
 * "https://fabrice-bouye.developpez.com/tutoriels/javafx/customisation-controle-virtualise-api-cell-javafx/">Tutoriel
 * sur l'API Cell de JavaFX</a>.
 * </p>
 *
 */
public class FileCell extends ListCell<File> {

  protected File fileOfCell;

  @Override
  protected void updateItem(File item, boolean empty) {
    super.updateItem(item, empty);
    String text = null;
    if (!empty && item != null) {
      this.fileOfCell = item;
      String name = item.getName();
      text = name.replaceFirst("[.][^.]+$", "");
      if (Model.getInstance().getaExtraireManuellement().get().containsKey(name)) {
        setStyle("-fx-background-color: #FFA6A6");
      } else {
        setStyle("");
      }
    }
    setText(text);
  }
}
