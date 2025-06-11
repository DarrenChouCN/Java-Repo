package topcoder.dp;

/*
ProblemsToSolve

  Your teacher has given you some problems to solve. You must first solve problem 0. After solving each problem i, you must either move on to problem i+1 or skip ahead to problem i+2. You are not allowed to skip more than one problem. For example, {0, 2, 3, 5} is a valid order, but {0, 2, 4, 7} is not because the skip from 4 to 7 is too long.

  You are given a int[] pleasantness, where pleasantness[i] indicates how much you like problem i. The teacher will let you stop solving problems once the range of pleasantness you've encountered reaches a certain threshold. Specifically, you may stop once the difference between the maximum and minimum pleasantness of the problems you've solved is greater than or equal to the int variety. If this never happens, you must solve all the problems. Return the minimum number of problems you must solve to satisfy the teacher's requirements.
 */
public class ProblemsToSolve {

  public int minNumber(int[] pleasantness, int variety) {
    int n = pleasantness.length;
    if (variety <= 0)
      return 1;

    int[] dp = new int[n];
    dp[0] = 1;
    for (int i = 1; i < n; i++) {
      int best = dp[i - 1];
      if (i - 2 >= 0)
        best = Math.min(best, dp[i - 2]);
      dp[i] = best + 1;
    }

    int ans = n;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (Math.abs(pleasantness[i] - pleasantness[j]) >= variety) {
          int dist = j - i;
          int extra = (dist + 1) / 2;
          ans = Math.min(ans, dp[i] + extra);
        }
      }
    }

    return ans;
  }

  int best;

  public int minNumberDFS(int[] pleasantness, int variety) {
    if (variety <= 0)
      return 1;

    best = pleasantness.length;
    return best;
  }

  public static void main(String[] args) {
    ProblemsToSolve solve = new ProblemsToSolve();
    int[] pleasantness1 = { 1, 2, 3 };
    int[] pleasantness2 = { 1, 2, 3, 4, 5 };
    int[] pleasantness3 = { 10, 1, 12, 101 };
    int[] pleasantness4 = { 10, 1 };
    int[] pleasantness5 = { 1 };
    System.out.println(solve.minNumber(pleasantness1, 2));
    System.out.println(solve.minNumber(pleasantness2, 4));
    System.out.println(solve.minNumber(pleasantness3, 100));
    System.out.println(solve.minNumber(pleasantness4, 9));
    System.out.println(solve.minNumber(pleasantness5, 1));
  }
}
