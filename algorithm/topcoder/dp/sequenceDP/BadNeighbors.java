package topcoder.dp.sequenceDP;

/*
BadNeighbors
  
  The old song declares "Go ahead and hate your neighbor", and the residents of
  Onetinville have taken those words to heart. Every resident hates his
  next-door neighbors on both sides. Nobody is willing to live farther away
  from the town's well than his neighbors, so the town has been arranged in a
  big circle around the well. Unfortunately, the town's well is in disrepair
  and needs to be restored. You have been hired to collect donations for the
  Save Our Well fund.

  Each of the town's residents is willing to donate a certain amount, as
  specified in the int[] donations, which is listed in clockwise order around
  the well. However, nobody is willing to contribute to a fund to which his
  neighbor has also contributed. Next-door neighbors are always listed
  consecutively in donations, except that the first and last entries in
  donations are also for next-door neighbors. You must calculate and return the
  maximum amount of donations that can be collected.
*/
public class BadNeighbors {

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
}
