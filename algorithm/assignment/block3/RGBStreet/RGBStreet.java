package block3.RGBStreet;

/*
RGBStreet

Problem Statement

  The people of RGB Street have decided to paint each of their houses red, green, or blue. They've also decided that no two neighboring houses will be painted the same color. The neighbors of house i are houses i-1 and i+1. The first and last houses are not neighbors.

  You will be given a String[] houses, where the ith element corresponds to house i. Each element of houses will be formatted as "R G B" (quotes for clarity only), where R, G and B are the costs of painting the corresponding house red, green, and blue, respectively. Return the minimal total cost required to perform the work.
 

Process:
  1. 1. Parse the original 'houses' arr, get the cost of three colors and fill the 2D arrays

  2. Create dynamic programming table, which R(0) G(1) B(2), column represents i-th house

  3. Fill the dp table in column-first order

  4. When a house choose a color, the neighbor cannot choose the same color, so the dp table reflects the minimum valid cost up to that house

  5. Get the minimum value from the last column of dp table — that is the minimum total cost

  6. The logic works because at each step we only consider valid color combinations (no two adjacent houses share the same color), and build from previous states
 */
public class RGBStreet {

  public int estimateCost(String[] houses) {
    int len = houses.length;
    int[][] costs = new int[len][3];
    for (int i = 0; i < len; i++) {
      String[] houseCost = houses[i].split(" ");

      int[] cost = new int[houseCost.length];
      for (int j = 0; j < houseCost.length; j++) {
        cost[j] = Integer.parseInt(houseCost[j]);
      }
      costs[i] = cost;
    }

    // row represents R(0) G(1) B(2), column represents i-th house
    int dp[][] = new int[3][len];

    for (int i = 0; i < 3; i++) {
      dp[i][0] = costs[0][i];
    }

    for (int j = 1; j < len; j++) {
      for (int i = 0; i < 3; i++) {
        fillDpTable(dp, costs, i, j);
      }
    }
    // for (int[] s : dp) {
    // System.out.println(Arrays.toString(s));
    // }
    int minCost = Integer.MAX_VALUE;
    for (int i = 0; i < 3; i++) {
      minCost = Math.min(minCost, dp[i][len - 1]);
    }

    return minCost;
  }

  private void fillDpTable(int dp[][], int[][] costs, int i, int j) {
    if (j == 2) {
      System.out.println();
    }
    int minCost = Integer.MAX_VALUE;
    // Red
    if (i == 0) {
      int GCost = dp[1][j - 1];
      int BCost = dp[2][j - 1];

      minCost = Math.min(GCost, BCost);
    }
    // Green
    else if (i == 1) {
      int RCost = dp[0][j - 1];
      int BCost = dp[2][j - 1];
      minCost = Math.min(RCost, BCost);
    }
    // Blue
    else {
      int RCost = dp[0][j - 1];
      int GCost = dp[1][j - 1];
      minCost = Math.min(RCost, GCost);
    }

    dp[i][j] = minCost + costs[j][i];
  }

  public static void main(String[] args) {
    RGBStreet street = new RGBStreet();

    String[] test1 = { "30 19 5", "64 77 64", "15 19 97", "4 71 57", "90 86 84", "93 32 91" };
    System.out.println(street.estimateCost(test1));
  }

}
