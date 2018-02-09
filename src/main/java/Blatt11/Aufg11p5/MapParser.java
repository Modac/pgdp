package Blatt11.Aufg11p5;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Diese Klasse erlaubt es, aus einer Datei im OSM-Format ein MapGraph-Objekt zu erzeugen. Sie nutzt
 * dazu einen XML-Parser.
 */
public class MapParser {

  public static MapGraph parseFile(String fileName) {
    try {
      File inputFile = new File(fileName);
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      MapGraph mapGraph = new MapGraph();

      WayHandler wayHandler = new WayHandler(mapGraph);
      saxParser.parse(inputFile, wayHandler);

      NodeHandler nodeHandler = new NodeHandler(mapGraph);
      saxParser.parse(inputFile, nodeHandler);

      System.out.println("Nodes: " + mapGraph.getNodes().size());

      mapGraph.filterEmptyEdges();

      int edgeCount = 0;

      for (Entry<Long, Set<MapEdge>> mapEdgeEntry : mapGraph.getEdges().entrySet()) {
        edgeCount += mapEdgeEntry.getValue().size();
      }

      System.out.println("Edges: " + edgeCount);

      return mapGraph;

    } catch (ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static Set<String> invalidHighways = new HashSet<>(Arrays.asList(
      "proposed",
      "construction"
  ));

  private static class WayHandler extends DefaultHandler {

    private MapGraph mapGraph;

    private long wayId;
    private LinkedList<Long> nodes;

    private boolean isHighway;
    private boolean isInvalidHighway;
    private boolean isOneway;
    private String name;


    public WayHandler(MapGraph mapGraph) {
      this.mapGraph = mapGraph;
      wayId = -1;
      nodes = new LinkedList<>();
      isHighway = false;
      isInvalidHighway = false;
      isOneway = false;
      name = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      if (qName.equalsIgnoreCase("way")) {
        wayId = Long.parseLong(attributes.getValue("id"));
      } else if (qName.equalsIgnoreCase("nd") && wayId != -1) {
        nodes.add(Long.valueOf(attributes.getValue("ref")));
      } else if (wayId != -1 && qName.equalsIgnoreCase("tag")) {
        if (attributes.getValue("k").equalsIgnoreCase("highway")) {
          isHighway = true;
          if (invalidHighways.contains(attributes.getValue("v"))) {
            isInvalidHighway = true;
          }
        } else if (attributes.getValue("k").equalsIgnoreCase("oneway") &&
            attributes.getValue("v").equalsIgnoreCase("yes")) {
          isOneway = true;
        } else if (attributes.getValue("k").equalsIgnoreCase("name")) {
          name = attributes.getValue("v");
        }

      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (qName.equalsIgnoreCase("way")) {
        if (isHighway && !isInvalidHighway) {
          OSMWay osmWay = new OSMWay(wayId, nodes.toArray(new Long[0]), isOneway, name);
          for (int i = 0; i < nodes.size(); i++) {
            if (!mapGraph.getEdges().containsKey(nodes.get(i))) {
              mapGraph.getEdges().put(nodes.get(i), new HashSet<>());
            }

            Set<MapEdge> mapEdges = mapGraph.getEdges().get(nodes.get(i));

            if (i < nodes.size() - 1) {
              mapEdges.add(new MapEdge(nodes.get(i + 1), osmWay));
            }

            if (!isOneway && i > 0) {
              mapEdges.add(new MapEdge(nodes.get(i - 1), osmWay));
            }
          }
        }

        wayId = -1;
        nodes.clear();
        isHighway = false;
        isInvalidHighway = false;
        isOneway = false;
        name = null;
      }
    }
  }

  private static class NodeHandler extends DefaultHandler {

    private MapGraph mapGraph;

    public NodeHandler(MapGraph mapGraph) {
      this.mapGraph = mapGraph;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      if (qName.equalsIgnoreCase("node")) {
        long id = Long.parseLong(attributes.getValue("id"));

        if (mapGraph.getEdges().containsKey(id)) {
          double lat = Double.parseDouble(attributes.getValue("lat"));
          double lon = Double.parseDouble(attributes.getValue("lon"));
          mapGraph.getNodes().put(id, new OSMNode(id, lat, lon));
        }
      }
    }
  }
}
