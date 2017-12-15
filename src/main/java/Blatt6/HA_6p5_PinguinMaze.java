package Blatt6;

import Libraries.Maze;
import Libraries.MiniJava;
import javax.annotation.Generated;

@Generated("Ignore Duplicate Code, because of the Files in Abgaben")
public class HA_6p5_PinguinMaze extends Maze {

  public static int[][] lab;

  public static void main(String[] args) {

    int width = MiniJava.readInt("Breite des Labyrinths:");
    while (width < 0) {
      width = MiniJava.readInt("Bitte keine negative Breite:");
    }

    int height = MiniJava.readInt("Höhe des Labyrinths:");
    while (height < 0) {
      height = MiniJava.readInt("Bitte keine negative Höhe:");
    }

    int maxDis = MiniJava.readInt("Maximale Distanz zur Startposition:");

    lab = generatePenguinMaze(width, height);
    int penguins = walk(1, 0, maxDis);
    MiniJava.write(penguins + " Pinguin" + (penguins != 1 ? "e" : "") + " gerettet.");
  }

  public static int walk(int x, int y, int maxDistance) {
    lab[x][y] = PLAYER;
    draw(lab);

    if (maxDistance < 2) {
      return 0;
    }

    int penguins = 0;
    for (int i = 0; i < 4; i++) {
      int[] nextPos = getNextPos(x, y, i);

      int nextX = nextPos[0];
      int nextY = nextPos[1];

      // Wenn die nächste Position nicht mehr im Labyrinth ist, eine Wand ist, schon besucht wurde
      // oder nicht von einer Wand umgeben ist, dann versuchs mit der nächsten
      if (isNotInRange(nextX, nextY) ||
          lab[nextX][nextY] == WALL ||
          !isSurroundedByWall(nextX, nextY) ||
          lab[nextX][nextY] == OLD_PATH_ACTIVE ||
          lab[nextX][nextY] == OLD_PATH_DONE) {
        continue;
      }

      if (lab[nextX][nextY] == PENGUIN) {
        penguins++;
        //System.out.println("Pinguin gerettet");
      }

      lab[x][y] = OLD_PATH_ACTIVE;
      lab[nextX][nextY] = PLAYER;

      draw(lab);

      penguins += walk(nextX, nextY, maxDistance - 1);
      lab[nextX][nextY] = OLD_PATH_DONE;
      lab[x][y] = PLAYER;
      draw(lab);

    }
    return penguins;
  }

  // Wenn ungültiger Index, dann true
  public static boolean isNotInRange(int x, int y) {
    return x < 0 || x > lab.length - 1 ||
        y < 0 || y > lab[x].length - 1;
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
  //
  public static int[] getNextPos(int x, int y, int i) {
    int nextX = x;
    int nextY = y;
    switch (i) {
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
      int[] nextPos = getNextPos(x, y, i);
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

  public static boolean isSurroundedByWall(int nextX, int nextY) {
    boolean res = false;
    for (int i = 0; i < 8; i++) {
      int[] testPos = getNextPosDia(nextX, nextY, i);
      if (!isNotInRange(testPos[0], testPos[1]) && lab[testPos[0]][testPos[1]] == WALL) {
        res = true;
        break;
      }
    }
    return res;
  }

}
