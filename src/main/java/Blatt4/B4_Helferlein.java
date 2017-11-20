package Blatt4;

import MiniJava.MiniJava;

public class B4_Helferlein extends MiniJava {

  public static void main(String[] args) {
    printHex(0xFFf);
    //System.out.println(calcFromBase2To10(354347357));
  }

  public static String calcFromBase2To10(int base10) {
    String res = "";
    int r = 0;
    int n = 0;
    int first = String.valueOf(base10).length() + 4;
    int second = first + String.valueOf(base10 / 2).length() + 1;
    while (base10 >= 1) {
      r = base10 % 2;
      System.out.print(base10 + "\\2");
      n = String.valueOf(base10).length() + 4;
      while (n <= first) {
        System.out.print(" ");
        n++;
      }
      System.out.print(" = " + (base10 = base10 / 2));
      n += String.valueOf(base10).length();
      while (n <= second) {
        System.out.print(" ");
        n++;
      }
      System.out.println("Rest " + r);
      res = r + res;
    }
    return res;
  }

  public static void printHex(int until) {
    int max=Integer.toHexString(until).length()+2;
    System.out.print(" ");
    for (int i = 0; i <= until; i++) {
      System.out.print(Integer.toHexString(i));
      for (int j  = Integer.toHexString(i).length(); j<=max; j++){
        System.out.print(" ");
      }
      if((i+1)%16==0){
        System.out.println();
      }
    }
  }
}
