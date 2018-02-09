package Blatt12.Aufg12p6;

import java.util.concurrent.ThreadLocalRandom;

public class Penguin implements Runnable {

  public static final int adultAge = 26;

  private boolean female;
  private int x;
  private int y;
  private int age;
  private Colony col;

  private boolean brueten;
  private boolean schluepfen;

  private boolean running;


  public Penguin(boolean female, int x, int y, int age, Colony col) {
    this.female = female;
    this.x = x;
    this.y = y;
    this.age = age;
    this.col = col;
    brueten = schluepfen = false;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void run() {
    running = true;

    while (running) {
      if(brueten){
        col.updatePeng(this, x, y);
        System.out.println(Thread.currentThread().getName() + " is breeding");
        try {
          Thread.sleep(9000);
        } catch (InterruptedException ignored) {}
        brueten = false;
        schluepfen = true;
      }
      if(!bruetenStarten()){
        move();
      }
      waitRandom();
    }

  }

  public int getFg() {
    return brueten ?
            GUI.SCHWANGUIN :
            female ?
                age >= adultAge ? GUI.FRAUIN : GUI.KLEINUININ :
                age >= adultAge ? GUI.MANNUIN : GUI.KLEINUIN;
  }

  public boolean isFemale() {
    return female;
  }

  public void waitRandom() {
    System.out.println(Thread.currentThread().getName() + " is waiting");
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(200, 500 + 1));
    } catch (InterruptedException ignored) {
    }
  }

  public void move() {
    int side = ThreadLocalRandom.current().nextInt(0, 3 + 1);

    int nX = x;
    int nY = y;

    switch (side) {
      // oben
      case 0:
        nY = y - 1;
        break;
      // rechts
      case 1:
        nX = x + 1;
        break;
      // unten
      case 2:
        nY = y + 1;
        break;
      // links
      case 3:
        nX = x - 1;
        break;
    }
    System.out.println(
        Thread.currentThread().getName() + " is trying to move to (" + nX + ", " + nY + ")"
    );

    if (col.outOfBounds(nX, nY)) {
      System.out.println(Thread.currentThread().getName() + " disappearing");
      col.remove(this, x, y);
      running = false;
      return;
    }

    synchronized (col.squareLocks[nX][nY]) {
      if (col.isFree(nX, nY)) {
        System.out.println(Thread.currentThread().getName() + " is moving to (" + nX + ", " + nY + ")");
        col.move(this, x, y, nX, nY);
        if(schluepfen) {
          col.spawn(x, y);
          schluepfen = false;
          System.out.println(Thread.currentThread().getName() + " spawned a new Penguin");
        }
        x = nX;
        y = nY;
        age++;
        System.out.println(Thread.currentThread().getName() + " got older");
      }

    }
  }

  public boolean bruetenStarten() {

    if(!female || !col.isMannuin(x-1, y) ||
        col.ocean(x-1, y) || col.ocean(x, y) || age < adultAge){
      return false;
    }

    //System.out.println(Thread.currentThread().getName() + " is maybe breeding");

    int doIt = ThreadLocalRandom.current().nextInt(0, 40);

    if(doIt > 1) {
      return false;
    }

    //System.out.println(Thread.currentThread().getName() + " is breeding");

    if(doIt == 0){
      brueten();
    } else {
      col.getPenguin(x-1, y).brueten();
    }

    return true;
  }

  public void brueten(){
    brueten = true;
  }


  public int getAge() {
    return age;
  }

  public boolean isBreeding() {
    return brueten;
  }
}
