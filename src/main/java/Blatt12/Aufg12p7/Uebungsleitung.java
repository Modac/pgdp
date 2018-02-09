package Blatt12.Aufg12p7;

import java.util.concurrent.ThreadLocalRandom;

public class Uebungsleitung {

  private Klausurkorrektur klausurkorrektur;

  public Uebungsleitung(Klausurkorrektur klausurkorrektur) {
    this.klausurkorrektur = klausurkorrektur;
  }

  public void starten(){
    new UeLThread(klausurkorrektur).start();
    new UeLThread(klausurkorrektur).start();
  }

  private class UeLThread extends Thread {

    private Klausurkorrektur klausurkorrektur;

    private UeLThread(Klausurkorrektur klausurkorrektur) {
      this.klausurkorrektur = klausurkorrektur;
    }

    @Override
    public void run() {
      while (!klausurkorrektur.isFinished()) {
        try {
          Klausur k = klausurkorrektur.getBuffer(8).consume();

          for (int i = 0; i < 8; i++) {
            int whatToDo = ThreadLocalRandom.current().nextInt(20);

            int korrektur = whatToDo == 0 ?
                -1 :
                whatToDo == 1 ?
                    1 :
                    0
            ;

            int punkte = k.getPunkte()[i];
            punkte = punkte <= 0 ?
                0 :
                punkte >= Korrekturschema.getMaxPunkte(i + 1) ?
                    Korrekturschema.getMaxPunkte(i + 1) :
                    punkte + korrektur
            ;

            k.setZweitkorrektur(i, punkte);
          }

          klausurkorrektur.getBuffer(9).produce(k);

        } catch (InterruptedException e) {
          e.printStackTrace();
        }


      }
    }
  }

}
