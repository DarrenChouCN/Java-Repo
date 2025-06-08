package topcoder.graph.dfs;

/*
 RockStar

  Inspired by the Dire Straits song, "Money for Nothing", you have decided to become a rock star. After a lengthy recording session, you have acquired:
    a total of ff songs that start fast and end fast,
    a total of fs songs that start fast and end slow,
    a total of sf songs that start slow and end fast, and
    a total of ss songs that start slow and end slow.

  It remains only to determine which of these songs should go on your first album, and in what order they should appear. Of course, no song can appear on the album more than once.

  Unfortunately, your record company has placed several restrictions on your album:
    1. A song that ends fast may only be immediately followed by a song that starts fast.
    2. A song that ends slow may only be immediately followed by a song that starts slow.
    3. If you have at least one song that starts fast, then the first song on the album must start fast. Otherwise, this restriction can be ignored.

  At this stage in your artistic career, you must do what your record company has ordered, but you do want to place as many songs as possible on the album.

  Given ints ff, fs, sf, and ss, representing the quantities described above, return the maximum number of songs that can be placed on a single album without violating the record company's restrictions.
 */
public class RockStar {

  private int maxSongs = 0;

  public int getNumSongs(int ff, int fs, int sf, int ss) {
    if (ff > 0)
      dfs(ff - 1, fs, sf, ss, 'f', 1);
    if (fs > 0)
      dfs(ff, fs - 1, sf, ss, 's', 1);

    if (ff == 0 && fs == 0) {
      if (sf > 0)
        dfs(ff, fs, sf - 1, ss, 'f', 1);
      if (ss > 0)
        dfs(ff, fs, sf, ss - 1, 's', 1);
    }

    return maxSongs;
  }

  private void dfs(int ff, int fs, int sf, int ss, char lastCh, int length) {

    maxSongs = Math.max(maxSongs, length);

    if (lastCh == 'f') {
      if (ff > 0)
        dfs(ff - 1, fs, sf, ss, 'f', length + 1);

      if (fs > 0)
        dfs(ff, fs - 1, sf, ss, 's', length + 1);

    } else {
      if (sf > 0)
        dfs(ff, fs, sf - 1, ss, 'f', length + 1);

      if (ss > 0)
        dfs(ff, fs, sf, ss - 1, 's', length + 1);
    }
  }

  public static void main(String[] args) {
    RockStar rockStar = new RockStar();
    System.out.println(rockStar.getNumSongs(0, 0, 0, 0));
  }

}
