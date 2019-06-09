package dialog;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * <p>
 * Dialogue après un clique droit sur la carte VAC et sélection d'un point dans le ContextMenu.
 * </p>
 * 
 * <p>
 * Permet de renseigner les coordonnées géographiques (latitude, longitude) du point courant.
 * </p>
 * 
 * @author Adrien LE ROHO
 * @author Abdoulaye DIOP
 * @version 2.0
 */
public class LatLngDialog extends Dialog<Pair<Double, Double>> {

  /**
   * Constructeur du LatLngDialog.
   */
  public LatLngDialog() {
    super();
    this.getDialogPane().getButtonTypes().add(ButtonType.OK);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField longitudeDegre = new TextField();
    longitudeDegre.setPromptText("Deg");
    longitudeDegre.setMaxWidth(50);

    TextField longitudeMin = new TextField();
    longitudeMin.setPromptText("Min");
    longitudeMin.setMaxWidth(50);

    TextField longitudeSec = new TextField("00");
    longitudeSec.setPromptText("Sec");
    longitudeSec.setMaxWidth(50);

    TextField latitudeDegre = new TextField();
    latitudeDegre.setPromptText("Deg");
    latitudeDegre.setMaxWidth(50);

    TextField latitudeMin = new TextField();
    latitudeMin.setPromptText("Min");
    latitudeMin.setMaxWidth(50);

    TextField latitudeSec = new TextField("00");
    latitudeSec.setPromptText("Sec");
    latitudeSec.setMaxWidth(50);

    grid.add(new Label("longitude:"), 0, 0);
    grid.add(longitudeDegre, 1, 0);
    grid.add(longitudeMin, 2, 0);
    grid.add(longitudeSec, 3, 0);

    grid.add(new Label("latitude:"), 0, 1);
    grid.add(latitudeDegre, 1, 1);
    grid.add(latitudeMin, 2, 1);
    grid.add(latitudeSec, 3, 1);

    this.getDialogPane().setContent(grid);
    getDialogPane().getChildren().get(2).setDisable(true);

    EventHandler<KeyEvent> myHandler = new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent e) {
        String text = ((TextField) e.getSource()).getText();

        if (isCorrect(text)) {
          ((TextField) e.getSource())
              .setStyle("-fx-background-color: #F0FFEE; -fx-border-color: #22FF00;");
        } else {
          ((TextField) e.getSource())
              .setStyle("-fx-background-color: #FFEEEE; -fx-border-color: #FF0000;");
        }
        if (isCorrect(latitudeDegre.getText()) && isCorrect(latitudeMin.getText())
            && isCorrect(latitudeSec.getText()) && isCorrect(longitudeDegre.getText())
            && isCorrect(longitudeMin.getText()) && isCorrect(longitudeSec.getText())) {
          getDialogPane().getChildren().get(2).setDisable(false);
        } else {
          getDialogPane().getChildren().get(2).setDisable(true);
        }
      }
    };

    longitudeDegre.setOnKeyReleased(myHandler);
    longitudeMin.setOnKeyReleased(myHandler);
    longitudeSec.setOnKeyReleased(myHandler);

    latitudeDegre.setOnKeyReleased(myHandler);
    latitudeMin.setOnKeyReleased(myHandler);
    latitudeSec.setOnKeyReleased(myHandler);

    this.setResultConverter(dialogButton -> {
      if (dialogButton == ButtonType.OK) {
        // TODO erreur quand pas de données saisies et ok
        double lat = Double.parseDouble(latitudeDegre.getText())
            + Double.parseDouble(latitudeMin.getText()) / 60
            + Double.parseDouble(latitudeSec.getText()) / 3600;
        double lon = Double.parseDouble(longitudeDegre.getText())
            + Double.parseDouble(longitudeMin.getText()) / 60
            + Double.parseDouble(longitudeSec.getText()) / 3600;
        return new Pair<>(lat, lon);

      }
      return null;
    });
  }

  /**
   * Test si le texte passé en paramètre est un double.
   * 
   * @param text le texte sur lequel faire le test
   * @return true si on a bien un double, false sinon
   */
  private boolean isCorrect(String text) {
    try {
      Double.parseDouble(text);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
