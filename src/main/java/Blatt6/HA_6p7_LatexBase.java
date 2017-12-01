package Blatt6;

import Libraries.MiniJava;
import javax.annotation.Generated;

@Generated("Ignore Duplicate Code, because of the Files in Abgaben")
public class HA_6p7_LatexBase extends MiniJava {

  public static void main(String[] args) {

    int[] a = readNumber();
    int[] b = readNumber();

    int base = readInt("Basis eingeben (zwischen 2 und 36)");
    while (base < 2 || base > 36 || base < minBase(a) || base < minBase(b)) {
      base = readInt(
          "Bitte nur Basen zwischen 2 und 36 eingeben und darauf achten, dass die Basis mindestens "
              + (minBase(a) > minBase(b) ? minBase(a) : minBase(b)) + " ist.");
    }

    writeLineConsole("\\begin{tabular}{" + appendStringNTimes("c", a.length + b.length + 2) + "}");

    writeConsole("&" + toLatexString(a, " & "));
    writeConsole(" &  $\\ast$  & ");
    writeConsole(toLatexString(b, " & "));
    writeLineConsole("\\\\");

    writeLineConsole("\\hline");

    int[] res = mul(a, b, base);
    writeLineConsole("\\hline");

    writeConsole("=" + appendStringNTimes("&", a.length + b.length + 2 - res.length) + " ");
    writeConsole(toLatexString(res, "& "));
    writeLineConsole("\\\\");

    writeLineConsole("\\end{tabular}");
    //test();
  }

/*
  public static void test() {

    int[] tN = readNumber();
    System.out.println(Arrays.toString(tN));
    System.out.println(toString(tN));
    System.out.println(toString(add(tN, readNumber(), 16)));

    System.out.println(toString(add(readNumber(), readNumber(), 16)));

    int[] test = {0, 8, 9, 3, 2, 1};
    int[] testT = test;
    for (int i = 1; i < 16; i++) {
      test = add(test, testT, 16);
    }
    System.out.println("---" + toString(test));

    int[] test2 = new int[]{8, 9, 3, 2, 1};
    System.out.println(mulDigit(test2, 1, 2, 16).length);
    System.out.println(toString(test2));

    System.out.println(toString(mul(test2, new int[]{9, 8, 1}, 16)));


  }
*/

  // string = "1", n = 4, Ergebnis = "1111"
  public static String appendStringNTimes(String string, int n) {
    String res = "";
    for (int i = 0; i < n; i++) {
      res += string;
    }
    return res;
  }

  // wie toString() nur zwischen den Ziffern kann noch ein String eingefügt werden
  public static String toLatexString(int[] number, String infix) {
    String res = "";
    char[] nC = toCharArray(number);
    for (int i = 0; i < nC.length; i++) {
      if (i != 0) {
        res += infix;
      }
      res += nC[i];
    }
    return res;
  }

  // Bestimmt die minimale Base in der die Zahl existieren kann
  public static int minBase(int[] number) {
    int res = 0;
    for (int i = 0; i < number.length; i++) {
      if (number[i] > res) {
        res = number[i];
      }
    }
    return res + 1;
  }

  public static int[] readNumber() {
    String number = "";
    boolean isWrong = true;
    int[] res = new int[0];

    while (isWrong) {
      isWrong = false;
      number = readString("Zahl eingeben (Ziffern: 0-9, A-Z)");
      res = new int[number.length()];

      for (int i = number.length() - 1; i >= 0; i--) {
        char tC = number.charAt(number.length() - 1 - i);
        if (('A' <= tC) && (tC <= 'Z')) {   // Großbuchstabe
          res[i] = tC - 'A' + 10;
        } else if (('0' <= tC) && (tC <= '9')) {
          res[i] = tC - '0';
        } else {
          isWrong = true;
        }
      }
    }
    return res;
  }

  public static String toString(int[] number) {
    String res = "";
    for (int i = number.length - 1; i >= 0; i--) {
      if (number[i] > 9) {
        res += (char) (number[i] - 10 + 'A');
      } else {
        res += (char) (number[i] + '0');
      }
    }
    return res;
  }

  // wie toString() nur werden die Ziffern einzeln in einem char-Array gespeichert
  // also eigtl so wie String.toCharArray()
  public static char[] toCharArray(int[] number) {
    char[] res = new char[number.length];
    for (int i = number.length - 1; i >= 0; i--) {
      if (number[i] > 9) {
        res[res.length - 1 - i] = (char) (number[i] - 10 + 'A');
      } else {
        res[res.length - 1 - i] = (char) (number[i] + '0');
      }
    }
    return res;
  }

  public static int[] add(int[] a, int[] b, int base) {

    int[] resT = new int[((a.length > b.length) ? a.length : b.length) + 5];
    for (int i = 0; i < resT.length; i++) {
      resT[i] = -1;
    }

    int c = 0;
    for (int i = 0; i < resT.length; i++) {
      int aD = i < a.length ? a[i] : 0;
      int bD = i < b.length ? b[i] : 0;

      // Wenn die Zahlen 'zu Ende' sind und es keinen Übertrag mehr gibt, dann beende die Addition
      if ((i >= a.length) && (i >= b.length) && c == 0) {
        break;
      }

      // Addition aufgeteilt in einzelne Inkremente für korrekte Überträge in anderen Basen
      int rS = c;
      c = 0;
      for (int j = 0; j < aD + bD; j++) {
        rS++;
        if (rS == base) {
          rS = 0;
          c++;
        }
      }
      resT[i] = rS;
    }

    // Bestimme wie lang das Ergebnis wirklich ist
    int n = 0;
    for (int i = 0; i < resT.length; i++) {
      if (resT[i] >= 0) {
        n++;
      }
    }

    int[] res = new int[n];
    for (int i = 0; i < res.length; i++) {
      res[i] = resT[i];
    }

    return res;

  }

  public static int[] mulDigit(int[] a, int digit, int shift, int base) {
    int[] res = a;

    // Multipliziere einmal mit digit und shift-mal mit base
    for (int i = 0; i < shift + 1; i++) {
      int factor = base;
      if (i == 0) {
        factor = digit;
      }

      // Multiplikation aufgeteilt in einzelne Additionen
      int[] resT = res;
      for (int j = 1; j < factor; j++) {
        res = add(res, resT, base);
      }
    }
    return res;

  }

  public static int[] mul(int[] a, int[] b, int base) {
    int[] res = new int[0];
    for (int i = 0; i < b.length; i++) {
      int[] t = mulDigit(a, b[i], i, base);

      writeConsole("+" + appendStringNTimes("&", a.length + b.length + 2 - t.length) + " ");
      writeConsole(toLatexString(t, "& "));
      writeLineConsole("\\\\");

      res = add(res, t, base);
    }
    return res;
  }
}
