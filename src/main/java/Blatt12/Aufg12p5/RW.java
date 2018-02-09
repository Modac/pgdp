package Blatt12.Aufg12p5;

public class RW {

  private int countReaders = 0;
  private boolean writeWaiting = false;

  public synchronized void startRead()
      throws InterruptedException {
    while (countReaders < 0 || writeWaiting) {
      wait();
    }
    countReaders++;
  }

  public synchronized void endRead() {
    countReaders--;
    if (countReaders == 0) {
      notifyAll();
    }
  }

  public synchronized void startWrite()
      throws InterruptedException {
    while (countReaders != 0) {
      try {
        writeWaiting = true;
        wait();
      } finally {
        writeWaiting = false;
      }
    }
    countReaders = -1;
  }

  public synchronized void endWrite() {
    countReaders = 0;
    notifyAll();
  }
}