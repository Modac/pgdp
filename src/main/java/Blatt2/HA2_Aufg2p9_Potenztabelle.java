package Blatt2;

import Libraries.MiniJava;

public class HA2_Aufg2p9_Potenztabelle extends MiniJava {

    public static void main(String[] args) {
        int n, i, m, x;
        n = readInt("Zahl: ");
        i = 1;
        m = 1;
        x = 1;
        writeLineConsole("\\\\begin{tabular}{lllll}");

        while(i<=n){
            while(m<=n){
                writeConsole(x);
                if(m!=n) {
                    writeConsole(" & ");
                }
                m = m + 1;
                x = x * i;
            }
            writeLineConsole("\\\\");
            i = i + 1;
            m = 1;
            x = 1;
        }

        writeLineConsole("\\\\end{tabular}");
    }
}
