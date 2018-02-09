package Blatt11.Aufg11p5;

/**
 * Diese Klasse implementiert einen Kartenpunkt. Ein
 * Kartenpunkt hat einen Position in Form eines Länge-
 * und Breitengrades.
 */
public class MapPoint {

  /**
   * Der Breitengrad
   */
  private double lat;

  public double getLat() {
    return lat;
  }

  /**
   * Der Längengrad
   */
  private double lon;

  public double getLon() {
    return lon;
  }

  public MapPoint(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }

  /**
   * Diese Methode berechnet den Abstand dieses Kartenpunktes
   * zu einem anderen Kartenpunkt.
   *
   * @param other der andere Kartenpunkt
   * @return der Abstand in Metern
   */
  public int distance(MapPoint other) {
    double R = 6371e3; // metres
    double lat1R = Math.toRadians(this.getLat());
    double lat2R = Math.toRadians(other.getLat());
    double dLatR = Math.toRadians(other.getLat() - this.getLat());
    double dLonR = Math.toRadians(other.getLon() - this.getLon());

    double a = Math.sin(dLatR / 2) * Math.sin(dLatR / 2) +
        Math.cos(lat1R) * Math.cos(lat2R) *
            Math.sin(dLonR / 2) * Math.sin(dLonR / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (int) (R * c);
  }

  @Override
  public String toString() {
    return "lat = " + lat + ", lon = " + lon;
  }

}
