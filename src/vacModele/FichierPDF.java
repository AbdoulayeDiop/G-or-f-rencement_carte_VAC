package vacModele;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


public class FichierPDF {
	private File fichier;
	private String name;//ajout pour searchbarController
	
	public FichierPDF(String fileName) {
		// TODO Auto-generated constructor stub
		fichier = new File(fileName);
		name = fichier.getName();//ajout pour searchbarController
	}
	
	public FichierPDF(File fichier) {
		// TODO Auto-generated constructor stub
		this.fichier = fichier;
		name = fichier.getName();//ajout pour searchbarController
	}
	
	public ArrayList<PagePDF> toPagePDF() throws Exception {
		// Creation d'une liste des differentes pages du fichier pdf
		
		PDDocument document = PDDocument.load(fichier);
		PDFRenderer pdfRenderer = new PDFRenderer(document);
	    
	    ArrayList<PagePDF> pagesPDF = new ArrayList<PagePDF>();
	    for (int i = 0; i < document.getNumberOfPages(); i++) {
	    	BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB); // Les page sont extraites sous forme d'image
            pagesPDF.add(new PagePDF(image));
        }
	    document.close();
	    return pagesPDF;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FichierPDF vac1 = new FichierPDF("VAC/LFBO.pdf");
		ArrayList<PagePDF> pagesPDF = vac1.toPagePDF();
		int i = 1;
		for(PagePDF page: pagesPDF){
			page.saveAsPNG("test" + i + ".png");
			i++;
		}
	}

	public String getName() { //ajout pour searchbarController
		return name;
	}

	public File getFichier() {
		return fichier;
	}

}
