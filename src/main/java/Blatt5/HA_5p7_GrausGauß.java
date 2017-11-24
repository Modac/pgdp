package Blatt5;

import Libraries.MiniJava;

public class HA_5p7_GrausGauß extends MiniJava {

  private static int lines;

  public static void main(String[] args) {
    lines = read("Geben sie die Anzahl der Gleichungen ein.");
    int[] matrix = readMatrix();
    int[] resArr = solveSystem(matrix);

    String resStr = "{ ";
    for (int i = 0; i < resArr.length; i++) {
      resStr += resArr[i];
      if (i < resArr.length - 1) {
        resStr += ", ";
      } else {
        resStr += " }";
      }
    }
    writeLineConsole(resStr);
    write(resStr);
  }

  public static void testAll() {
    int[] testMatrix = new int[]{0, 1, 1, 1,
        1, 1, 1, 1,
        -2, -2, 1, 1};
    lines = 3;
    printMatrix(testMatrix);
    writeLineConsole(get(testMatrix, 2, 1));
    multLine(testMatrix, 0, -4);
    printMatrix(testMatrix);
    multAddLine(testMatrix, 1, 2, 2);
    printMatrix(testMatrix);
    swap(testMatrix, 1, 2);
    printMatrix(testMatrix);
    searchSwap(testMatrix, 0);
    printMatrix(testMatrix);
    searchSwap(testMatrix, 1);
    printMatrix(testMatrix);
    writeLineConsole(kgv(13, 437));

    // PrintMatrix ein bisschen missusen
    printMatrix(rowEchelonToResult(new int[]{1, 2, 3, 2, 0, -1, -2, 0, 0, 0, -2, -6}));
  }

  public static int[] readMatrix() {
    if (lines<0){
      return null;
    }
    int[] matrix = new int[lines * (lines + 1)];
    for (int i = 0; i < matrix.length; i++) {
      int line = i / (lines + 1) + 1;
      int column = i % (lines + 1) + 1;
      if (column <= lines) {
        matrix[i] = read("Gib den " + column + ". Koeffizienten der " + line + ". Gleichung ein.");
      } else {
        matrix[i] = read("Gib das Ergebniss der " + line + ". Gleichung ein.");
      }
    }
    return matrix;
  }

  public static void printMatrix(int[] matrix) {
    String matStr = "";

    int[] digitsArr = new int[matrix.length];
    int[] maxDigitsArr = new int[lines + 1];
    for (int i = 0; i < matrix.length; i++) {
      int m = 100;
      int pow = 1;
      int stellen = 1;
      int zahl = matrix[i];
      if (zahl < 0) {
        zahl *= -1;
        stellen++;
      }

      while (stellen < m) {
        pow *= 10;
        if (pow > zahl) {
          // abbruch der while-Schleife
          m = stellen;
        } else {
          stellen++;
        }
      }
      digitsArr[i] = stellen;

      if (stellen > maxDigitsArr[i % (lines + 1)]) {
        maxDigitsArr[i % (lines + 1)] = stellen;
      }
    }

    for (int i = 0; i < matrix.length; i++) {

      int line = i / (lines + 1) + 1;
      int column = i % (lines + 1) + 1;
      if (column == 1) {
        if (lines == 1) {
          matStr += "<";
        } else if (line == 1) {
          matStr += "/";
        } else if (line == lines) {
          matStr += "\\";
        } else {
          matStr += "|";
        }
      }

      for (int j = digitsArr[i]; j <= maxDigitsArr[column - 1]; j++) {
        matStr += " ";
      }

      matStr += matrix[i];

      if (column == lines + 1) {
        if (lines == 1) {
          matStr += " >";
        } else if (line == 1) {
          matStr += " \\";
        } else if (line == lines) {
          matStr += " /";
        } else {
          matStr += " |";
        }
        matStr += "\n";
      }
    }
    writeLineConsole(matStr);
    // Bei dem write Fenster sind die Zeichen nicht einheitlich breit ;(
    //write(matStr);
  }

  public static int get(int[] matrix, int line, int column) {
    return matrix[line * (lines + 1) + column];
  }

  public static void set(int[] matrix, int line, int column, int value) {
    matrix[line * (lines + 1) + column] = value;
  }

  public static void multLine(int[] matrix, int line, int factor) {
    for (int i = 0; i < lines + 1; i++) {
      set(matrix, line, i, get(matrix, line, i) * factor);
    }
  }

  public static void multAddLine(int[] matrix, int line, int otherLine, int factor) {
    for (int i = 0; i < lines + 1; i++) {
      set(matrix, otherLine, i, get(matrix, line, i) * factor + get(matrix, otherLine, i));
    }
  }

  public static void swap(int[] matrix, int lineA, int lineB) {
    for (int i = 0; i < lines + 1; i++) {
      int t = get(matrix, lineA, i);
      set(matrix, lineA, i, get(matrix, lineB, i));
      set(matrix, lineB, i, t);
    }
  }

  public static void searchSwap(int[] matrix, int fromLine) {
    if (get(matrix, fromLine, fromLine) != 0) {
      return;
    }
    for (int i = fromLine + 1; i < lines; i++) {
      if (get(matrix, i, fromLine) != 0) {
        swap(matrix, fromLine, i);
        return;
      }
    }
  }

  public static int kgv(int a, int b) {
    int h;
    int ggT = a;
    int bT = b;
    while (bT != 0) {
      h = ggT % bT;
      ggT = bT;
      bT = h;
    }
    return a * b / ggT;
  }

  public static int[] rowEchelonToResult(int[] matrix) {
    int[] resArr = new int[lines];
    for (int i = lines - 1; i >= 0; i--) {
      int res = get(matrix, i, lines);
      for (int j = i + 1; j < resArr.length; j++) {
        res -= resArr[j] * get(matrix, i, j);
      }
      resArr[i] = res / get(matrix, i, i);
    }
    return resArr;
  }

  public static int[] solveSystem(int[] matrix) {
    for (int i = 0; i < lines; i++) {
      searchSwap(matrix, i);
      for (int j = i + 1; j < lines; j++) {
        int ei = get(matrix, j, i);
        if (ei != 0) {
          int d = get(matrix, i, i);
          int kgv = kgv(d, ei);
          multLine(matrix, j, kgv / ei);
          multAddLine(matrix, i, j, (-kgv) / d);
        }
      }
    }
    return rowEchelonToResult(matrix);
  }

}
