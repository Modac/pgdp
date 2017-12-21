package Blatt8;

public class Video {

  private String titel;
  private int id;
  private String[] genres;

  private static int count = 0;

  public Video(String titel, int id, String[] genres) {
    this.titel = titel;
    this.id = id;
    this.genres = genres;
  }

  public Video(String titel) {
    this(titel, count++, new String[5]);
  }

  public int addGenre(String genre){
    for (int i = 0; i < genres.length; i++) {
      if(genres[i]==null){
        genres[i] = genre;
        return i+1;
      } else if(genres[i].equals(genre)){
        return -1;
      }
    }
    return -1;
  }

  public String getTitel() {
    return titel;
  }

  public int getId() {
    return id;
  }
  public String[] getGenres() {
    return genres;
  }


}
