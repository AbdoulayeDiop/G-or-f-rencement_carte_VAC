package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utils.CarteVAC;
import utils.FichierPDF;
import utils.PagePDF;

/**
 * Model est la classe faisant l'intermédiare entre l'interface garphique et le package Utils.
 * 
 * @author Abdoulaye DIOP
 * @author Nadir RANEM
 * @author Adrien LE ROHO
 * @version 2.0
 */
public class Model {
  public final String erreurExtractionPDFBOX =
      "Une erreur s'est produite lors de l'extraction des pages du fichier sélectionné";

  /**
   * Dictionnaire des PDF contenant des cartes VAC : (key : nom du PDF ; value : tableau de
   * {@link CarteVAC}).
   */
  HashMap<String, ArrayList<CarteVAC>> MapPDF;

  /**
   * Le status de chargement des {@link CarteVAC} en cours d'extraction.
   */
  private DoubleProperty loadStatus;

  /**
   * Liste de fichiers entrés par l'utilisateur.
   */
  private ObservableList<File> fileList;
  /**
   * Dossier de sortie des métadonnées sélectionné par l'utilisateur.
   */
  private ObjectProperty<File> outputDirectory;
  /**
   * Tableau des fichiers PDF sélectionnés par l'utilisateur.
   */
  private ArrayList<FichierPDF> pdfArray;

  /**
   * {@link CarteVAC} courante.
   */
  private ObjectProperty<CarteVAC> currentVAC;

  /**
   * fichier courant.
   */
  private StringProperty currentFile;

  /**
   * L'ensemble des fichiers contenant des cartes qui ont été mal extraites
   * 
   * example : {"LFBO.pdf" = [0, 2]} où "LFBO.pdf" est le nom du fichier et [0, 2] les indices des
   * cartes mal extraites dans la liste {@link #MapPDF}.get("LFBO.pdf") des cartes contenues dans le
   * fichier
   * 
   * @since 2.0
   */
  private ObjectProperty<HashMap<String, ArrayList<Integer>>> aExtraireManuellement;

  /**
   * get currentVAC
   * 
   * @return {@link #currentVAC}
   */
  public ObjectProperty<CarteVAC> getCurrentVAC() {
    return currentVAC;
  }

  /**
   * get aExtraireManuellement
   * 
   * @return {@link #aExtraireManuellement}
   * @since 2.0
   */
  public ObjectProperty<HashMap<String, ArrayList<Integer>>> getaExtraireManuellement() {
    return aExtraireManuellement;
  }

  /**
   * get currentFile
   * 
   * @return {@link #currentFile}
   */
  public StringProperty getCurrentFile() {
    return currentFile;
  }

  /**
   * L'instance static du modèle
   */
  private final static Model instance = new Model();

  /**
   * get instance
   * 
   * @return {@link #instance}
   */
  public static Model getInstance() {
    return instance;
  }

  public Model() {
    super();
    loadStatus = new SimpleDoubleProperty(0);
    MapPDF = new HashMap<String, ArrayList<CarteVAC>>();
    fileList = FXCollections.observableArrayList();
    outputDirectory = new SimpleObjectProperty<>();
    pdfArray = new ArrayList<FichierPDF>();
    aExtraireManuellement = new SimpleObjectProperty<HashMap<String, ArrayList<Integer>>>();
    aExtraireManuellement.set(new HashMap<String, ArrayList<Integer>>());
    currentFile = new SimpleStringProperty();
    currentFile.addListener(new ChangeListener<String>() {

      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue,
          String newValue) {
        try {
          extraireCarteVAC(newValue);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    currentVAC = new SimpleObjectProperty<>();
  }

  /**
   * get loadStatus
   * 
   * @return {@link #loadStatus}
   */
  public DoubleProperty getLoadStatus() {
    return loadStatus;
  }

  /**
   * get fileList
   * 
   * @return {@link #fileList}
   */
  public ObservableList<File> getFileList() {
    return fileList;

  }

  /**
   * get outputDirectory
   * 
   * @return {@link #outputDirectory}
   */
  public ObjectProperty<File> getOutputDirectory() {
    return outputDirectory;
  }

  /**
   * Remplace le contenu de {@link #outputDirectory} par <i>file</i>.
   * 
   * @param file la nouvelle valeur contenue dans {@link #outputDirectory}
   */
  public void setOutputDirectory(File file) {
    outputDirectory.set(file);
  }

  /**
   * Set fileList
   * 
   * @param fileList liste de fichiers
   */
  public void setFileList(ObservableList<File> fileList) {
    this.fileList = fileList;
  }

  /**
   * get MapPDF
   * 
   * @return {@link #MapPDF}
   */
  public HashMap<String, ArrayList<CarteVAC>> getMapPDF() {
    return MapPDF;
  }

  /**
   * get pdfArray
   * 
   * @return {@link #pdfArray}
   */
  public ArrayList<FichierPDF> getPdfArray() {
    return pdfArray;
  }

  /**
   * set pdfArray
   * 
   * @param pdfArray liste de FichierPDF
   */
  public void setPdfArray(ArrayList<FichierPDF> pdfArray) {
    this.pdfArray = pdfArray;
  }

  /**
   * Ajoute les fichiers de la liste <i>files</i> à {@link #fileList}.
   * 
   * @param files la liste de fichiers à ajouté
   */
  public void addFiles(List<File> files) {
    fileList.addAll(files);
  }

  /**
   * Initialise la liste de {@link FichierPDF} avec la liste de fichier {@link #fileList}.
   * 
   * @throws Exception L'exception lorsque fileList est nulle
   */
  public void computePDFArray() throws Exception {
    if (fileList != null) {
      for (File file : fileList) {
        FichierPDF pdf = new FichierPDF(file);
        pdfArray.add(pdf);// met à jour pdfArray
      }
    } else {
      throw new Exception("Invalid fileList : fileList null");
    }
  }

  /**
   * Remplace la carte VAC courante {@link #currentVAC} du modèle par la carte suivante ou
   * précédente de la liste des cartes du fichier PDF courant {@link #currentFile}.
   * 
   * @param direction Si <i>direction</i> = 1, la carte courante sera remplacée par la suivante Si
   *        <i>direction</i> = -1, la carte courante sera remplacée par la précédente
   * @throws Exception L'exception lorsque le paramètre direction n'est pas valide (cad différent de
   *         1 ou -1)
   */
  public void nextVac(int direction) throws Exception {
    if (direction == 1 || direction == -1) {

      String currentFile = getCurrentFile().get();
      if (getMapPDF().get(currentFile) != null) {
        ArrayList<CarteVAC> currentArrayList = getMapPDF().get(currentFile);
        int currentIndex = currentArrayList.indexOf(getCurrentVAC().get());

        int size = currentArrayList.size();
        int nextIndex = (currentIndex + direction + size) % size;

        CarteVAC nextVac = currentArrayList.get(nextIndex);
        getCurrentVAC().set(nextVac);
      }

    } else {
      throw new Exception("Invalid parameter : direction can only takes 1 or -1");
    }
  }

  /**
   * Extrait la ou les carte(s) VAC contenue(s) dans le fichier placé en paramètre.
   * <p>
   * Si le pdf est connu par le model, les cartes VAC sont extraites en utilisant le module
   * {@link PagePDF}. Le status de la barre de progression {@link loadStatus} est mise à jour
   * progressivement au cours de l'extraction.
   * </p>
   * 
   * @param nomPDF nom du PDF utilisé pour la recherche du fichier dans le dictionnaire des PDF.
   * 
   * 
   */
  public void extraireCarteVAC(String nomPDF) {
    for (File file : fileList) {
      if (file.getName().equals(nomPDF)) {
        if (!MapPDF.containsKey(nomPDF)) {
          Runnable r = new Runnable() {
            public void run() {
              ArrayList<CarteVAC> cartesCourrantes = new ArrayList<CarteVAC>();
              final FichierPDF pdf;
              pdf = new FichierPDF(file);
              pdfArray.add(pdf);
              loadStatus.set(0.001);
              ArrayList<PagePDF> page_list;
              try {
                page_list = pdf.toPagePDF(loadStatus);
                for (PagePDF page : page_list) {
                  if (page.isContientCarteVAC()) {
                    for (CarteVAC carteVAC : page.extraireCarteVAC()) {
                      loadStatus.set(loadStatus.get() + 0.1);
                      cartesCourrantes.add(carteVAC);
                    }
                  }
                }
                MapPDF.put(nomPDF, cartesCourrantes);
                currentVAC.set(MapPDF.get(nomPDF).get(0));
                loadStatus.set(0);
              } catch (Exception e) {
                // TODO Auto-generated catch block
                Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("Erreur d'extraction");
                    alert.setContentText(erreurExtractionPDFBOX);
                    alert.showAndWait();
                  }
                });
              }
            }
          };

          Thread t = new Thread(r);
          t.start();
        } else {
          currentVAC.set(MapPDF.get(nomPDF).get(0));
        }
      }
    }
  }

  /**
   * Extrait automatiquement la ou les carte(s) VAC contenue(s) dans l'ensemble des fichiers pdf
   * connus par le model. L'orsque l'extraction automatique échoue pur certains fichiers, ces
   * derniers sont ajouté dans {@link #aExtraireManuellement} avec les indices des cartes
   * concernées. Ces fichiers apparaissent ensuite sur fond rouge.
   * 
   * @since 2.0
   */

  public void extraireCarteVAC() {
    Runnable r = new Runnable() {
      public void run() {
        int i = 0;
        for (File file : fileList) {
          if (!MapPDF.containsKey(file.getName())) {
            ArrayList<CarteVAC> cartesCourrantes = new ArrayList<CarteVAC>();
            final FichierPDF pdf;
            pdf = new FichierPDF(file);
            pdfArray.add(pdf);
            loadStatus.set(0.001);
            ArrayList<PagePDF> page_list;
            try {
              page_list = pdf.toPagePDF(loadStatus);
              for (PagePDF page : page_list) {
                if (page.isContientCarteVAC()) {
                  for (CarteVAC carteVAC : page.extraireCarteVAC()) {
                    loadStatus.set(loadStatus.get() + 0.1);
                    cartesCourrantes.add(carteVAC);
                  }
                }
              }
            } catch (Exception e) {
              // TODO Auto-generated catch block
            }
            MapPDF.put(file.getName(), cartesCourrantes);
            loadStatus.set(0);
          }
          HashMap<String, ArrayList<Integer>> mapManuelle =
              new HashMap<String, ArrayList<Integer>>(aExtraireManuellement.get());
          int j = 0;
          for (CarteVAC carteVAC : MapPDF.get(file.getName())) {
            i++;
            currentVAC.set(carteVAC);
            try {
              carteVAC.getMetaDonnees().setIdOACI(file.getName().split(".p")[0]);
              if (carteVAC.calculerEchelle() == 0) {
                if (carteVAC.getMetaDonnees().getEchelleVerticale() > 0.15) {
                  carteVAC.getMetaDonnees().setType("Approche");
                } else if (carteVAC.getMetaDonnees().getEchelleVerticale() > 0) {
                  carteVAC.getMetaDonnees().setType("Atterrissage");
                }
                carteVAC.save(outputDirectory.get());
              } else {
                if (mapManuelle.containsKey(file.getName())) {
                  mapManuelle.get(file.getName()).add(j);
                } else {
                  mapManuelle.put(file.getName(), new ArrayList<Integer>());
                  mapManuelle.get(file.getName()).add(j);
                }
              }
            } catch (NumberFormatException | IOException e) {
              // TODO Auto-generated catch block
              if (mapManuelle.containsKey(file.getName())) {
                mapManuelle.get(file.getName()).add(j);
              } else {
                mapManuelle.put(file.getName(), new ArrayList<Integer>());
                mapManuelle.get(file.getName()).add(j);
              }
            }
            j++;
            aExtraireManuellement.set(mapManuelle);
          }
        }
        currentFile.set(fileList.get(fileList.size() - 1).getName());
      }
    };
    Thread t = new Thread(r);
    t.start();
  }

}
