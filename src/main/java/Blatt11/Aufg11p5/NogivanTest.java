package Blatt11.Aufg11p5;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.xml.sax.SAXException;

public class NogivanTest {
  private int lengthAndCheck(MapGraph g, OSMNode[] path) {
    int length = 0;
    for (int i = 1; i < path.length; i++) {
      assertTrue(g.hasEdge(path[i - 1], path[i]));
      length += path[i - 1].getLocation().distance(path[i].getLocation());
    }
    return length;
  }

  @Test
  public void testabcd1() throws SAXException, IOException, ParserConfigurationException {
    System.out.println("Reading OSM data...");
    MapGraph g = MapParser.parseFile("campus_garching.osm");
    System.out.println("Finished reading OSM data...");

    RoutingResult rr = g.route(new MapPoint(48.2626633, 11.6689035), new MapPoint(48.2622312, 11.6662273));
    
    assertNotNull(rr);
    
    OSMNode[] path = rr.getPath();
    assertTrue(path.length > 0);
    assertTrue(path[0].getId() == 277698459L);
    assertTrue(path[path.length - 1].getId() == 277698572L);
    int pathLength = lengthAndCheck(g, rr.getPath());
    assertTrue(pathLength == rr.getDistance());
    
    assertTrue(rr.getDistance() == 254);

    new GPXWriter("testabcd1.gpx").writeGPX(rr);
//    GPXWriter.write("/home/jucs/Desktop/testabcd1.gpx", rr);
  }
  
  @Test
  public void testabcd2() throws SAXException, IOException, ParserConfigurationException {
    System.out.println("Reading OSM data...");
    MapGraph g = MapParser.parseFile("campus_garching.osm");
    System.out.println("Finished reading OSM data...");

    RoutingResult rr = g.route(new MapPoint(48.26313, 11.67459), new MapPoint(48.26632, 11.66750));
    
    assertNotNull(rr);
    
    OSMNode[] path = rr.getPath();
    assertTrue(path.length > 0);
    assertTrue(path[0].getId() == 2496758189L);
    assertTrue(path[path.length - 1].getId() == 277698564L);
    int pathLength = lengthAndCheck(g, rr.getPath());
    assertTrue(pathLength == rr.getDistance());
    
    assertTrue(rr.getDistance() == 804);

    new GPXWriter("testabcd2.gpx").writeGPX(rr);
  }

  @Test
  public void testBigLong() throws FileNotFoundException {
    System.out.println("Reading OSM data...");
    long startMillis = System.currentTimeMillis();
    MapGraph g = MapParser.parseFile("mapBig.osm");
    System.out.println("Finished reading OSM data...");
    System.out.println("Took " + (System.currentTimeMillis() - startMillis) + "ms ");

    startMillis = System.currentTimeMillis();
    System.out.println("Calculating route...");
    RoutingResult rr = g.route(new MapPoint(48.29281, 11.2594), new MapPoint(48.26341, 11.66918));
    System.out.println("Took " + (System.currentTimeMillis() - startMillis) + "ms ");


    new GPXWriter("testBigLong.gpx").writeGPX(rr);

  }

  @Test
  public void testBigShort() throws FileNotFoundException {
    System.out.println("Reading OSM data...");
    long startMillis = System.currentTimeMillis();
    MapGraph g = MapParser.parseFile("mapBig.osm");
    System.out.println("Finished reading OSM data...");
    System.out.println("Took " + (System.currentTimeMillis() - startMillis) + "ms ");

    startMillis = System.currentTimeMillis();
    System.out.println("Calculating route...");
    RoutingResult rr = g.route(new MapPoint(48.25042, 11.43212), new MapPoint(48.25452, 11.44359));
    System.out.println("Took " + (System.currentTimeMillis() - startMillis) + "ms ");

    new GPXWriter("testBigShort.gpx").writeGPX(rr);

  }

  @Test
  public void testBigShortAngabe() throws FileNotFoundException {
    System.out.println("Reading OSM data...");
    long startMillis = System.currentTimeMillis();
    MapGraph g = MapParser.parseFile("mapBig.osm");
    System.out.println("Finished reading OSM data...");
    System.out.println("Took " + (System.currentTimeMillis() - startMillis) + "ms ");

    startMillis = System.currentTimeMillis();
    System.out.println("Calculating route...");
    RoutingResult rr = g.route(new MapPoint(48.2639, 11.66621), new MapPoint(48.269, 11.67515));
    System.out.println("Took " + (System.currentTimeMillis() - startMillis) + "ms ");


    new GPXWriter("testBigShortAngabe.gpx").writeGPX(rr);

  }

  //@Test
  public void testBayernLong() throws FileNotFoundException {
    System.out.println("Reading OSM data...");
    long startMillis = System.currentTimeMillis();
    MapGraph g = MapParser.parseFile("bayern-latest.osm");
    System.out.println("Finished reading OSM data...");
    System.out.println("Took " + msToString(System.currentTimeMillis() - startMillis) + "ms ");

    startMillis = System.currentTimeMillis();
    System.out.println("Calculating route...");
    RoutingResult rr = g.route(new MapPoint(47.4105, 10.2818), new MapPoint(50.1916, 11.7691));
    System.out.println("Took " + msToString(System.currentTimeMillis() - startMillis) + "ms ");


    new GPXWriter("testBayern.gpx").writeGPX(rr);

  }

  @Test
  public void testPiazza() throws FileNotFoundException {
    System.out.println("Reading OSM data...");
    long startMillis = System.currentTimeMillis();
    MapGraph g = MapParser.parseFile("mapPiazza.osm");
    System.out.println("Finished reading OSM data...");
    System.out.println("Took " + msToString(System.currentTimeMillis() - startMillis));

    startMillis = System.currentTimeMillis();
    System.out.println("Calculating route...");
    RoutingResult rr = g.route(new MapPoint(47.862916, 11.0275), new MapPoint(48.349388, 11.768416));
    System.out.println("Took " + msToString(System.currentTimeMillis() - startMillis));


    new GPXWriter("testPiazza.gpx").writeGPX(rr);

  }

  public static String msToString(long ms) {
    long totalSecs = ms / 1000;
    long hours = (totalSecs / 3600);
    long mins = (totalSecs / 60) % 60;
    long secs = totalSecs % 60;
    String minsString = (mins == 0) ?
        "00"
        : ((mins < 10) ?
            ("0" + mins)
            : "" + mins);
    String secsString = (secs == 0)
        ? "00"
        : ((secs < 10)
            ? "0" + secs
            : "" + secs);
    if (hours > 0) {
      return hours + ":" + minsString + ":" + secsString;
    } else if (mins > 0) {
      return mins + ":" + secsString;
    } else {
      return secsString + "s";
    }
  }

}
