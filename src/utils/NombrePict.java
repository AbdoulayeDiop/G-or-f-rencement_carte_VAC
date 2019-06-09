package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NombrePict extends BufferedImage {
	/**
	 * Le nom / code OACI de la carte VAC
	 */
	private String nomVAC;
	/**
	 * La probabilité que le nombre ai été bien extrait
	 */
	private double pExtraction = 0; // probabilitÃ© estimÃ© de la bonne extraction

	public NombrePict(int width, int height, int imageType) {
		super(width, height, imageType);
		// TODO Auto-generated constructor stub
	}

	public NombrePict(BufferedImage img) {
		super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);

		// Dessiner l'image dans le buffured image
		Graphics2D bGr = this.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
	}
	/**
	 * @return code OACI de la carte VAC à laquelle appartient l'objet
	 */
	public String getNomVAC() {
		return nomVAC;
	}
	/**
	 * @param nomVAC code OACI de la carte VAC
	 */
	public void setNomVAC(String nomVAC) {
		this.nomVAC = nomVAC;
	}
	/**
	 * @return la probabilité que l'image du nombre a bien été extraite
	 */
	public double getpExtraction() {
		return pExtraction;
	}
	/**
	 * @param d la probabilité que l'image du nombre a bien été extraite
	 */
	public void setpExtraction(double d) {
		this.pExtraction = d;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	   * <p>
	   * Verification de la couleur d'un object en paramètre, selon la valeur de color
	   * </p>
	   * 
	   * @param o objet à verifier
	   * @param color représente la couleur RGB(0,102,181) si color = 1 et Blanc sinon
	   * @return vrai (faux sinon) si la couleur correspond
	   */
	private boolean testColor(Object o, int color) {
		ColorModel modeleCouleur = this.getColorModel();
		// couleur recherchÃ©e : 0,102,181 du bord bleu

		Boolean redCheck, greenCheck, blueCheck;
		if (color == 1) {
			redCheck = modeleCouleur.getRed(o) == 0;
			greenCheck = modeleCouleur.getGreen(o) == 102;
			blueCheck = modeleCouleur.getBlue(o) == 181;
		} else {
			redCheck = modeleCouleur.getRed(o) == 255;
			greenCheck = modeleCouleur.getGreen(o) == 255;
			blueCheck = modeleCouleur.getBlue(o) == 255;
		}
		return redCheck && greenCheck && blueCheck;
	}
	/**
	   * <p>
	   * Detecte le debut du nombre en partant du haut
	   * </p>
	   * 
	   * @param p probabilité de bonne extraction, definit la façon d'extraction du nombre, en utilisant les contour blanc du nombre ou non
	   * @return position en pixel du début détecté, 100 si non detecté
	   */
	public int detectTopNumber(double p) {
		Raster trameNombre = this.getRaster();
		int width = (2 * this.getWidth()) / 3, height = this.getHeight();// on evite le symbole degres
		int[] listDetected = new int[width];
		int indexMin = 0;
		boolean precedentBleu = false, detected = false, whiteDetected = false;
		for (int j = 0; j < width; j++) {
			int i = 0;
			whiteDetected = true;
			if (p < 0.5) {
				whiteDetected = false;
			}
			while (!detected && i < height) {
				Object objCouleur = trameNombre.getDataElements(j, i, null);
				if (whiteDetected && precedentBleu && testColor(objCouleur, 1)) {// prec = blanc & courant = bleu
					detected = true;
					listDetected[j] = i;// on a detectÃ© un dÃ©but de chiffre
				}

				if (testColor(objCouleur, 1)) {// si le pixel courant est bleu
					precedentBleu = true;
				} else {
					precedentBleu = false;
				}

				if (testColor(objCouleur, 0)) {
					whiteDetected = true;
				}

				i++;
			}
			if (!detected) {
				listDetected[j] = 100;// on met un grand nombre pour pas le choisir
			}
			detected = false;// on remet a jour pour la prochaine trame de pixel
		}
		for (int i = 0; i < listDetected.length; i++) {
			if (listDetected[i] < listDetected[indexMin]) {
				indexMin = i;
			}
		}

		return listDetected[indexMin];
	}
	/**
	   * <p>
	   * Detecte le debut du nombre en partant de la gauche
	   * </p>
	   * 
	   * @param p probabilité de bonne extraction, definit la façon d'extraction du nombre, en utilisant les contour blanc du nombre ou non
	   * @return position en pixel du début détecté, 100 si non detecté
	   */
	public int detectLeftNumber(double p) {
		Raster trameNombre = this.getRaster();
		int width = this.getWidth();// on evite le symbole degres
		int height = this.getHeight();
		int[] listDetected = new int[height];
		int indexMin = 0;
		boolean precedentBleu = false, whiteDetected = false, detected = false;

		for (int i = 0; i < height; i++) {
			int j = 0;
			whiteDetected = true;
			if (p < 0.5) {
				whiteDetected = false;
			}
			while (!detected && j < width) {
				Object objCouleur = trameNombre.getDataElements(j, i, null);

				if (whiteDetected && precedentBleu && testColor(objCouleur, 1)) {// prec = bleu & courant = bleu
					detected = true;
					listDetected[i] = j;// on a detectÃ© un dÃ©but de chiffre
				}

				if (testColor(objCouleur, 1)) {// si le pixel courant est bleu
					precedentBleu = true;
				} else {
					precedentBleu = false;
				}
				j++;

				if (testColor(objCouleur, 0)) {
					whiteDetected = true;
				}

			}
			if (!detected) {
				listDetected[i] = 100;// on met un grand nombre pour pas le choisir
			}
			detected = false;// on remet a jour pour la prochaine trame de pixel

		}
		for (int i = 0; i < listDetected.length; i++) {
			if (listDetected[i] < listDetected[indexMin]) {
				indexMin = i;
			}
		}

		return listDetected[indexMin];
	}

}
