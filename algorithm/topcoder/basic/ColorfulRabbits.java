package topcoder.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
ColorfulRabbits

  Cat Pochi visited a town of rabbits and asked some of the rabbits the following question: "How many rabbits in this town other than yourself have the same color as you?". The rabbits all replied truthfully, and no rabbit was asked the question more than once. You are given the rabbits' replies in the int[] replies. Return the minimum possible number of rabbits in this town.

  1. Use a HashMap to group the rabbits' answers: the key is the number x that a rabbit answered (meaning there are x other rabbits with the same color); the value is how many rabbits gave that same answer.

  2. For each unique answer, retrieve how many rabbits gave that response.

  3. Each answer x implies that those rabbits belong to a group of x + 1 rabbits of the same color.

  4. Divide the total number of such rabbits into full groups of size x + 1. Each full group fully satisfies the condition that each rabbit has x others like it.

  5. If there are leftover rabbits that cannot form a complete group: I still need to count one extra group of size x + 1 to account for them; this assumes that some same-colored rabbits exist but were not surveyed.

  6. If only one rabbit gave a particular answer x, I must still assume it belongs to a full group of x + 1 rabbits.

  7. Sum all the rabbits across all groups to get the minimum possible total number of rabbits in the town.
 */
public class ColorfulRabbits {

  public int getMinimum(int[] replies) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int reply : replies) {
      int count = map.getOrDefault(reply, 0);
      map.put(reply, ++count);
    }

    int minimum = 0;
    for (Entry<Integer, Integer> entry : map.entrySet()) {
      int replyCount = entry.getKey();
      int groupCount = entry.getValue();

      if (groupCount != 1) {
        int actualCount = replyCount + 1;
        int fullGroup = groupCount / actualCount;
        minimum += fullGroup * actualCount;

        if (groupCount % actualCount != 0) {
          minimum += actualCount;
        }
      } else {
        minimum += (replyCount + 1);
      }

    }

    return minimum;
  }

  public static void main(String[] args) {
    ColorfulRabbits rabbits = new ColorfulRabbits();
    int[] test = { 2, 2, 2, 2, 2 };
    System.out.println(rabbits.getMinimum(test));
  }
}
