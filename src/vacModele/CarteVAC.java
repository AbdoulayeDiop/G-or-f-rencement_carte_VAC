package vacModele;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class CarteVAC extends BufferedImage {
	private MetaDonnees metaDonnees;

	public CarteVAC(int width, int height, int imageType) {
		super(width, height, imageType);
		// TODO Auto-generated constructor stub
	}

	public CarteVAC(int width, int height, int imageType, IndexColorModel cm) {
		super(width, height, imageType, cm);
		// TODO Auto-generated constructor stub
	}

	public CarteVAC(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
		super(cm, raster, isRasterPremultiplied, properties);
		// TODO Auto-generated constructor stub
	}

	public void setMetaDonnees(MetaDonnees metaDonnees) {
		this.metaDonnees = metaDonnees;
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

	public void save(String source) {
		saveAsPNG(source + metaDonnees.getIdOACI() + metaDonnees.getType() + ".png");
		try {
			FileWriter fichierMetaDonnees = new FileWriter(source + "metaDonnees.txt", true);
			fichierMetaDonnees.write(metaDonnees.getIdOACI() + " ");
			fichierMetaDonnees.write(metaDonnees.getType() + " ");
			fichierMetaDonnees.write(metaDonnees.getEchelle() + " ");
			fichierMetaDonnees.write(metaDonnees.getCoordHautGauche().toString() + " ");
			fichierMetaDonnees.write(metaDonnees.getCoordBasDroite().toString() + "\n");
			fichierMetaDonnees.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
