package models;

import java.util.*;

/**
 * Divide & Conquer Patterns – Go8 CS Exams Helper
 * ------------------------------------------------
 * 2.x 系列与 Greedy.java 对应，涵盖常见的分治套路。
 * 每个方法均在 O(log n)~O(n log n) 范围内，满足典型期末 / 竞赛限时要求。
 * ▸ 单文件、无第三方依赖，Java 17，可直接粘贴提交。
 */
public class DivideConquer {

  /*
   * --------------------------------------------------
   * 2.1 Merge Sort (stable) – 经典 O(n log n)
   * Applicable: 排序基础、排序后再做二分 / 去重。
   */
  public void mergeSort(int[] a) {
    if (a.length < 2)
      return;
    int[] buf = new int[a.length];
    mergeSort(a, buf, 0, a.length - 1);
  }

  private void mergeSort(int[] a, int[] buf, int l, int r) {
    if (l >= r)
      return;
    int m = l + ((r - l) >> 1);
    mergeSort(a, buf, l, m);
    mergeSort(a, buf, m + 1, r);
    merge(a, buf, l, m, r);
  }

  private void merge(int[] a, int[] buf, int l, int m, int r) {
    int i = l, j = m + 1, k = l;
    while (i <= m && j <= r)
      buf[k++] = a[i] <= a[j] ? a[i++] : a[j++];
    while (i <= m)
      buf[k++] = a[i++];
    while (j <= r)
      buf[k++] = a[j++];
    System.arraycopy(buf, l, a, l, r - l + 1);
  }

  /*
   * --------------------------------------------------
   * 2.2 Inversion Count – 归并统计，O(n log n)
   * Applicable: 数列『不排序度』、交换次数下界、逆序对。
   */
  public long inversionCount(int[] a) {
    if (a.length < 2)
      return 0;
    int[] buf = new int[a.length];
    return inversionCount(a, buf, 0, a.length - 1);
  }

  private long inversionCount(int[] a, int[] buf, int l, int r) {
    if (l >= r)
      return 0;
    int m = l + ((r - l) >> 1);
    long inv = inversionCount(a, buf, l, m) + inversionCount(a, buf, m + 1, r);
    int i = l, j = m + 1, k = l;
    while (i <= m && j <= r) {
      if (a[i] <= a[j])
        buf[k++] = a[i++];
      else {
        buf[k++] = a[j++];
        inv += (m - i + 1); // 左边剩余均大于 a[j]
      }
    }
    while (i <= m)
      buf[k++] = a[i++];
    while (j <= r)
      buf[k++] = a[j++];
    System.arraycopy(buf, l, a, l, r - l + 1);
    return inv;
  }

  /*
   * --------------------------------------------------
   * 2.3 Maximum Subarray (Divide & Conquer variant) – O(n log n)
   * Applicable: 当无法使用线性扫描 (Kadane) 时需展现分治思路
   */
  public int maxSubArray(int[] a) {
    return maxSubArray(a, 0, a.length - 1);
  }

  private int maxSubArray(int[] a, int l, int r) {
    if (l == r)
      return a[l];
    int m = l + ((r - l) >> 1);
    int leftMax = maxSubArray(a, l, m);
    int rightMax = maxSubArray(a, m + 1, r);
    int crossMax = maxCross(a, l, m, r);
    return Math.max(Math.max(leftMax, rightMax), crossMax);
  }

  private int maxCross(int[] a, int l, int m, int r) {
    int leftSum = Integer.MIN_VALUE, sum = 0;
    for (int i = m; i >= l; i--) {
      sum += a[i];
      leftSum = Math.max(leftSum, sum);
    }
    int rightSum = Integer.MIN_VALUE;
    sum = 0;
    for (int i = m + 1; i <= r; i++) {
      sum += a[i];
      rightSum = Math.max(rightSum, sum);
    }
    return leftSum + rightSum;
  }

  /*
   * --------------------------------------------------
   * 2.4 Quickselect – 第 k 小元素, 期望 O(n)
   * Applicable: 中位数 / percentiles / Top‑K 前处理。
   */
  public int quickSelect(int[] a, int k) { // k: 1‑based index
    if (k < 1 || k > a.length)
      throw new IllegalArgumentException();
    return quickSelect(a, 0, a.length - 1, k - 1);
  }

  private int quickSelect(int[] a, int l, int r, int idx) {
    while (true) {
      if (l == r)
        return a[l];
      int p = partition(a, l, r, l + (int) (Math.random() * (r - l + 1)));
      if (idx == p)
        return a[p];
      if (idx < p)
        r = p - 1;
      else
        l = p + 1;
    }
  }

  private int partition(int[] a, int l, int r, int pivotIdx) {
    int pivot = a[pivotIdx];
    swap(a, pivotIdx, r);
    int store = l;
    for (int i = l; i < r; i++)
      if (a[i] < pivot)
        swap(a, store++, i);
    swap(a, store, r);
    return store;
  }

  private void swap(int[] a, int i, int j) {
    int t = a[i];
    a[i] = a[j];
    a[j] = t;
  }

  /*
   * --------------------------------------------------
   * 2.5 Modular Fast Exponentiation – O(log e)
   * Applicable: 大整数幂模运算 (密码学 / 组合数学)
   */
  public long modPow(long base, long exp, long mod) {
    if (mod == 1)
      return 0;
    long res = 1 % mod;
    base %= mod;
    while (exp > 0) {
      if ((exp & 1) == 1)
        res = (res * base) % mod;
      base = (base * base) % mod;
      exp >>= 1;
    }
    return res;
  }

  /*
   * --------------------------------------------------
   * 2.6 Closest Pair of Points (2‑D, Euclidean) – O(n log n)
   * Applicable: Computational Geometry – 航迹最短距离，传感器布站等。
   */
  public double closestPair(Point[] pts) {
    Point[] byX = pts.clone();
    Arrays.sort(byX, Comparator.comparingDouble(p -> p.x));
    Point[] buf = new Point[pts.length];
    return closestPair(byX, buf, 0, pts.length - 1);
  }

  private double closestPair(Point[] a, Point[] buf, int l, int r) {
    if (r - l <= 3) {
      double d = Double.POSITIVE_INFINITY;
      for (int i = l; i <= r; i++)
        for (int j = i + 1; j <= r; j++)
          d = Math.min(d, a[i].dist(a[j]));
      Arrays.sort(a, l, r + 1, Comparator.comparingDouble(p -> p.y));
      return d;
    }
    int m = l + ((r - l) >> 1);
    double midX = a[m].x;
    double d = Math.min(closestPair(a, buf, l, m), closestPair(a, buf, m + 1, r));

    // merge by y
    mergeByY(a, buf, l, m, r);

    // strip within d of mid line
    int sz = 0;
    for (int i = l; i <= r; i++)
      if (Math.abs(a[i].x - midX) < d)
        buf[sz++] = a[i];
    for (int i = 0; i < sz; i++)
      for (int j = i + 1; j < sz && (buf[j].y - buf[i].y) < d; j++)
        d = Math.min(d, buf[i].dist(buf[j]));
    return d;
  }

  private void mergeByY(Point[] a, Point[] buf, int l, int m, int r) {
    int i = l, j = m + 1, k = l;
    while (i <= m && j <= r)
      buf[k++] = a[i].y <= a[j].y ? a[i++] : a[j++];
    while (i <= m)
      buf[k++] = a[i++];
    while (j <= r)
      buf[k++] = a[j++];
    System.arraycopy(buf, l, a, l, r - l + 1);
  }

  /* ------------------------------------ */
  public static class Point {
    public final double x, y;

    public Point(double x, double y) {
      this.x = x;
      this.y = y;
    }

    double dist(Point o) {
      double dx = x - o.x, dy = y - o.y;
      return Math.hypot(dx, dy);
    }
  }
}
