package Blatt4;

import Libraries.MiniJava;
import javax.annotation.Generated;

@Generated("Ignore Duplicate Code, because of the Files in Abgaben")
public class HA_4p7_Binnenmajuskel extends MiniJava {

  public static void main(String[] args) {

    String[] lA = new String[10];
    String[] sA = new String[10];
    int n = 0;
    String in = " ";
    boolean isOk = true;
    while (in.length() > 0) {

      if (isOk) {
        in = readString("Begriff eingeben: ");
      } else {
        in = readString("Bitte nur Buchstaben eingeben: ");
      }

      for (int i = 0; i < in.length(); i++) {
        isOk = (('A' <= in.charAt(i)) && (in.charAt(i) <= 'Z')) || (('a' <= in.charAt(i)) && (
            in.charAt(i) <= 'z'));

        if (!isOk) {
          break;
        }
      }

      if (isOk) {
        if (n == sA.length - 1) {
          lA = sA;
          sA = new String[sA.length + 10];
          for (int i = 0; i < lA.length; i++) {
            sA[i] = lA[i];
          }
        }
        sA[n++] = in;
      }
    }

    n--;

    for (int i = 0; i < n; i++) {
      String res = "";
      for (int j = 0; j < sA[i].length(); j++) {
        if (sA[i].charAt(j) <= 'Z') {
          res += (char) (sA[i].charAt(j) + ('a' - 'A'));
        } else {
          res += sA[i].charAt(j);
        }
      }
      sA[i] = res;
    }

    //System.out.println(Arrays.toString(sA));

    String startcase = "";
    String uppercase = "";
    String snakecase = "";
    String pascalcase = "";

    if (n > 0) {

      // Startcase
      for (int i = 0; i < sA[0].length(); i++) {
        if (i == 0) {
          startcase += (char) (sA[0].charAt(i) - 'a' + 'A');
        } else {
          startcase += sA[0].charAt(i);
        }
      }
      for (int i = 1; i < n; i++) {
        startcase += sA[i];
      }

      // UPPERCASE
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < sA[i].length(); j++) {
          uppercase += (char) (sA[i].charAt(j) - 'a' + 'A');
        }
      }

      // snake_case
      for (int i = 0; i < n; i++) {
        snakecase += sA[i];
        if (i + 1 < n) {
          snakecase += "_";
        }
      }

      // PascalCase
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < sA[i].length(); j++) {
          if (j == 0) {
            pascalcase += (char) (sA[i].charAt(j) - 'a' + 'A');
          } else {
            pascalcase += sA[i].charAt(j);
          }
        }
      }

    }

    writeLineConsole("Startcase: " + startcase);
    writeLineConsole("UPPERCASE: " + uppercase);
    writeLineConsole("snake_case: " + snakecase);
    writeLineConsole("PascalCase: " + pascalcase);

  }

}
