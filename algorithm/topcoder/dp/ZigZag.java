package topcoder.dp;

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
public class ZigZag {

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
}
