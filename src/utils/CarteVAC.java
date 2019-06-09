package utils;

import geometrie.Point;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 * CarteVAC est la classe représentant une carte VAC sous forme d'une image.
 * 
 * @author Nadir RANEM
 * @author Abdoulaye DIOP
 * @version 2.0
 */
public class CarteVAC extends BufferedImage {
  private MetaDonnees metaDonnees;
  static String nomVAC;
  ArrayList<Integer> echellePetitTirets = new ArrayList<Integer>();

  /**
   * Les métadonnées associées à la carte VAC.
   */
  public CarteVAC(int width, int height, int imageType) {
    super(width, height, imageType);
    metaDonnees = new MetaDonnees();
  }

  public CarteVAC(int width, int height, int imageType, IndexColorModel cm) {
    super(width, height, imageType, cm);
    metaDonnees = new MetaDonnees();
  }

  public CarteVAC(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
      Hashtable<?, ?> properties) {
    super(cm, raster, isRasterPremultiplied, properties);
    metaDonnees = new MetaDonnees();
  }

  public CarteVAC(BufferedImage img) {
    super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);

    // Dessiner l'image dans le buffured image
    Graphics2D bGr = this.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();
    metaDonnees = new MetaDonnees();
  }

  public MetaDonnees getMetaDonnees() {
    return metaDonnees;
  }

  /**
   * <p>
   * Met à jour {@link #metaDonnees metaDonnees} en calculant
   * {@link utils.MetaDonnees#echelleVerticale MetaDonnees.echelleVerticale} et
   * {@link utils.MetaDonnees#echelleHorizontale MetaDonnees.echelleHorizontale} puis
   * {@link utils.MetaDonnees#coordHautGauche MetaDonnees.coordHautGauche} et
   * {@link utils.MetaDonnees#coordBasDroite MetaDonnees.coordBasDroite} grâce à deux couples de
   * référence (<i>pixel1</i>, <i>coord1</i>) et (<i>pixel2</i>, <i>coord2</i>).
   * </p>
   * 
   * @param pixel1 position en pixel du premier point sur l'image
   * @param coord1 coordonnées réelles du premier point
   * @param pixel2 position en pixel du deuxième point sur l'image
   * @param coord2 coordonnées réelles du deuxième point
   */
  public void calculerEchelle(Point pixel1, Point coord1, Point pixel2, Point coord2) {
    double echelleVerticale =
        Math.abs((coord2.getY() - coord1.getY()) / (pixel2.getY() - pixel1.getY()));
    System.out.println(metaDonnees.getIdOACI() + " " + "echelleVerticale : " + echelleVerticale);
    double echelleHorizontale =
        Math.abs((coord2.getX() - coord1.getX()) / (pixel2.getX() - pixel1.getX()));
    System.out
        .println(metaDonnees.getIdOACI() + " " + "echelleHorizontale : " + echelleHorizontale);
    metaDonnees.setEchelleVerticale(echelleVerticale);
    metaDonnees.setEchelleHorizontale(echelleHorizontale);
    double x, y;
    x = (coord1.getX() - pixel1.getX() * echelleHorizontale) / 1000;
    y = (coord1.getY() + pixel1.getY() * echelleVerticale) / 1000;
    metaDonnees.setCoordHautGauche(new Point(x, y));
    x = (coord1.getX() + (getWidth() - pixel1.getX()) * echelleHorizontale) / 1000;
    y = (coord1.getY() - (getHeight() - pixel1.getY()) * echelleVerticale) / 1000;
    metaDonnees.setCoordBasDroite(new Point(x, y));
  }

  public int calculerEchelle() throws IOException {
    Point pixel1, coord1, pixel2, coord2;

    ArrayList<EchellePictLat> latImages = scaleToPicLat();
    ArrayList<EchellePictLong> longImages = scaleToPicLong();
    if (latImages.size() >= 2 && longImages.size() >= 2) {
      EchellePictLat latImage1 = latImages.get(0);
      EchellePictLat latImage2 = latImages.get(1);
      EchellePictLong longImage1 = longImages.get(0);
      EchellePictLong longImage2 = longImages.get(1);
      boolean lat1Found = false;
      boolean lat2Found = false;
      boolean long1Found = false;
      boolean long2Found = false;

      System.out.println("-----------------------");

      HashMap<String, Integer> lat1Degres = new HashMap<String, Integer>();
      HashMap<String, Integer> lat1Minute = new HashMap<String, Integer>();
      HashMap<String, Integer> lat1Seconde = new HashMap<String, Integer>();

      int i = 0;
      while (!lat1Found && i < latImages.size() - 1) {
        try {
          latImage1 = latImages.get(i);
          lat1Degres = Ocr.recognize(latImage1.getDegres());
          System.out.println("lat1Degres : " + lat1Degres.get("value"));
          System.out.println("mean_conf: " + lat1Degres.get("conf"));
          lat1Minute = Ocr.recognize(latImage1.getMinute());
          System.out.println("lat1Minute : " + lat1Minute.get("value"));
          System.out.println("mean_conf: " + lat1Minute.get("conf"));
          lat1Seconde = new HashMap<String, Integer>();
          lat1Seconde.put("value", 0);
          lat1Seconde.put("conf", 100);
          if (latImage1.getSecond() != null) {
            lat1Seconde = Ocr.recognize(latImage1.getSecond());
            System.out.println("lat1Seconde : " + lat1Seconde.get("value"));
            System.out.println("mean_conf: " + lat1Seconde.get("conf"));
          }
          if (lat1Degres.get("conf") >= 40 && lat1Minute.get("conf") >= 40
              && lat1Seconde.get("conf") >= 40) {
            lat1Found = true;
          } else {
            i++;
          }
        } catch (Exception e) {
          // TODO: handle exception
          i++;
        }
      }
      if (!lat1Found) {
        return 1;
      }

      i++;
      HashMap<String, Integer> lat2Degres = new HashMap<String, Integer>();
      HashMap<String, Integer> lat2Minute = new HashMap<String, Integer>();
      HashMap<String, Integer> lat2Seconde = new HashMap<String, Integer>();
      while (!lat2Found && i < latImages.size()) {
        try {
          latImage2 = latImages.get(i);
          lat2Degres = Ocr.recognize(latImage2.getDegres());
          System.out.println("lat2Degres : " + lat2Degres.get("value"));
          System.out.println("mean_conf: " + lat2Degres.get("conf"));
          lat2Minute = Ocr.recognize(latImage2.getMinute());
          System.out.println("lat2Minute : " + lat2Minute.get("value"));
          System.out.println("mean_conf: " + lat2Minute.get("conf"));
          lat2Seconde = new HashMap<String, Integer>();
          lat2Seconde.put("value", 0);
          lat2Seconde.put("conf", 100);
          if (latImage2.getSecond() != null) {
            lat2Seconde = Ocr.recognize(latImage2.getSecond());
            System.out.println("lat2Seconde : " + lat2Seconde.get("value"));
            System.out.println("mean_conf: " + lat2Seconde.get("conf"));
          }
          if (lat2Degres.get("conf") >= 40 && lat2Minute.get("conf") >= 40
              && lat2Seconde.get("conf") >= 40) {
            lat2Found = true;
          } else {
            i++;
          }
        } catch (Exception e) {
          // TODO: handle exception
          i++;
        }
      }

      if (!lat2Found) {
        return 1;
      }

      i = 0;
      HashMap<String, Integer> long1Degres = new HashMap<String, Integer>();
      HashMap<String, Integer> long1Minute = new HashMap<String, Integer>();
      while (!long1Found && i < latImages.size() - 1) {
        try {
          longImage1 = longImages.get(i);
          long1Degres = Ocr.recognize(longImage1.getDegres());
          System.out.println("long1Degres : " + long1Degres.get("value"));
          System.out.println("mean_conf: " + long1Degres.get("conf"));
          long1Minute = Ocr.recognize(longImage1.getMinute());
          System.out.println("long1Minute : " + long1Minute.get("value"));
          System.out.println("mean_conf: " + long1Minute.get("conf"));
          if (long1Degres.get("conf") >= 40 && long1Minute.get("conf") >= 40) {
            long1Found = true;
          } else {
            i++;
          }
        } catch (Exception e) {
          // TODO: handle exception
          i++;
        }
      }
      if (!long1Found) {
        return 1;
      }

      i++;
      HashMap<String, Integer> long2Degres = new HashMap<String, Integer>();
      HashMap<String, Integer> long2Minute = new HashMap<String, Integer>();
      while (!long2Found && i < latImages.size()) {
        try {
          longImage2 = longImages.get(i);
          long2Degres = Ocr.recognize(longImage2.getDegres());
          System.out.println("long2Degres : " + long2Degres.get("value"));
          System.out.println("mean_conf: " + long2Degres.get("conf"));
          long2Minute = Ocr.recognize(longImage2.getMinute());
          System.out.println("long2Minute : " + long2Minute.get("value"));
          System.out.println("mean_conf: " + long2Minute.get("conf"));
          if (long2Degres.get("conf") >= 40 && long2Minute.get("conf") >= 40) {
            long2Found = true;
          } else {
            i++;
            System.out.println("i = " + i);
          }
        } catch (Exception e) {
          // TODO: handle exception
          i++;
        }
      }
      if (!long2Found) {
        return 1;
      }

      double lat1 = 1000 * lat1Degres.get("value") + 1000 * lat1Minute.get("value") / 60
          + lat1Seconde.get("value") / 3.6;
      double lat2 = 1000 * lat2Degres.get("value") + 1000 * lat2Minute.get("value") / 60
          + lat2Seconde.get("value") / 3.6;;
      double long1 = 1000 * long1Degres.get("value") + 1000 * long1Minute.get("value") / 60;
      double long2 = 1000 * long2Degres.get("value") + 1000 * long2Minute.get("value") / 60;

      coord1 = new Point(long1, lat1);
      coord2 = new Point(long2, lat2);
      pixel1 = new Point(longImage1.getPosition(), latImage1.getPosition());
      pixel2 = new Point(longImage2.getPosition(), latImage2.getPosition());

      calculerEchelle(pixel1, coord1, pixel2, coord2);
      return 0;
    } else if (latImages.size() == 0 && longImages.size() == 0) {
      return 0;
    }
    return 1;
  }

  /**
   * <p>
   * Sauvegarde l'image contenue dans l'objet {@link CarteVAC} au format PNG sous le nom
   * <i>filename</i>.
   * </p>
   * 
   * @param filename chemin du fichier de sauvegarde
   */
  public void saveAsPNG(String filename) {
    File fichierImage = new File(filename);
    String format = "PNG";
    try {
      ImageIO.write(this, format, fichierImage);
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  /**
   * <p>
   * Sauvegarde l'image contenue dans l'objet {@link CarteVAC} et les {@link #metaDonnees
   * metaDonnees} associées dans le fichier de destination <i>file</i>.
   * </p>
   * 
   * @param file Le fichier de destination de la sauvegarde
   */
  public void save(File file) {
    String source = file.getPath().replace("\\", "/") + "/";
    saveAsPNG(source + metaDonnees.getIdOACI() + metaDonnees.getType() + ".png");
    try {
      FileWriter fichierMetaDonnees = new FileWriter(source + "metaDonnees.csv", true);
      fichierMetaDonnees.write(metaDonnees.getIdOACI() + ",");
      fichierMetaDonnees.write(metaDonnees.getType() + ",");
      fichierMetaDonnees.write(metaDonnees.getEchelleVerticale() + ",");
      fichierMetaDonnees.write(metaDonnees.getEchelleHorizontale() + ",");
      fichierMetaDonnees.write(metaDonnees.getCoordHautGauche().toString() + ",");
      fichierMetaDonnees.write(metaDonnees.getCoordBasDroite().toString() + "\n");
      fichierMetaDonnees.close();
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

  /**
   * <p>
   * Verification de la couleur d'un object en param�tre (pixel) utiliser pour detecter les tirets
   * de graduations
   * </p>
   * 
   * @param o objet � verifier
   * @return vrai (faux sinon) si la couleur est bleue RGB(0,102,181)
   */
  private Boolean colorScaleCheck(Object o) {
    ColorModel modeleCouleur = this.getColorModel();
    // couleur recherchée : 0,102,181 du bord bleu

    Boolean redCheck = modeleCouleur.getRed(o) < 5;
    Boolean greenCheck = modeleCouleur.getGreen(o) == 102;
    Boolean blueCheck = modeleCouleur.getBlue(o) == 181;

    return redCheck && greenCheck && blueCheck;

  }

  /**
   * <p>
   * Fournit la liste des positions en pixels des tirets bleu �en latitude de graduations
   * </p>
   * 
   * @return tableau d'entier des positions des tirets en latitude par rapport au coin sup�rieur
   *         droit (0,0)
   */
  private ArrayList<Integer> scaleAutoExtractorLat() {
    Raster tramePixel = this.getRaster();
    int nb1 = 0, nb2 = 0, espaceMinEntreDeuxEchelles = 150, offsetDebutTiret = 5;
    ArrayList<Integer> echelle = new ArrayList<Integer>();

    ArrayList<Integer> compteurs = new ArrayList<Integer>();
    for (int i = 23; i < tramePixel.getHeight() - 23; i++) {
      int compteur = 0, compteurLittle = 0;
      // System.out.println("Ligne " + i + " : -------------------------------");

      for (int j = 0; j < 50; j++) {
        Object objCouleur = tramePixel.getDataElements(j, i, null);
        if (!colorScaleCheck(objCouleur) && j == offsetDebutTiret) {
          compteur = -100;
        }
        if (colorScaleCheck(objCouleur)) {
          compteur += 1;
          if (j < 30) {
            compteurLittle += 1;
          }
        }

      }
      // System.out.println("detectés grands = " + compteur);
      // System.out.println("detectés petits = " + compteurLittle);

      if (compteurLittle > 17 && compteurLittle < 25) { // les petit tirets bleus
        nb1 += 1;
        echellePetitTirets.add(i);

      }

      if (compteur > 30) { // les grands tirets bleus
        if (echelle.size() != 0) {
          if (i - echelle.get(nb2 - 1) > espaceMinEntreDeuxEchelles) { // les echelles proches sont
                                                                       // pas
                                                                       // possibles
            nb2 += 1;
            compteurs.add(compteur);
            echelle.add(i);
          }
        } else {
          nb2 += 1;
          compteurs.add(compteur);
          echelle.add(i);
        }
      }
    }

    for (int petitTiret : echellePetitTirets) {
      if (detectEchelle(petitTiret)) {
        echelle.add(petitTiret);
      }
    }
    System.out.println("petits tirets Lat : " + nb1);
    System.out.println("grands tirets Lat : " + nb2);
    System.out.println(echelle);
    System.out.println(compteurs);

    return echelle;
  }

  /**
   * <p>
   * Fournit la liste des positions en pixels des tirets bleu de graduations en longitude
   * </p>
   * 
   * @return tableau d'entier des positions des tirets en longitude par rapport au coin inf�rieur
   *         droit (height,0)
   */
  private ArrayList<Integer> scaleAutoExtractorLong() {
    Raster tramePixel = this.getRaster();
    int nb1 = 0, nb2 = 0, espaceMinEntreDeuxEchelles = 150, offsetDebutTiret = 5;
    ArrayList<Integer> echelle = new ArrayList<Integer>();
    ArrayList<Integer> compteurs = new ArrayList<Integer>();
    for (int j = 33; j < getWidth() - 29; j++) {
      // System.out.println("Ligne " + j + " : -------------------------------");
      int compteur = 0, compteurLittle = 0;
      for (int i0 = 0; i0 < 50; i0++) {
        int i = getHeight() - i0 - 1;
        Object objCouleur = tramePixel.getDataElements(j, i, null);
        if (!colorScaleCheck(objCouleur) && i0 == offsetDebutTiret) {// si au debut pas bleu c'est
                                                                     // pas un tiret
          compteur = -100;
        }
        if (colorScaleCheck(objCouleur)) {
          compteur += 1;
          if (i0 < 30) {
            compteurLittle += 1;
          }
        }

      }

      if (compteurLittle > 17 && compteurLittle < 25) { // les petit tirets bleus
        nb1 += 1;
      }

      if (compteur > 30) { // les grands tirets bleus
        if (echelle.size() != 0) {
          if (j - echelle.get(nb2 - 1) > espaceMinEntreDeuxEchelles) { // les echelles proches sont
                                                                       // pas
                                                                       // possibles
            nb2 += 1;
            compteurs.add(compteur);
            echelle.add(j);
          }
        } else {
          nb2 += 1;
          compteurs.add(compteur);
          echelle.add(j);
        }
      }
    }
    System.out.println("petits tirets Long: " + nb1);
    System.out.println("grands tirets Long: " + nb2);
    System.out.println(echelle);
    System.out.println(compteurs);

    return echelle;
  }

  private boolean verifyLimit(int limitInf, int limitSup, int nombre) {
    return nombre < limitSup && nombre > limitInf;
  }

  private boolean detectEchelle(int tiret) {
    int width = 20, height = 70, compteur = 0;
    boolean detectedNumber = false, blueDetected = false;
    Raster trameCarteVAC = this.getRaster();
    if (verifyLimit(getHeight() - height, height / 2, tiret)) {
      return false;
    }
    for (int i = 0; i < height; i++) {
      blueDetected = false;
      int j = 0;
      while (j < width && !blueDetected) {
        if (verifyLimit(0, getHeight(), i + tiret - height / 2)) {
          Object obj = trameCarteVAC.getDataElements(j, i + tiret - height / 2, null);
          // System.out.println(colorScaleCheck(obj) + " : " + modeleCouleur.getRed(obj) +
          // ","
          // + modeleCouleur.getGreen(obj) + "," + modeleCouleur.getBlue(obj) + " / ");
          if (colorScaleCheck(obj)) {
            blueDetected = true;
            compteur++;
          }
        }
        j++;
      }

    }
    if (compteur > 15) {// nombre de ligne contenant du bleu = grde proba de présence d'un nombre
      detectedNumber = true;

    }
    // System.out.println("nombre pixel bleu petit tiret(" + tiret + ") : " +
    // compteur);
    return detectedNumber;
  }

  private EchellePictLat latToEchelle(int tiret) {
    int leftOffset = 4;
    Raster trameCarteVAC = this.getRaster();

    int width = 50 - leftOffset, height = 70, heightFor = height;// carre d'extraction de la photo
                                                                 // contenant
                                                                 // l'echelle
    EchellePictLat img = new EchellePictLat(width, height, TYPE_INT_ARGB);
    WritableRaster trameEchelle = img.getRaster();
    if (!verifyLimit(0, getHeight(), tiret + height / 2)) {
      heightFor = getHeight() - tiret + height / 2;
    }

    for (int i = 0; i < heightFor; i++) {
      for (int j = 0; j < width; j++) {
        if (verifyLimit(0, getHeight(), i + tiret - height / 2)) {
          trameEchelle.setDataElements(j, i,
              trameCarteVAC.getDataElements(j + leftOffset, i + tiret - height / 2, null));
        }
      }
    }
    return img;
  }

  private EchellePictLat extractPetitTiretLat(int tiret) {
    EchellePictLat img = latToEchelle(tiret);
    int leftOffset = 4;
    int width = 50 - leftOffset, height = 35, echelleHeight = 70;// carre d'extraction de la photo
                                                                 // contenant les
                                                                 // secondes
    NombrePict second = new NombrePict(width, height, TYPE_INT_ARGB);

    Raster trameCarteVAC = this.getRaster();
    WritableRaster trameSecond = second.getRaster();
    // System.out.print("height : " + String.valueOf(getHeight()));

    for (int i = 0; i < height - 1; i++) {
      for (int j = 0; j < width; j++) {
        if (verifyLimit(0, getHeight(), i + tiret + echelleHeight / 2)) {
          // System.out.print("val : " + i + "," + j);
          trameSecond.setDataElements(j, i,
              trameCarteVAC.getDataElements(j + leftOffset, i + tiret + echelleHeight / 2, null));
        }
      }
    }
    if (detectEchelle(tiret + 3 * height / 2) && verifyLimit(0, getHeight(), tiret + height)) {
      if (second.getpExtraction() > 0.5) {
        img.setSecond(
            img.resize(trameSecond, second.detectTopNumber(1.0), second.detectLeftNumber(1.0)));
      } else {
        img.setSecond(
            img.resize(trameSecond, second.detectTopNumber(0.0), second.detectLeftNumber(0.0)));
      }
    }

    return img;
  }

  private EchellePictLong longToEchelle(int tiret) {
    int bottomOffset = 4;
    int width = 120, height = 50 - bottomOffset;// carre d'extraction de la photo contenant
                                                // l'echelle
    int widthFor = width;
    EchellePictLong img = new EchellePictLong(width, height, TYPE_INT_ARGB);
    Raster trameCarteVAC = this.getRaster();
    WritableRaster trameEchelle = img.getRaster();
    if (tiret + width / 2 > getWidth()) {
      widthFor = getWidth() - tiret + width / 2;
    }
    for (int i0 = 0; i0 < height; i0++) {
      int i = getHeight() - height + i0 - bottomOffset;
      for (int j = 0; j < widthFor; j++) {
        if (verifyLimit(0, getWidth(), j + tiret - width / 2)) {
          trameEchelle.setDataElements(j, i0,
              trameCarteVAC.getDataElements(j + tiret - width / 2, i, null));
        }
      }
    }
    return img;
  }

  private boolean isGrandTiret(int tiret) {
    boolean res = false;
    int width = 50, compteur = 0;
    Raster trameCarteVAC = this.getRaster();

    for (int j = 0; j < width; j++) {
      Object obj = trameCarteVAC.getDataElements(j, tiret, null);
      if (colorScaleCheck(obj)) {
        compteur++;
      }
    }
    if (compteur > 30) {
      res = true;
    }
    return res;
  }

  /**
   * <p>
   * Fournit la liste des objets {@link EchellePictLat} extraits en latitude
   * </p>
   * 
   * @return tableau de {@link EchellePictLat}
   */
  public ArrayList<EchellePictLat> scaleToPicLat() {
    ArrayList<EchellePictLat> imgEchelles = new ArrayList<>();
    ArrayList<Integer> echelle = scaleAutoExtractorLat();

    for (int echelle_i : echelle) {

      EchellePictLat img = latToEchelle(echelle_i);
      if (!isGrandTiret(echelle_i)) {
        img = extractPetitTiretLat(echelle_i);

      }
      img.setPosition(echelle_i);
      img.setNomVAC(nomVAC);
      img.divideInNombrePic();// diviser l'image en deux images degrès et minute
      imgEchelles.add(img);
      // img.saveAsPNG("test" + nomVAC + "/echelle/lat" + echelle_i + ".png");

    }

    return imgEchelles;
  }

  /**
   * <p>
   * Fournit la liste des objets {@link EchellePictLong} extraits en longitude
   * </p>
   * 
   * @return tableau de {@link EchellePictLong}
   */
  public ArrayList<EchellePictLong> scaleToPicLong() {
    ArrayList<EchellePictLong> imgEchelles = new ArrayList<>();
    ArrayList<Integer> echelle = scaleAutoExtractorLong();
    for (int echelle_i : echelle) {
      try {
        EchellePictLong img = longToEchelle(echelle_i);
        img.setPosition(echelle_i);
        img.setNomVAC(nomVAC);
        img.divideInNombrePic();// diviser l'image en deux images degrès et minute
        imgEchelles.add(img);
        // img.saveAsPNG("test" + nomVAC + "/echelle/long" + echelle_i + ".png");
      } catch (ArrayIndexOutOfBoundsException e) {
        // TODO: handle exception
      }
    }

    return imgEchelles;
  }

}
