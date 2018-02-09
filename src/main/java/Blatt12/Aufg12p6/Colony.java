package Blatt12.Aufg12p6;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class Colony extends GUI {

  private final int[][] landscape;
  private final Penguin[][] placed;

  private HashMap<Penguin, Boolean> updated;

  public final Object[][] squareLocks;
  public final Object drawLock = new Object();


  public Colony(int width, int height, boolean standard) {
    placed = new Penguin[width][height];
    landscape = new int[placed.length][placed[0].length];

    updated = new HashMap<>();

    squareLocks = new Object[placed.length][placed[0].length]; // all still null

    for (Object[] objA : squareLocks) {
      Arrays.fill(objA, new Object());
    }

    generateAntarctic(landscape, placed, standard);

    for (Penguin[] pA : placed) {
      for (Penguin p : pA) {
        if(p == null) {
          continue;
        }
        updated.put(p, false);
        new Thread(p).start();
      }
    }

    draw();
  }

  private void draw(){
    synchronized (drawLock) {
      update();
      draw(landscape);
    }
  }

  private void update(){
    for (Entry<Penguin, Boolean> e : updated.entrySet()) {
      e.setValue(true);
    }
  }

  public void move(Penguin peng, int x, int y, int xNew, int yNew) {
    if(!updated.get(peng)){
      draw();
    }

    GUI.setForeground(landscape, x, y, GUI.NIXUIN);
    GUI.setForeground(landscape, xNew, yNew, peng.getFg());

    placed[x][y] = null;
    placed[xNew][yNew] = peng;

    updated.put(peng, false);

  }

  public boolean outOfBounds(int nX, int nY) {
    return nX >= landscape.length || nX < 0 || nY < 0 || nY >= landscape[0].length;
  }

  public void remove(Penguin penguin, int x, int y) {
    GUI.setForeground(landscape, x, y, GUI.NIXUIN);

    placed[x][y] = null;

    draw();
  }

  public boolean isFree(int nX, int nY) {
    return placed[nX][nY] == null;
  }

  public boolean isMannuin(int x, int y) {
    return !outOfBounds(x, y) &&
        placed[x][y] != null &&
        !placed[x][y].isFemale() &&
        placed[x][y].getAge()>=Penguin.adultAge &&
        !placed[x][y].isBreeding();
  }

  public Penguin getPenguin(int x, int y){
    return placed[x][y];
  }

  public boolean ocean(int x, int y) {
    return !outOfBounds(x, y) && GUI.ocean(landscape, x, y);
  }

  public void spawn(int x, int y) {
    Penguin newP = new Penguin(Math.random() >= 0.5, x, y, 0, this);
    GUI.setForeground(landscape, x, y, newP.getFg());
    placed[x][y] = newP;
    updated.put(newP, false);
    new Thread(newP).start();
  }

  public void updatePeng(Penguin penguin, int x, int y) {
    if(!updated.get(penguin)){
      draw();
    }

    GUI.setForeground(landscape, x, y, penguin.getFg());

    updated.put(penguin, false);

  }
}
