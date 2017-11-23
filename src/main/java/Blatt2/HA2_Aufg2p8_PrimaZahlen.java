package Blatt2;

import Libraries.MiniJava;

public class HA2_Aufg2p8_PrimaZahlen extends MiniJava {

    public static void main(String[] args) {
        int n, primzahl;
        primzahl = 0;
        n=-1;
        while(n<0) {
            n = readInt("Zahl: ");
        }
        if(n==2) {
            primzahl=1;
        } else if(n%2==0) {
            primzahl=0;
        } else{
            int m=3;
            while(m<n) {
                if(n%m==0) m=n+1;
                m = m + 1;
            }
            if(n==m) primzahl=1;
        }

        if(primzahl==1) {
            write(n + " ist eine Primzahl");
        } else {
            write(n + " ist keine Primzahl");
        }
    }

    /*
    public static void main(String[] args) {
        for (int i = 0; i<100; i++) testePrimzahl(i);
    }

    private static void testePrimzahl(int i){
        int n, primzahl;
        primzahl = 0;
        n=-1;
        while(n<0) {
            n = i;
        }
        if(n==2) {
            primzahl=1;
        } else if(n%2==0) {
            primzahl=0;
        } else{
            int m=3;
            while(m<n) {
                if(n%m==0) m=n+1;
                m = m + 1;
            }
            if(n==m) primzahl=1;
        }

        if(primzahl==1) {
            System.out.println(n + "+++++");
        } else {
            System.out.println(n + "--");
        }
    }
    */
}
