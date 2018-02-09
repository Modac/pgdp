package Blatt10.Aufg10p6;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class Seerettung {

  private static PriorityQueue<Eisscholle> nachbarschollen;

  public static List<Eisscholle> findeWeg(
      Eisscholle[] eisschollen,
      List<Seeweg> seewege,
      int startIndex,
      int endIndex) {
    nachbarschollen = new PriorityQueue<>(new EisschollenComparator());

    // Kann weggelassen werden, ist zum debuggen aber ganz hilfreich
    // Set<Eisscholle> bekannt = new HashSet<>(eisschollen.length);

    for (Eisscholle e : eisschollen) {
      e.reset();
    }

    Set<Eisscholle> vermutet = new HashSet<>(eisschollen.length);

    eisschollen[startIndex].setDistance(0);
    eisschollen[startIndex].setState(Eisscholle.VERMUTET);
    vermutet.add(eisschollen[startIndex]);

    nachbarschollen.add(eisschollen[startIndex]);

    //while (vermutet.size() > 0) {
    while (nachbarschollen.size() > 0) {

      // System.out.println(vermutet.toString());
      // System.out.println(bekannt.toString());

     // nachbarschollen.clear();
      //nachbarschollen.addAll(vermutet);
      Eisscholle e = nachbarschollen.poll();

      // System.out.println(e.toString());

      //vermutet.remove(e);

      e.setState(Eisscholle.BEKANNT);

      // DEBUG
      // bekannt.add(e);

      findSeewegeFromEisscholle(seewege, e).forEach(sw -> {
        // Für jede Nachfolger-Eisscholle überprüfe ob die neue Distanz kleiner ist als die alte
        if (e.getDistance() + sw.getDistance() < sw.getTo().getDistance()) {
          sw.getTo().setVorgaenger(e);
          sw.getTo().setDistance(e.getDistance() + sw.getDistance());
          if (nachbarschollen.contains(sw.getTo())){
            nachbarschollen.remove(sw.getTo());
            nachbarschollen.add(sw.getTo());
          }
        }

        // Jede nicht bekannte Eisscholle in die Menge der Vermuteten
        if (sw.getTo().getState() != Eisscholle.BEKANNT) {
          sw.getTo().setState(Eisscholle.VERMUTET);
          //vermutet.add(sw.getTo());
          nachbarschollen.add(sw.getTo());
        }
      });

      // System.out.println("Ende While:");
      // System.out.println(vermutet.toString());
      // System.out.println(bekannt.toString());

    }

    List<Eisscholle> res = new LinkedList<>();

    Eisscholle t = eisschollen[endIndex];

    // Rückwärts die Vorgänger ab Ziel-Eisscholle ablaufen
    while (t != null) {
      res.add(t);
      t = t.getVorgaenger();
    }

    // Reihenfolge umdrehen, weil Ziel-Eisscholle an erster Stelle und Anfangs-Eisscholle an Letzter
    Collections.reverse(res);

    // System.out.println("\nRes:");
    // System.out.println(res.toString());

    return res;
  }

  private static List<Seeweg> findSeewegeFromEisscholle(List<Seeweg> seewege,
      Eisscholle eisscholle) {
    return seewege.stream()
        .filter(o -> o.getFrom().equals(eisscholle))
        .collect(Collectors.toList());
  }
}
