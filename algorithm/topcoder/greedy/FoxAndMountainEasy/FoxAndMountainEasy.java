package topcoder.greedy.FoxAndMountainEasy;

/*
FoxAndMountainEasy
  Fox Ciel lives in a beautiful countryside. She loves climbing mountains. Yesterday, she went hiking in the mountains.

  Her trip can be described as a sequence of (n+1) integers: h[0], h[1], ..., h[n]. These values represent altitudes visited by Fox Ciel during the trip, in order. Fox Ciel does not remember the precise sequence, but she remembers the following:
    for each i, h[i] >= 0
    h[0] = h0
    h[n] = hn
    for each i, abs(h[i+1]-h[i]) = 1

  The last condition means that in each step the altitude of Fox Ciel either increased by 1, or decreased by 1. We will call the two types of steps "steps up" and "steps down", respectively. Steps up will be denoted 'U' and steps down will be denoted 'D'.

  You are given the ints n, h0, and hn: the length of the trip, the altitude at the beginning, and the altitude at the end. In addition to these, Fox Ciel remembers some contiguous segment of her trip. You are given this segment as a String history. Each character of history is either 'U' or 'D'.

  Check whether there is a valid trip that matches everything Fox Ciel remembers. Return "YES" (quotes for clarity) if there is at least one such trip, or "NO" if there is none.
 */
public class FoxAndMountainEasy {

  public String possible(int n, int h0, int hn, String history) {
    int m = history.length();

    int delta = 0;
    int rel = 0, minRel = 0;
    for (char ch : history.toCharArray()) {
      rel += (ch == 'U') ? 1 : -1;
      minRel = Math.min(minRel, rel);
    }
    delta = rel;

    for (int pos = 0; pos <= n - m; pos++) {
      int prefix = pos;
      int suffix = n - m - pos;

      for (int d = -prefix; d <= prefix; d++) {
        if ((prefix - Math.abs(d)) % 2 != 0)
          continue;

        int hMid = h0 + d;
        if (hMid < 0)
          continue;

        int need = Math.abs(hn - (hMid + delta));
        if (need > suffix)
          continue;

        if (hMid + minRel < 0)
          continue;

        return "YES";
      }
    }

    return "NO";
  }

  public static void main(String[] args) {
    FoxAndMountainEasy easy = new FoxAndMountainEasy();
    System.out.println(easy.possible(20, 0, 0, "UUUUUUUUUUU")); // NO
    System.out.println(easy.possible(4, 0, 4, "UU")); // YES
    System.out.println(easy.possible(4, 0, 4, "D")); // NO
    System.out.println(easy.possible(4, 100000, 100000, "DDU")); // YES
    System.out.println(easy.possible(4, 0, 0, "DDU")); // NO
    System.out.println(easy.possible(20, 20, 20, "UDUDUDUDUD"));// YES
    System.out.println(easy.possible(20, 0, 0, "UUUUUUUUUUU"));// NO
    System.out.println(easy.possible(7274, 72, 492,
        "DDDDDDDDDDDDDDUDDUDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"));// YES
    // System.out.println(easy.possible(37, 36, 31, "U"));
    System.out.println(easy.possible(10370, 4658, 3990,
        "UUDUUDUDDUDUUUUDDUDDUUDUUDDDUUUUUDUDUDDDDDUUDDUU"));// YES
    // System.out.println(easy.possible(20, 15, 17, "UDDUDUUUUUDUUUUUUU"));
  }

}
