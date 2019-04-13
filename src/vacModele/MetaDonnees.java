package vacModele;

import geometrie.Point;

public class MetaDonnees {
	private Point coordHautGauche;
	private Point coordBasDroite;
	private String type;
	private String idOACI;
	double echelle;

	public MetaDonnees(CarteVAC carteVAC, Point pixel1, Point coord1, Point pixel2, Point coord2, String type, String idOACI) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.idOACI = idOACI;
		this.echelle = (coord2.getX() - coord1.getX())/(pixel2.getX() - pixel1.getX());
		double x, y;
		x = coord1.getX() - pixel1.getX()*echelle;
		y = coord1.getY() - pixel1.getY()*echelle;
		coordHautGauche = new Point(x, y);
		x = coord1.getX() + (carteVAC.getWidth() - pixel1.getX())*echelle;
		y = coord1.getY() - (carteVAC.getHeight() - pixel1.getY())*echelle;
		coordBasDroite = new Point(x, y);
	}

	public Point getCoordHautGauche() {
		return coordHautGauche;
	}

	public Point getCoordBasDroite() {
		return coordBasDroite;
	}

	public String getType() {
		return type;
	}

	public String getIdOACI() {
		return idOACI;
	}

	public double getEchelle() {
		return echelle;
	}
}
