package utils;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;
import javax.imageio.IIOImage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoggHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Une classe héritant de {@link Tesseract} et permettant réaliser la reconnaissance automatique
 * 
 * @author Abdoulaye DIOP
 * @version 2.0
 * @since 2.0
 */
public class MyTesseract extends Tesseract {

  /**
   * Représente le niveau confience concernat le résultat d'une reconnaissance
   */
  private int conf;

  private RenderedFormat renderedFormat = RenderedFormat.TEXT;
  private static final Logger logger = LoggerFactory.getLogger(new LoggHelper().toString());

  public MyTesseract() {
    // TODO Auto-generated constructor stub
    super();
  }

  /**
   * get conf
   * 
   * @return conf
   */
  public int getConf() {
    return conf;
  }

  /**
   * Effectue la reconnaissance automatique et calcule en plus la confience
   * 
   * see {@link Tesseract#doOCR(List, Rectangle)}
   * 
   * @return une chaine de caractère correspondant au contenu de l'image
   */
  @Override
  public String doOCR(List<IIOImage> imageList, String filename, Rectangle rect)
      throws TesseractException {
    init();
    setTessVariables();

    try {
      StringBuilder sb = new StringBuilder();
      int pageNum = 0;

      for (IIOImage oimage : imageList) {
        pageNum++;
        try {
          setImage(oimage.getRenderedImage(), rect);
          sb.append(getOCRText(filename, pageNum));
        } catch (IOException ioe) {
          // skip the problematic image
          logger.error(ioe.getMessage(), ioe);
        }
      }

      if (renderedFormat == RenderedFormat.HOCR) {
        sb.insert(0, htmlBeginTag).append(htmlEndTag);
      }

      return sb.toString();
    } finally {
      conf = getAPI().TessBaseAPIMeanTextConf(getHandle());
      dispose();
    }
  }

}
