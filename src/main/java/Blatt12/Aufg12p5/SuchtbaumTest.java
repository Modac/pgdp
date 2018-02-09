package Blatt12.Aufg12p5;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class SuchtbaumTest {
  private Random random = new Random();
  
  @Test
  public void testContains() throws InterruptedException {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 100;
    for (int i = 0; i < n; i++)
      testSet.add(random.nextInt(20*n));
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for(Integer i : testSet)
      suchti.insert(i);
    System.out.println(suchti.toString());
    for (int i = 0; i < 20*n; i++)
      assertEquals(testSet.contains(i), suchti.contains(i));
  }
  
  @Test
  public void testContainsRemove() throws InterruptedException {
    HashSet<Integer> testSet = new HashSet<>();
    int n = 10000;
    for (int i = 0; i < n; i++)
      testSet.add(random.nextInt(20*n));
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    for(Integer i : testSet)
      suchti.insert(i);
    for (int i = 0; i < n; i++) {
      int next = random.nextInt(20*n);
      if(testSet.contains(next)) {
        suchti.remove(next);
        testSet.remove(next);
      }
    }
    for (int i = 0; i < 20*n; i++)
      assertEquals(testSet.contains(i), suchti.contains(i));
  }

  @Test
  public void testToString() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    System.out.println(suchtI.toString());

  }

  private void fillTree(Suchtbaum<Integer> suchtI) throws InterruptedException {
    suchtI.insert(33);

    suchtI.insert(5);
    suchtI.insert(77);

    suchtI.insert(9);
    suchtI.insert(76);
    suchtI.insert(85);

    suchtI.insert(17);
    suchtI.insert(73);
    suchtI.insert(84);

    suchtI.insert(39);
  }

  @Test(expected = java.lang.RuntimeException.class)
  public void testRemoveEmpty() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    suchtI.remove(3);

  }

  @Test
  public void testRemoveOnlyRoot() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    suchtI.insert(3);

    suchtI.remove(3);

    assertEquals(suchtI.getRoot(), null);

  }

  @Test
  public void testRemoveNoChild() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    fillTree(suchtI);

    suchtI.remove(39);

    System.out.println(suchtI.toString());

    assertEquals(suchtI.contains(39), false);
  }

  @Test
  public void testRemoveRightChild() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    fillTree(suchtI);

    suchtI.remove(9);

    System.out.println(suchtI.toString());

    assertEquals(suchtI.contains(9), false);
  }

  @Test
  public void testRemoveLeftChild() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    fillTree(suchtI);

    suchtI.remove(76);

    System.out.println(suchtI.toString());

    assertEquals(suchtI.contains(76), false);
  }

  @Test
  public void testRemoveTwoChildren() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    fillTree(suchtI);

    suchtI.remove(77);

    System.out.println(suchtI.toString());

    assertEquals(suchtI.contains(77), false);
  }

  @Test
  public void testRemoveTwoChildrenRoot() throws InterruptedException {
    Suchtbaum<Integer> suchtI = new Suchtbaum<>();

    fillTree(suchtI);

    suchtI.remove(33);

    System.out.println(suchtI.toString());

    assertEquals(suchtI.contains(33), false);
  }

  @Test
  public void testRemoveSize() throws InterruptedException {
    int n = 1000;
    Suchtbaum<Integer> suchti = new Suchtbaum<>();
    List<Integer> liste = new ArrayList<>();
    for(int i = 0; i < n; i++) {
      liste.add(new Integer(i));
    }
    Collections.shuffle(liste);

    for(Integer i: liste) {
      suchti.insert(i);
    }

    System.out.println(suchti.toString());

    int count = 0;
    for(int i = 0; i < n; i++) {
      if(Math.random() < 0.5) {
        //System.out.println(suchti.toString());
        suchti.remove(new Integer(i));
        count++;
      }
    }

    int sizeCount = 0;

    for(int i = 0; i < n; i++) {
      if(suchti.contains(i))
        sizeCount++;
    }
    System.out.println("SizeCount: " + sizeCount);
    assertEquals(sizeCount, n - count);
  }
}
