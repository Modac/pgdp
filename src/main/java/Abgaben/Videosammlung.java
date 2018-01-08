import java.util.Arrays;

public class Videosammlung {

  private Video[] videos;
  private int verbleibende;

  public Videosammlung(int n) {
    this.videos = new Video[n];
    verbleibende = n;
  }

  public int addVideo(Video v) {
    for (int i = 0; i < videos.length; i++) {
      if (videos[i] == null) {
        videos[i] = v;
        verbleibende--;
        return i;
      }
    }
    return -1;
  }

  public Video verkaufen(int index) {
    if (index < 0 || index >= videos.length) {
      return null;
    }
    Video v = videos[index];
    if (v != null) {
      videos[index] = null;
      verbleibende++;
    }
    return v;
  }

  public Video verkaufen(String titel) {
    for (int i = 0; i < videos.length; i++) {
      if (videos[i] != null && videos[i].getTitel().equals(titel)) {
        Video v = videos[i];
        videos[i] = null;
        verbleibende++;
        return v;
      }
    }
    return null;
  }

  public String[] videosInGenre(String genre) {
    String[] videoTitles = new String[videos.length - verbleibende];
    int videoN = 0;

    for (int i = 0; i < videos.length; i++) {
      if (videos[i] == null) {
        continue;
      }
      String[] genres = videos[i].getGenres();
      for (int j = 0; j < genres.length; j++) {
        if (genres[j] != null && genres[j].equals(genre)) {
          videoTitles[videoN++] = videos[i].getTitel();
        }
      }
    }

    return Arrays.copyOf(videoTitles, videoN);
  }

  public int getVerbleibende() {
    return verbleibende;
  }

  public Video[] getVideos() {
    return videos;
  }
}
