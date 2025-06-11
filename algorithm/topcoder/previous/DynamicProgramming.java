package topcoder.previous;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DynamicProgramming {

  /*
   * BadNeighbors
   * 
   * The old song declares "Go ahead and hate your neighbor", and the residents of
   * Onetinville have taken those words to heart. Every resident hates his
   * next-door neighbors on both sides. Nobody is willing to live farther away
   * from the town's well than his neighbors, so the town has been arranged in a
   * big circle around the well. Unfortunately, the town's well is in disrepair
   * and needs to be restored. You have been hired to collect donations for the
   * Save Our Well fund.
   * 
   * Each of the town's residents is willing to donate a certain amount, as
   * specified in the int[] donations, which is listed in clockwise order around
   * the well. However, nobody is willing to contribute to a fund to which his
   * neighbor has also contributed. Next-door neighbors are always listed
   * consecutively in donations, except that the first and last entries in
   * donations are also for next-door neighbors. You must calculate and return the
   * maximum amount of donations that can be collected.
   */
  public int maxDonations(int[] donations) {
    int n = donations.length;
    if (n == 0)
      return 0;
    if (n == 1)
      return donations[0];

    int max1 = robLinear(donations, 0, n - 2);

    int max2 = robLinear(donations, 1, n - 1);

    return Math.max(max1, max2);
  }

  private int robLinear(int[] donations, int start, int end) {
    if (start == end)
      return donations[start];

    // Init first two elements
    int prev2 = donations[start];
    int prev1 = Math.max(donations[start], donations[start + 1]);

    for (int i = start + 2; i <= end; i++) {
      // dp[i] = max(dp[i-1], dp[i-2] + donations[i])
      int curr = Math.max(prev1, prev2 + donations[i]);
      prev2 = prev1;
      prev1 = curr;
    }

    return prev1;
  }

  /*
   * AvoidRoads
   * 
   * Problem contains images. Plugin users can view them in the applet.
   * In the city, roads are arranged in a grid pattern. Each point on the grid
   * represents a corner where two blocks meet. The points are connected by line
   * segments which represent the various street blocks. Using the cartesian
   * coordinate system, we can assign a pair of integers to each corner as shown
   * below.
   * 
   * You are standing at the corner with coordinates 0,0. Your destination is at
   * corner width,height. You will return the number of distinct paths that lead
   * to your destination. Each path must use exactly width+height blocks. In
   * addition, the city has declared certain street blocks untraversable. These
   * blocks may not be a part of any path. You will be given a String[] bad
   * describing which blocks are bad. If (quotes for clarity) "a b c d" is an
   * element of bad, it means the block from corner a,b to corner c,d is
   * untraversable. For example, let's say
   */
  public long numWays(int width, int height, String[] bad) {
    long[][] dp = new long[width + 1][height + 1];
    dp[0][0] = 1;

    List<String> badList = Arrays.asList(bad);

    for (int x = 0; x <= width; x++) {
      for (int y = 0; y <= height; y++) {

        // From left
        if (x > 0) {
          String path1 = (x - 1) + " " + y + " " + x + " " + y;
          String path2 = x + " " + y + " " + (x - 1) + " " + y;
          if (!badList.contains(path1) && !badList.contains(path2)) {
            dp[x][y] += dp[x - 1][y];
          }
        }

        // From bottom
        if (y > 0) {
          String path1 = x + " " + (y - 1) + " " + x + " " + y;
          String path2 = x + " " + y + " " + x + " " + (y - 1);
          if (!badList.contains(path1) && !badList.contains(path2)) {
            dp[x][y] += dp[x][y - 1];
          }
        }
      }
    }
    return dp[width][height];
  }

  /*
   * EmoticonsDiv2
   * 
   * You are very happy because you advanced to the next round of a very important
   * programming contest. You want your best friend to know how happy you are.
   * Therefore, you are going to send him a lot of smile emoticons. You are given
   * an int smiles: the exact number of emoticons you want to send.
   * 
   * You have already typed one emoticon into the chat. Then, you realized that
   * typing is slow. Instead, you will produce the remaining emoticons using copy
   * and paste.
   * 
   * You can only do two different operations:
   * Copy all the emoticons you currently have into the clipboard.
   * Paste all emoticons from the clipboard.
   * Each operation takes precisely one second. Copying replaces the old content
   * of the clipboard. Pasting does not empty the clipboard. Note that you are not
   * allowed to copy just a part of the emoticons you already have.
   * 
   * Return the smallest number of seconds in which you can turn the one initial
   * emoticon into smiles emoticons.
   */
  public int printSmiles(int smiles) {
    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visted = new boolean[smiles + 1][smiles + 1];

    // emoticons, clips, time
    queue.offer(new int[] { 1, 0, 0 });
    visted[1][0] = true;

    while (!queue.isEmpty()) {
      int[] state = queue.poll();
      int cur = state[0];
      int clip = state[1];
      int time = state[2];

      if (cur == smiles)
        return time;

      // copy
      if (!visted[cur][cur]) {
        visted[cur][cur] = true;
        queue.offer(new int[] { cur, cur, time + 1 });
      }

      // paste
      if (clip > 0 && cur + clip <= smiles && !visted[cur + clip][clip]) {
        visted[cur + clip][clip] = true;
        queue.offer(new int[] { cur + clip, clip, time + 1 });
      }
    }

    return -1;
  }
}
