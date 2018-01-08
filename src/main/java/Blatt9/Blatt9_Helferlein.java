package Blatt9;

import java.util.HashMap;
import java.util.Map;

public class Blatt9_Helferlein {

  public static void main(String[] args) {

    Map<String, Integer> map = new HashMap<>();

    map.put("Test", 3);

    System.out.println(map.get("Test"));

    System.out.println((Object)"Hallp" instanceof String);

    System.out.println(~-1);
  }

}
