package Blatt3;

import MiniJava.MiniJava;

public class MinMax extends MiniJava {

  public static void main(String[] args) {
    // Anzahl der einzugebenden Zahlen abfragen
    int eingaben = readInt("Wie viele Zahlen moechten Sie eingeben?");
    if (eingaben < 1) {
      return;
    }

    // Array anlegen
    int[] zahlen = new int[eingaben];


    // Array mit Zahlen fuellen
    int zahl;
    int i = 0;
    while (i < eingaben) {
      zahl = readInt("Bitte geben Sie die " + (i + 1) + ". Zahl ein.");
      zahlen[i] = zahl;
      i++;
    }

    // min und max berechnen
    int min = zahlen[0];
    int max = zahlen[0];
    i = 1;
    while (i < eingaben) {
      if (zahlen[i] < min) {
        min = zahlen[i];
      }
      if (zahlen[i] > max) {
        max = zahlen[i];
      }
      i++;
    }

    // Ausgeben
    write("Die kleinste Zahl war " + min);
    write("Die groesste Zahl war " + max);

  }
}
