package topcoder.divide_conquer;

/*
 * Divide array by midpoint or pivot, solve subarrays recursively, merge or select Kth
 * 
 * Classic Problems (Come from Topcoder):
 * - MergeSort (SRM 278 / pm 1705)
 * - RandomizedQuickSort (SRM 312 / pm 6135)
 * - MergeToSort: (2021 Regional 2 / pm 17125)
 */
public class TSortingAndSelection {

  /*
   * RandomizedQuickSort (SRM 312 / pm 6135)
   * 
   * Introduction of QuickSort
   * Assume that partition(A) takes a * length(A) time units and slowsort(A) takes
   * b * length(A)^2 time units. Further assume that the total running time is
   * simply equal to the sum of the running times of all calls to partition() and
   * slowsort(). As seen in the pseudo-code, quicksort does not call itself
   * recursively on lists of length S or less, but instead calls slowsort().
   * 
   * Consider using the randomized quicksort algorithm to sort a list that is some
   * permutation of elements {x | 1 <= x <= listSize}. Hence, the list is of
   * length listSize, only contains integers between 1 and listSize, inclusive,
   * and contains no duplicates. You are given the constants a, b and S, and asked
   * to compute the expected total running time of randomized quicksort on a list
   * of size listSize that obeys the constraints given above.
   * 
   * The question requires compute expected running time, so it is not a D&C
   * question, it is a DP question.
   */
  public double getExpectedTime(int listSize, int S, int a, int b) {
    double[] dp = new double[listSize + 1];
    double prefix = 0.0;

    dp[0] = 0.0;

    for (int n = 1; n <= listSize; n++) {
      double runningTime;
      if (n <= S) {
        runningTime = b * ((double) n * n);
      } else {
        // 2.0 * prefix / n: ∑(T(k)+T(n-k-1)) = 2*prefix(n)
        runningTime = a * (double) n + 2.0 * prefix / n;
      }

      dp[n] = runningTime;
      prefix += runningTime;
    }

    return dp[listSize];
  }

  /*
   * MergeSort (SRM 278 / pm 1705)
   * 
   * // Mergesort introduction
   * 
   * Given a int[] numbers, return the number of comparisons the MergeSort
   * algorithm (as described in pseudocode below) makes in order to sort that
   * list. In this context, a single comparison takes two numbers x, y (from the
   * list to be sorted) and determines which of x < y, x = y and x > y holds.
   */
  private long comparisons;

  public int howManyComparisons(int[] numbers) {
    comparisons = 0;
    if (numbers == null || numbers.length <= 1)
      return 0;
    mergeSort(numbers, 0, numbers.length - 1);
    return (int) comparisons;
  }

  // divide-and-conquer
  private void mergeSort(int[] a, int left, int right) {
    if (left >= right)
      return;

    int mid = (left + right) >>> 1;
    mergeSort(a, left, mid);
    mergeSort(a, mid + 1, right);
    merge(a, left, mid, right);
  }

  private void merge(int[] a, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    int[] L = new int[n1];
    int[] R = new int[n2];
    System.arraycopy(a, left, L, 0, n1);
    System.arraycopy(a, mid + 1, R, 0, n2);

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) {
      comparisons++;
      if (L[i] <= R[j]) {
        a[k++] = L[i++];
      } else {
        a[k++] = R[j++];
      }
    }
    while (i < n1)
      a[k++] = L[i++];
    while (j < n2)
      a[k++] = R[j++];
  }

}
