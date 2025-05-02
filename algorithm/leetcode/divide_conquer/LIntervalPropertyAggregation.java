package leetcode.divide_conquer;

/*
 * Each segment returns struct/stats; merge includes cross-segment answers
 * 
 * Classic Problems (Come from Topcoder):
 * - 53: Maximum Subarray
 * - 169: Majority Element
 * - 241: Different Ways to Add Parentheses
 */
public class LIntervalPropertyAggregation {

  /*
   * 53: Maximum Subarray
   * 
   * Given an integer array nums, find the subarray with the largest sum, and
   * return its sum.
   */

  // structure
  private static class Status {
    int lSum, rSum, mSum, iSum;

    Status(int lSum, int rSum, int mSum, int iSum) {
      this.lSum = lSum;
      this.rSum = rSum;
      this.mSum = mSum;
      this.iSum = iSum;
    }
  }

  public int maxSubArray(int[] nums) {
    return getInfo(nums, 0, nums.length - 1).mSum;
  }

  private Status getInfo(int[] a, int l, int r) {
    if (l == r) {
      int v = a[l];
      return new Status(v, v, v, v);
    }

    int m = (l + r) >>> 1;
    Status left = getInfo(a, l, m);
    Status right = getInfo(a, m + 1, r);
    return pushUp(left, right);
  }

  private Status pushUp(Status left, Status right) {
    int iSum = left.iSum + right.iSum;
    int lSum = Math.max(left.lSum, left.iSum + right.lSum);
    int rSum = Math.max(right.rSum, right.iSum + left.rSum);
    int mSum = Math.max(Math.max(left.mSum, right.mSum), left.rSum + right.lSum);
    return new Status(lSum, rSum, mSum, iSum);
  }
}
