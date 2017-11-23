package Blatt3;

import Libraries.MiniJava;
import javax.annotation.Generated;

@Generated("Ignore Duplicate Code, because of the Files in Abgaben")
public class HA_3p8_Palina extends MiniJava {

    public static void main(String[] args) {
        int zahl = read("Bitte gebe eine positive Zahl ein: ");
        while (zahl < 0) {
            zahl = read("Bitte gebe eine POSITIVE Zahl ein: ");
        }

        // unerreichbar groß, aber damit keine Endlosschleife entsteht
        int i = 100;
        int pow = 1;
        int stellen = 1;
        while (stellen < i) {
            pow *= 10;
            if (pow > zahl) {
                // abbruch der while-Schleife
                i = stellen;
            } else {
                stellen++;
            }
        }

        int[] ziffern = new int[stellen];
        i = stellen - 1;
        pow = 1;
        while (i >= 0) {
            ziffern[i] = (zahl / pow) - (zahl / pow / 10 * 10);
            i--;
            pow *= 10;
        }

        /* Debug
        i = 0;
        while (i < stellen) {
            writeConsole(ziffern[i] + ", ");
            i++;
        }
        */

        i=0;
        stellen--;
        boolean istPalindrom = true;
        while(i<=stellen){
            if(ziffern[i] != ziffern[stellen]){
                istPalindrom = false;
            }
            i++;
            stellen--;
        }

        if(istPalindrom){
            write("Die Zahl " + zahl + " ist ein Palindrom");
        } else {
            write("Die Zahl " + zahl + " ist kein Palindrom");
        }

    }
}
