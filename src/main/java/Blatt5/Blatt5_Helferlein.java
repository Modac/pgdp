package Blatt5;

import Libraries.MiniJava;

public class Blatt5_Helferlein extends MiniJava {

  public static void main(String[] args) {

    System.out.println((int)9.7-3.3);
    System.out.println(4/7);
    System.out.println(((3&3)==0));
    System.out.println(true!=false);

    System.out.println("\u001C");

    loop: for (int i = 0; i< 10;i++){
      for (int j = 0; j< 10;j++){
        System.out.println(i + ", " + j + ", " + (i==j?"3":(j=j+1)));
        if (i>7 && i == j) break;
      }
    }

  }
}
