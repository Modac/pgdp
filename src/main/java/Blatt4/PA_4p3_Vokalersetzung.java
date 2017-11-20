package Blatt4;

import MiniJava.MiniJava;

public class PA_4p3_Vokalersetzung extends MiniJava {

  public static void main(String[] args) {
    String text = "Hat der alte Hexenmeister\n" +
        "sich doch einmal wegbegeben!\n" +
        "Und nun sollen seine Geister\n" +
        "auch nach meinem Willen leben.\n" +
        "Seine Wort und Werke\n" +
        "merkt ich und den Brauch,\n" +
        "und mit Geistesstärke\n" +
        "tu ich Wunder auch.\n" +
        "Walle! walle\n" +
        "Manche Strecke,\n" +
        "daß, zum Zwecke,\n" +
        "Wasser fließe\n" +
        "und mit reichem, vollem Schwalle\n" +
        "zu dem Bade sich ergieße.\n" +
        "aAbBeEiIoOuU";

    String vok = readString("Alle Vokale ersetzen durch Vokal: ");
    while (vok.length() != 1 || (
        vok.charAt(0) != 'A' &&
            vok.charAt(0) != 'E' &&
            vok.charAt(0) != 'I' &&
            vok.charAt(0) != 'O' &&
            vok.charAt(0) != 'U' &&
            vok.charAt(0) != 'a' &&
            vok.charAt(0) != 'e' &&
            vok.charAt(0) != 'i' &&
            vok.charAt(0) != 'o' &&
            vok.charAt(0) != 'u')) {
      vok = readString("Bitte nur einen Vokal eingeben: ");
    }

    char vokC = vok.charAt(0);

    if (vokC > 'Z') {
      vokC -= 'a' - 'A';
    }

    String res = "";
    for (int i = 0; i < text.length(); i++) {
      char charT = text.charAt(i);
      if (charT == 'A' || charT == 'E' || charT == 'I' || charT == 'O' || charT == 'U') {
        charT = vokC;
      } else if (charT == 'a' || charT == 'e' || charT == 'i' || charT == 'o' || charT == 'u') {
        charT = (char) (('a' - 'A') + vokC);
      }
      res+=charT;
    }

    writeConsole(res);

  }
}
