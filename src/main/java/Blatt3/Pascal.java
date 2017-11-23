package Blatt3;

import Libraries.MiniJava;

public class Pascal extends MiniJava {

  public static void main(String[] args) {
    int zeilenzahl = read("Geben Sie die Anzahl der Zeilen im Dreieck an.");
    while (zeilenzahl < 0) {
      zeilenzahl =
          read("Geben Sie die Anzahl der Zeilen im Dreieck an. Geben Sie sich dabei etwas Mühe!");
    }

    int[] dreieck = new int[zeilenzahl * (zeilenzahl + 1) / 2];
    // Wir berechnen die Zeilen m in einer Schleife.
    int m = 0;
    while (m < zeilenzahl) {
      // Wir nutzen den kleinen Gauß, um den Offset der aktuellen Zeile
      // zu berechnen.
      int currentLine = m * (m + 1) / 2;

      dreieck[currentLine + 0] = 1; // Nulltes Element = 1.
      dreieck[currentLine + m] = 1; // Letztes Element = 1.
      // Berechnung der Elemente hinter dem 0tn und vor dem letzten Element.
      int i1 = 1;
      while (i1 < m) {
        // Wir berechnen den Offset der letzten Zeile.
        int lastLine = (m - 1) * m / 2;
        dreieck[currentLine + i1] = dreieck[lastLine + i1 - 1] + dreieck[lastLine + i1];
        i1++;
      }
      m++;
    }

    // Wir geben das Dreieck zeilenweise aus.
    for (int i = 0; i < zeilenzahl; i++) {
      writeConsole("n = " + i + "\t");
      // Ausgabe der Elemente in der Zeile
      for (int j = 0; j < i + 1; j++) {
        int currentLine = i * (i + 1) / 2;
        writeConsole(dreieck[currentLine + j] + "\t");
      }
      writeLineConsole();
    }
  }
}
