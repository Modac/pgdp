package Blatt9.Aufg9p5;

public class Baumstamm extends MultiObject {

  public Baumstamm(int x, int y, int breite) {
    super(x, y, breite);
  }

  @Override
  public void initSingleObjects() {
    boolean lr = Math.random() < 0.5;

    parts.add(lr ? new StammLinks(x, y) : new StammMitte(x, y));
    for (int i = x+1; i < 2 * breite + 1 + x; i++) {
      parts.add(new StammMitte(i, y));
    }
    parts.add(lr ? new StammRechts(2 * breite + 1 + x, y) : new StammMitte(2 * breite + 1 + x, y));
  }
}
