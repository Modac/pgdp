package Blatt2;

import Libraries.MiniJava;

public class MeiernJ extends MiniJava {

    public static void main(String[] args) {
        while (true) spielen();
    }

    private static void spielen(){
        int i, letzterWurf, wurf;
        wurf = 0;
        i=-1;
        do {
            i=(i+1)%2;
            letzterWurf = wurf;
            wurf = wuerfeln();
            write(((i==0)?"Spieler":"Computer") + ": " + wurf);
            if(wurf==21) {
                i=(i+1)%2;
                break;
            }
        } while (istHoeher(wurf, letzterWurf));
        write((i==1)?"Gewonnen":"Verloren");
    }

    /**
     *
     * Gibt an ob wurf1 höher ist als wurf2
     *
     * @param wurf1 Wurf1
     * @param wurf2 Wurf2
     * @return Ist wurf1 höher als wurf2 ?
     */
    private static boolean istHoeher(int wurf1, int wurf2) {

        if(wurf1==21 && wurf2!=21) return true;
        if(istPasch(wurf1) && istPasch(wurf2) && wurf1>wurf2) return true;
        if(istPasch(wurf1) && !istPasch(wurf2)) return true;
        if(!istPasch(wurf1) && istPasch(wurf2)) return false;
        return wurf1 > wurf2;
    }

    private static boolean istPasch(int wurf){
        return wurf==66||wurf==55||wurf==44||wurf==33||wurf==22||wurf==11;
    }

    private static int wuerfeln(){

        int i1, i2;
        i1 = dice();
        i2 = dice();

        return ((i1>i2)?i1*10:i2*10)+((i1>i2)?i2:i1);
    }
}
