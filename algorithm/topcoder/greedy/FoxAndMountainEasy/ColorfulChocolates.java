package topcoder.greedy.FoxAndMountainEasy;

import java.util.ArrayList;
import java.util.List;

/*
ColorfulChocolates

  Beaver Bindu has some chocolates arranged in a row. The wrapping of each chocolate has a single color. Multiple chocolates can share the same color. In this problem, each of the possible colors is represented by an uppercase letter. You are given a String chocolates. For each i, the i-th chocolate (0-based index) in the row has the color chocolates[i].

  The spread of a row of chocolates is the maximum number of adjacent chocolates that all share the same color. Formally, the spread can be defined as the maximum value of (j-i+1), where i &lt;= j and all the chocolates in the positions between i and j, inclusive, have the same color.

  You are also given an int maxSwaps. Bindu can swap any two adjacent chocolates. She has decided to make at most maxSwaps such swaps.

  Return the maximum spread she can obtain.
 */
public class ColorfulChocolates {

  public int maximumSpread(String chocolates, int maxSwaps) {
    int n = chocolates.length();
    int best = 1;

    for (char color = 'A'; color <= 'Z'; color++) {
      List<Integer> pos = new ArrayList<>();
      for (int i = 0; i < n; i++) {
        if (chocolates.charAt(i) == color) {
          pos.add(i);
        }
      }
      int m = pos.size();
      if (m == 0)
        continue;

      for (int len = 1; len <= m; len++) {
        for (int left = 0; left + len <= m; left++) {
          // int right = left + len - 1;

          int middle = left + len / 2;
          int base = pos.get(middle) - len / 2;

          int cost = 0;
          for (int j = 0; j < len; j++) {
            int target = base + j;
            cost += Math.abs(pos.get(left + j) - target);
          }
          if (cost <= maxSwaps)
            best = Math.max(best, len);
        }
      }
    }

    return best;
  }

  public static void main(String[] args) {
    ColorfulChocolates chocolates = new ColorfulChocolates();
    System.out.println(chocolates.maximumSpread("ABCDCBC", 1));// 2
    System.out.println(chocolates.maximumSpread("ABCDCBC", 2));// 3
    System.out.println(chocolates.maximumSpread("ABBABABBA", 3));// 4
    System.out.println(chocolates.maximumSpread("ABBABABBA", 4));// 5
    System.out.println(chocolates.maximumSpread("QASOKZNHWNFODOQNHGQKGLIHTPJUVGKLHFZTGPDCEKSJYIWFOO", 77));// 5

    System.out.println(chocolates.maximumSpread("AABBBAAAABBBAAACCAA", 14));// 8
    System.out.println(chocolates.maximumSpread("ABCDEFA", 2));// 1
    System.out.println(chocolates.maximumSpread("AAAAA", 5));// 5
    System.out.println(chocolates.maximumSpread("YEHXIDONKPSZLVCNGMPKYTDAOP", 50));// 3
    System.out.println(chocolates.maximumSpread("CAAACCCCA", 3));// 5
  }

}
