package Blatt9.Aufg9p5;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MultiObject extends Weihnachtsobjekt {

  protected List<SingleObject> parts;
  protected int breite;

  public MultiObject(int x, int y, int breite) {
    super(x, y);
    this.breite = breite;
    this.parts = new LinkedList<>();

    initSingleObjects();
  }

  public abstract void initSingleObjects();

  @Override
  public void addObjektToSpielfeld(int[][] spielfeld) {
    if (this.spielfeld != null) {
      return;
    }
    parts.forEach(singleObject -> singleObject.addObjektToSpielfeld(spielfeld));
    this.spielfeld = spielfeld;
  }

  public void removeObjektFromSpielfeld() {
    if (this.spielfeld == null) {
      return;
    }
    parts.forEach(SingleObject::removeObjektFromSpielfeld);
    this.spielfeld = null;
  }


  @Override
  public boolean moveDown(boolean[][] staticObjects) {
    //System.out.println("MO: " + toString() + " | moveDown");
    if (spielfeld == null /*|| y + 1 >= staticObjects[x].length - 1*/) {
      return false;
    }
    //System.out.println();
    if (cantMoveDown(staticObjects)) {
      return false;
    }

    int[][] sf = spielfeld;
    parts.forEach(singleObject -> singleObject.moveDown(staticObjects));
    y++;
    return true;
  }

  protected boolean cantMoveDown(boolean[][] staticObjects) {
    boolean cantMove = false;
    for (int i = x; i < 2 * breite + 2 + x && !cantMove; i++) {
      cantMove |= staticObjects[i][y + 1];
    }
    return cantMove;
  }

  @Override
  public int moveLeft(boolean[][] staticObjects) {
    if (spielfeld == null || cantMoveLeft(staticObjects) || cantMoveDown(staticObjects)) {
      return 0;
    }
    int[][] sf = spielfeld;
    parts.forEach(singleObject -> singleObject.moveLeft(staticObjects));
    x--;
    if (markForDeath()) {
      removeObjektFromSpielfeld();
      return -1;
    }
    return 1;
  }

  private boolean cantMoveLeft(boolean[][] staticObjects) {
    return (x - 1 >= 0 && staticObjects[x - 1][y]);
  }

  @Override
  public int moveRight(boolean[][] staticObjects) {
    if (spielfeld == null || cantMoveRight(staticObjects) || cantMoveDown(staticObjects)) {
      return 0;
    }
    int[][] sf = spielfeld;

    List<SingleObject> partsR = new LinkedList<>(parts);
    Collections.reverse(partsR);

    partsR.forEach(singleObject -> singleObject.moveRight(staticObjects));
    x++;
    if (markForDeath()) {
      removeObjektFromSpielfeld();
      return -1;
    }
    return 1;
  }

  private boolean cantMoveRight(boolean[][] staticObjects) {
    return (2 * breite + x + 3 < staticObjects.length && staticObjects[2 * breite + x + 3][y]);
  }

  private boolean markForDeath() {
    return markedForDeath = (x < 1 || 2 * breite + x + 2 > 30-1);
  }

  @Override
  public String toString() {
    return "MultiObject{" +
        "parts=" + parts +
        ", breite=" + breite +
        '}';
  }
}
