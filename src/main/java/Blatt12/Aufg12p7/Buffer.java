package Blatt12.Aufg12p7;

import java.util.concurrent.Semaphore;

public class Buffer {

  private int cap, first, last;
  private Semaphore free, occupied;
  private Klausur[] a;

  public Buffer(int n) {
    cap = n;
    first = last = 0;
    a = new Klausur[n];
    free = new Semaphore(n);
    occupied = new Semaphore(0);
  }

  public void produce(Klausur d) throws
      InterruptedException {
    free.acquire();
    synchronized (this) {
      a[last] = d;
      last = (last + 1) % cap;
    }
    occupied.release();
  }

  public Klausur consume() throws
      InterruptedException {
    Klausur result;
    occupied.acquire();
    synchronized (this) {
      result = a[first];
      first = (first + 1) % cap;
    }
    free.release();
    return result;
  }

  public int getLength() {
    return cap;
  }

  public synchronized int getNumberOfElements(){
    return Math.abs(last-first);
  }
}
