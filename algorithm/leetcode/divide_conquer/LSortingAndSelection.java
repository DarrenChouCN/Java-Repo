package leetcode.divide_conquer;

import java.util.Arrays;
import java.util.Random;

/*
 * Divide array by midpoint or pivot, solve subarrays recursively, merge or select Kth
 * 
 * Classic Problems (Come from Leetcode):
 * - 912: Sort an Array
 * - 215: Kth Largest Element in an Array
 * - 973: K Closest Points to Origin
 */
public class LSortingAndSelection {

  /*
   * 912. Sort an Array(Merge Sort)
   */
  public int[] sortArray(int[] nums) {
    mergeSort(nums, 0, nums.length - 1);
    return nums;
  }

  private void mergeSort(int[] nums, int left, int right) {
    if (left >= right)
      return;

    int mid = left + (right - left) / 2;

    mergeSort(nums, left, mid);
    mergeSort(nums, mid + 1, right);

    merge(nums, left, mid, right);
  }

  private void merge(int[] nums, int left, int mid, int right) {
    int[] temp = new int[right - left + 1];

    int i = left;
    int j = mid + 1;
    int k = 0;

    while (i <= mid && j <= right) {
      if (nums[i] <= nums[j])
        temp[k++] = nums[i++];
      else
        temp[k++] = nums[j++];
    }

    while (i <= mid)
      temp[k++] = nums[i++];
    while (j <= right)
      temp[k++] = nums[j++];

    for (int l = 0; l < temp.length; l++) {
      nums[left + l] = temp[l];
    }
  }

  /*
   * 215. Kth Largest Element in an Array
   * 
   * Given an integer array nums and an integer k, return the kth largest element
   * in the array.
   * 
   * Note that it is the kth largest element in the sorted order, not the kth
   * distinct element.
   */
  public int findKthLargest(int[] nums, int k) {
    int n = nums.length;
    // k-th largest
    return quickSelect1(nums, 0, n - 1, n - k);
  }

  private int quickSelect1(int[] nums, int left, int right, int targetIndex) {
    if (left == right)
      return nums[left];

    int pivotIndex = partition1(nums, left, right);
    if (pivotIndex == targetIndex)
      return nums[pivotIndex];

    else if (pivotIndex < targetIndex)
      return quickSelect1(nums, pivotIndex + 1, right, targetIndex);

    else
      return quickSelect1(nums, left, pivotIndex - 1, targetIndex);
  }

  private int partition1(int[] nums, int left, int right) {
    int pivotIndex = left + new Random().nextInt(right - left + 1);
    swap(nums, pivotIndex, right);

    int pivot = nums[right];
    int i = left;

    for (int j = left; j < right; j++) {
      if (nums[j] < pivot) {
        swap(nums, i, j);
        i++;
      }
    }
    swap(nums, i, right);
    return i;
  }

  private void swap(int[] nums, int a, int b) {
    int temp = nums[a];
    nums[a] = nums[b];
    nums[b] = temp;
  }

  /*
   * 973. K Closest Points to Origin
   * 
   * Given an array of points where points[i] = [xi, yi] represents a point on the
   * X-Y plane and an integer k, return the k closest points to the origin (0, 0).
   * 
   * The distance between two points on the X-Y plane is the Euclidean distance
   * (i.e., √(x1 - x2)2 + (y1 - y2)2).
   * 
   * You may return the answer in any order. The answer is guaranteed to be unique
   * (except for the order that it is in).
   */
  public int[][] kClosest(int[][] points, int k) {
    quickSelect2(points, 0, points.length - 1, k);
    return Arrays.copyOfRange(points, 0, k);
  }

  private void quickSelect2(int[][] points, int left, int right, int k) {
    if (left >= right)
      return;

    int pivotIndex = partition2(points, left, right);
    int count = pivotIndex - left + 1;

    if (count == k)
      return;

    else if (count < k)
      quickSelect2(points, pivotIndex + 1, right, k - count);

    else
      quickSelect2(points, left, pivotIndex - 1, k);
  }

  private int partition2(int[][] points, int left, int right) {
    int[] pivot = points[right];
    int pivotDist = distanceSquared(pivot);
    int i = left;

    for (int j = left; j < right; j++) {
      if (distanceSquared(points[j]) <= pivotDist) {
        swap2(points, i, j);
        i++;
      }
    }
    swap2(points, i, right);
    return i;
  }

  private int distanceSquared(int[] point) {
    return point[0] * point[0] + point[1] * point[1];
  }

  private void swap2(int[][] points, int i, int j) {
    int[] temp = points[i];
    points[i] = points[j];
    points[j] = temp;
  }
}
