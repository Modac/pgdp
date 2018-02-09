package Blatt12.Aufg12p7;

import org.junit.Test;

public class KlausurkorrekturTest {
  @Test
  public void test0() {
    new Klausurkorrektur(new Klausur[0]).startKorrektur();
  }

  @Test
  public void test1() {
    new Klausurkorrektur(new Klausur[] {new Klausur()}).startKorrektur();
  }

  @Test
  public void test1700() {
    Klausur[] klausuren = new Klausur[1700];
    for (int i = 0; i < 1700; i++)
      klausuren[i] = new Klausur();
    new Klausurkorrektur(klausuren).startKorrektur();
  }

  @Test(expected = Exception.class)
  public void testTooMany() {
    Klausur[] klausuren = new Klausur[1000000];
    for (int i = 0; i < klausuren.length; i++)
      klausuren[i] = new Klausur();
    new Klausurkorrektur(klausuren).startKorrektur();
  }
}