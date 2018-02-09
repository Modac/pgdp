package Blatt11.Aufg11p5;

import org.jetbrains.annotations.NotNull;

public class DijkstraNode implements Comparable<DijkstraNode> {
  public final static int UNBEKANNT = 0;
  public final static int VERMUTET = 1;
  public final static int BEKANNT = 2;


  private int distance;
  private DijkstraNode vorgaenger;
  private final OSMNode osmNode;
  private int state = UNBEKANNT;

  public DijkstraNode(OSMNode osmNode) {
    this.osmNode = osmNode;
    reset();
  }

  public void reset(){
    distance = Integer.MAX_VALUE;
    vorgaenger = null;
    state = UNBEKANNT;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance>=0?distance:Integer.MAX_VALUE;
  }

  public DijkstraNode getVorgaenger() {
    return vorgaenger;
  }

  public void setVorgaenger(DijkstraNode vorgaenger) {
    this.vorgaenger = vorgaenger;
  }

  public OSMNode getOsmNode() {
    return osmNode;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }


  @Override
  public int compareTo(@NotNull DijkstraNode o) {
    return this.distance - o.getDistance();
  }
}
