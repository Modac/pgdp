package Blatt12.Aufg12p7;

public class Tutor extends Thread{

  private Klausurkorrektur klausurkorrektur;
  private int mode;

  public Tutor(Klausurkorrektur klausurkorrektur, int mode) {
    this.klausurkorrektur = klausurkorrektur;
    this.mode = mode < 0 ?
        0 :
        mode > 8 ?
            8 :
            mode;
  }

  @Override
  public void run() {
    while(!klausurkorrektur.isFinished()){
      try {
        //System.out.println("Getting Klausur from Buffer " + (mode==8?9:mode));
        Klausur k = klausurkorrektur.getBuffer(mode==8?9:mode).consume();
        //System.out.println("Got Klausur from Buffer " + (mode==8?9:mode));


        if(mode == 8){
          //System.out.println("Zähle Punkte der Klausur von " + k.getNachname() + ", " + k.getVorname());
          k.setGesamtpunktzahl(0);
          for (int i = 0; i < 8; i++) {
            k.setGesamtpunktzahl(k.getGesamtpunktzahl()+k.getZweitkorrektur()[i]);
          }
          k.setNote(Korrekturschema.note(k.getGesamtpunktzahl()));
          klausurkorrektur.oneFinished();
          System.out.println(k.toString());
        } else {
          //System.out.println("Korrigiere Aufgabe " + (mode+1) + " der Klausur von " + k.getNachname() + ", " + k.getVorname());
          k.setPunkte(mode, Korrekturschema.punkte(mode+1, k.getAntwort(mode)));
          klausurkorrektur.getBuffer(mode+1).produce(k);
        }


      } catch (InterruptedException e) {
        e.printStackTrace();
      }


    }
    System.out.println(Thread.currentThread().getName() + " beendet");
  }
}
