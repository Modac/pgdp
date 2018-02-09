package Blatt10.Aufg10p6;
public class Seeweg {
  private int distance;
  private Eisscholle from;
  private Eisscholle to;

  public Seeweg(int distance, Eisscholle from, Eisscholle to) {
    this.distance = distance;
    this.from = from;
    this.to = to;
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public Eisscholle getFrom() {
    return from;
  }

  public void setFrom(Eisscholle from) {
    this.from = from;
  }

  public Eisscholle getTo() {
    return to;
  }

  public void setTo(Eisscholle to) {
    this.to = to;
  }
}
