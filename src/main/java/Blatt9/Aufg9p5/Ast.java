package Blatt9.Aufg9p5;

public class Ast extends MultiObject{

  public Ast(int x, int y, int breite) {
    super(x, y, breite);
  }

  @Override
  public void initSingleObjects() {
    parts.add(new AstLinks(x, y));
    for (int i = x+1; i < 2 * breite + 1 + x; i++) {
      parts.add(new AstMitte(i, y));
    }
    parts.add(new AstRechts(2*breite+1+x, y));
  }
}
