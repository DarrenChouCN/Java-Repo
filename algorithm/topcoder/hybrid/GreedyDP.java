package topcoder.hybrid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GreedyDP {

  /*
   * FlowerGarden
   * 
   * You are planting a flower garden with bulbs to give you joyous flowers
   * throughout the year. However, you wish to plant the flowers such that they do
   * not block other flowers while they are visible.
   * 
   * You will be given a int[] height, a int[] bloom, and a int[] wilt. Each type
   * of flower is represented by the element at the same index of height, bloom,
   * and wilt. height represents how high each type of flower grows, bloom
   * represents the morning that each type of flower springs from the ground, and
   * wilt represents the evening that each type of flower shrivels up and dies.
   * Each element in bloom and wilt will be a number between 1 and 365 inclusive,
   * and wilt[i] will always be greater than bloom[i]. You must plant all of the
   * flowers of the same type in a single row for appearance, and you also want to
   * have the flowers in rows which are more towards the front be as tall as
   * possible. However, if a flower type is taller than another type, and both
   * types can be out of the ground at the same time, the shorter flower must be
   * planted in front of the taller flower to prevent blocking. A flower blooms in
   * the morning, and wilts in the evening, so even if one flower is blooming on
   * the same day another flower is wilting, one can block the other.
   * 
   * For example, if your garden of flowers has heights 1, 2, and 3, and the
   * flowers of height 3 bloom and wilt with flowers of height 1, but neither
   * conflicts with 2, your best arrangement would be 2,1,3. 1,3,2 would be
   * possible, but then the first row of flowers is shorter, which is less
   * desirable.
   * 
   * You should return a int[] which contains the elements of height in the order
   * you should plant your flowers to acheive the above goals. The front of the
   * garden is represented by the first element in your return value, and is where
   * you view the garden from. The elements of height will all be unique, so there
   * will always be a well-defined ordering.
   */

  /*
   * First, sort the flowers by height. Then, traverse the sorted array and, as
   * long as the blooming and wilting periods do not overlap, insert each flower
   * from back to front into the correct position based on height.
   */
  private static class Flower {
    int h, b, w;

    Flower(int h, int b, int w) {
      this.h = h;
      this.b = b;
      this.w = w;
    }
  }

  private boolean overlap(Flower a, Flower b) {
    // wilt < bloom ?
    return !(a.w < b.b || b.w < a.b);
  }

  public int[] getOrdering(int[] height, int[] bloom, int[] wilt) {
    int n = height.length;
    List<Flower> list = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      list.add(new Flower(height[i], bloom[i], wilt[i]));
    }

    // process in ascending order of height
    list.sort(Comparator.comparingInt(f -> f.h));

    List<Flower> rows = new ArrayList<>();
    for (Flower f : list) {
      int pos = rows.size();
      // find the insertion point from back to front
      for (int i = rows.size() - 1; i >= 0; i--) {
        if (overlap(f, rows.get(i)))
          break;
        pos = i;
      }
      rows.add(pos, f);
    }

    int[] ans = new int[n];
    for (int i = 0; i < n; i++) {
      ans[i] = rows.get(i).h;
    }
    return ans;
  }

  /*
   * CasketOfStar (Interval DP)
   * 
   * The Casket of Star (sic) is a device in the Touhou universe. Its purpose is
   * to generate energy rapidly. Initially it contains n stars in a row. The stars
   * are labeled 0 through n-1 from the left to the right. You are given a int[]
   * weight, where weight[i] is the weight of star i.
   * 
   * The following operation can be repeatedly used to generate energy:
   * Choose a star x other than the very first star and the very last star.
   * The x-th star disappears.
   * This generates weight[x-1] * weight[x+1] units of energy.
   * We decrease n and relabel the stars 0 through n-1 from the left to the right.
   * 
   * Your task is to use the device to generate as many units of energy as
   * possible. Return the largest possible total amount of generated energy.
   */

  /*
   * Solve using interval DP. For each interval [l, r], iterate over all possible
   * positions k between l and r, calculate the combined result from the left and
   * right subintervals plus the current operation, and store the optimal value in
   * dp[l][r]. Although operating on intervals, the approach evaluates each
   * possible choice within, and the DP table records only the optimal result,
   * ensuring full coverage of all options.
   */
  public int maxEnergy(int[] weight) {
    int n = weight.length;
    int[][] dp = new int[n][n];

    for (int len = 2; len < n; len++) {
      for (int left = 0; left + len < n; left++) {
        int right = left + len;
        for (int k = left + 1; k < right; k++) {
          int energy = dp[left][k] + dp[k][right] + weight[left] * weight[right];
          dp[left][right] = Math.max(dp[left][right], energy);
        }
      }
    }
    return dp[0][n - 1];
  }

  public static void main(String[] args) {
    GreedyDP greedyDP = new GreedyDP();
    int[] weight = { 671, 547, 393, 594, 655, 704, 376, 320, 711, 671, 500, 265, 638, 457, 468, 549, 485, 695, 252, 655,
        262, 670, 450, 310, 716, 497, 619, 496, 301, 642, 332, 350, 675, 363, 253 };
    System.out.println(greedyDP.maxEnergy(weight));
  }

}
