package quiz;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
WhiteShoes
 
  There is a number of people in a room, and each of them wears shoes which are either black shoes or white shoes. 
  Every person counts the number of other people wearing white shoes. You are given an array of integers count, the i-th element of which is the number counted by the i-th person. Return the total number of people wearing white shoes, or -1 if count doesn't correspond to a valid situation.
 */
public class WhiteShoes {

  public int howMany(int[] count) {
    int n = count.length;
    Map<Integer, Integer> map = new HashMap<>();

    for (int c : count) {
      if (c < 0 || c > n - 1)
        return -1;
      map.put(c, map.getOrDefault(c, 0) + 1);
    }

    if (map.size() > 2)
      return -1;

    if (map.size() == 1) {
      int val = map.keySet().iterator().next();
      if (val == 0)
        return 0;
      if (val == n - 1)
        return n;
      return -1;
    }

    int low = Integer.MAX_VALUE, high = Integer.MIN_VALUE;
    int lowCount = 0, highCount = 0;

    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
      int val = entry.getKey();
      int countVal = entry.getValue();

      if (val < low) {
        high = low;
        highCount = lowCount;
        low = val;
        lowCount = countVal;
      } else {
        high = val;
        highCount = countVal;
      }
    }

    if (low != high - 1)
      return -1;
    int w = high;
    return (lowCount == w && highCount == n - w) ? w : -1;
  }

  // partial error
  public int howMany2(int[] count) {
    int n = count.length;
    Arrays.sort(count);
    if (count[n - 1] == 0) {
      return 0;
    }
    int sum = 0;
    for (int i = 0; i < n; i++) {
      sum += count[i];
    }
    if (sum % n == 0 && count[n - 1] == sum / n && count[n - 1] < n) {
      return n;
    }
    if (count[n - 1] >= n) {
      return -1;
    }
    if (count[n - 1] == 0) {
      return 0;
    }
    if (count[n - 1] == n - 1) {
      return n - 1;
    }
    return count[n - 1];
  }

  public static void main(String[] args) {
    WhiteShoes whiteShoes = new WhiteShoes();
    // Output expected: 8
    int[] count = { 1, 1, 1, 2 };
    System.out.println(whiteShoes.howMany(count));
  }
}
