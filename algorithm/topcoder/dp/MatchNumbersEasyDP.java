package topcoder.dp;

import java.util.Arrays;

import topcoder.greedy.MatchNumbersEasy;

/*
MatchNumbersEasy

  Each digit can be represented using a certain number of matches. Your goal is to create the largest possible number using the matches that you have. For example, if you need 6 matches for zero, 7 matches for one, and 8 matches for two, and you have 21 matches, the largest number you can create is 210 (8 + 7 + 6 = 21 matches).

  You are given a int[] matches and an int n. The ith element (zero-indexed) of matches is the number of matches needed to represent the digit i. n is the number of matches you have. Return the largest possible number you can create without extra leading zeros.
 */
public class MatchNumbersEasyDP {
  public String maxNumber(int[] matches, int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, Integer.MIN_VALUE);
    // 在恰好使用 i 根火柴的前提下，最多可以拼出多少位数字。
    dp[0] = 0;

    for (int i = 0; i <= n; i++)
      if (dp[i] >= 0)
        for (int d = 0; d <= 9; d++) {
          int cost = matches[d];
          // i+cost 消耗了多少根火柴
          // dp[i]+1
          if (i + cost <= n)
            // dp[i + cost] 取加上一位当前的数字，或者不加当前数字原来存放值，两者较大的那个（和问题要求保持一致）
            dp[i + cost] = Math.max(dp[i + cost], dp[i] + 1);
        }

    int len = dp[n];
    if (len < 0)
      return "";

    StringBuilder stringBuilder = new StringBuilder();
    int rem = 0;
    for (int pos = 1; pos <= len; pos++) {
      for (int d = 9; d >= 0; d--) {
        if (pos == 1 && len > 1 && d == 0)
          continue;
        int cost = matches[d];
        if (rem - cost < 0)
          continue;
        if (dp[rem - cost] == len - pos) {
          stringBuilder.append(d);
          rem -= cost;
          break;
        }
      }
    }

    return stringBuilder.toString();
  }

  public static void main(String[] args) {
    MatchNumbersEasy solver = new MatchNumbersEasy();

    int[] test1 = { 6, 7, 8 };
    int[] test2 = { 5, 23, 24 };
    int[] test3 = { 1, 5, 3, 2 };
    int[] test4 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

    System.out.println(solver.maxNumber(test1, 21));
    System.out.println(solver.maxNumber(test2, 30));
    System.out.println(solver.maxNumber(test3, 1));
    System.out.println(solver.maxNumber(test4, 50));
  }
}
