package topcoder.enumeration;

/*
BlackAndWhiteSolitaire

  Manao has N cards arranged in a sequence. He numbered them from left to right with numbers from 0 to N-1. Each card is colored black on one side and white on the other. Initially, each of the cards may lie on a different side. That is, some of the cards (possibly none or all of them) will be black side up and others will be white side up. Manao wants to flip some cards over to obtain an alternating configuration: every pair of successive cards must be of different colors.

  You are given a String cardFront consisting of N characters. For each i, character i of cardFront is 'B' if card i lies black side up, and 'W' otherwise. Count and return the minimum number of cards which must be flipped to obtain an alternating configuration.
 */
public class BlackAndWhiteSolitaire {

  public int minimumTurns(String cardFront) {
    int n = cardFront.length();
    int startWithB = 0;
    int startWithW = 0;

    for (int i = 0; i < n; i++) {
      char expectedB = (i % 2 == 0) ? 'B' : 'W';
      char expectedW = (i % 2 == 0) ? 'W' : 'B';

      if (cardFront.charAt(i) != expectedB)
        startWithB++;
      if (cardFront.charAt(i) != expectedW)
        startWithW++;
    }

    return Math.min(startWithW, startWithB);
  }

  public static void main(String[] args) {
    BlackAndWhiteSolitaire solitaire = new BlackAndWhiteSolitaire();
    System.out.println(solitaire.minimumTurns("BBBW"));
    System.out.println(solitaire.minimumTurns("WBWBW"));
    System.out.println(solitaire.minimumTurns("WWWWWWWWW"));
    System.out.println(solitaire.minimumTurns("BBWBWWBWBWWBBBWBWBWBBWBBW"));
  }
}
