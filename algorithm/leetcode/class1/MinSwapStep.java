package leetcode.class1;

/*
MinSwapStep

  You are given a character array chars consisting only of 'G' and 'B'.
  You can swap adjacent characters (i.e., swap chars[i] with chars[i+1]).

  Your goal is to move all 'G' characters to one side (either all to the left or all to the right), and all 'B' characters to the other side.

  Return the minimum number of adjacent swaps required to achieve this.
 */
public class MinSwapStep {

  public int minSteps(String s) {
    if (s == null || s.equals("")) {
      return 0;
    }
    char[] str = s.toCharArray();

    return str.length;
  }
}
