package Blatt2;

import MiniJava.MiniJava;

public class LustigeSieben extends MiniJava {

    public static void main(String[] args) {

        int kontostand, tipp, einsatz, wurf;

        kontostand = 100;
        tipp=1;

        while(kontostand>0 && tipp!=0) {

            tipp = readInt("Tipp: ");
            einsatz = readInt("Einsatz: ");
            if (tipp < 13 && tipp > 1 && einsatz > 0 && einsatz <= kontostand) {
                kontostand = kontostand - einsatz;
                wurf = dice() + dice();
                write("Wurf: " + wurf);
                int gewinn = 0;
                if (tipp == 7 && wurf == 7) {
                    gewinn = 3 * einsatz;
                } else if (tipp == wurf) {
                    gewinn = 2 * einsatz;
                } else if ((wurf < 7 && tipp < 7) || (wurf > 7 && tipp > 7)) {
                    gewinn = einsatz;
                } else {
                    gewinn = -einsatz;
                }
                kontostand = kontostand + gewinn;
                write("Gewinn: " + gewinn + ", Kontostand: " + kontostand);
            }

        }
    }
}
