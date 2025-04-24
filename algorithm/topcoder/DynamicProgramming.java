package topcoder;

import java.util.Arrays;
import java.util.List;

public class DynamicProgramming {

  /*
   * ZigZag
   * 
   * A sequence of numbers is called a zig-zag sequence if the differences between
   * successive numbers strictly alternate between positive and negative. The
   * first difference (if one exists) may be either positive or negative. A
   * sequence with fewer than two elements is trivially a zig-zag sequence.
   * 
   * For example, 1,7,4,9,2,5 is a zig-zag sequence because the differences
   * (6,-3,5,-7,3) are alternately positive and negative. In contrast, 1,4,7,2,5
   * and 1,7,4,5,5 are not zig-zag sequences, the first because its first two
   * differences are positive and the second because its last difference is zero.
   * 
   * Given a sequence of integers, sequence, return the length of the longest
   * subsequence of sequence that is a zig-zag sequence. A subsequence is obtained
   * by deleting some number of elements (possibly zero) from the original
   * sequence, leaving the remaining elements in their original order.
   */
  public int longestZigZag(int[] sequence) {

    int[][] dp = new int[2][sequence.length];

    // dp[down][0]
    dp[0][0] = 1;
    // dp[up][0]
    dp[1][0] = 1;

    for (int i = 0; i < 2; i++) {
      for (int j = 1; j < sequence.length; j++) {
        if (sequence[j] > sequence[j - 1]) {
          // up
          dp[1][j] = dp[0][j - 1] + 1;
          dp[0][j] = dp[0][j - 1];
        } else if (sequence[j] < sequence[j - 1]) {
          // down
          dp[0][j] = dp[1][j - 1] + 1;
          dp[1][j] = dp[1][j - 1];
        } else {
          dp[0][j] = dp[0][j - 1];
          dp[1][j] = dp[1][j - 1];
        }
      }
    }

    // for (int[] row : dp) {
    // System.err.println(Arrays.toString(row));
    // }
    int maxSeqCount = Integer.MIN_VALUE;
    for (int i = 0; i < dp.length; i++) {
      for (int j = 0; j < dp[0].length; j++) {
        if (dp[i][j] > maxSeqCount) {
          maxSeqCount = dp[i][j];
        }
      }
    }
    return maxSeqCount;
  }

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
   * ChessMetric
   * 
   * Suppose you had an n by n chess board and a super piece called a kingknight.
   * Using only one move the kingknight denoted 'K' below can reach any of the
   * spaces denoted 'X' or 'L' below:
   * .......
   * ..L.L..
   * .LXXXL.
   * ..XKX..
   * .LXXXL.
   * ..L.L..
   * .......
   * In other words, the kingknight can move either one space in any direction
   * (vertical, horizontal or diagonally) or can make an 'L' shaped move. An 'L'
   * shaped move involves moving 2 spaces horizontally then 1 space vertically or
   * 2 spaces vertically then 1 space horizontally. In the drawing above, the 'L'
   * shaped moves are marked with 'L's whereas the one space moves are marked with
   * 'X's. In addition, a kingknight may never jump off the board.
   * 
   * Given the size of the board, the start position of the kingknight and the end
   * position of the kingknight, your method will return how many possible ways
   * there are of getting from start to end in exactly numMoves moves. start and
   * finish are int[]s each containing 2 elements. The first element will be the
   * (0-based) row position and the second will be the (0-based) column position.
   * Rows and columns will increment down and to the right respectively. The board
   * itself will have rows and columns ranging from 0 to size-1 inclusive.
   * 
   * Note, two ways of getting from start to end are distinct if their respective
   * move sequences differ in any way. In addition, you are allowed to use spaces
   * on the board (including start and finish) repeatedly during a particular path
   * from start to finish. We will ensure that the total number of paths is less
   * than or equal to 2^63-1 (the upper bound for a long).
   */
  public long howMany(int size, int[] start, int[] end, int numMoves) {

    int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1, -2, -2, -1, -1, 1, 1, 2, 2 };
    int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1, -1, 1, -2, 2, -2, 2, -1, 1 };

    long[][][] dp = new long[size][size][numMoves + 1];
    dp[start[0]][start[1]][0] = 1;

    for (int k = 0; k < numMoves; k++) {
      for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
          if (dp[x][y][k] > 0) {
            for (int dir = 0; dir < 16; dir++) {
              int nx = x + dx[dir];
              int ny = y + dy[dir];

              if (nx >= 0 && nx < size && ny >= 0 && ny < size) {
                dp[nx][ny][k + 1] += dp[x][y][k];
              }
            }
          }
        }
      }
    }

    return dp[end[0]][end[1]][numMoves];
  }

  public static void main(String[] args) {
    DynamicProgramming dp = new DynamicProgramming();

    int[] test1 = { 3, 7 };
    int[] test2 = { 11, 5 };
    System.out.println(dp.howMany(13, test1, test2, 14));
  }
}
