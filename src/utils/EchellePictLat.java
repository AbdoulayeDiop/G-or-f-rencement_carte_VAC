package utils;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class EchellePictLat extends BufferedImage {
	private String nomVAC;
	private double position;
	private NombrePict degres, minute, second;

	public EchellePictLat(int width, int height, int imageType) {
		super(width, height, imageType);
		// TODO Auto-generated constructor stub
	}

	public EchellePictLat(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
		// TODO Auto-generated constructor stub
	}

	public EchellePictLat(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied,
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

	public NombrePict resize(Raster r, int top, int left) {
		int height = 23, width = 27, dt = 3, dl = 3;
		NombrePict res = new NombrePict(width, height, TYPE_INT_ARGB);
		WritableRaster trameRes = res.getRaster();
		if (left - dl < 0 || left > 25) {
			left = 0;
			dl = 0;
		}
		if (top - dt < 0 || top > 20) {
			top = 0;
			dt = 0;
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// System.out.println(String.valueOf(j + left - dl) + "," + String.valueOf(i +
				// top - dt));
				if (j + left - dl < r.getWidth() && i + top - dt < r.getHeight()) {
					trameRes.setDataElements(j, i, r.getDataElements(j + left - dl, i + top - dt, null));
				}
			}
		}
		return res;
	}

	public void divideInNombrePic() {
		int pixelARetire = 1;
		degres = new NombrePict(getWidth(), getHeight() / 2 - pixelARetire, TYPE_INT_ARGB);
		minute = new NombrePict(getWidth(), getHeight() / 2 - pixelARetire, TYPE_INT_ARGB);
		Raster trameEchelle = this.getRaster();
		WritableRaster trameDeg = degres.getRaster();
		WritableRaster trameMin = minute.getRaster();
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (i < getHeight() / 2 - pixelARetire) {
					trameDeg.setDataElements(j, i, trameEchelle.getDataElements(j, i, null));
				} else if (i > getHeight() / 2 + pixelARetire) {
					trameMin.setDataElements(j, i - (getHeight() / 2 + pixelARetire),
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
		degres = resize(trameDeg, topDeg, leftDeg);
		minute = resize(trameMin, topMin, leftMin);
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

	public NombrePict getSecond() {
		return second;
	}

	public void setSecond(NombrePict second) {
		this.second = second;
	}
}