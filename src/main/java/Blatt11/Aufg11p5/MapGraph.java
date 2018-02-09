package Blatt11.Aufg11p5;

import Blatt11.heap.BinomialHeap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Diese Klasse repräsentiert den Graphen der Straßen und Wege aus
 * OpenStreetMap.
 */
public class MapGraph {

  private Map<Long, OSMNode> nodes;
  private Map<Long, Set<MapEdge>> edges;

  public MapGraph() {
    nodes = new HashMap<>();
    edges = new HashMap<>();
  }

  /**
   * Ermittelt, ob es eine Kante im Graphen zwischen zwei Knoten gibt.
   *
   * @param from der Startknoten
   * @param to der Zielknoten
   * @return 'true' falls es die Kante gibt, 'false' sonst
   */
  boolean hasEdge(OSMNode from, OSMNode to) {
    long fromId = from.getId();
    long toId = to.getId();

    if (edges.containsKey(fromId)) {
      for (MapEdge mapEdge : edges.get(fromId)) {
        if (mapEdge.getTo() == toId) {
          return true;
        }
      }

    }
    return false;
  }

  /**
   * Diese Methode findet zu einem gegebenen Kartenpunkt den
   * nähesten OpenStreetMap-Knoten. Gibt es mehrere Knoten mit
   * dem gleichen kleinsten Abstand zu, so wird derjenige Knoten
   * von ihnen zurückgegeben, der die kleinste Id hat.
   *
   * @param p der Kartenpunkt
   * @return der OpenStreetMap-Knoten
   */
  public OSMNode closest(MapPoint p) {
    OSMNode res = null;
    int smallestDist = Integer.MAX_VALUE;

    for (Entry<Long, OSMNode> entry : nodes.entrySet()) {
      MapPoint mapPointT = entry.getValue().getLocation();
      int dist = p.distance(mapPointT);
      if (dist < smallestDist ||
          (res != null && dist == smallestDist && entry.getValue().getId() < res.getId())) {
        res = entry.getValue();
        smallestDist = dist;
      }
    }

    return res;
  }

  /**
   * Diese Methode sucht zu zwei Kartenpunkten den kürzesten Weg durch
   * das Straßen/Weg-Netz von OpenStreetMap.
   *
   * @param from der Kartenpunkt, bei dem gestartet wird
   * @param to der Kartenpunkt, der das Ziel repräsentiert
   * @return eine mögliche Route zum Ziel und ihre Länge; die Länge
   * des Weges bezieht sich nur auf die Länge im Graphen, der Abstand
   * von 'from' zum Startknoten bzw. 'to' zum Endknoten wird
   * vernachlässigt.
   */
  public RoutingResult route(MapPoint from, MapPoint to) {
    OSMNode fromNode = closest(from);
    OSMNode toNode = closest(to);

    if (fromNode == null || toNode == null) {
      return null;
    }

    Map<Long, DijkstraNodeHandlePair> dnhMap = new HashMap<>();

    BinomialHeap<DijkstraNode> vermutet = new BinomialHeap<>();

    DijkstraNode start = new DijkstraNode(fromNode);
    start.setDistance(0);
    start.setState(DijkstraNode.VERMUTET);

    dnhMap.put(
            start.getOsmNode().getId(),
            new DijkstraNodeHandlePair(start, vermutet.insert(start)));

    while (vermutet.getSize() > 0) {

      DijkstraNode e = vermutet.poll();
      dnhMap.get(e.getOsmNode().getId()).setHandle(null);

      if (!edges.containsKey(e.getOsmNode().getId())) {
        continue;
      }

      Set<MapEdge> eEdges = edges.get(e.getOsmNode().getId());

      for (MapEdge mapEdge : eEdges) {
        if (!dnhMap.containsKey(mapEdge.getTo())){
          dnhMap.put(mapEdge.getTo(),
              new DijkstraNodeHandlePair(new DijkstraNode(nodes.get(mapEdge.getTo())),
                  null));
        }

        int distance = e.getOsmNode().getLocation().distance(nodes.get(mapEdge.getTo()).getLocation());

        DijkstraNode toDNode = dnhMap.get(mapEdge.getTo()).getNode();

        if (toDNode.getState() != DijkstraNode.BEKANNT) {
          if (e.getDistance() + distance < toDNode.getDistance()) {
            toDNode.setVorgaenger(e);
            toDNode.setDistance(e.getDistance() + distance);
          }
          if (toDNode.getState() == DijkstraNode.VERMUTET) {
            vermutet.replaceWithSmallerElement(
                dnhMap.get(toDNode.getOsmNode().getId()).getHandle(), toDNode);
          } else {
            dnhMap.get(toDNode.getOsmNode().getId()).setHandle(vermutet.insert(toDNode));
            // System.out.println(nachbarschollen.size());
            toDNode.setState(DijkstraNode.VERMUTET);
          }
        }
      }

      e.setState(DijkstraNode.BEKANNT);

    }

    DijkstraNode aktuell = dnhMap.get(toNode.getId()).getNode();
    ArrayList<DijkstraNode> nodes = new ArrayList<>();
    // String str = "";
    while (aktuell != null) {
      nodes.add(0, aktuell);
      // str = " -> " + aktuell.getName()+ "(" + aktuell.getDistance() + ")" + str;
      aktuell = aktuell.getVorgaenger();
    }

    OSMNode[] osmNodes = new OSMNode[nodes.size()];
    for (int i = 0; i < nodes.size(); i++) {
      osmNodes[i] = nodes.get(i).getOsmNode();
    }
    // System.out.println(str);
    return new RoutingResult(osmNodes, dnhMap.get(toNode.getId()).getNode().getDistance());


  }

  public void filterEmptyEdges() {
    edges.entrySet().removeIf(longSetEntry -> longSetEntry.getValue().isEmpty());
  }

  public Map<Long, OSMNode> getNodes() {
    return nodes;
  }

  public Map<Long, Set<MapEdge>> getEdges() {
    return edges;
  }

  private class DijkstraNodeHandlePair {

    private DijkstraNode node;
    private Object handle;

    public DijkstraNodeHandlePair(DijkstraNode node, Object handle) {
      this.node = node;
      this.handle = handle;
    }

    public DijkstraNode getNode() {
      return node;
    }

    public void setNode(DijkstraNode node) {
      this.node = node;
    }

    public Object getHandle() {
      return handle;
    }

    public void setHandle(Object handle) {
      this.handle = handle;
    }
  }
}
