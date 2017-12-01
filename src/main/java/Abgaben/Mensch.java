public class Mensch extends Aerger {

  private static int[] arrYellow = {-1, -1, -1, -1};
  private static int[] arrBlue = {-1, -1, -1, -1};
  private static int[] arrRed = {-1, -1, -1, -1};
  private static int[] arrGreen = {-1, -1, -1, -1};
  private static int[] startPos = {0, 10, 30, 20};
  private static int[][] playerArrs = {arrYellow, arrBlue, arrRed, arrGreen};
  private static String[] playerNames = {"Gelb", "Blau", "Rot", "Grün"};

  public static void main(String[] args) {

    int minPlayers = 2;
    int maxPlayers = 4;

    int players = readInt("Wie viele Spieler spielen?");
    while (players < minPlayers || players > maxPlayers) {
      players = readInt("Es können nur 2 bis 4 Spieler spielen");
    }

    // Für immer über die Player loopen
    int i = 0;
    while (true) {
      i = i % players;

      paintField(arrYellow, arrBlue, arrRed, arrGreen);

      int dice = dice();
      int stein = getStein(i, dice);

      // stein nach eins nach unten korrigieren, Java zählt von 0 ab, Eingabe geht ab 1 los
      stein--;

      // Versuche solange den Stein zu setzen, bis er nicht von einem anderen Stein blockiert wird
      boolean canRun = false;
      while (!canRun) {
        canRun = true;

        int prevPos = playerArrs[i][stein];

        // Wenn der Stein im Haus stand, dann setzte ihn jetzt auf ein Feld hinter die Startposition,
        // damit wenn man mit 1 raus kommt, auf der Startposition landet
        if (playerArrs[i][stein] == -1) {
          playerArrs[i][stein] = startPos[i] - 1;
        }

        // Trivial
        playerArrs[i][stein] += dice;
        playerArrs[i][stein] %= 40;

        // Wenn die jetzige Position des Steins über oder auf der Startposition liegt und
        // der Stein davor nicht im Haus und vor der Startposition stand,
        // dann versetze den Stein in den Garten
        // Spezialfall: wenn die Startposition 0 ist dann simuliere, dass alles um 10 verschoben ist
        if (((playerArrs[i][stein] >= startPos[i]) && (prevPos != -1) && (prevPos < startPos[i])) ||
            ((startPos[i] == 0) && ((playerArrs[i][stein] + 10) % 40 >= startPos[i] + 10) &&
                (prevPos != -1) && ((prevPos + 10) % 40 < startPos[i] + 10))) {
          playerArrs[i][stein] = 40;
        }

        // Überprüfe ob ein anderer Stein des selben Spielers schon an der neuen Position liegt
        for (int j = 0; j < playerArrs[i].length; j++) {
          // Überspringe den aktuell bewegten Stein, weil der liegt natürlich an seiner eigenen Position
          if (j == stein) {
            continue;
          }

          // Wenn ein Stein die selbe Position hat und nicht schon im Garten ist
          if (playerArrs[i][j] == playerArrs[i][stein] && playerArrs[i][j] <= 39) {
            // Wenn ja, setze den Stein an seine vorherige Position, frag nach einem Anderen Stein
            // und sag der while Schleife sie soll weiter laufen
            playerArrs[i][stein] = prevPos;
            write(
                "Dein Zug wurde durch einen deiner anderen Steine blockiert. Bitte wähle einen anderen Stein.");
            stein = getStein(i, dice);
            stein--;
            canRun = false;
          }
        }
      }

      // Schau ob ein Stein eines anderen Spielers schon an der Position liegt
      for (int j = 0; j < playerArrs.length; j++) {
        // Eigentlich unnötig, weil vorhin schon überprüft wurde ob eigene Steine im Weg sind, aber hey
        if (j == i) {
          continue;
        }

        for (int f = 0; f < playerArrs[j].length; f++) {
          // Wenn es einen gibt, dann versetze diesen ins Haus zurück
          if (playerArrs[j][f] == playerArrs[i][stein] && playerArrs[j][f] <= 39) {
            playerArrs[j][f] = -1;
          }
        }
      }

      // Schau ob ein Spieler jetzt alle Steine im Garten hat.
      boolean allInGarden = true;
      for (int j = 0; j < playerArrs[i].length; j++) {
        allInGarden &= playerArrs[i][j] > 39;
      }
      if (allInGarden) {
        paintField(arrYellow, arrBlue, arrRed, arrGreen);
        write("Spieler " + playerNames[i] + " hat gewonnen.");
        return;
      }
      i++;
    }

  }

  private static int getStein(int i, int dice) {
    int stein = readInt("Spieler " + playerNames[i] + " hat eine " + dice
        + " gewürfelt. Wähle welcher Stein bewegt werden soll.");


    while (stein < 1 || stein > 4 || playerArrs[i][stein - 1] > 39) {
      // Kurzschlussausswertung, damit keine IndexOutOfException entsteht
      if (((stein >= 1 && stein <= 4) && playerArrs[i][stein - 1] > 39)) {
        stein = readInt(
            "Du hast eine " + dice
                + " gewürfelt. Bitte wähle einen Stein zwischen 1 und 4, der noch nicht im Garten steht.");
      } else {
        stein = readInt(
            "Du hast eine " + dice + " gewürfelt. Bitte wähle einen Stein zwischen 1 und 4.");
      }
    }

    return stein;
  }

}
