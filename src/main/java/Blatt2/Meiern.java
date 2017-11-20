package Blatt2;

import MiniJava.MiniJava;

public class Meiern extends MiniJava {

    public static void main(String[] args) {

        while (true) spielen();
    }

    private static void spielen(){
        int wurf, i, letzterWurf;

        wurf = 1;
        letzterWurf = 0;
        i = 0;

        while (wurf > letzterWurf) {

            letzterWurf = wurf;

            int zahl1, zahl2, ausgabe;
            zahl1 = dice();
            zahl2 = dice();
            if (zahl1 > zahl2) {
                wurf = zahl1 * 10 + zahl2;
                ausgabe = wurf;
            } else if (zahl2 > zahl1) {
                wurf = zahl2 * 10 + zahl1;
                ausgabe = wurf;
            } else { // Pasch
                wurf = zahl1 * 100 + zahl2 * 10; //mehr wert, zum leichteren Vergleich
                ausgabe = wurf / 10;
            }

            if (wurf == 21) {  // Meier
                wurf = wurf * 1000;
            }

            if (i == 0) {
                write("Spieler: " + ausgabe);
            } else {
                write("Computer: " + ausgabe);
            }

            i = (i + 1) % 2;
        }
        if (i == 0) {
            write("Gewonnen");
        } else {
            write("Verloren");
        }

    }
}
