package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.PrimaryPageView;

/**
 * <p>
 * Main de l'application.
 * </p>
 * 
 * <p>
 * Application testée sur OpenJDK11.
 * </p>
 * 
 * <p>
 * Besoin des bibliothèques : <a href ="https://gluonhq.com/products/javafx/">JavaFX11 LTS</a> ;
 * <a href ="https://pdfbox.apache.org/download.cgi">PDFBox 2.0.15</a>
 * </p>
 * 
 * <p>
 * Arguments de la VM : "--module-path /path/to/javafx-sdk-11.0.2/lib --add-modules
 * javafx.controls,javafx.fxml,javafx.swing". Enlever l'option : "Use the -XstartOnFirstThread
 * argument when launching with SWT".
 * </p>
 */
public class Main extends Application {

  /**
   * Référence à la primaryStage de l'application.
   */
  private Stage primaryStage;

  /**
   * Retourne la primaryStage de l'application.
   * 
   * @return Stage La primaryStage de l'application.
   */
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  /**
   * <p>
   * Méthode start appelé par la méthode launch de l'application.
   * </p>
   * 
   * <p>
   * Set la primaryStage, créer une instance PrimaryPageView puis l'affiche dans la primaryStage.
   * </p>
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;

    PrimaryPageView primaryPage = new PrimaryPageView(Main.this, 400, 300);
    primaryPage.show();
  }

  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
  }

  /**
   * Méthode main de l'application qui lance la méthode launch pour démarrer l'application JavaFX.
   * 
   * @param args Arguments à donner à l'application.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
