package leetcode.class1;

/*
  Given a positive integer n, return the smallest power of 2 that is greater than or equal to n.

  Formally, return the smallest integer of the form 2^k such that 2^k >= n.

  找出最接近n的，2的某次方的值
 */
public class TODO_Near2Power {

  /* TODO: bit manipulation */

  // search approach
  public int smallestPowerOfTwo(int n) {
    int power = 1;
    while (power < n) {
      power *= 2;
    }
    return power;
  }
}
