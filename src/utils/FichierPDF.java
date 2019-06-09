package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class FichierPDF {
  private File fichier;
  private String name;// ajout pour searchbarController

  public FichierPDF(String fileName) {
    // TODO Auto-generated constructor stub
    fichier = new File(fileName);
    name = fichier.getName();// ajout pour searchbarController
  }

  public FichierPDF(File fichier) {
    // TODO Auto-generated constructor stub
    this.fichier = fichier;
    name = fichier.getName();// ajout pour searchbarController
  }

  public ArrayList<PagePDF> toPagePDF() throws Exception {
    // Creation d'une liste des differentes pages du fichier pdf

    PDDocument document = PDDocument.load(fichier);
    PDFRenderer pdfRenderer = new PDFRenderer(document);

    ArrayList<PagePDF> pagesPDF = new ArrayList<PagePDF>();
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB); // Les page sont
                                                                                   // extraites sous
                                                                                   // forme d'image
      pagesPDF.add(new PagePDF(image));
    }
    document.close();
    return pagesPDF;
  }

  public ArrayList<PagePDF> toPagePDF(DoubleProperty status) throws Exception {
    // Creation d'une liste des differentes pages du fichier pdf

    PDDocument document = PDDocument.load(fichier);
    PDFRenderer pdfRenderer = new PDFRenderer(document);

    ArrayList<PagePDF> pagesPDF = new ArrayList<PagePDF>();
    double step = 1 / (1.5 * document.getNumberOfPages());
    for (int i = 0; i < document.getNumberOfPages(); i++) {
      BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB); // Les page sont
                                                                                   // extraites sous
                                                                                   // forme d'image
      status.set(status.get() + step);
      pagesPDF.add(new PagePDF(image));
    }
    document.close();
    return pagesPDF;
  }

  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    String nom = "LFAD";
    FichierPDF vac1 = new FichierPDF("../VAC/" + nom + ".pdf");
    ArrayList<PagePDF> pagesPDF = vac1.toPagePDF();
    int i = 1;
    for (PagePDF page : pagesPDF) {
      page.saveAsPNG("test" + nom + "/page" + i + ".png");
      i++;
    }
  }

  public String getName() { // ajout pour searchbarController
    return name;
  }

  public File getFichier() {
    return fichier;
  }

}
