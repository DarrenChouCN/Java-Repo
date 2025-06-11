package topcoder.dp;

/*
 * GearsDiv2
 * 
 * Goose Tattarrattat has a machine that contains N gears (cogwheels). The gears
 * are numbered 0 through N-1. Currently, the gears are arranged into a cycle:
 * for each i, gear i meshes with gears i-1 and i+1 (counting modulo N). In
 * particular, gear 0 meshes with gear N-1.
 * 
 * Let X and Y be two meshing gears. Note that if X is turning, Y must clearly
 * be turning in the opposite direction (clockwise vs. counter-clockwise).
 * 
 * For each of the N gears we have a desired direction of turning. You are given
 * this information encoded as a String Directions. Character i of Directions is
 * 'R' if we want gear i to turn to the right (clockwise), and it is 'L' if we
 * want it to turn to the left.
 * 
 * Of course, it may be impossible to satisfy all the desired directions of
 * turning. Return the minimal number of gears that have to be removed from the
 * machine so that all remaining gears can turn in the desired directions at the
 * same time.
 */
public class GearsDiv2 {

  public int getmin(String directions) {
    int len = directions.length();
    if (len == 0)
      return 0;
    if (len == 1)
      return 0;

    int[][] dp1 = dpLinear(directions, 0, len - 2);
    int[][] dp2 = dpLinear(directions, 1, len - 1);

    int min1 = Math.min(dp1[len - 2][0], dp1[len - 2][1]) + 1;
    int min2 = Math.min(dp2[len - 2][0], dp2[len - 2][1]) + 1;

    return Math.min(min1, min2);
  }

  private int[][] dpLinear(String directions, int start, int end) {
    int n = end - start + 1;

    char first = directions.charAt(start);

    // 0->L, 1->R
    int[][] dp = new int[n][2];

    dp[0][0] = first == 'L' ? 0 : 1;
    dp[0][1] = first == 'R' ? 0 : 1;

    for (int i = 1; i < n; i++) {
      char curChar = directions.charAt(start + i);
      int curDir = curChar == 'L' ? 0 : 1;
      int curOpposite = 1 - curDir;

      dp[i][curDir] = Math.min(dp[i - 1][curDir] + 1, dp[i - 1][curOpposite]);
      dp[i][curOpposite] = dp[i - 1][curDir] + 1;
    }

    return dp;
  }
}
