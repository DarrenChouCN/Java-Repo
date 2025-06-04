package topcoder.greedy;

import java.util.ArrayList;
import java.util.List;

/*
RoughStrings

  Given a string s, its roughness is calculated as follows: Let c1 be the letter that appears most frequently in s, and let c2 be the letter that appears least frequently (c2 must appear at least once). The roughness of s is the number of occurrences of c1 minus the number of occurrences of c2.

  You are allowed to modify s by erasing between 0 and n characters, inclusive (see example 1 for clarification). Return the minimum possible roughness that can be achieved by such a modification.
 */
public class RoughStrings {

  public int minRoughness(String s, int n) {
    int[] cnt = new int[26];
    for (char c : s.toCharArray()) {
      cnt[c - 'a']++;
    }

    List<Integer> freq = new ArrayList<>();
    int maxCnt = 0;
    for (int v : cnt) {
      if (v > 0) {
        freq.add(v);
        maxCnt = Math.max(maxCnt, v);
      }
    }
    if (freq.size() <= 1)
      return 0;

    int best = Integer.MAX_VALUE;
    for (int low = 0; low <= maxCnt; low++) {
      for (int high = 0; high <= maxCnt; high++) {
        int del = 0;
        int kept = 0;
        for (int f : freq) {
          if (f < low) {
            del += f;
          }
          // Delete (f - high) characters to reduce the frequency of this letter to
          // 'high',
          // and keep 'high' characters because this frequency falls within the [low,
          // high] range.
          else if (f > high) {
            del += (f - high);
            kept += high;
          } else {
            kept += f;
          }
          // don't meet the requirement
          if (del > n)
            break;
        }

        if (del <= n && kept > 0) {
          best = Math.min(best, high - low);
          if (best == 0)
            return 0;
        }
      }
    }

    return best == Integer.MAX_VALUE ? 0 : best;
  }

  public static void main(String[] args) {
    RoughStrings roughStrings = new RoughStrings();
    System.out.println(roughStrings.minRoughness("aaaaabbc", 1));
    System.out.println(roughStrings.minRoughness("aaaabbbbc", 5));
    System.out.println(roughStrings.minRoughness("veryeviltestcase", 1));
    System.out.println(roughStrings.minRoughness("gggggggooooooodddddddllllllluuuuuuuccckkk", 5));
    System.out.println(roughStrings.minRoughness("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz", 17));
    System.out.println(roughStrings.minRoughness("bbbccca", 2));
  }

}
