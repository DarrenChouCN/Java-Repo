package models;

import java.util.*;

/**
 * Dynamic Programming Patterns – Go8 CS Exams Helper
 * --------------------------------------------------
 * 3.x 系列，继 Greedy(1.x) 与 DivideConquer(2.x) 之后，收集常见 DP 套路。
 * ▸ 单文件 / Java 17 / 无第三方依赖，可直接复制到答题框。
 * ▸ 每节先标【适用场景】，再给完整实现；复杂度 & 记忆化技巧附在注释。
 */
public class DynamicProgramming {

  /*
   * --------------------------------------------------
   * 3.1 Fibonacci – Top‑Down + Bottom‑Up O(n)
   * Applicable: 展示『记忆化搜索』⇄『迭代滚动数组』等同。
   * 
   * 台阶跳跃问题（Climbing Stairs）
   * 
   * 解码方式数（Decode Ways）：给一个数字串（如 "123"），每个数字对应一个字母（1→A, 2→B...26→Z），问有多少种合法解码方式。
   * if (s[i-1] 有效) dp[i] += dp[i-1];
   * if (s[i-2,i-1] 组合有效) dp[i] += dp[i-2];
   * 
   * 最小/最大路径代价（Min Path Sum）
   * dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j];
   * 
   * 线性结构的 DP 问题：前一个状态决定当前状态
   * 任意形如 dp[i] = f(dp[i - 1], dp[i - 2]) 的问题
   */
  public long fibMemo(int n) {
    return fibMemo(n, new HashMap<>());
  }

  private long fibMemo(int n, Map<Integer, Long> memo) {
    if (n <= 1)
      return n;
    if (!memo.containsKey(n)) {
      memo.put(n, fibMemo(n - 1, memo) + fibMemo(n - 2, memo));
    }
    return memo.get(n);
  }

  public long fibIter(int n) {
    if (n <= 1)
      return n;
    long a = 0, b = 1;
    for (int i = 2; i <= n; i++) {
      long c = a + b;
      a = b;
      b = c;
    }
    return b;
  }

  /*
   * --------------------------------------------------
   * 3.2 0/1 Knapsack – O(n * W) (W = capacity)
   * Applicable: 背包系题，能进一步改写成一维滚动数组节省空间。
   * 
   * 最多能装下几个任务（价值恒为 1）
   * 每个任务有时间成本（视作重量），价值为 1，问最多能选多少任务不超过时间上限。
   * dp[j] = max(dp[j], dp[j - time[i]] + 1)
   * 
   * 判断是否可以刚好填满容量（布尔型 DP）
   * 给一组物品，是否能恰好装满背包。
   * dp[j] |= dp[j - weight[i]]
   * 
   * 子集和 / 能否分成相等两部分
   * 给一组正整数，判断是否可以划分成两个和相等的子集。
   * dp[j] |= dp[j - nums[i]]
   */
  public int knap01(int[] weight, int[] value, int capacity) {
    int n = weight.length;
    int[] dp = new int[capacity + 1]; // dp[j] = max value with capacity j
    for (int i = 0; i < n; i++)
      for (int j = capacity; j >= weight[i]; j--)
        dp[j] = Math.max(dp[j], dp[j - weight[i]] + value[i]);
    return dp[capacity];
  }

  /*
   * --------------------------------------------------
   * 3.3 Longest Increasing Subsequence – O(n log n)
   * Applicable: 序列优化 DP；展示二分 + patience sorting 思想。
   */
  public int LIS(int[] a) {
    int[] tails = new int[a.length];
    int len = 0;
    for (int x : a) {
      int i = Arrays.binarySearch(tails, 0, len, x);
      if (i < 0)
        i = -i - 1;
      tails[i] = x;
      if (i == len)
        len++;
    }
    return len;
  }

  /*
   * --------------------------------------------------
   * 3.4 Longest Common Subsequence – O(n * m)
   * Applicable: diff 工具、DNA 比对、编辑距离基础。
   */
  public int LCSLength(String a, String b) {
    int n = a.length(), m = b.length();
    int[] dp = new int[m + 1];
    for (int i = 1; i <= n; i++) {
      int prev = 0;
      for (int j = 1; j <= m; j++) {
        int tmp = dp[j];
        if (a.charAt(i - 1) == b.charAt(j - 1))
          dp[j] = prev + 1;
        else
          dp[j] = Math.max(dp[j], dp[j - 1]);
        prev = tmp;
      }
    }
    return dp[m];
  }

  /* optional reconstruction */
  public String LCS(String a, String b) {
    int n = a.length(), m = b.length();
    int[][] dp = new int[n + 1][m + 1];
    for (int i = n - 1; i >= 0; i--)
      for (int j = m - 1; j >= 0; j--)
        dp[i][j] = a.charAt(i) == b.charAt(j) ? dp[i + 1][j + 1] + 1 : Math.max(dp[i + 1][j], dp[i][j + 1]);
    // recover
    StringBuilder sb = new StringBuilder();
    int i = 0, j = 0;
    while (i < n && j < m) {
      if (a.charAt(i) == b.charAt(j)) {
        sb.append(a.charAt(i));
        i++;
        j++;
      } else if (dp[i + 1][j] >= dp[i][j + 1])
        i++;
      else
        j++;
    }
    return sb.toString();
  }

  /*
   * --------------------------------------------------
   * 3.5 Edit Distance (Levenshtein) – O(n * m)
   * Applicable: 拼写纠错、DNA，代码抄袭检测。
   */
  public int editDistance(String a, String b) {
    int n = a.length(), m = b.length();
    int[] dp = new int[m + 1];
    for (int j = 0; j <= m; j++)
      dp[j] = j;
    for (int i = 1; i <= n; i++) {
      int prev = i - 1;
      dp[0] = i;
      for (int j = 1; j <= m; j++) {
        int tmp = dp[j];
        if (a.charAt(i - 1) == b.charAt(j - 1))
          dp[j] = prev;
        else
          dp[j] = 1 + Math.min(prev, Math.min(dp[j - 1], dp[j])); // replace, insert, delete
        prev = tmp;
      }
    }
    return dp[m];
  }

  /*
   * --------------------------------------------------
   * 3.6 Coin Change – Fewest coins to make amount O(n * amount)
   * Applicable: 最小硬币数、货币系统、DP BFS 对照。
   */
  public int coinChange(int[] coins, int amount) {
    int INF = amount + 1;
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, INF);
    dp[0] = 0;
    for (int c : coins)
      for (int j = c; j <= amount; j++)
        dp[j] = Math.min(dp[j], dp[j - c] + 1);
    return dp[amount] == INF ? -1 : dp[amount];
  }

  /*
   * --------------------------------------------------
   * 3.7 Bitmask DP – Traveling Salesman (n ≤ 20) O(n²·2^n)
   * Applicable: Hamilton 路径、状态压缩。
   */
  public int tsp(int[][] dist) {
    int n = dist.length;
    int FULL = 1 << n;
    int INF = 1_000_000_000;
    int[][] dp = new int[FULL][n];
    for (int[] row : dp)
      Arrays.fill(row, INF);
    dp[1][0] = 0; // start at 0
    for (int mask = 1; mask < FULL; mask++)
      for (int u = 0; u < n; u++)
        if ((mask & (1 << u)) != 0) {
          int cur = dp[mask][u];
          if (cur >= INF)
            continue;
          for (int v = 0; v < n; v++)
            if ((mask & (1 << v)) == 0) {
              int next = mask | (1 << v);
              dp[next][v] = Math.min(dp[next][v], cur + dist[u][v]);
            }
        }
    int res = INF;
    for (int u = 1; u < n; u++)
      res = Math.min(res, dp[FULL - 1][u] + dist[u][0]);
    return res;
  }

  /*
   * --------------------------------------------------
   * 3.8 Tree DP – Max path sum root→leaf O(n)
   * Applicable: 多叉树结构、项目依赖 DAG 等。
   */
  public static class Node {
    public int val;
    List<Node> ch = new ArrayList<>();

    public Node(int v) {
      val = v;
    }
  }

  public int treeMaxPath(Node root) {
    if (root == null)
      return Integer.MIN_VALUE;
    if (root.ch.isEmpty())
      return root.val;
    int best = Integer.MIN_VALUE;
    for (Node c : root.ch)
      best = Math.max(best, treeMaxPath(c));
    return root.val + best;
  }
}
