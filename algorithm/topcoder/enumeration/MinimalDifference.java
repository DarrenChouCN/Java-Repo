package topcoder.enumeration;

/*
MinimalDifference 

  The digit sum of an integer is the sum of its digits in decimal notation. For example, the digit sum of 1234 is 1+2+3+4=10, and the digit sum of 3443 is 3+4+4+3=14.
  You are given three integers: A, B and C. Return the integer X between A and B, inclusive, such that the absolute difference between the digit sum of X and the digit sum of C is as small as possible. If there are multiple possible values for X, return the smallest among them.
 */

public class MinimalDifference {

  // 在区间 [A, B] 中找到一个整数 X 使得：
  // X 的数字和与 C 的数字和之差的绝对值尽可能小
  public int findNumber(int A, int B, int C) {
    int targetSum = digitSum(C);
    int bestX = A;
    int minDiff = Math.abs(digitSum(A) - targetSum);

    for (int x = A + 1; x <= B; x++) {
      int diff = Math.abs(digitSum(x) - targetSum);
      if (diff < minDiff || (diff == minDiff && x < bestX)) {
        bestX = x;
        minDiff = diff;
      }
    }

    return bestX;
  }

  private int digitSum(int x) {
    int sum = 0;
    x = Math.abs(x); // 防止负数影响位数相加
    while (x > 0) {
      sum += x % 10;
      x /= 10;
    }
    return sum;
  }
}
