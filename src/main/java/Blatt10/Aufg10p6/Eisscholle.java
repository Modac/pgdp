package Blatt10.Aufg10p6;
public class Eisscholle {

  public final static int UNBEKANNT = 0;
  public final static int VERMUTET = 1;
  public final static int BEKANNT = 2;


  private int distance;
  private Eisscholle vorgaenger;
  private final String name;
  private int state = UNBEKANNT;

  public Eisscholle(String name) {
    this.name = name;
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

  public Eisscholle getVorgaenger() {
    return vorgaenger;
  }

  public void setVorgaenger(Eisscholle vorgaenger) {
    this.vorgaenger = vorgaenger;
  }

  public String getName() {
    return name;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Eisscholle)){
      return false;
    }
    return name.equals(((Eisscholle) obj).name);
  }

  @Override
  public String toString() {
    return "Eisscholle: " + name +
        ", dist: " + distance +
        ", Vorgänger: " + (vorgaenger == null?"null":vorgaenger.getName()) +
        ", state: " + state;
  }
}
