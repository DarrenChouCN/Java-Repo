package topcoder.dp.sequenceDP;

import java.util.Set;

/*
NiceOrUgly 

  A string is called ugly if it has 3 vowels in a row, or 5 consonants in a row, or both. A string is called nice if it is not ugly. You are given a string s, consisting of uppercase letters ('A'-'Z') and question marks ('?'). Return "UGLY" if the string is definitely ugly (that means you cannot substitute letters for question marks so that the string becomes nice), "NICE" if the string is definitely nice, and "42" if it can be either ugly or nice (quotes for clarity only).
 */
public class NiceOrUgly {

  private static final Set<Character> VOWELS = Set.of('A', 'E', 'I', 'O', 'U');

  public String describe(String s) {
    boolean[][] dp = new boolean[3][5];
    dp[0][0] = true;

    boolean uglyPossible = false;

    for (char ch : s.toCharArray()) {
      boolean[][] next = new boolean[3][5];

      for (int v = 0; v < 3; v++) {
        for (int c = 0; c < 5; c++)
          if (dp[v][c]) {
            if (ch == '?' || isVowel(ch)) {
              int nv = v + 1;
              if (nv >= 3) {
                uglyPossible = true;
              } else {
                next[nv][0] = true;
              }
            }
            if (ch == '?' || !isVowel(ch)) {
              int nc = c + 1;
              if (nc >= 5) {
                uglyPossible = true;
              } else {
                next[0][nc] = true;
              }
            }
          }
      }
      dp = next;
    }

    boolean nicePossible = false;
    for (int v = 0; v < 3 && !nicePossible; v++) {
      for (int c = 0; c < 5 && !nicePossible; c++) {
        if (dp[v][c])
          nicePossible = true;
      }
    }

    if (nicePossible && uglyPossible)
      return "42";
    if (nicePossible)
      return "NICE";
    return "UGLY";
  }

  private static boolean isVowel(char ch) {
    return VOWELS.contains(ch);
  }

}
