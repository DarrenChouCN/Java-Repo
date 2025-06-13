package topcoder.last;

import java.util.HashSet;
import java.util.Set;

/*
LuckyNumbers

  Andrew thinks that 5 and 8 are lucky digits. A positive integer is called an lucky number if it has only lucky digits in its decimal representation.

  You are given three int[]s a, b and c. Return that number of lucky numbers which can be expressed as a[i] + b[j] + c[k] for some indices i, j and k.

  Note that you must not count the same lucky number more than once.
 */
public class LuckyNumbers {
  public int countLucky(int[] a, int[] b, int[] c) {
    Set<Integer> luckySet = new HashSet<>();
    for (int x : a)
      for (int y : b)
        for (int z : c) {
          int sum = x + y + z;
          if (isLucky(sum))
            luckySet.add(sum);
        }

    return luckySet.size();
  }

  private boolean isLucky(int num) {
    if (num < 0)
      return false;
    while (num > 0) {
      int digit = num % 10;
      if (digit != 5 && digit != 8)
        return false;
      num /= 10;
    }

    return true;
  }

}
