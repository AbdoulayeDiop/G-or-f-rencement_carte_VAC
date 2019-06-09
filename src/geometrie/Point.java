package geometrie;

/**
 * Point géométrique en 2D
 * 
 * @author Abdoulaye DIOP
 * @version 2.0
 */
public class Point {

  /**
   * L'abscisse du Point.
   */

  private double x;
  /**
   * L'ordonnée du Point.
   */
  private double y;

  /**
   * Constructeur du Point 2D.
   * 
   * @param x Initialise l'abscisse du Point à x
   * @param y Initialise l'ordonnée du Point à y
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }


  /**
   * Retourne l'abscisse du Point.
   * 
   * @return l'abscisse du Point
   */
  public double getX() {
    return x;
  }

  /**
   * Set l'abscisse du Point à x.
   * 
   * @param x the x to set
   */
  public void setX(double x) {
    this.x = x;
  }

  /**
   * Retourne l'ordonnée du Point.
   * 
   * @return l'ordonnée du Point
   */
  public double getY() {
    return y;
  }

  /**
   * Set l'ordonnée du Point à y.
   * 
   * @param y the y to set
   */
  public void setY(double y) {
    this.y = y;
  }

  /**
   * Renvoie une représentation du point (x y).
   * 
   * @return retourne {@link String} sous la forme "{@link #x} {@link #y}"
   */
  @Override
  public String toString() {
    return (x + " " + y);
  }
}
