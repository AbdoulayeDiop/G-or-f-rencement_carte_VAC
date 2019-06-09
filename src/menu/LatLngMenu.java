package menu;

import dialog.LatLngDialog;
import geometrie.Point;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.util.Pair;
import model.Model;
import utils.CarteVAC;

/**
 * Menu contextuel
 * 
 * @author Adrien LE ROHO
 * @author Abdoulaye DIOP
 * @version 2.0
 */

public class LatLngMenu extends ContextMenu {
  private Model model;
  private Point pA;
  private Point pB;
  private Point pixelA;
  private Point pixelB;

  private ContextMenuEvent firstEvent;

  public Point getpA() {
    return pA;
  }

  public Point getpB() {
    return pB;
  }

  /**
   * 
   */
  public LatLngMenu() {
    super();
    EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {

        LatLngDialog latLngDialog = new LatLngDialog();

        Optional<Pair<Double, Double>> result = latLngDialog.showAndWait();

        result.ifPresent(latlng -> {
          CarteVAC carteVAC = model.getCurrentVAC().get();
          double imageWidth = ((ImageView) firstEvent.getSource()).getFitWidth();
          double imageHeight = ((ImageView) firstEvent.getSource()).getFitHeight();

          double toPixelH = carteVAC.getWidth() / imageWidth;
          double toPixelV = carteVAC.getHeight() / imageHeight;

          if (((MenuItem) event.getSource()).getText().equals("Point A")) {
            pA = new Point(latlng.getValue() * 1000, latlng.getKey() * 1000);
            pixelA = new Point(firstEvent.getX() * toPixelH, firstEvent.getY() * toPixelV);
            if (pB != null) {
              carteVAC.calculerEchelle(pixelA, pA, pixelB, pB);
            }
          } else {
            pB = new Point(latlng.getValue() * 1000, latlng.getKey() * 1000);
            pixelB = new Point(firstEvent.getX() * toPixelH, firstEvent.getY() * toPixelV);
            if (pA != null) {
              carteVAC.calculerEchelle(pixelA, pA, pixelB, pB);
            }
          }
        });

      }
    };

    MenuItem pointA = new MenuItem("Point A");
    pointA.setOnAction(eh);
    MenuItem pointB = new MenuItem("Point B");
    pointB.setOnAction(eh);
    this.getItems().addAll(pointA, pointB);
  }

  public void setFirstEvent(ContextMenuEvent event) {
    this.firstEvent = event;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

}
