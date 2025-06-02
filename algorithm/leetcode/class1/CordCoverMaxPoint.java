package leetcode.class1;

/*
CordCoverMaxPoint
  You are given a sorted array arr, representing points on the X-axis.
  You are also given a positive integer k, representing the length of a rope.

  You can place the rope anywhere on the X-axis.
  Return the maximum number of points that can be covered by the rope.
  A point is considered covered if it lies within or on the edge of the rope.

  数组代表坐标轴上的点，用长度固定的绳子覆盖，求绳子能最多盖住几个点
 */
public class CordCoverMaxPoint {

  // Sliding window
  public static int maxPointsCovered(int[] arr, int ropeLength) {
    int maxCovered = 0;
    int n = arr.length;

    int start = 0;
    for (int end = 0; end < n; end++) {
      while (arr[end] - arr[start] > ropeLength) {
        start++;
      }
      // [1, 2, 4, 7, 10]
      // misunderstanding: arr[end] - arr[start] + 1: 7-2+1=6
      // end - start + 1: 3-1+1=3
      int pointCovered = end - start + 1;
      maxCovered = Math.max(maxCovered, pointCovered);
    }

    return maxCovered;
  }

  public static void main(String[] args) {
    int[] arr = { 1, 2, 4, 7, 10 };
    int ropeLength = 5;
    System.out.println(maxPointsCovered(arr, ropeLength));
  }

  // sorted array: binary search
  public int maxPointBS(int[] arr, int L) {
    int res = Integer.MIN_VALUE;
    for (int i = 0; i < arr.length; i++) {
      int nearest = nearestIndex(arr, i, arr[i] - L);
      res = Math.max(nearest, res);
    }
    return res;
  }

  public int nearestIndex(int[] arr, int R, int value) {
    int L = 0;
    int index = R;
    while (L <= R) {
      int mid = L + ((R - L) >> 2);
      if (arr[mid] >= value) {
        // 中值更大，在左边
        index = mid;
        R = mid - 1;
      } else {
        // 中值更小，在右边
        L = mid + 1;
      }
    }
    return index;
  }
}
