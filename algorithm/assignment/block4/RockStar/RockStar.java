package block4.RockStar;

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

  public int getNumSongs(int ff, int fs, int sf, int ss) {
    return 0;
  }

  public static void main(String[] args) {
    RockStar rockStar = new RockStar();
    System.out.println(rockStar.getNumSongs(0, 0, 0, 0));
  }

}
