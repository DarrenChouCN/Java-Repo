package topcoder.last;

/*
ToolMission

  You are on a mission to visit a series of research stations spread across n regions. Each region has its own rules about how many tools you are allowed to carry when entering. These rules are defined by two arrays, minTools and maxTools, each of length n.

  As you travel from one region to the next, you must collect at least one new tool between each region. This means the total number of tools you're carrying must form a strictly increasing sequence T[0] < T[1] < ... < T[n-1], and for each region i, the number of tools must satisfy minTools[i] ≤ T[i] ≤ maxTools[i].

  Your task is to determine how many such valid sequences of tool collection are possible under these constraints. Since the answer can be large, return it modulo 998244353. 
 */
public class ToolMission {

  final int MOD = 998244353;

  public int planRoutes(int[] minTools, int[] maxTools) {
    int n = minTools.length;
    int maxTool = 0;

    for (int x : maxTools) {
      maxTool = Math.max(maxTool, x);
    }

    int[] dpPrev = new int[maxTool + 2];
    for (int t = minTools[0]; t <= maxTools[0]; t++) {
      dpPrev[t] = 1;// 1个递增序列数
    }

    // i表示区域
    for (int i = 1; i < n; i++) {
      int[] prefixSum = new int[maxTool + 2];
      for (int t = 1; t <= maxTool; t++) {
        prefixSum[t] = prefixSum[t - 1] + dpPrev[t];
        if (prefixSum[t] >= MOD)
          prefixSum[t] -= MOD;
      }

      int[] dpCurr = new int[maxTool + 2];
      for (int t = minTools[i]; t <= maxTools[i]; t++) {
        // 到第 i 个区域，选中工具数为 t 时，有多少种合法递增序列？
        dpCurr[t] = prefixSum[t - 1];
      }
      dpPrev = dpCurr;
    }

    long total = 0;
    for (int val : dpPrev)
      total += val;

    return (int) (total % MOD);
  }

  public static void main(String[] args) {
    ToolMission toolMission = new ToolMission();

    int[] minTools1 = { 1, 3, 1, 4 };
    int[] maxTools1 = { 6, 5, 4, 6 };
    System.out.println(toolMission.planRoutes(minTools1, maxTools1));// 4

    int[] minTools2 = { 30, 10 };
    int[] maxTools2 = { 40, 20 };
    System.out.println(toolMission.planRoutes(minTools2, maxTools2));// 0

    int[] minTools3 = { 10, 30 };
    int[] maxTools3 = { 20, 40 };
    System.out.println(toolMission.planRoutes(minTools3, maxTools3));// 121

    int[] minTools4 = { 35, 20 };
    int[] maxTools4 = { 90, 45 };
    System.out.println(toolMission.planRoutes(minTools4, maxTools4));// 55

    int[] minTools5 = { 4, 46, 46, 35, 20, 77, 20 };
    int[] maxTools5 = { 41, 65, 84, 90, 49, 86, 88 };
    System.out.println(toolMission.planRoutes(minTools5, maxTools5));// 2470

    int[] minTools6 = { 1 };
    int[] maxTools6 = { 1000000000 };
    System.out.println(toolMission.planRoutes(minTools6, maxTools6));// 1755647
  }

}
