package topcoder.recursion;

import java.util.ArrayList;
import java.util.List;

/*
NumberSplit

Problem Statement
    	
    We start with an integer and create a sequence of successors using the following procedure: First split the decimal representation of the given number into several (at least two) parts, and multiply the parts to get a possible successor. With the selected successor, we repeat this procedure to get a third number, and so on, until we reach a single-digit number.

    For example, let's say we start with the number 234. The possible successors are:

    - 23 * 4 = 92,
    - 2 * 34 = 68 and
    - 2 * 3 * 4 = 24.

    If we select 68 as the successor, we then generate 6 * 8 = 48 (the only possibility), from this we generate 4 * 8 = 32 and finally 3 * 2 = 6. With this selection, we have generated a sequence of 5 integers (234, 68, 48, 32, 6).
    Given the starting number, start, return the length of the longest sequence that can be generated with this procedure. In the example, the given sequence would be the longest one since the other selections in the first step would give the sequences: (234, 92, 18, 8) and (234, 24, 8), which are both shorter than (234, 68, 48, 32, 6).
 */
public class NumberSplit {

  public static int longestSequence(int start) {
    if (start < 10) {
      return 1;
    }
    String numStr = Integer.toString(start);
    int maxLen = 0;
    List<List<String>> splits = generateSplits(numStr);
    for (List<String> split : splits) {
      int product = 1;
      for (String part : split) {
        product *= Integer.parseInt(part);
      }
      int currentLen = 1 + longestSequence(product);
      if (currentLen > maxLen) {
        maxLen = currentLen;
      }
    }
    return maxLen;
  }

  private static List<List<String>> generateSplits(String numStr) {
    List<List<String>> splits = new ArrayList<>();
    for (int i = 1; i < numStr.length(); i++) {
      String left = numStr.substring(0, i);
      String right = numStr.substring(i);
      if ((left.length() > 1 && left.charAt(0) == '0') || (right.length() > 1 && right.charAt(0) == '0')) {
        continue;
      }
      List<String> split = new ArrayList<>();
      split.add(left);
      split.add(right);
      splits.add(split);
      List<List<String>> subSplits = generateSplits(right);
      for (List<String> subSplit : subSplits) {
        List<String> newSplit = new ArrayList<>();
        newSplit.add(left);
        newSplit.addAll(subSplit);
        splits.add(newSplit);
      }
    }
    return splits;
  }

  public static void main(String[] args) {
    System.out.println(longestSequence(6));
    System.out.println(longestSequence(97));
    System.out.println(longestSequence(234));
    System.out.println(longestSequence(876));
    System.out.println(longestSequence(99999));
  }

}
