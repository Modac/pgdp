package Blatt9.Aufg9p5;

import java.util.ArrayList;

/*
.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:.
    .     *                                       .
    .    /.\                                      .
    .   /..'\                                     .
    .   /'.'\                                     .
    .  /.''.'\                                    .
    .  /.'.'.\                                    .
    . /'.''.'.\                                   .
    . ^^^[_]^^^                                   .
    .                                             .
    .                                             .
    .jgs                                          .
    .:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:._.:*~*:.
*/

public class Weihnachtsbaum extends BitteNichtAbgeben {

  private static final int[][] landscape = generateLandscape(30, 33);
  private static ArrayList<Weihnachtsobjekt> objekte = new ArrayList<>();
  private static ArrayList<Weihnachtsobjekt> staemmeUndAeste = new ArrayList<>();
  private static ArrayList<Weihnachtsobjekt> foregroundObjects = new ArrayList<>();

  private static boolean[][] staticObjectsBack = new boolean[30][33];
  private static boolean[][] staticObjectsFront = new boolean[30][33];

  private static int phase = 1;

  public static Weihnachtsobjekt createRandomObjekt() {
    if (phase == 1) {

      int[] rT = {(int) (2 * Math.random()), -1};

      if (rT[0] == WeihnachtsElfen.FALLING_GREEN) {
        rT[1] = (int) (8 * Math.random());
        return new Ast((int) ((29 - (2 * rT[1] + 2)) * Math.random()) + 1, 1, rT[1]);
      } else {
        rT[1] = (int) (5 * Math.random());
        return new Baumstamm((int) ((29 - (2 * rT[1] + 2)) * Math.random()) + 1, 1, rT[1]);
      }
    }

    int rO = (int) (3 * Math.random());
    int rX = (int) (30 * Math.random());
    if (rO == 0) {
      return new Weihnachtskugel(rX, 1);
    } else if (rO == 1) {
      return new Schneeflocke(rX, 1);
    } else {
      return new Pinguin(rX, 1);
    }
  }


  public static void keyPressed(int key) {
    draw(landscape);
    if (key != WeihnachtsElfen.NO_KEY) {
      System.out.println(key);

      if (key == WeihnachtsElfen.KEY_DOWN) {
        moveDown();
      } else if (key == WeihnachtsElfen.KEY_LEFT) {
        moveLeft();
        moveDown();
      } else if (key == WeihnachtsElfen.KEY_RIGHT) {
        moveRight();
        moveDown();
      } else if (key == WeihnachtsElfen.KEY_UP) {
        phase = phase%2+1;
      }

      int z = (int) (5 * Math.random());

      if (z == 0) {
        Weihnachtsobjekt wO = createRandomObjekt();

        if (wO == null) {
          return;
        }

        System.out.println("Objekt hinzugefügt");

        objekte.add(wO);

        wO.addObjektToSpielfeld(landscape);

        draw(landscape);
      }

      //objekte.forEach(System.out::println);


    }
  }

  private static void moveRight() {
    scanStaticObjectsBackDown();
    scanStaticObjectsFrontLR();

    objekte.forEach(weihnachtsobjekt -> {
      if (weihnachtsobjekt instanceof Schneeflocke){
        // Schneeflocken fallen in gerader linie nach unten
      } else if(weihnachtsobjekt instanceof Pinguin ||
          weihnachtsobjekt instanceof Weihnachtskugel) {
        weihnachtsobjekt.moveRight(staticObjectsFront);

      } else {
        weihnachtsobjekt.moveRight(staticObjectsBack);

      }
    });

    WeihnachtsElfen.removeMarkedForDeath(objekte);

    //System.out.println(objekte.toString());

    draw(landscape);
  }

  private static void moveLeft() {
    scanStaticObjectsBackDown();
    scanStaticObjectsFrontLR();

    objekte.forEach(weihnachtsobjekt -> {
      if (weihnachtsobjekt instanceof Schneeflocke){
        // Schneeflocken fallen in gerader linie nach unten
      } else if(weihnachtsobjekt instanceof Pinguin ||
          weihnachtsobjekt instanceof Weihnachtskugel) {
        weihnachtsobjekt.moveLeft(staticObjectsFront);

      } else {
        weihnachtsobjekt.moveLeft(staticObjectsBack);

      }
    });

    WeihnachtsElfen.removeMarkedForDeath(objekte);

    System.out.println(objekte.toString());

    draw(landscape);

  }


  private static void scanStaticObjectsFrontLR() {
    WeihnachtsElfen.sortWeihnachtsbjectsByYCoordinate(objekte);

    SingleObject[][] frontObjects = new SingleObject[30][33];


    objekte.forEach(weihnachtsobjekt -> {
      if (weihnachtsobjekt instanceof SingleObject) {
        frontObjects[weihnachtsobjekt.x][weihnachtsobjekt.y] = (SingleObject) weihnachtsobjekt;
      }
    });

    staticObjectsFront = new boolean[30][33];

    for (int x = 0; x < 30; x++) {
      staticObjectsFront[x][staticObjectsFront[x].length-1] = true;
    }

    for (int y = 33 - 1; y >= 0; y--) {
      for (int x = 0; x < 30; x++) {

        SingleObject sO = frontObjects[x][y];

        if (y == 33 - 1) {
          if (sO != null) {
            staticObjectsFront[x][y] = true;
          }
        } else {

          if (sO != null && (staticObjectsFront[x][y + 1] || sO.isAstHereAndNoAstBeneath())) {
            staticObjectsFront[x][y] = true;
          }
        }


      }
    }

    //printStaticObjectsArray(staticObjectsFront);

  }


  private static void moveDown() {
    scanStaticObjectsFrontDown();
    scanStaticObjectsBackDown();

    objekte.forEach(weihnachtsobjekt -> {
      if (weihnachtsobjekt instanceof Schneeflocke ||
          weihnachtsobjekt instanceof Pinguin ||
          weihnachtsobjekt instanceof Weihnachtskugel) {
        weihnachtsobjekt.moveDown(staticObjectsFront);


      } else {
        weihnachtsobjekt.moveDown(staticObjectsBack);

      }
    });

    draw(landscape);
  }

  private static void scanStaticObjectsBackDown() {
    WeihnachtsElfen.sortWeihnachtsbjectsByYCoordinate(objekte);

    MultiObject[][] backObjects = new MultiObject[30][33];

    objekte.forEach(weihnachtsobjekt -> {
      if (weihnachtsobjekt instanceof MultiObject) {
        backObjects[weihnachtsobjekt.x][weihnachtsobjekt.y] = (MultiObject) weihnachtsobjekt;
      }
    });

    staticObjectsBack = new boolean[30][33];

    for (int x = 0; x < 30; x++) {
      staticObjectsBack[x][staticObjectsBack[x].length-1] = true;
    }

    for (int y = 33 - 1; y >= 0; y--) {
      for (int x = 0; x < 30; x++) {

        MultiObject mO = backObjects[x][y];

        if (mO != null && mO.cantMoveDown(staticObjectsBack)) {
          for (int i = x; i < 2 * mO.breite + 2 + x; i++) {
            staticObjectsBack[i][y] = true;
          }
        }
      }
    }

    //printStaticObjectsArray(staticObjectsBack);

  }

  private static void printStaticObjectsArray(boolean[][] staticObjects) {

    for (int y = 0; y < staticObjects[0].length; y++) {
      for (int x = 0; x < staticObjects.length; x++) {
        System.out.print(staticObjects[x][y] ? "x" : "-");
      }
      System.out.println();
    }

  }

  private static void scanStaticObjectsFrontDown() {
    WeihnachtsElfen.sortWeihnachtsbjectsByYCoordinate(objekte);

    SingleObject[][] frontObjects = new SingleObject[30][33];

    MultiObject[][] backObjects = new MultiObject[30][33];


    objekte.forEach(weihnachtsobjekt -> {
      if (weihnachtsobjekt instanceof SingleObject) {
        frontObjects[weihnachtsobjekt.x][weihnachtsobjekt.y] = (SingleObject) weihnachtsobjekt;
      } else if (weihnachtsobjekt instanceof MultiObject) {
        for (int i = weihnachtsobjekt.x; i < 2 * ((MultiObject) weihnachtsobjekt).breite + 2 + weihnachtsobjekt.x; i++) {
          backObjects[i][weihnachtsobjekt.y] = (MultiObject) weihnachtsobjekt;
        }

      }
    });

    staticObjectsFront = new boolean[30][33];

    for (int x = 0; x < 30; x++) {
      staticObjectsFront[x][staticObjectsFront[x].length-1] = true;
    }

    for (int y = 33 - 1; y >= 0; y--) {
      for (int x = 0; x < 30; x++) {

        SingleObject sO = frontObjects[x][y];

        if (y == 33 - 1) {
          if (sO != null) {
            staticObjectsFront[x][y] = true;
          }
        } else {

          if (backObjects[x][y] instanceof Ast && !(backObjects[x][y + 1] instanceof Ast)) {
            staticObjectsFront[x][y+1] = true;
          }

          if (staticObjectsFront[x][y + 1] && sO != null) {
            staticObjectsFront[x][y] = true;
          }

        }


      }
    }

    printStaticObjectsArray(staticObjectsFront);

  }

  /*********************************************/
  /* Ab hier soll nichts mehr geändert werden! */

  /*********************************************/

  public static void main(String[] args) {
    draw(landscape);
    handleUserInput();
  }

  private static void handleUserInput() {
    while (true) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ie) {
        /* Intentionally left blank */
      }
      int step = nextStep();
      if (step != NO_KEY) {
        // System.out.print(step+",");
        keyPressed(step);
      }
    }
  }
}
