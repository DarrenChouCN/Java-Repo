package topcoder.dp.sequenceDP;

/*
ZigZag

  A sequence of numbers is called a zig-zag sequence if the differences between successive numbers strictly alternate between positive and negative. The first difference (if one exists) may be either positive or negative. A sequence with fewer than two elements is trivially a zig-zag sequence.

  For example, 1,7,4,9,2,5 is a zig-zag sequence because the differences (6,-3,5,-7,3) are alternately positive and negative. In contrast, 1,4,7,2,5 and 1,7,4,5,5 are not zig-zag sequences, the first because its first two differences are positive and the second because its last difference is zero.

  Given a sequence of integers, sequence, return the length of the longest subsequence of sequence that is a zig-zag sequence. A subsequence is obtained by deleting some number of elements (possibly zero) from the original sequence, leaving the remaining elements in their original order.

  相邻数字之间的差值严格交替为正数和负数为锯齿序列，求给定数组最长的锯齿序列
 */
public class ZigZag {

  public int longestZigZag(int[] sequence) {
    int n = sequence.length;
    if (n < 2)
      return n;

    int[] up = new int[n];
    int[] down = new int[n];

    for (int i = 0; i < n; i++) {
      up[i] = 1;
      down[i] = 1;
    }

    for (int i = 1; i < n; i++) {
      // 枚举i前面的所有位置j
      for (int j = 0; j < i; j++) {
        if (sequence[i] > sequence[j]) {
          // 后大于前，升序，更新up
          up[i] = Math.max(up[i], down[j] + 1);
        } else if (sequence[i] < sequence[j]) {
          // 前大于后,降序，更新down
          down[i] = Math.max(down[i], up[j] + 1);
        }
      }
    }

    int maxLen = 1;
    for (int i = 0; i < n; i++) {
      maxLen = Math.max(maxLen, Math.max(up[i], down[i]));
    }
    return maxLen;
  }

  public static void main(String[] args) {
    int[] seq1 = { 1, 7, 4, 9, 2, 5 };// 6
    int[] seq2 = { 1, 17, 5, 10, 13, 15, 10, 5, 16, 8 };// 7
    int[] seq3 = { 10, 20 };// 2
    int[] seq4 = { 20, 10 };// 2
    int[] seq5 = { 20, 20 };// 1

    int[] seq6 = { 3, 8, 1, 5, 3, 10, 2, 7, 3, 1, 9, 3, 3, 9, 8, 4, 7, 6, 8, 7, 2, 5, 5, 10, 8, 1, 7, 4, 5, 2, 6 };// 24
    int[] seq7 = { 5, 10, 7, 4, 5, 3, 8, 7, 4, 5, 5, 2, 2, 1, 4, 3, 7, 5, 1, 5, 7, 10, 2, 10, 9, 3, 9, 1, 6, 1, 2 };// 22
    int[] seq8 = { 1, 8, 2, 3, 5, 1, 1, 7 };// 6
    int[] seq9 = { 5, 8, 3, 5, 4, 5, 2, 3 };// 8
    int[] seq10 = { 9, 10, 7, 8, 4, 1, 8 };// 6

    ZigZag zigZag = new ZigZag();
    System.out.println(zigZag.longestZigZag(seq1));
    System.out.println(zigZag.longestZigZag(seq2));
    System.out.println(zigZag.longestZigZag(seq3));
    System.out.println(zigZag.longestZigZag(seq4));
    System.out.println(zigZag.longestZigZag(seq5));
    System.out.println(zigZag.longestZigZag(seq6));
    System.out.println(zigZag.longestZigZag(seq7));
    System.out.println(zigZag.longestZigZag(seq8));
    System.out.println(zigZag.longestZigZag(seq9));
    System.out.println(zigZag.longestZigZag(seq10));
  }

  public int longestZigZag2D(int[] sequence) {

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
