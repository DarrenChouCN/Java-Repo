package block3.MatchNumbersEasy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
MatchNumbersEasy

Problem Statement

  Each digit can be represented using a certain number of matches. Your goal is to create the largest possible number using the matches that you have. For example, if you need 6 matches for zero, 7 matches for one, and 8 matches for two, and you have 21 matches, the largest number you can create is 210 (8 + 7 + 6 = 21 matches).

  You are given a int[] matches and an int n. The ith element (zero-indexed) of matches is the number of matches needed to represent the digit i. n is the number of matches you have. Return the largest possible number you can create without extra leading zeros.

 */
public class MatchNumbersEasy {

  public String maxNumber(int[] matches, int n) {
    return "";
  }

  public static void main(String[] args) {
    MatchNumbersEasy easy = new MatchNumbersEasy();
    int[] test1 = { 6, 7, 8 };
    System.out.println(easy.maxNumber(test1, 21));
  }

}
