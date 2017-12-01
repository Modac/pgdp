package Blatt4;

import Libraries.MiniJava;
import javax.annotation.Generated;

@Generated("Ignore Duplicate Code, because of the Files in Abgaben")
public class HA_4p8_XorVerschluesselungImCBCModus extends MiniJava {

  public static void main(String[] args) {

    int key = -1;
    while (key < 0 || key > 63) {
      key = readInt("Bitte gebe ein Zahl zwischen 0 und 63 als Schlüssel ein: ");
    }

    int iv = -1;
    while (iv < 0 || iv > 63) {
      iv = readInt("Bitte gebe ein Zahl zwischen 0 und 63 als IV ein: ");
    }

    String text = readString("Zu verschlüsselnder Text: ");
    int[] iA = new int[text.length()];

    for (int i = 0; i < text.length(); i++) {
      char charT = text.charAt(i);
      if (('A' <= charT) && (charT <= 'Z')) {   // Großbuchstabe
        iA[i] = charT + (26 - 'A');
      } else if (('a' <= charT) && (charT <= 'z')) {   // Kleinbuchstabe
        iA[i] = charT + (0 - 'a');
      } else if (('0' <= charT) && (charT <= '9')) {   // Zahlen
        iA[i] = charT + (52 - '0');
      } else if (charT == ' ') {   // Leerzeichen
        iA[i] = 62;
      } else if (charT == '.') {   // Punkt
        iA[i] = 63;
      } else {
        write("Text kann nicht verschlüsselt werden!");
        return;
      }
    }

    //System.out.println(Arrays.toString(iA));
    //System.out.println(dekodieren(iA));

    // Verschlüsseln
    int[] vA = new int[iA.length];
    int prev = iv;
    for (int i = 0; i < iA.length; i++) {
      vA[i] = iA[i] ^ prev ^ key;
      prev = vA[i];
    }

    //System.out.println(Arrays.toString(vA));

    write("Verschlüsselter Text:\n" + dekodieren(vA));
    writeLineConsole(dekodieren(vA));

    // Entschlüsseln
    int[] eA = new int[vA.length];
    for (int i = 0; i < vA.length; i++) {
      if (i == 0) {
        eA[i] = vA[i] ^ key ^ iv;
      } else {
        prev = vA[i - 1];
        eA[i] = vA[i] ^ key ^ prev;
      }
    }

    //System.out.println(Arrays.toString(eA));

    write("Entschlüsselter Text:\n" + dekodieren(eA));
    writeLineConsole(dekodieren(eA));

  }


  public static String dekodieren(int[] iA) {
    String res = "";
    for (int i = 0; i < iA.length; i++) {
      int charT = iA[i];
      if ((26 <= charT) && (charT <= 51)) {   // Großbuchstabe
        res += (char) (charT + ('A' - 26));
      } else if ((0 <= charT) && (charT <= 25)) {   // Kleinbuchstabe
        res += (char) (charT + ('a' - 0));
      } else if ((52 <= charT) && (charT <= 61)) {   // Zahlen
        res += (char) (charT + ('0' - 52));
      } else if (charT == 62) {   // Leerzeichen
        res += ' ';
      } else if (charT == 63) {   // Punkt
        res += '.';
      } else {
        return null;
      }
    }
    return res;

  }

}
