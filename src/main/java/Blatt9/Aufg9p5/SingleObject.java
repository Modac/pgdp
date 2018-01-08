package Blatt9.Aufg9p5;

public class SingleObject extends Weihnachtsobjekt {

  protected int background;
  protected int foreground;


  public SingleObject(int x, int y, int background, int foreground) {
    super(x, y);
    this.background = background;
    this.foreground = foreground;
  }

  @Override
  public String toString() {
    return "" + this.getClass().getName() + "{" +
        "x=" + x +
        ", y=" + y +
        ", background=" + background +
        ", foreground=" + foreground +
        ", markedForDeath=" + markedForDeath +
        "} ";
  }

  @Override
  public void addObjektToSpielfeld(int[][] spielfeld) {
    if (this.spielfeld != null) {
      return;
    }
    // Wenn Hinter- oder Vordergrund negativ, dann nicht überschreiben
    spielfeld[x][y] =
        (background < 0 ? spielfeld[x][y] & (0xFF << 8) : background) + (foreground < 0 ?
            spielfeld[x][y] & 0xFF : foreground);
    this.spielfeld = spielfeld;
  }

  public void removeObjektFromSpielfeld() {
    if (this.spielfeld == null) {
      return;
    }
    // Wenn Hinter- oder Vordergrund negativ, dann nicht überschreiben
    this.spielfeld[x][y] =
        (background < 0 ? spielfeld[x][y] & (0xFF << 8) : WeihnachtsElfen.BACKGROUND_EMPTY) +
            (foreground < 0 ? spielfeld[x][y] & 0xFF : WeihnachtsElfen.FOREGROUND_EMPTY);
    this.spielfeld = null;
  }

  @Override
  public boolean moveDown(boolean[][] staticObjects) {
    //System.out.println("SO: " + toString() + " | moveDown");
    if (spielfeld == null /*|| y+1>=staticObjects[x].length-1*/ || staticObjects[x][y + 1]) {
      return false;
    }
    int[][] sf = spielfeld;
    removeObjektFromSpielfeld();
    y++;
    addObjektToSpielfeld(sf);
    return true;
  }

  @Override
  public int moveLeft(boolean[][] staticObjects) {
    if (spielfeld == null || (x - 1 >= 0 && staticObjects[x - 1][y]) || staticObjects[x][y + 1]
        || isAstHereAndNoAstBeneath()) {
      return 0;
    }
    int[][] sf = spielfeld;
    removeObjektFromSpielfeld();
    x--;
    if (markForDeath()) {
      return -1;
    }
    addObjektToSpielfeld(sf);
    return 1;
  }

  @Override
  public int moveRight(boolean[][] staticObjects) {
    if (spielfeld == null || (x + 1 < staticObjects.length && staticObjects[x + 1][y])
        || staticObjects[x][y + 1] || isAstHereAndNoAstBeneath()) {
      return 0;
    }
    int[][] sf = spielfeld;
    removeObjektFromSpielfeld();
    x++;
    if (markForDeath()) {
      return -1;
    }
    addObjektToSpielfeld(sf);
    return 1;
  }

  private boolean markForDeath() {
    return markedForDeath = (x < 1 || x >= 30 - 1);
  }

  public boolean isAstHereAndNoAstBeneath() {
    return isAst(x, y) && !isAst(x, y + 1);
  }

  public boolean isAst(int x, int y) {
    int background = spielfeld[x][y] & (0xFF << 8);
    return background == WeihnachtsElfen.BACKGROUND_GREEN_LEFT ||
        background == WeihnachtsElfen.BACKGROUND_GREEN_RIGHT ||
        background == WeihnachtsElfen.BACKGROUND_GREEN_MIDDLE;
  }
}
