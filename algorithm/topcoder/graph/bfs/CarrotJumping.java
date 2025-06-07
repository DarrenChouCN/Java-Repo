package topcoder.graph.bfs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/*
CarrotJumping

  Rabbits often feel hungry, so when they go out to eat carrots, they jump as quickly as possible.

  Initially, rabbit Hanako stands at position init. From position x, she can jump to either position 4*x+3 or 8*x+7 in a single jump. She can jump at most 100,000 times because she gets tired by jumping.

  Carrots are planted at position x if and only if x is divisible by 1,000,000,007 (i.e. carrots are planted at position 0, position 1,000,000,007, position 2,000,000,014, and so on). Return the minimal number of jumps required to reach a carrot. If it's impossible to reach a carrot using at most 100,000 jumps, return -1.
 */
public class CarrotJumping {

  final int MOD = 1_000_000_007;
  final int LIMIT = 100_000;

  public int theJump(int init) {
    return bfs(init);
  }

  private int bfs(int init) {
    int start = mod(init);

    if (start == 0)
      return 0;

    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[] { start, 0 });

    Set<Integer> visited = new HashSet<>();
    visited.add(start);

    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      int x = curr[0];
      int steps = curr[1];

      if (steps == LIMIT)
        return -1;

      int nx1 = jump4xPlus3(x);
      int nx2 = jump8xPlus7(x);

      // valid result
      if (nx1 == 0 || nx2 == 0)
        return steps + 1;

      if (visited.add(nx1))
        queue.offer(new int[] { nx1, steps + 1 });

      if (visited.add(nx2))
        queue.offer(new int[] { nx2, steps + 1 });
    }

    return -1;
  }

  private int mod(long v) {
    long r = v % MOD;
    return r < 0 ? (int) (r + MOD) : (int) r;
  }

  private int jump4xPlus3(int x) { // (4*x + 3) % MOD
    return (int) (((long) x * 4 + 3) % MOD);
  }

  private int jump8xPlus7(int x) { // (8*x + 7) % MOD
    return (int) (((long) x * 8 + 7) % MOD);
  }

  public static void main(String[] args) {
    CarrotJumping jumping = new CarrotJumping();
    System.out.println(jumping.theJump(411233746));
  }

}
