package vacModele;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class PagePDF extends BufferedImage {
	private boolean contientCarteVAC;
	private int[] cadreCarteVAC;

	public PagePDF(Image img) {
		super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);

		// Dessiner l'image dans le buffured image
		Graphics2D bGr = this.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Chercher le cadre de la Carte VAC
		cadreCarteVAC = new int[4];
		contientCarteVAC = false;
		trouverCadre();
	}

	public PagePDF(BufferedImage img) {
		super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);

		// Dessiner l'image dans le buffured image
		Graphics2D bGr = this.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Chercher le cadre de la Carte VAC
		cadreCarteVAC = new int[4];
		contientCarteVAC = false;
		trouverCadre();
	}

	public PagePDF(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
		cadreCarteVAC = new int[4];
		contientCarteVAC = false;
		trouverCadre();
	}

	public PagePDF(int arg0, int arg1, int arg2, IndexColorModel arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
		cadreCarteVAC = new int[4];
		contientCarteVAC = false;
		trouverCadre();
	}

	public PagePDF(ColorModel arg0, WritableRaster arg1, boolean arg2, Hashtable<?, ?> arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
		cadreCarteVAC = new int[4];
		contientCarteVAC = false;
		trouverCadre();
	}

	private boolean memeCouleurNoire(Object o1, Object o2) {
		// Indique si o1 et o2 ont la même couleur noire
		ColorModel modeleCouleur = this.getColorModel();
		boolean cond1 = modeleCouleur.getRed(o1) == modeleCouleur.getRed(o2) && modeleCouleur.getRed(o2) <= 50;
		boolean cond2 = modeleCouleur.getBlue(o1) == modeleCouleur.getBlue(o2) && modeleCouleur.getBlue(o2) <= 50;
		boolean cond3 = modeleCouleur.getGreen(o1) == modeleCouleur.getGreen(o2) && modeleCouleur.getGreen(o2) <= 50;
		return cond1 && cond2 && cond3;
	}

	private int[] trouverCadre() {
		// Permet de trouver le cadre noir qui entoure la carte VAC contenue dans la
		// page
		// Renvoie un tableau d'entier [i1, j1, i2, j2] avec i1 (resp i2) la ligne haute
		// (resp basse)
		// Et j1 (resp j2) la colone gauche (resp droite). i1 = j1 = i2 = j2 = 0 s'il
		// n'y a pas de cadre

		Raster tramePixel = this.getRaster();
		boolean cadreTrouve = false;
		int i = 0;
		while (!cadreTrouve && i < this.getHeight() / 2) {
			int j = 0;
			Object objCouleur = tramePixel.getDataElements(0, i, null);
			while (j < this.getWidth() / 2) {
				int tailleH = 1;
				int tailleV = 1;
				int tolerance = 0;
				while (tolerance<10) {
					if (memeCouleurNoire(objCouleur, tramePixel.getDataElements(j + tailleH, i, null))) {
						tolerance = 0;
					}else {
						tolerance ++;
					}
					tailleH += 1;
				}
				tailleH -= tolerance;
				if (tailleH >= this.getWidth() / 2) {
					cadreCarteVAC[0] = i;
					cadreCarteVAC[1] = j;
					cadreCarteVAC[3] = j + tailleH;
					tolerance = 0;
					while (tolerance < 10 ) {
						if (memeCouleurNoire(objCouleur,tramePixel.getDataElements(j + tailleH - 1, i + tailleV, null))) {
							tolerance = 0;
						}else {
							tolerance ++;
						}
						tailleV += 1;
					}
					tailleV -= tolerance;
					if (tailleV >= this.getHeight() / 2) {
						cadreCarteVAC[2] = i + tailleV;
						cadreTrouve = true;
						contientCarteVAC = true;
					}

				}
				j += tailleH;
				objCouleur = tramePixel.getDataElements(j, i, null);
			}
			i++;
		}
		return cadreCarteVAC;
	}

	public CarteVAC extraireCarteVAC() throws Exception {
		// Retourne la carteVAC contenue dans la pagePDF
		if (!contientCarteVAC) {
			throw new Exception("Cette page ne contient pas de carte VAC !");
		}
		CarteVAC carteVAC = new CarteVAC(cadreCarteVAC[3] - cadreCarteVAC[1], cadreCarteVAC[2] - cadreCarteVAC[0],
				CarteVAC.TYPE_INT_ARGB);
		WritableRaster trameCarteVAC = carteVAC.getRaster();
		Raster tramePagePDF = this.getRaster();
		int i, j;
		for (i = 0; i < carteVAC.getHeight(); i++) {
			for (j = 0; j < carteVAC.getWidth(); j++) {
				trameCarteVAC.setDataElements(j, i,
						tramePagePDF.getDataElements(j + cadreCarteVAC[1], i + cadreCarteVAC[0], null));
			}
		}
		return carteVAC;
	}

	public void saveAsPNG(String filename) {
		File fichierImage = new File(filename);
		String format = "PNG";
		try {
			ImageIO.write(this, format, fichierImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		PagePDF page = new PagePDF(ImageIO.read(new File("testLFBO/test2.png")));
		CarteVAC carteVAC = page.extraireCarteVAC();
		carteVAC.saveAsPNG("testLFBO/test2CarteVAC.png");
	}
}
