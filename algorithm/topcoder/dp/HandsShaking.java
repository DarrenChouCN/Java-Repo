package topcoder.dp;

/*
 HandsShaking 

  Consider a meeting of n businessmen sitting around a circular table. To start the meeting, they must shake hands. Each businessman shakes the hand of exactly one other businessman. All handshakes happen simultaneously. We say that the shake is perfect if no arms cross each other. Given an int n, return the number of perfect shakes that exist for n businessmen. See examples for further clarification.
 */
public class HandsShaking {
  public long countPerfect(int n) {
    if (n % 2 != 0)
      return 0;

    int pairs = n / 2;
    long[] dp = new long[pairs + 1];

    dp[0] = 1;

    // a loop body similar to insertion sort
    for (int i = 1; i <= pairs; i++) {
      dp[i] = 0;
      for (int k = 0; k < i; k++) {
        dp[i] += dp[k] * dp[i - k - 1];
      }
    }

    return dp[pairs];
  }

  public static void main(String[] args) {
    HandsShaking shaking = new HandsShaking();
    System.out.println(shaking.countPerfect(2));
    System.out.println(shaking.countPerfect(4));
    System.out.println(shaking.countPerfect(6));
    System.out.println(shaking.countPerfect(8));
    System.out.println(shaking.countPerfect(10));
    System.out.println(shaking.countPerfect(12));
    System.out.println(shaking.countPerfect(14));
    System.out.println(shaking.countPerfect(50));
  }
}
