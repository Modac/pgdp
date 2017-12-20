import Libraries.PenguinPen;

public class HilfPingu extends PenguinPen {

  // private static final int[][] penguinPen = generateStandardPenguinPen(24, 17);
  private static final int[][] penguinPen = generatePenguinPen(24, 17);

  private static int[] playerPos = {1, 0};
  private static int[][] zufulline = {};
  private static int[][] wechsuline = {};
  private static int[][] springuine = {};
  private static int[][] schlauine = {};
  // Damit die Arrays in einer Methode ausgetauscht werden können
  private static int[][][] penguins = {zufulline, wechsuline, springuine, schlauine};
  private static final int ZUFULLINE = 0;
  private static final int WECHSULINE = 1;
  private static final int SPRINGUINE = 2;
  private static final int SCHLAUINE = 3;

  private static final int UP = 0;
  private static final int RIGHT = 1;
  private static final int DOWN = 2;
  private static final int LEFT = 3;

  private static int[][] wStates = {};

  private static boolean firstRun = true;

  public static void move(int direction) {
    System.out.println(direction);

    // Füttere alle Pinguine an den angrezenden Feldern
    for (int i = 0; i < 8; i++) {
      int[] nextPos = getNextPosDia(playerPos[0], playerPos[1], i);
      if (isInRange(nextPos[0], nextPos[1]) &&
          getFieldVal(nextPos) >= PENGUIN_OOO &&
          getFieldVal(nextPos) <= PENGUIN_IOO) {

        switch (getFieldVal(nextPos)) {
          case PENGUIN_OOI:
            removePenguinFromArray(ZUFULLINE, nextPos[0], nextPos[1]);
            break;
          case PENGUIN_OIO:
            removePenguinFromArray(WECHSULINE, nextPos[0], nextPos[1]);
            break;
          case PENGUIN_OII:
            removePenguinFromArray(SPRINGUINE, nextPos[0], nextPos[1]);
            break;
          case PENGUIN_IOO:
            removePenguinFromArray(SCHLAUINE, nextPos[0], nextPos[1]);
            break;
          default:
        }
        setFieldVal(nextPos, FREE);

        System.out.println("(" + nextPos[0] + ", " + nextPos[1] + ") wird frei -- " +
            "Pinguin ist satt.");
      }
    }

    // Versuche den Zookeeper zu bewegen
    int[] nextPlPos = getNextPos(playerPos[0], playerPos[1], direction);
    if (isInRange(nextPlPos[0], nextPlPos[1]) && getFieldVal(nextPlPos) == FREE) {
      setFieldVal(playerPos, FREE);
      setFieldVal(nextPlPos, ZOOKEEPER);

      System.out.println("(" + playerPos[0] + ", " + playerPos[1] + ") ==>" +
          " (" + nextPlPos[0] + ", " + nextPlPos[1] + ") -- Spielschritt.");

      playerPos = nextPlPos;

      // Finde am Anfang alle Pinguine
      if (firstRun) {
        resetPenguins();
        findPenguins();
        initWechsulineStates();
        firstRun = false;
      }

      for (int i = 0; i < zufulline.length; i++) {
        int[] penPos = zufulline[i];
        int[] nextPos = penPos;
        byte fieldMask = 0b1110000;
        // Damit das erste OR unnötig ist und wir uns damit ein if sparen
        int n = 4;

        // Solange die neue potenzielle Position außerhalb des Spielfeldes ist oder
        // nicht frei suche ein neues zufälliges Feld
        while (!isInRange(nextPos[0], nextPos[1]) || (getFieldVal(nextPos) != FREE)) {

          // Wenn alle möglichen Felder durchprobiert wurden, hör auf
          if (fieldMask == 0b1111111) {
            break;
          }

          // Speichere, dass das letzte Feld schon probiert wurde
          fieldMask |= 0b1 << n;
          n = (int) (4 * Math.random());
          nextPos = getNextPos(penPos[0], penPos[1], n);
        }

        // Wenn eine Bewegung möglich ist
        if (fieldMask != 0b1111111) {
          setFieldVal(penPos, FREE);
          setFieldVal(nextPos, PENGUIN_OOI);
          zufulline[i] = nextPos;
          printPenMove(penPos, nextPos);
        }
      }

      // Im Folgenden ist wechsuline[i][0] die Richtung des jeweiligen Pinguins
      // und wechsuline[i][1] = 0 wenn der Pinguin noch nicht die RHR anwendet
      for (int i = 0; i < wechsuline.length; i++) {
        int[] penPos = wechsuline[i];
        int[] nextPos = penPos;

        // Noch nicht RHR
        if (wStates[i][1] == 0) {
          int[] nextPosT = getNextPosIntuitiv(penPos, RIGHT);
          if (getFieldVal(nextPosT) == WALL) {
            // Jetzt RHR anwenden
            // System.out.println("Jetzt RHR");
            wStates[i][1] = 1;
            wStates[i][0] = UP;
          } else if (getFieldVal(nextPosT) == FREE) {
            nextPos = nextPosT;
          }
        }

        if (wStates[i][1] == 1) {
          // DEBUG: System.out.println("RHR");
          int[] nextPosT = getNextPosIntuitiv(penPos, (wStates[i][0] + 1) % 4);
          // Ist eine Wand rechts von uns
          if (getFieldVal(nextPosT) == WALL) {
            // DEBUG: System.out.println("Wand rechts neben uns");

            do {
              nextPosT = getNextPosIntuitiv(penPos, wStates[i][0]);

              if (!isInRange(nextPosT[0], nextPosT[1])) {
                // DEBUG: System.out.println("Not in range");

                // 180 Grad Drehung und geradeaus laufen
                wStates[i][0] = (wStates[i][0] + 2) % 4;
                nextPosT = getNextPosIntuitiv(penPos, wStates[i][0]);
                break;
              } else if (getFieldVal(nextPosT) == WALL) {
                // System.out.println("Wall");
                // Nach links drehen und weiter probieren
                wStates[i][0] = (wStates[i][0] - 1) < 0 ? 3 : (wStates[i][0] - 1);
              } else if (getFieldVal(nextPosT) != FREE) {
                // System.out.println("Pinguin im weg");
                // Nicht bewegen, weil Pinguin im Weg
                break;
              }
            } while (getFieldVal(nextPosT) != FREE);

            if (getFieldVal(nextPosT) == FREE) {
              nextPos = nextPosT;
            }
          } else if (getFieldVal(nextPosT) == FREE) {
            wStates[i][0] = (wStates[i][0] + 1) % 4;
            nextPos = nextPosT;

          }
        }

        setFieldVal(penPos, FREE);
        setFieldVal(nextPos, PENGUIN_OIO);
        wechsuline[i] = nextPos;

        printPenMove(penPos, nextPos);
      }

      for (int i = 0; i < springuine.length; i++) {
        int[] penPos = springuine[i];
        int[] nextPos = penPos;

        // Suche solange nach einem zufälligem Feld, solang es nicht frei ist
        while (getFieldVal(nextPos) != FREE) {
          int nX = (int) (penguinPen.length * Math.random());
          int nY = (int) (penguinPen[nX].length * Math.random());
          nextPos = new int[]{nX, nY};
        }
        setFieldVal(penPos, FREE);
        setFieldVal(nextPos, PENGUIN_OII);
        springuine[i] = nextPos;
        printPenMove(penPos, nextPos);
      }

      for (int i = 0; i < schlauine.length; i++) {
        int[] penPos = schlauine[i];
        int maxDis = 0;
        int maxDisK = -1;
        int[] nextPos;

        // Damit die jetzige Position die Letzte ist, die überprüft wird und damit im Zweifel
        // lieber nicht bewegt wird
        for (int k = 7; k >= -1; k--) {
          nextPos = getNextPos(penPos[0], penPos[1], k);

          // Wenn die mögliche nächste Position nicht gleich der jetzigen entspricht und dann
          // entweder die nächste Position außerhalb des Spielfeldes oder nicht frei ist überspring sie
          if (!(nextPos[0] == penPos[0] && nextPos[1] == penPos[1]) && (
              !isInRange(nextPos[0], nextPos[1]) || getFieldVal(nextPos) != FREE)) {
            continue;
          }
          int disX = nextPos[0] - playerPos[0];
          disX = disX < 0 ? -disX : disX;
          int disY = nextPos[1] - playerPos[1];
          disY = disY < 0 ? -disY : disY;

          //System.out.println("k: " + k + ", dis: " + (disX+disY) + ", maxDis: " + maxDis);

          if (disX + disY > maxDis) {
            maxDis = disX + disY;
            maxDisK = k;
          }
        }
        nextPos = getNextPos(penPos[0], penPos[1], maxDisK);

        if (isInRange(nextPos[0], nextPos[1])) {
          setFieldVal(penPos, FREE);
          setFieldVal(nextPos, PENGUIN_IOO);
          schlauine[i] = nextPos;
          printPenMove(penPos, nextPos);
        }
      }

    }
    PenguinPen.draw(penguinPen);

  }

  private static void initWechsulineStates() {
    wStates = new int[wechsuline.length][2];
    for (int i = 0; i < wStates.length; i++) {
      wStates[i] = new int[]{RIGHT, 0};
    }
  }

  private static void removePenguinFromArray(int penAI, int x, int y) {
    int penIndex = -1;
    for (int i = 0; i < penguins[penAI].length; i++) {
      if (penguins[penAI][i][0] == x && penguins[penAI][i][1] == y) {
        penIndex = i;
        break;
      }
    }

    if (penIndex != -1) {
      int[][] penArrT = new int[penguins[penAI].length - 1][2];
      for (int i = 0; i < penArrT.length; i++) {
        penArrT[i] = penguins[penAI][i >= penIndex ? i + 1 : i];
      }
      penguins[penAI] = penArrT;
      setPenguins();

      if (penAI == WECHSULINE) {
        int[][] wechsulineStatesT = new int[wStates.length - 1][2];
        for (int i = 0; i < wechsulineStatesT.length; i++) {
          wechsulineStatesT[i] = wStates[i >= penIndex ? i + 1 : i];
        }
        wStates = wechsulineStatesT;

      }
    }
  }

  private static void setPenguins() {
    zufulline = penguins[ZUFULLINE];
    wechsuline = penguins[WECHSULINE];
    springuine = penguins[SPRINGUINE];
    schlauine = penguins[SCHLAUINE];
  }

  private static void printPenMove(int[] pos, int[] nextPos) {
    System.out.println("(" + pos[0] + ", " + pos[1] + ") ==> (" + nextPos[0] + ", " + nextPos[1]
        + ") -- Pinguin flüchtet.");
  }


  // Leere die Pinguin-Arrays
  private static void resetPenguins() {
    penguins = new int[][][]{{}, {}, {}, {}};
    setPenguins();
  }

  // Füge für jeden Pinguin ein {x, y} von der Position in deren zugehöriges Array ein
  private static void findPenguins() {
    for (int i = 0; i < penguinPen.length; i++) {
      for (int j = 0; j < penguinPen[i].length; j++) {
        switch (penguinPen[i][j]) {
          case PENGUIN_OOI:
            insertPenguinInArray(ZUFULLINE, i, j);
            break;
          case PENGUIN_OIO:
            insertPenguinInArray(WECHSULINE, i, j);
            break;
          case PENGUIN_OII:
            insertPenguinInArray(SPRINGUINE, i, j);
            break;
          case PENGUIN_IOO:
            insertPenguinInArray(SCHLAUINE, i, j);
            break;
          default:
            // Ignore
        }
      }
      setPenguins();
    }
  }

  // Füge eine Pinguin-Position in dem entsprechenden Pinguin-Array ein
  // ist zwar sehr ineffizient aber einfach gelöst ^^
  private static void insertPenguinInArray(int penI, int x, int y) {
    int[][] penguinsT = new int[penguins[penI].length + 1][2];
    for (int k = 0; k < penguins[penI].length; k++) {
      penguinsT[k] = penguins[penI][k];
    }
    penguinsT[penguins[penI].length] = new int[]{x, y};
    penguins[penI] = penguinsT;
  }

  public static int getFieldVal(int[] pos) {
    return penguinPen[pos[0]][pos[1]];
  }

  public static void setFieldVal(int[] pos, int val) {
    penguinPen[pos[0]][pos[1]] = val;
  }

  // Wenn gültiger Index, dann true
  public static boolean isInRange(int x, int y) {
    return x >= 0 && x < penguinPen.length &&
        y >= 0 && y < penguinPen[x].length;
  }

  // Bestimme x und y der nächsten Position in Abhängigkeit eines i
  // Angepasst auf die Konstanten in PenguinPen
  //
  //    | 2 |
  // ___|___|___
  //  0 | x | 1
  // ___|_y_|___
  //    | d |
  //    |   |
  // -1 gibt die aktuelle Position wieder zurück
  public static int[] getNextPos(int x, int y, int i) {
    int nextX = x;
    int nextY = y;
    switch (i) {
      case -1:
        break;
      case 0:
        nextX = x - 1;
        break;
      case 1:
        nextX = x + 1;
        break;
      case 2:
        nextY = y - 1;
        break;
      default:
        nextY = y + 1;
    }
    return new int[]{nextX, nextY};
  }


  // Bestimme x und y der nächsten Position in Abhängigkeit eines i
  //
  //
  //    | 0 |
  // ___|___|___
  //  d | x | 1
  // ___|___|___
  //    | 2 |
  //    |   |
  // -1 gibt die aktuelle Position wieder zurück
  public static int[] getNextPosIntuitiv(int x, int y, int i) {
    int nextX = x;
    int nextY = y;
    switch (i) {
      case -1:
        break;
      case 0:
        nextY = y - 1;
        break;
      case 1:
        nextX = x + 1;
        break;
      case 2:
        nextY = y + 1;
        break;
      default:
        nextX = x - 1;
    }
    return new int[]{nextX, nextY};
  }

  public static int[] getNextPosIntuitiv(int[] pos, int i) {
    return getNextPosIntuitiv(pos[0], pos[1], i);
  }


  // Bestimme x und y der nächsten Position in Abhängigkeit eines i mit Diagonalen
  //
  //
  //  d | 0 | 4
  // ___|___|___
  //  3 | x | 1
  // ___|___|___
  //  6 | 2 | 5
  //    |   |
  //
  public static int[] getNextPosDia(int x, int y, int i) {
    int nextX = x;
    int nextY = y;
    if (i < 4) {
      int[] nextPos = getNextPosIntuitiv(x, y, i);
      nextX = nextPos[0];
      nextY = nextPos[1];
    } else {
      switch (i) {
        case 4:
          nextX = x + 1;
          nextY = y - 1;
          break;
        case 5:
          nextY = y + 1;
          nextX = x + 1;
          break;
        case 6:
          nextX = x - 1;
          nextY = y + 1;
          break;
        default:
          nextX = x - 1;
          nextY = y - 1;
      }

    }
    return new int[]{nextX, nextY};
  }

  /*********************************************/
  /* Ab hier soll nichts mehr geändert werden! */

  /*********************************************/

  public static void main(String[] args) {
    draw(penguinPen);
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
      if (step != NO_MOVE) {
        // System.out.print(step+",");
        move(step);
      }
    }
  }
}
