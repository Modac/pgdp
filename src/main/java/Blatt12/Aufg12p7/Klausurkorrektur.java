package Blatt12.Aufg12p7;

import java.util.concurrent.atomic.AtomicInteger;

public class Klausurkorrektur {

  private Buffer initBuffer;
  private Buffer buffer1;
  private Buffer buffer2;
  private Buffer buffer3;
  private Buffer buffer4;
  private Buffer buffer5;
  private Buffer buffer6;
  private Buffer buffer7;
  private Buffer buffer8;
  private Buffer bufferZweit;

  private AtomicInteger finished;

  private int klausurenCount;

  public Klausurkorrektur(Klausur[] klausuren) {
    if (klausuren.length > 1700) {
      throw new RuntimeException("Zu viele Klausuren");
    }

    initBuffer = new Buffer(1700);
    buffer1 = new Buffer(50);
    buffer2 = new Buffer(50);
    buffer3 = new Buffer(50);
    buffer4 = new Buffer(50);
    buffer5 = new Buffer(50);
    buffer6 = new Buffer(50);
    buffer7 = new Buffer(50);
    buffer8 = new Buffer(1700);
    bufferZweit = new Buffer(1700);

    finished = new AtomicInteger(0);

    for (int i = 0; i < klausuren.length; i++) {
      try {
        initBuffer.produce(klausuren[i]);
      } catch (InterruptedException ignored) { }
    }

    klausurenCount = klausuren.length;
    
  }

  public void startKorrektur() {

    int tC = 0;

    for (int i = 0; i < 8; i++) {

      for (int j = 0; j < 7 /*(i < 3 ? 8 : 7)*/; j++) {
        new Tutor(this, i).start();
        tC++;
      }
      
    }

    System.out.println("Threads: " + tC);

    new Tutor(this, 8).start();
    new Tutor(this, 8).start();
    new Tutor(this, 8).start();
    new Tutor(this, 8).start();

    new Uebungsleitung(this).starten();

    while(!isFinished()) {
      /*
      try {
        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
          System.out.println("Buffer" + i + ": " + getBuffer(i).getNumberOfElements());
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      */
    }

    System.out.println("Korrektur der Info 1 Klausur beendet :)");

  }

  public Buffer getBuffer(int i){
    switch (i) {
      case 1:
        return buffer1;
      case 2:
        return buffer2;
      case 3:
        return buffer3;
      case 4:
        return buffer4;
      case 5:
        return buffer5;
      case 6:
        return buffer6;
      case 7:
        return buffer7;
      case 8:
        return buffer8;
      case 9:
        return bufferZweit;
      default:
        return initBuffer;
    }
  }

  public boolean isFinished(){
    return finished.get() == klausurenCount;
  }

  public void oneFinished(){
    finished.incrementAndGet();
  }
}
