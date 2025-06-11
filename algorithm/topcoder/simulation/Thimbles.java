package topcoder.simulation;

/*
Thimbles

  Thimbles is a hazard game with the following rules. The dealer puts three identical thimbles on the table, with a small ball underneath the first one. Then, he repeatedly selects a pair of thimbles and swaps their positions. Finally, he asks you "Where is the ball?". You win if you point to the right thimble and lose otherwise.

  You are writing the computer version of this game, and in this problem, you must write the module that determines the position of the ball after all the thimble swaps have been done.

  You will be given a String[] swaps which describes the swaps made, in order. Each element of swaps will be in the format "X-Y" (quotes for clarity), which means that the thimble in position X was swapped with the thimble in position Y. The positions are 1, 2 or 3. Your method should return the position of the thimble with the ball after all the swaps.
 */
public class Thimbles {

  public int thimbleWithBall(String[] swaps) {
    int ball = 1;

    for (String swap : swaps) {
      int x = swap.charAt(0) - '0';
      int y = swap.charAt(2) - '0';

      if (ball == x) {
        ball = y;
      } else if (ball == y) {
        ball = x;
      }
    }

    return ball;
  }

}
