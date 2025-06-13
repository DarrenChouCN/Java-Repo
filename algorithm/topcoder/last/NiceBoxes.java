package topcoder.last;

/*
NiceBoxes
  Sergey has three boxes arranged in a row. The first box currently contains b1 candies, the second one contains b2 candies, and the third one contains b3 candies.

  Sergey thinks that the three boxes would look nice if they had the following two properties:

    Each box should contain at least one candy.
    The numbers of candies should form a strictly increasing sequence. In other words, the first box should contain fewer candies than the second box, and the second box should contain fewer candies than the third one.

  Sergey can only modify the current content of the boxes in one way: he can eat some of the candies.

  You are given the integers b1, b2, and b3. Compute and return the smallest possible number of candies Sergey should eat in order to make his three boxes look nice. If he has no way to make his boxes look nice, return -1 instead. 
 */
public class NiceBoxes {

  public int minRemove(int a, int b, int c) {
    int minCandiesEaten = Integer.MAX_VALUE;

    for (int first = 1; first <= a; first++) {
      for (int second = first + 1; second <= b; second++) {
        for (int third = second + 1; third <= c; third++) {
          if (first <= a && second <= b && third <= c) {
            int candiesEaten = (a - first) + (b - second) + (c - third);
            minCandiesEaten = Math.min(minCandiesEaten, candiesEaten);
          }
        }
      }
    }

    return minCandiesEaten == Integer.MAX_VALUE ? -1 : minCandiesEaten;
  }
}
