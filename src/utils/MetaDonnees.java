package utils;

import geometrie.Point;

public class MetaDonnees {
	private Point coordHautGauche;
	private Point coordBasDroite;
	private String type;
	private String idOACI;
	private double echelleVerticale;
	private double echelleHorizontale;

	public MetaDonnees() {
		coordHautGauche = new Point(100, 100);
		coordBasDroite = new Point(100, 100);
		type = "indefini";
		idOACI = "indefini";
		echelleHorizontale = 0;
		echelleVerticale = 0;
	}

	public Point getCoordHautGauche() {
		return coordHautGauche;
	}

	public void setCoordHautGauche(Point coordHautGauche) {
		this.coordHautGauche = coordHautGauche;
	}

	public Point getCoordBasDroite() {
		return coordBasDroite;
	}

	public void setCoordBasDroite(Point coordBasDroite) {
		this.coordBasDroite = coordBasDroite;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdOACI() {
		return idOACI;
	}

	public void setIdOACI(String idOACI) {
		this.idOACI = idOACI;
	}

	public double getEchelleVerticale() {
		return echelleVerticale;
	}

	public void setEchelleVerticale(double echelleVerticale) {
		this.echelleVerticale = echelleVerticale;
	}

	public double getEchelleHorizontale() {
		return echelleHorizontale;
	}

	public void setEchelleHorizontale(double echelleHorizontale) {
		this.echelleHorizontale = echelleHorizontale;
	}

}
