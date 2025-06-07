package topcoder.graph.bfs;

import java.util.LinkedList;
import java.util.Queue;

/*
 JumpyNum

  A jumpy number is a positive integer, all of whose adjacent digits differ by at least 2. For example,
          
          NOT JUMPY: 28459, 28549, 1091919, 97753, 111111
              JUMPY: 290464, 13131313, 97539753, 5
  Create a class JumpyNum that contains a method howMany that is given low and high and returns the number of jumpy numbers that are between low and high, inclusive.
 */
public class JumpyNumBFS {

  public int howMany(int low, int high) {
    int count = 0;

    Queue<Long> queue = new LinkedList<>();
    for (int i = 1; i <= 9; i++) {
      queue.offer((long) i);
    }

    while (!queue.isEmpty()) {
      long num = queue.poll();

      if (num >= low) {
        count++;
      }

      int lastDigit = (int) num % 10;
      for (int d = 0; d <= 9; d++) {
        if (Math.abs(d - lastDigit) >= 2) {
          long next = num * 10 + d;
          if (next <= high) {
            queue.offer(next);
          }
        }
      }
    }

    return count;
  }

  public static void main(String[] args) {
    JumpyNumBFS jumpyNum = new JumpyNumBFS();
    System.out.println(jumpyNum.howMany(8000, 20934));
  }

}
