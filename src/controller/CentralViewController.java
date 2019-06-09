package controller;

import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import menu.LatLngMenu;
import model.Model;
import utils.CarteVAC;

/**
 * Contrôleur associé à la CentralView.
 * 
 * @author Abdoulaye DIOP
 * @author Adrien LE ROHO
 * @author Ilyass HAMMOUAMRI
 * 
 * @version 2.0
 */
public class CentralViewController {

  /**
   * Référence à l'instance de la classe Model.
   */
  private Model model;

  /**
   * Menu de selection des points de référence sur la carte.
   */
  private LatLngMenu latLngMenu;

  /**
   * Barre de chargement représentant la progression lors de l'extraction de cartes VAC.
   */
  private ProgressBar progressBar;

  /**
   * Référence au leftButton de la CentralView. Permet de faire défiler les cartes affichées dans la
   * fenêtre centrale
   */
  @FXML
  private Button leftButton;

  /**
   * Référence au rightButton de la CentralView. Permet de faire défiler les cartes affichées dans
   * la fenêtre centrale
   */
  @FXML
  private Button rightButton;

  /**
   * Référence à la Pane contenant l' {@link #imageView} de la carte couramment affichée.
   */
  @FXML
  private Pane pane;

  /**
   * Référence à la ScrollPane pour réaliser le zoom.
   */
  @FXML
  private ScrollPane scrollPane;

  /**
   * Ratio de la carte VAC courante (hauteur / largeur).
   */
  private double ratio;

  /**
   * Property représentant le zoom.
   */
  final DoubleProperty zoomProperty = new SimpleDoubleProperty(400);

  /**
   * Référence à l'ImageView contenant l'{@link Image} correspondant à la carte couramment affichée.
   */
  @FXML
  private ImageView imageView;

  /**
   * Après un clique sur {@link #leftButton}, fait défiler la listes des cartes courantes d'un pas
   * vers la gauche pour afficher la carte précédente.
   * 
   * @param event l'évènement lié au clique
   */
  @FXML
  public void handleLeftButtonOnPressed(MouseEvent event) {
    try {
      model.nextVac(-1);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Après un clique sur {@link #rightButton}, fait défiler la listes des cartes courantes d'un pas
   * vers la droite pour afficher la carte suivante.
   * 
   * @param event l'évènement lié au clique
   */
  @FXML
  public void handleRightButtonOnPressed(MouseEvent event) {
    try {
      model.nextVac(1);
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Affiche l'image contenue dans la carte <i>carte</i>.
   * 
   * @param carte la carte à afficher
   */
  public void afficherCarteVAC(CarteVAC carte) {
    Image image = SwingFXUtils.toFXImage(carte, null);
    imageView.setImage(image);
    imageView.setFitWidth(400);
    imageView.setFitHeight(400 * image.getHeight() / image.getWidth());
    ratio = image.getHeight() / image.getWidth();
    scrollPane.setPrefViewportWidth(400);
    scrollPane.setPrefViewportHeight(400 * ratio);

    imageView.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
        new EventHandler<ContextMenuEvent>() {

          @Override
          public void handle(ContextMenuEvent event) {

            latLngMenu.setFirstEvent(event);

            latLngMenu.show(imageView, event.getScreenX(), event.getScreenY());

          }
        });
  }

  /**
   * Set model, et écoute les changements de CurrentVac, de LoadStatus et bind la progressBar avec
   * le LoadStatus.
   * 
   * @param m the model to set
   */
  public void setModel(Model m) {
    model = m;
    latLngMenu.setModel(m);

    model.getCurrentVAC().addListener(new ChangeListener<CarteVAC>() {

      @Override
      public void changed(ObservableValue<? extends CarteVAC> observable, CarteVAC oldValue,
          CarteVAC newValue) {
        afficherCarteVAC(newValue);
        if (model.getCurrentFile().get() != null) {
          String currentFile = model.getCurrentFile().get();
          ArrayList<CarteVAC> currentArrayList = model.getMapPDF().get(currentFile);
          int currentIndex = currentArrayList.indexOf(newValue);
          if (model.getaExtraireManuellement().get().containsKey(currentFile)) {
            if (model.getaExtraireManuellement().get().get(currentFile).contains(currentIndex)) {
              scrollPane.setStyle("-fxborder-style: solid;-fx-border-color: #FF0000");
            } else {
              scrollPane.setStyle("-fxborder-style: solid;-fx-border-color: #22FF00");
            }
          } else {
            scrollPane.setStyle("");
          }
        }
      }
    });

    progressBar.progressProperty().bind(model.getLoadStatus());

    model.getLoadStatus().addListener(new ChangeListener<Number>() {

      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        if (newValue.doubleValue() == 0) {
          progressBar.setVisible(false);
        } else {
          progressBar.setLayoutX((pane.getWidth() - progressBar.getWidth()) / 2);
          progressBar.setLayoutY((pane.getHeight() - progressBar.getHeight()) / 2);
          progressBar.setVisible(true);
        }
      }
    });
  }

  /**
   * <p>
   * Initialise {@link #latLngMenu} et {@link #progressBar}.
   * </p>
   * 
   * <p>
   * Ajoute {@link #progressBar} à la {@link #pane}.
   * </p>
   * 
   * <p>
   * Change le curseur de {@link #imageView} en deux lignes horizontale et verticale pour faciliter
   * la lecture des coordonnées au bord des cartes.
   * </p>
   */
  @FXML
  public void initialize() {
    latLngMenu = new LatLngMenu();

    progressBar = new ProgressBar();
    pane.getChildren().add(progressBar);

    progressBar.setLayoutX((pane.getWidth() - progressBar.getWidth()) / 2);
    progressBar.setLayoutY((pane.getHeight() - progressBar.getHeight()) / 2);
    progressBar.setVisible(false);

    Line ligneVerticale = new Line();
    pane.getChildren().add(ligneVerticale);

    ligneVerticale.setVisible(false);
    ligneVerticale.startYProperty().bind(imageView.layoutYProperty());
    ligneVerticale.endYProperty().bind(imageView.fitHeightProperty());

    Line ligneHorizontale = new Line();
    pane.getChildren().add(ligneHorizontale);

    ligneHorizontale.setVisible(false);
    ligneHorizontale.startXProperty().bind(imageView.layoutXProperty());
    ligneHorizontale.endXProperty().bind(imageView.fitWidthProperty());

    pane.setCursor(Cursor.NONE);

    pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {

        if (event.getX() < imageView.getFitWidth() && event.getY() < imageView.getFitHeight()) {
          ligneVerticale.setLayoutX(event.getX() + 1);
          ligneHorizontale.setLayoutY(event.getY() + 1);
        }
      }
    });

    pane.setOnMouseExited(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {

        ligneHorizontale.setVisible(false);
        ligneVerticale.setVisible(false);

      }
    });

    pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        if (imageView.getImage() != null) {
          ligneHorizontale.setVisible(true);
          ligneVerticale.setVisible(true);
        }
      }
    });

    zoomProperty.addListener(new InvalidationListener() {
      @Override
      public void invalidated(Observable arg0) {

        imageView.setFitWidth(zoomProperty.get());
        imageView.setFitHeight(zoomProperty.get() * ratio);
      }
    });

    scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
      @Override
      public void handle(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
          zoomProperty.set(zoomProperty.get() * 1.1);
        } else if (event.getDeltaY() < 0 && zoomProperty.get() > 440) {
          zoomProperty.set(zoomProperty.get() / 1.1);
          scrollPane.setHvalue(scrollPane.getHvalue() - 0.1);
          scrollPane.setVvalue(scrollPane.getVvalue() - 0.1);
        }
      }
    });

    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

  }

}
