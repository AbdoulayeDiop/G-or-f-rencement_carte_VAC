package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class PagePDF extends BufferedImage {
	private boolean contientCarteVAC;
	private ArrayList<int[]> cadresCarteVAC;

	public PagePDF(Image img) {
		super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);

		// Dessiner l'image dans le buffured image
		Graphics2D bGr = this.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Chercher le cadre de la Carte VAC
		cadresCarteVAC = new ArrayList<int[]>();
		contientCarteVAC = false;
		trouverCadre(0);
	}

	public PagePDF(BufferedImage img) {
		super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);

		// Dessiner l'image dans le buffured image
		Graphics2D bGr = this.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Chercher le cadre de la Carte VAC
		cadresCarteVAC = new ArrayList<int[]>();
		contientCarteVAC = false;
		trouverCadre(0);
	}

	public PagePDF(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
		cadresCarteVAC = new ArrayList<int[]>();
		contientCarteVAC = false;
		trouverCadre(0);
	}

	public PagePDF(int arg0, int arg1, int arg2, IndexColorModel arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
		cadresCarteVAC = new ArrayList<int[]>();
		contientCarteVAC = false;
		trouverCadre(0);
	}

	public PagePDF(ColorModel arg0, WritableRaster arg1, boolean arg2, Hashtable<?, ?> arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
		cadresCarteVAC = new ArrayList<int[]>();
		contientCarteVAC = false;
		trouverCadre(0);
	}

	private boolean memeCouleurNoire(Object o1, Object o2) {
		// Indique si o1 et o2 ont la m�me couleur noire
		ColorModel modeleCouleur = this.getColorModel();
		boolean cond1 = modeleCouleur.getRed(o1) == modeleCouleur.getRed(o2) && modeleCouleur.getRed(o2) <= 50;
		boolean cond2 = modeleCouleur.getBlue(o1) == modeleCouleur.getBlue(o2) && modeleCouleur.getBlue(o2) <= 50;
		boolean cond3 = modeleCouleur.getGreen(o1) == modeleCouleur.getGreen(o2) && modeleCouleur.getGreen(o2) <= 50;
		return cond1 && cond2 && cond3;
	}

	private boolean goodEpaisseur(int i, int j, int direction) {
		Raster tramePixel = this.getRaster();
		Object objCouleur = tramePixel.getDataElements(j, i, null);
		int e1 = 0, e2 = 0;
		if (direction == 0) {
			while (e1 + 1 < (this.getWidth() - j)
					&& memeCouleurNoire(objCouleur, tramePixel.getDataElements(j + e1 + 1, i, null))) {
				e1 += 1;
				if (e1 + e2 + 1 > 4) {
					return false;
				}
			}
			;
			while (e2 < j && memeCouleurNoire(objCouleur, tramePixel.getDataElements(j - e2 - 1, i, null))) {
				e2 += 1;
				if (e1 + e2 + 1 > 4) {
					return false;
				}
			}
			;
		} else {
			while (e1 + 1 < (this.getHeight() - i)
					&& memeCouleurNoire(objCouleur, tramePixel.getDataElements(j, i + e1 + 1, null))) {
				e1 += 1;
				if (e1 + e2 + 1 > 4) {
					return false;
				}
			}
			;
			while (e2 < j && memeCouleurNoire(objCouleur, tramePixel.getDataElements(j, i - e2 - 1, null))) {
				e2 += 1;
				if (e1 + e2 + 1 > 4) {
					return false;
				}
			}
			;
		}
		if (e1 + e2 + 1 >= 3) {
			return true;
		} else {
			return false;
		}
	}

	public int[] trouverLigne(int k) {
		Raster tramePixel = this.getRaster();
		boolean ligneTrouvee = false;
		int[] ligne = new int[3];
		int i = k;
		while (i < this.getHeight() && !ligneTrouvee) {
			int j = 0;
			while (j < this.getWidth()) {
				Object objCouleur = tramePixel.getDataElements(j, i, null);
				int tailleH = 1;
				int tolerance = 0;
				while (tolerance < 10 && j + tailleH < getWidth()) {
					if (memeCouleurNoire(objCouleur, tramePixel.getDataElements(j + tailleH, i, null))
							&& goodEpaisseur(i, j + tailleH, 1)) {
						tolerance = 0;
					} else {
						tolerance++;
					}
					tailleH += 1;
				}
				tailleH -= tolerance;
				if (tailleH >= this.getWidth() / 2) {
					ligne[0] = i;
					ligne[1] = j;
					ligne[2] = j + tailleH + 3;
					ligneTrouvee = true;

				}
				j += tailleH;
			}
			;
			i++;
		}
		return ligne;
	}

	private void trouverCadre(int k) {
		if (k >= getHeight()) {

		} else {
			int[] ligne1 = trouverLigne(k);
			if (ligne1[0] != 0) {
				int[] ligne2 = trouverLigne(ligne1[0] + 4);
				if (ligne2[0] != 0) {
					contientCarteVAC = true;
					int[] cadre = new int[4];
					cadre[0] = ligne1[0];
					cadre[1] = Math.min(ligne1[1], ligne2[1]);
					cadre[2] = ligne2[0] + 3;
					cadre[3] = Math.max(ligne1[2], ligne2[2]);
					cadresCarteVAC.add(cadre);
					trouverCadre(ligne2[0] + 4);
				}
			}

		}
	}

	public ArrayList<CarteVAC> extraireCarteVAC() throws Exception {
		// Retourne la carteVAC contenue dans la pagePDF
		if (!contientCarteVAC) {
			throw new Exception("Cette page ne contient pas de carte VAC !");
		}
		ArrayList<CarteVAC> cartesVAC = new ArrayList<CarteVAC>();
		for (int[] cadreCarteVAC : cadresCarteVAC) {
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
			cartesVAC.add(carteVAC);
		}
		return cartesVAC;
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
		String nom = "LFAD";
		PagePDF page = new PagePDF(ImageIO.read(new File("test" + nom + "/page1.png")));
		ArrayList<CarteVAC> cartesVAC = page.extraireCarteVAC();
		int i = 1;
		for (CarteVAC carteVAC : cartesVAC) {
			carteVAC.saveAsPNG("test" + nom + "/page1CarteVAC" + i + ".png");
			i++;
		}
	}

	public boolean isContientCarteVAC() {
		return contientCarteVAC;
	}

}
