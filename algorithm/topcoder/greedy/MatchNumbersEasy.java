package topcoder.greedy;

/*
MatchNumbersEasy

Problem Statement

  Each digit can be represented using a certain number of matches. Your goal is to create the largest possible number using the matches that you have. For example, if you need 6 matches for zero, 7 matches for one, and 8 matches for two, and you have 21 matches, the largest number you can create is 210 (8 + 7 + 6 = 21 matches).

  You are given a int[] matches and an int n. The ith element (zero-indexed) of matches is the number of matches needed to represent the digit i. n is the number of matches you have. Return the largest possible number you can create without extra leading zeros.

Analysis

  After analyzing the problem, I realized that there are three variables involved: the number i, the number of matches required to form i (denoted as matches[i]), and the remaining number of matches. This implies that if I were to use dynamic programming, I would need to establish a three-dimensional table, with the last variable (the remaining matches) constantly changing. This makes solving the problem using dynamic programming quite complex.

  As a result, I turned to the greedy algorithm to solve the problem.

  To apply a greedy approach, we need to identify the corresponding greedy strategy for this problem. Since the task requires us to find the maximum value under given conditions, we can devise the following greedy strategy:
    1. The more digits a number has, the larger its value. Therefore, we should prioritize using the number with the fewest matchsticks to form as many digits as possible (note: the leading digit cannot be 0, unless the result is "0").
    2. When multiple digits require the same number of matchsticks, we should prioritize the larger number to maximize the overall value.
    3. Using the remaining matchsticks, we should check from the highest place to the lowest place, attempting to replace the current digit with a larger one, as long as the remaining matchsticks are sufficient to cover the cost of the replacement.

Thought Process:

  1. Use a loop to find the digit i that requires the fewest matchsticks. If i is 0, skip it and look for the next smallest digit to avoid having a leading zero.
  2. Use the available minimum number of matchsticks to form a non-zero digit as the first digit, then use the remaining matchsticks to form as many digits as possible.
  3. Using the remaining matchsticks, check from the highest place to the lowest place, and try replacing the current digit with a larger one if possible.
  4. Return the result.
 */
public class MatchNumbersEasy {

  public String maxNumber(int[] matches, int n) {
    int len = matches.length;

    int minCost = Integer.MAX_VALUE;
    int minCostNonZero = Integer.MAX_VALUE;

    int minCostDigit = -1;
    int notZeroMinCostDigit = -1;
    for (int i = 0; i < len; i++) {
      if (matches[i] < minCost) {
        minCost = matches[i];
        minCostDigit = i;
      }
      if (i != 0 && matches[i] < minCostNonZero) {
        minCostNonZero = matches[i];
        notZeroMinCostDigit = i;
      }
    }

    if (n < minCostNonZero) {
      return "0";
    }

    // Using the remaining matchsticks, check from the highest place to the lowest
    // place, and try replacing the current digit with a larger one if possible.
    int maxDigits = (n - minCostNonZero) / minCost + 1;

    StringBuilder builder = new StringBuilder();

    int[] result = new int[maxDigits];
    result[0] = notZeroMinCostDigit;
    for (int i = 1; i < maxDigits; i++) {
      result[i] = minCostDigit;
    }
    int currentUsed = minCostNonZero + (maxDigits - 1) * minCost;
    int rest = n - currentUsed;

    for (int i = 0; i < maxDigits; i++) {
      int firstDigitCost = i == 0 ? minCostNonZero : minCost;
      for (int j = len - 1; j > result[i]; j--) {
        int restCost = matches[j] - firstDigitCost;
        if (restCost <= rest) {
          rest -= restCost;
          result[i] = j;
          break;
        }
      }
    }

    for (int digit : result) {
      builder.append(digit);
    }
    return builder.toString();
  }

  public static void main(String[] args) {
    MatchNumbersEasy solver = new MatchNumbersEasy();

    int[] test1 = { 6, 7, 8 };
    int[] test2 = { 5, 23, 24 };
    int[] test3 = { 1, 5, 3, 2 };
    int[] test4 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

    System.out.println(solver.maxNumber(test1, 21));
    System.out.println(solver.maxNumber(test2, 30));
    System.out.println(solver.maxNumber(test3, 1));
    System.out.println(solver.maxNumber(test4, 50));
  }

}
