package Blatt8;

import java.util.Arrays;

public class Worstorage {

  private Penguin[] store;
  private int[] count;

  public Worstorage(Penguin[] penguins) {
    store = penguins;
    count = new int[getLevel(store.length + 1)];
  }

  private int getLevel(int index) {
    return (int) (Math.log(index) / Math.log(2));
  }

  public Worstorage() {
    this(new Penguin[1]);
  }

  public void add(Penguin penguin) {


    int p = 1;
    for (int i = 0; i <= count.length; i++) {
      //System.out.println("p: " + p);

      // Wenn eine neue Ebene hinzugefügt werden muss
      if (p > store.length) {
        expandStorage();
      }


      if (store[p - 1] == null) {
        store[p - 1] = penguin;
        count[i]++;
      }

      // Wenn schon vorhanden
      if (penguin.compareTo(store[p - 1]) == 0) {
        return;
      }
      // Wenn der neue Pinguin knuddeliger ist, gehe in den rechten Teilbaum
      else if (penguin.compareTo(store[p - 1]) > 0) {
        p = 2 * p + 1;
      }
      // Sonst gehe in den linken
      else {
        p = 2 * p;
      }
    }
  }

  private void expandStorage() {
    //System.out.println("Expanding");
    store = Arrays.copyOf(store, 2 * store.length + 1);
    count = Arrays.copyOf(count, count.length + 1);
  }

  public boolean find(Penguin penguin) {
    int p = 1;
    for (int aCount : count) {

      if (store[p - 1] == null || p > store.length) {
        return false;
      }

      // Fast genau so wie in add()
      if (penguin == store[p - 1]) {
        return true;
      } else if (penguin.compareTo(store[p - 1]) > 0) {
        p = 2 * p + 1;
      } else {
        p = 2 * p;
      }
    }
    return false;
  }

  public void remove(Penguin penguin) {
    int p = 1;
    for (int aCount : count) {

      if (store[p - 1] == null || p > store.length) {
        return;
      }

      if (penguin == store[p - 1]) {
        break;
      } else if (penguin.compareTo(store[p - 1]) > 0) {
        p = 2 * p + 1;
      } else {
        p = 2 * p;
      }
    }

    remove(p);

  }

  private void remove(int position) {
    int p = position;

    // Wenn das zu löschende Element keine Nachfolger hat
    // dann setzte es einfach gleich null
    if ((2*p > store.length || store[2 * p - 1] == null) && (2*p+1 > store.length || store[2 * p] == null)) {
      store[p - 1] = null;
    }

    // Wenn es einen Nachfolger hat
    // dann verschiebe den nachfolgenden Teilbaum an die Stelle von dem Element
    else if (store[2 * p - 1] != null ^ store[2 * p] != null) {
      int n = 2 * p;
      if (store[2 * p] != null) {
        n = 2 * p + 1;
      }
      moveSubtree(n, p);
    }

    // Wenn es zwei Nachfolger hat
    // dann suche nach dem größten Element im linken Teilbaum,
    // ersetzte das zu löschende Element damit und lösche es anschließend selbst
    //
    // (Da es maximal einen weiteren Nachfolger hat, da es sonst nicht das größte Element wäre,
    // ist das kein weiteres Problem)
    else {

      int n = 2*p;

      int max = n;
      for (int i = 0; i < count.length; i++) {

        if (n > store.length || store[n - 1] == null) {
          break;
        }

        max = n;

        n = 2*n+1;
        //System.out.println("Next pos min: " + n);

      }

      //System.out.println("Replace " + p + " with " + max);

      store[p-1] = store[max-1];

      remove(max);

    }

    // System.out.println(Arrays.toString(count));

    updateCount();

    reduceStorage();

  }

  private void updateCount() {

    count = new int[count.length];

    for (int i = 0; i < store.length; i++) {
      if(store[i]!=null){
        count[getLevel(i+1)]++;
      }
    }

  }

  private void reduceStorage() {
    if (count[count.length - 1] > 0) {
      return;
    }

    store = Arrays.copyOf(store, (store.length - 1) / 2);
    count = Arrays.copyOf(count, count.length - 1);
  }

  private void moveSubtree(int from, int to) {

    //System.out.println(from + " -> " + to);

    boolean isLeft = from % 2 == 0;

    // store[to - 1] = store[from - 1];

    // Wird benötigt um später einen relativen Index im Teilbaum zu einem absoluten Index
    // im Bezug auf den ganzen Baum umzurechnen
    int pDis = to - 1;

    // Level
    int e = 0;
    for (to = 1; (e = getLevel(to)) < count.length; to++) {

      // Das Element welches das an Index 'to' ersetzt liegt entweder am Index to + 2^e
      // oder to + 2^(e+1)
      if (isLeft) {
        from = (int) (to + Math.pow(2, e));
      } else {
        from = (int) (to + Math.pow(2, e + 1));
      }

      // Das ersetzende Element ist immer eine Ebene höher
      int fromI = subTreeIndexToArrayIndex(from, pDis, e + 1);

      int toI = subTreeIndexToArrayIndex(to, pDis, e);

      /*
      System.out.println("To: " + to);
      System.out.println("ToI: " + (toI + 1));
      System.out.println("From: " + from);
      System.out.println("FromI: " + (fromI + 1));
      System.out.println("pDis: " + pDis);
      System.out.println("Level: " + e);
      */

      // Wenn fromI einmal ausserhalb des Arrays liegt wissen wir,
      // dass alle darauf folgenden fromI's in mindestens der gleichen Ebene liegen werden.
      // Und da diese Ebene nicht existiert können wir auch gleich aufhören
      if (fromI >= store.length) {
        return;
      }

      store[toI] = store[fromI];
      store[fromI] = null;

    }

  }

  //         1
  //      2    3
  //   4   5 6   7
  //
  //   3    ->    1       3-1 = 2 = (3-1)*(2^0)
  // 6   7  ->  2   3     6-2 = 4, 7-3 = 4  = (3-1)*(2^1)
  //
  //   2    ->    1       2-1 = 1 = (2-1)*(2^0)
  // 4   5  ->  2   3     4-2 = 2, 5-3 = 2  = (2-1)*(2^1)
  //
  // allgemein:     Index innerhalb des gesamten Baums =
  //    Index innerhalb des Teilbaums  + (Index der Wurzel des Teilbaums - 1) * (2 ^ Ebene des Index im Teilbaum)
  private int subTreeIndexToArrayIndex(int subTreeIndex, int subTreeRootIndex, int level) {

    return (subTreeIndex + subTreeRootIndex * (int) Math.pow(2, level)) - 1;
  }

  public int getLevels() {
    return count.length;
  }


  @Override
  public String toString() {
    String res = "";
    for (Penguin p : store) {
      res += (p == null ? "" : p.getCuddliness()) + ",";
    }
    return res;
  }

  // Genau so wie toString() nur wird der Baum in In-Order traversiert und nicht in Level-Order
  public String toStringInorder() {
    return toStringInorder(1);
  }

  private String toStringInorder(int i) {
    String res = "";
    res += (2 * i > store.length || store[2 * i - 1] == null) ? "" : (toStringInorder(2 * i));
    res += store[i - 1] == null ? "" : (store[i - 1].getCuddliness() + ",");
    res += (2 * i + 1 > store.length || store[2 * i] == null) ? "" : (toStringInorder(2 * i + 1));
    return res;
  }

  // Ein sehr sporadischer versuch den Baum bäumisch darzustellen
  // Klappt naja bei Werten mit nur einer Stelle
  // Klappt noch schlechter bei großen Bäumen und mit Werten mit mehreren Stellen
  public void printTree() {
    int prevL = -1;
    for (int i = 0; i < store.length; i++) {
      if (prevL != getLevel(i + 1)) {
        prevL = getLevel(i + 1);
        System.out.println("");
        System.out.print(Test.stringNTimes(" ",
            ((int) Math.pow(getLevels(), 2) - (int) Math.pow(getLevel(i + 1), 2)) / 2));
      }
      System.out.print(store[i] == null ? "x" : store[i].getCuddliness());
      // System.out.print(" (L:" + getLevel(i+1) + ") ");
      System.out.print(" ");
    }
    System.out.println("");
  }


  // Zum Testen (Code sollte außerhalb der zu testenden Klasse liegen)
  public static void main(String[] args) {
    Test.main();
  }
}

class Penguin implements Comparable<Penguin> {

  private int cuddliness;

  public Penguin(int cuddliness) {
    this.cuddliness = cuddliness;
  }

  public int getCuddliness() {
    return this.cuddliness;
  }

  public void setCuddliness(int cuddliness) {
    this.cuddliness = cuddliness;
  }

  // Note: this class has a natural ordering that is inconsistent with equals.
  public int compareTo(Penguin other) {
    int oc = other.cuddliness;
    if (cuddliness < oc) {
      return -1;
    }
    if (cuddliness > oc) {
      return 1;
    }
    return 0;
  }
}

class Test {

  public static void main() {
    System.out.println("Hier passiert was.");
    Worstorage ws = new Worstorage();

    Penguin[] pens = new Penguin[100];

    for (int i = 0; i < pens.length; i++) {
      pens[i] = new Penguin(i);
    }

    ws.add(pens[3]);
    System.out.println(ws.toString());

    ws.add(pens[2]);
    System.out.println(ws.toString());

    ws.add(pens[1]);
    System.out.println(ws.toString());

    ws.add(pens[5]);
    System.out.println(ws.toString());

    ws.remove(pens[3]);
    System.out.println(ws.toString());

    ws.add(pens[4]);
    System.out.println(ws.toString());

    ws.add(pens[8]);
    System.out.println(ws.toString());

    ws.add(pens[3]);
    System.out.println(ws.toString());

    ws.remove(pens[5]);
    System.out.println(ws.toString());

    ws.add(pens[7]);
    System.out.println(ws.toString());

    ws.printTree();


    /*
    Penguin[] storeT = new Penguin[((int) (Math.pow(2, 5)) - 1)];
    storeT[0] = pens[2];
    storeT[1] = pens[1];
    storeT[2] = pens[20];
    storeT[6] = pens[30];
    storeT[13] = pens[25];
    storeT[14] = pens[35];
    storeT[27] = pens[22];

    ws = new Worstorage(storeT);

    System.out.println(stringNTimes("-", (int) Math.pow(2, ws.getLevels() - 1) * 2));
    ws.printTree();

    System.out.println(ws.toStringInorder());

    ws.remove(pens[20]);
    ws.printTree();

    System.out.println(ws.toStringInorder());
    */
  }

  public static String stringNTimes(String str, int n) {
    StringBuilder res = new StringBuilder();

    for (int i = 0; i < n; i++) {
      res.append(str);
    }

    return res.toString();
  }

}
