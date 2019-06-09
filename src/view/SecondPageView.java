package view;

import controller.CentralViewController;
import controller.InfoWidgetController;
import controller.SearchBarController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Model;

/**
 * <p>
 * Vue de la seconde fenêtre après avoir cliqué sur le bouton start de la PrimaryPageView.
 * </p>
 * 
 * <p>
 * Permet de choisir le fichier PDF à traiter, de renseigner toutes les informations utiles pour le
 * fichier PDF en cours (renseigner les métadonnées de chaque carte VAC du PDF courant : type de
 * carte ; code OACI de l'aéroport ; latitude et longitude de deux points servant à calculer les
 * coordonnées des points extrêmes de la carte.) et d'exporter ces données dans un fichier.
 * </p>
 * 
 * <p>
 * Cette vue contient plusieurs sous-widgets : * CentralView pour renseigner les informations pour
 * la carte VAC du fichier PDF courant * SearchBar pour choisir le fichier PDF courant * InfoWidget
 * pour renseigner d'autres informations sur la carte VAC courante et sauvegarder les informations
 * dans un fichier
 * </p>
 * 
 */
public class SecondPageView extends BorderPane {

  /**
   * Référence à la scène de cette vue.
   */
  private Scene scene;

  /**
   * <p>
   * Constructeur de la SecondPageView appelé après avoir cliqué sur le bouton start de la
   * PrimaryPageView {@link controller.PrimaryPageController#handleStartButtonAction}.
   * </p>
   * 
   * <p>
   * Charge tous les fichiers fxml nécessaires pour cette vue, set les contrôleurs associés puis la
   * scène associée à cette vue.
   * </p>
   * 
   * <p>
   * Ajoute un écouteur d'évènement sur KEY_PRESSED pour naviguer entre les cartes VAC du fichier
   * PDF courant.
   * </p>
   * 
   * @param stage La primaryStage de l'application.
   * @param model Le model.
   * @throws IOException Exception lors de l'accés au fxml associé à la SecondPage (jamais atteint).
   */
  public SecondPageView(Stage stage, Model model) throws IOException {
    super();

    FXMLLoader searchBarLoader = new FXMLLoader(getClass().getResource("/fxml/SearchBar.fxml"));
    Parent searchBar = searchBarLoader.load();

    FXMLLoader infoWidgetLoader = new FXMLLoader(getClass().getResource("/fxml/InfoWidget.fxml"));
    Parent infoWidget = infoWidgetLoader.load();

    VBox vbox = new VBox();
    vbox.getChildren().addAll(searchBar, infoWidget);
    vbox.setAlignment(Pos.CENTER_RIGHT);

    FXMLLoader centralViewLoader = new FXMLLoader(getClass().getResource("/fxml/CentralView.fxml"));
    Parent centralView = centralViewLoader.load();

    vbox.setStyle("-fx-border-color: gray");
    setLeft(vbox);
    setCenter(centralView);

    stage.centerOnScreen();
    stage.setMinWidth(860);
    stage.setMinHeight(660);

    CentralViewController centralViewController = centralViewLoader.getController();
    centralViewController.setModel(model);

    InfoWidgetController infoWidgetController = infoWidgetLoader.getController();
    infoWidgetController.setModel(model);

    scene = new Scene(this);

    scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      switch (event.getCode()) {
        case RIGHT:
          try {
            model.nextVac(1);
            event.consume();
          } catch (Exception e1) {
            e1.printStackTrace();
          }
          break;
        case LEFT:
          try {
            model.nextVac(-1);
            event.consume();
          } catch (Exception e1) {
            e1.printStackTrace();
          }
          break;
        case ENTER:
          SearchBarController searchBarController = searchBarLoader.getController();
          searchBarController.showCurrentCell();
          break;
        default:
          break;
      }
    });

  }
}
