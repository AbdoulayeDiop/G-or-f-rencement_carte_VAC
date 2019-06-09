package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class EchellePictLong extends BufferedImage {
	private String nomVAC;
	private int position;
	private NombrePict degres, minute;

	public EchellePictLong(BufferedImage img) {
		super(img.getWidth(null), img.getHeight(null), PagePDF.TYPE_INT_ARGB);
		// Dessiner l'image dans le buffured image
		Graphics2D bGr = this.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
	}

	public EchellePictLong(int width, int height, int imageType) {
		super(width, height, imageType);
		// TODO Auto-generated constructor stub
	}

	public EchellePictLong(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
		// TODO Auto-generated constructor stub
	}

	public EchellePictLong(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
			Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
		// TODO Auto-generated constructor stub
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

	public void setPosition(int position) {
		this.position = position;
	}

	public void setNomVAC(String nomVAC) {
		this.nomVAC = nomVAC;
	}

	public NombrePict resize(Raster r, int top, int left, boolean type) {
		int height = 23, width = 29, dt = 3, dl = 5;// dl et dt evitent de couper le nombre à l'endroit où le
													// leftNumber
		// et topNumber ont été détectés
		if (type) {// le resize sur les degrés est plus long : 3 chiffres > 2 chiffres
			width += 14;
		}
		NombrePict res = new NombrePict(width, height, TYPE_INT_ARGB);
		WritableRaster trameRes = res.getRaster();
		if (left - dl < 0 || left > 25) {
			left = 0;
			dl = 0;
		}
		if (top - dt < 0) {
			top = 0;
			dt = 0;
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				trameRes.setDataElements(j, i, r.getDataElements(j + left - dl, i + top - dt, null));
			}
		}
		return res;
	}

	public void divideInNombrePic() {
		int pixelARetire = 1;
		degres = new NombrePict(getWidth() / 2 - pixelARetire, getHeight(), TYPE_INT_ARGB);
		minute = new NombrePict(getWidth() / 2 - pixelARetire, getHeight(), TYPE_INT_ARGB);
		Raster trameEchelle = this.getRaster();
		WritableRaster trameDeg = degres.getRaster();
		WritableRaster trameMin = minute.getRaster();
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (j < getWidth() / 2 - pixelARetire) {
					trameDeg.setDataElements(j, i, trameEchelle.getDataElements(j, i, null));
				} else if (j > (getWidth() / 2 + pixelARetire)) {
					trameMin.setDataElements(j - (getWidth() / 2 + pixelARetire), i,
							trameEchelle.getDataElements(j, i, null));
				}
			}
		}
		int topDeg = degres.detectTopNumber(1.0), leftDeg = degres.detectLeftNumber(1.0);
		int topMin = minute.detectTopNumber(1.0), leftMin = minute.detectLeftNumber(1.0);
		if (topDeg > 3 && leftDeg > 3) {
			degres.setpExtraction(0.7);
		} else {
			degres.setpExtraction(0.3);
			topDeg = degres.detectTopNumber(0.3);
			leftDeg = degres.detectLeftNumber(0.3);
		}
		if (topMin > 3 && leftMin > 3) {
			minute.setpExtraction(0.7);
		} else {
			minute.setpExtraction(0.3);
			topMin = minute.detectTopNumber(0.3);
			leftMin = minute.detectLeftNumber(0.3);
		}
		degres = resize(trameDeg, topDeg, leftDeg, true);// resize 3 chiffres
		minute = resize(trameMin, topMin, leftMin, false);// resize 2 chiffres
		degres.setNomVAC(nomVAC);
	}

	public NombrePict getDegres() {
		return degres;
	}

	public void setDegres(NombrePict degres) {
		this.degres = degres;
	}

	public NombrePict getMinute() {
		return minute;
	}

	public void setMinute(NombrePict minute) {
		this.minute = minute;
	}

	public double getPosition() {
		return position;
	}

	public static void main(String[] args) throws IOException {
		String nomVAC = "LFCP";
		int position = 249;
		System.out.println("test" + nomVAC + "/echelle/long391.png");
		EchellePictLong ech = new EchellePictLong(
				ImageIO.read(new File("test" + nomVAC + "/echelle/long" + position + ".png")));
		ech.setNomVAC(nomVAC);
		ech.setPosition(position);
		ech.divideInNombrePic();
	}
}