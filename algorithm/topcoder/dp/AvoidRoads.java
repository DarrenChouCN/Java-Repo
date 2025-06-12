package topcoder.dp;

import java.util.Arrays;
import java.util.List;

/*
 * AvoidRoads
 * 
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
public class AvoidRoads {

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
}
