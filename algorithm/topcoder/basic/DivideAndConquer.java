package topcoder.basic;

public class DivideAndConquer {

  /*
   * MergeSort (SRM 278 / pm 1705)
   * 
   * // Mergesort introduction
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

  public static void main(String[] args) {
    DivideAndConquer conquer = new DivideAndConquer();
    int[] test1 = { 17, 45, 20, 16, 36, 4, 49, 18, 38, 21, 16 };
    System.out.println(conquer.howManyComparisons(test1));
  }

}