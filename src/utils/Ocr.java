package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

/**
 * Ocr pour la reconnaissance automatique.
 * 
 * @author Abdoulaye DIOP
 * @version 2.0
 * @since 2.0
 */
public class Ocr {

  /**
   * Modifie en place <i>img</i> pour le transformer en une image en noir et blanc
   * 
   * @param img l'image à modifer
   * @return l'image transformée en noir et blanc
   */
  public static BufferedImage toGrayScale(BufferedImage img) {
    for (int i = 0; i < img.getHeight(); i++) {
      for (int j = 0; j < img.getWidth(); j++) {
        float r = new Color(img.getRGB(j, i)).getRed();
        float g = new Color(img.getRGB(j, i)).getGreen();
        float b = new Color(img.getRGB(j, i)).getBlue();

        int grayScaled = (int) (r + g + b) / 3;
        img.setRGB(j, i, new Color(grayScaled, grayScaled, grayScaled).getRGB());
      }
    }
    return img;
  }

  /**
   * Permet de reconnaitre le contenu de l'image
   * 
   * @param img l'image à reconnaitre
   * @return un dictionnaire {"value"=val, "conf"=c} où val est la valeur reconnu et c le niveau de
   *         confience en cette valeur
   */
  public static HashMap<String, Integer> recognizeColor(BufferedImage imgBuff) throws Exception {
    MyTesseract tess = new MyTesseract();
    tess.setDatapath(new File(".").getPath());

    // Get OCR result
    String outText = tess.doOCR(imgBuff);
    System.out.println(outText);
    int conf = tess.getConf();
    String result = "";
    for (String s : outText.split("")) {
      try {
        result += Integer.valueOf(s);
      } catch (Exception e) {
        // TODO: handle exception
      }
    }

    HashMap<String, Integer> map = new HashMap<String, Integer>();
    map.put("value", Integer.valueOf(result));
    map.put("conf", conf);
    if (map.get("value") > 90 || map.get("value") < -90) {
      throw new Exception("Erreur de reconnaissance");
    }
    return map;
  }

  /**
   * Permet de reconnaitre le contenu de l'image en la transformant en noir et blanc d'abord
   * 
   * @param img l'image à reconnaitre
   * @return un dictionnaire {"value"=val, "conf"=c} où val est la valeur reconnu et c le niveau de
   *         confience en cette valeur
   */
  public static HashMap<String, Integer> recognizeGray(BufferedImage imgBuff) throws Exception {
    MyTesseract tess = new MyTesseract();
    tess.setDatapath(new File(".").getPath());

    // Get OCR result
    String outText = tess.doOCR(toGrayScale(imgBuff));
    System.out.println(outText);
    int conf = tess.getConf();
    String result = "";
    for (String s : outText.split("")) {
      try {
        result += Integer.valueOf(s);
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
    if (result.length() > 2) {
      result = "";
    }
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    map.put("value", Integer.valueOf(result));
    map.put("conf", conf);
    if (map.get("value") > 90 || map.get("value") < -90) {
      throw new Exception("Erreur de reconnaissance");
    }
    return map;
  }

  /**
   * Permet de reconnaitre le contenu de l'image en combinant {@link #recognizeColor(BufferedImage)}
   * et {@link #recognizeGray(BufferedImage)}
   * 
   * @param img l'image à reconnaitre
   * @return un dictionnaire {"value"=val, "conf"=c} où val est la valeur reconnu et c le niveau de
   *         confience en cette valeur
   */
  public static HashMap<String, Integer> recognize(BufferedImage imgBuff) throws Exception {
    HashMap<String, Integer> map1;
    HashMap<String, Integer> map2;
    try {
      map1 = recognizeColor(imgBuff);
      try {
        map2 = recognizeGray(imgBuff);
        if (map1.get("conf") >= map2.get("conf")) {
          return map1;
        } else {
          return map2;
        }
      } catch (Exception e) {
        // TODO: handle exception
        return map1;
      }
    } catch (Exception e) {
      return recognizeGray(imgBuff);
    }
  }

}
