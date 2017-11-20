package Blatt2;

import MiniJava.MiniJava;

public class HA2_Aufg2p7_3und7 extends MiniJava {

  public static void main(String[] args) {
    int n;
    int summe;
    n = -1;
    summe = 0;
    while (n < 0) {
      n = readInt("Zahl: ");
    }
    while (n > 0) {
      if ((n % 3) == 0 || (n % 7) == 0) {
        summe = summe + n;
      }
      n = n - 1;
    }
    write("Summe: " + summe);
  }
}
