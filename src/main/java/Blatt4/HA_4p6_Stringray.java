package Blatt4;

import Libraries.MiniJava;

public class HA_4p6_Stringray extends MiniJava {

  public static void main(String[] args) {

    String text = readString("Gib einen beliebigen Text ein: ");

    int t = 0;
    while (t < 1 || t > 3) {
      t = readInt("Teilaufgabe wählen:\n" +
          "1 = 1. Häufigkeit der Buchstaben\n" +
          "2 = 2. Buchstaben ersetzen\n" +
          "3 = 3. Wortweise spiegeln");
    }

    if (t == 1) {
      teilaufg1(text);
    } else if (t == 2) {
      teilaufg2(text);
    } else if (t == 3) {
      teilaufg3(text);
    }

  }

  private static void teilaufg1(String text) {

    int[] bH = new int[29];

    for (int i = 0; i < text.length(); i++) {
      char charT = text.charAt(i);

      if (('A' <= charT) && (charT <= 'Z')) {   // Großbuchstabe
        bH[charT - 'A']++;
      } else if (('a' <= charT) && (charT <= 'z')) {   // Kleinbuchstabe
        bH[charT - 'a']++;
      } else if (charT == 'Ä' || charT == 'ä') {
        bH[bH.length - 3]++;
      } else if (charT == 'Ö' || charT == 'ö') {
        bH[bH.length - 2]++;
      } else if (charT == 'Ü' || charT == 'ü') {
        bH[bH.length - 1]++;
      }
    }

    for (int i = 0; i < bH.length; i++) {
      if (bH[i] > 0) {
        char buchT = 'A';
        if (i == 26) { // Ä
          buchT = 'Ä';
        } else if (i == 27) { // Ö
          buchT = 'Ö';
        } else if (i == 28) { // Ü
          buchT = 'Ü';
        } else {
          buchT = (char) (i + 'A');
        }
        writeConsole(buchT + ": " + bH[i] + " ");
      }
    }
  }

  private static void teilaufg2(String text) {
    String buchA = readString("Zu ersetzender Buchstabe: ");
    while (buchA.length() != 1 ||
        !((('A' <= buchA.charAt(0)) && (buchA.charAt(0) <= 'Z')) ||
            ('Z' <= buchA.charAt(0)) && (buchA.charAt(0) <= 'z'))) {
      buchA = readString("Bitte nur einen Buchstaben eingeben: ");
    }

    String buchB = readString("Zu setzender Buchstabe: ");
    while (buchB.length() != 1 ||
        !((('A' <= buchB.charAt(0)) && (buchB.charAt(0) <= 'Z')) ||
            ('Z' <= buchB.charAt(0)) && (buchB.charAt(0) <= 'z'))) {
      buchB = readString("Bitte nur einen Buchstaben eingeben: ");
    }

    char charA = buchA.charAt(0);
    char charB = buchB.charAt(0);

    if (charA > 'Z') {
      charA -= 'a' - 'A';
    }

    if (charB > 'Z') {
      charB -= 'a' - 'A';
    }

    String res = "";

    for (int i = 0; i < text.length(); i++) {
      char charT = text.charAt(i);

      if (charT == charA) {
        res += charB;
      } else if (charT == (charA + ('a' - 'A'))) {
        res += (char) (charB + ('a' - 'A'));
      } else {
        res += charT;
      }
    }

    writeLineConsole(res);

  }

  private static void teilaufg3(String text) {
    char charP = 0;
    String rWord = "";
    String res = "";
    for (int i = 0; i < text.length(); i++) {
      char charT = text.charAt(i);
      if ((charT != ' ' && charP == ' ') || (charT != ' ' && charP != ' ')) {
        rWord = charT + rWord;
      } else if (charT == ' ' && charP != ' ') {
        res += rWord + charT;
        rWord = "";
      } else {
        res += charT;
      }

      charP = charT;
    }

    if (rWord != "") {
      res += rWord;
    }

    writeLineConsole(res);

  }
}
