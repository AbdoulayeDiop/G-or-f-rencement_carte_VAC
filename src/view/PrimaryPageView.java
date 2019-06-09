package view;

import controller.PrimaryPageController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Main;

/**
 * <p>
 * Vue de la fenêtre à l'ouverture de l'application.
 * </p>
 * 
 * <p>
 * Permet de sélectionner les fichiers PDF à traiter ainsi que le dossier de destination des
 * fichiers créés par l'application.
 * </p>
 * 
 */
public class PrimaryPageView {

  /**
   * Référence à l'instance de la classe Main.
   */
  private Main application;

  /**
   * Référence à la scène de cette vue.
   */
  private Scene scene;

  /**
   * Référence au contrôleur associé à cette vue.
   * 
   * @see controller.PrimaryPageController
   */
  private PrimaryPageController controller;

  /**
   * <p>
   * Constructeur de la PrimaryPageView appelé au lancement de l'application
   * {@link main.Main#start}.
   * </p>
   * 
   * <p>
   * Charge le fxml associé, puis à partir du loader charge le controller défini dans le fxml pour
   * lui passer une référence à l'application principale. Enfin, set la scène de la PrimaryPageView.
   * </p>
   * 
   * @param application L'application principale.
   * @param width La taille de la scène en px.
   * @param height La hauteur de la scène en px.
   * @throws IOException Exception lors de l'accés au fxml associé à la PrimaryPage (jamais
   *         atteint).
   */
  public PrimaryPageView(Main application, double width, double height) throws IOException {
    this.application = application;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrimaryPage.fxml"));

    Parent root = loader.load();

    controller = loader.getController();
    controller.setApplication(application);

    scene = new Scene(root, width, height);

  }

  /**
   * Affiche la scène de la PrimaryPageView sur la primaryStage de l'application.
   */
  public void show() {
    Stage primaryStage = application.getPrimaryStage();

    primaryStage.setTitle("Charger des cartes VAC");
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

}
