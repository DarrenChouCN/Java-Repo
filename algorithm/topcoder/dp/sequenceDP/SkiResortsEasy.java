package topcoder.dp.sequenceDP;

import java.util.Arrays;

/*
SkiResortsEasy

  Fox Ciel is the owner of a ski resort. The ski resort has N places numbered 0 through N-1. You are given a int[] altitude. For each i, the i-th element of altitude is the altitude of the place i.

  The skiers would like to follow the path (place 0) -> (place 1) -> ... -> (place N-1). The trip will only be possible if the altitudes of the places are non-increasing. In order to make the trip possible, Ciel now needs to decrease the altitudes of some places. In other words, Ciel wants to decrease some of the altitudes so that altitude[0] -> altitude[1] -> ... -> altitude[N-1] holds. It costs 1 unit of money to decrease the altitude of one place by 1 unit of height.

  Return the minimal cost required for the change.

  以最小代价修改altitude数组使得altitude非升序
  dp: 线性 DP + 区间约束转移
 */
public class SkiResortsEasy {

  final int INF = 1 << 29;

  public int minCost(int[] altitude) {
    int n = altitude.length;
    if (n <= 1)
      return 0;

    int maxAlt = 0;
    for (int h : altitude)
      maxAlt = Math.max(maxAlt, h);

    int H = maxAlt + 1;

    // dpPrev: 第 i-1 座山最终被降到高度 h 时，总代价是多少
    int[] dpPrev = new int[H];
    Arrays.fill(dpPrev, INF);
    for (int h = 0; h <= altitude[0]; h++) {
      dpPrev[h] = altitude[0] - h;// +0 初始化不用降
    }

    for (int i = 1; i < n; i++) {
      // 从高度 h 到最高高度 H-1 中的最小代价
      int[] suffixMin = new int[H];
      suffixMin[H - 1] = dpPrev[H - 1];
      for (int h = H - 2; h >= 0; h--) {
        // dpPrev[h]: 上一座山降到高度 h 的总代价
        // suffixMin[h]: 上一座山降到 任何高度 ≥ h 的总代价最小值
        suffixMin[h] = Math.min(dpPrev[h], suffixMin[h + 1]);
      }

      int[] dpCurr = new int[H];
      Arrays.fill(dpCurr, INF);

      // 枚举当前山最终高度 h（只能降低到 ≤ altitude[i]）
      for (int h = 0; h <= altitude[i]; h++) {
        dpCurr[h] = (altitude[i] - h) + suffixMin[h];
      }

      dpPrev = dpCurr;
    }

    int ans = INF;
    for (int v : dpPrev)
      ans = Math.min(v, ans);

    return ans;
  }

  public static void main(String[] args) {
    SkiResortsEasy rEasy = new SkiResortsEasy();
    System.out.println(rEasy.minCost(new int[] { 30, 20, 20, 10 })); // 0
    System.out.println(rEasy.minCost(new int[] { 5, 7, 3 })); // 2
    System.out.println(rEasy.minCost(new int[] { 6, 8, 5, 4, 7, 4, 2, 3, 1 })); // 6
    System.out.println(rEasy.minCost(new int[] { 23, 507 })); // 484
    System.out.println(rEasy.minCost(new int[] { 249, 739, 471 })); // 712
    System.out.println(rEasy.minCost(new int[] { 836, 515, 256, 985 })); // 729
  }

}
