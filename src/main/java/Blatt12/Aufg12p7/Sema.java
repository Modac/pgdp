package Blatt12.Aufg12p7;

public class Sema {

  private int x;

  public Sema(int n) {
    x = n;
  }

  public synchronized void up() {
    x++;
    if (x <= 0) {
      System.out.println("nOTIFY");
      this.notify();
    }
  }

  public synchronized void down()
      throws InterruptedException {
    x--;
    while (x < 0) {
      System.out.println("Waiting" + Thread.currentThread().getName());
      this.wait();
      System.out.println("Wakeup" + Thread.currentThread().getName());
    }
  }
}
