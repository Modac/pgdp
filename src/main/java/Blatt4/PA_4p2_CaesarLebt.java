package Blatt4;

import Libraries.MiniJava;

public class PA_4p2_CaesarLebt extends MiniJava {

  public static void main(String[] args) {
    String text = readString("Text zum verschlüsseln: ");

    int shift = readInt("Shift der Verschlüsselung: ");

    String res = "";

    for (int i = 0; i < text.length(); i++) {
      char charT = text.charAt(i);
      char lowerB = charT;
      char upperB = charT;
      if (('A' <= charT) && (charT <= 'Z')) {   // Großbuchstabe
        lowerB = 'A';
        upperB = 'Z';
      } else if (('a' <= charT) && (charT <= 'z')) { // Kleinbuchstabe
        lowerB = 'a';
        upperB = 'z';
      }

      if (shift >= 0) {
        int shiftC = shift % (upperB - lowerB + 1);
        if (shiftC > (upperB - charT)) {
          charT += (shiftC - (upperB - lowerB + 1));
        } else {
          charT += shiftC;
        }
      } else {
        int shiftC = -((-shift) % (upperB - lowerB + 1));
        if (shiftC < (lowerB - charT)) {
          charT += (upperB - lowerB + 1) + shiftC;
        } else {
          charT += shiftC;
        }
      }

      res += charT;
    }

    writeLineConsole(res);
    write(res);

  }

}
