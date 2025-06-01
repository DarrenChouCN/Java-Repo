package quiz;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/*
PlayerTraining

  You are a coach for a martial arts gym and you wish to train the strongest team for an international competition. 
  You already have the list of players in your team and trainDays to train them. The number of players you have is given in a int[] count. It contains N elements and its i-th element is the number of players of level i. During each day, you can choose one player and train them. Training increases that player's level by 1, i.e., a player of level 0 becomes a player of level 1, a player of level 1 becomes a player of level 2, and so on. 

  The only exception is players of level N-1 - such players can't be trained as N-1 is the largest possible level. You can train the same player during more than one day. For example, if you train a player during 3 days, it will gain 3 levels. You can also skip days and not train any players during those days.

  You are given a int[] strength, where the i-th element of strength is the strength of one player of level i. (note this is not always an increasing sequence).  The strength of your team is the sum of the strengths of all its players. 
  Return the maximum possible strength your team can have after all the planned training has been completed.
 */
public class PlayerTraining {

  public long maxStrength(int[] count, int[] strength, int trainDays) {
    int n = count.length;
    long res = 0;

    // calculate initial total strength
    for (int i = 0; i < n; i++) {
      res += (long) count[i] * strength[i];
    }

    // each element: [from, to, cost, gain]
    PriorityQueue<int[]> q = new PriorityQueue<>((x, y) -> {
      double r1 = (double) y[3] / y[2];
      double r2 = (double) x[3] / x[2];
      return Double.compare(r1, r2); // sort by efficiency
    });

    // collect all possible upgrade paths
    for (int i = 0; i < n - 1; i++) {
      if (count[i] == 0)
        continue;
      for (int j = i + 1; j < n; j++) {
        int gain = strength[j] - strength[i];
        int cost = j - i;
        if (gain > 0) {
          q.offer(new int[] { i, j, cost, gain });
        }
      }
    }

    int[] pool = Arrays.copyOf(count, n);
    long extra = 0;

    // allocate training time based on best gain/cost ratio
    while (!q.isEmpty() && trainDays > 0) {
      int[] cur = q.poll();
      int from = cur[0], to = cur[1], cost = cur[2], gain = cur[3];
      if (pool[from] == 0 || trainDays < cost)
        continue;

      int move = Math.min(pool[from], trainDays / cost);
      trainDays -= move * cost;
      extra += (long) move * gain;
      pool[from] -= move;
      pool[to] += move;
    }

    return res + extra;
  }

  public static void main(String[] args) {
    PlayerTraining playerTraining = new PlayerTraining();
    int[] count1 = { 1, 2, 3, 4, 5 };
    int[] strength1 = { 1, 4, 9, 16, 25 };
    int trainDays1 = 10;
    System.out.println(playerTraining.maxStrength(count1, strength1, trainDays1));

    int[] count2 = { 1000, 0, 0, 0, 0 };
    int[] strength2 = { 0, 100, 10, 1000, 0 };
    int trainDays2 = 8;
    System.out.println(playerTraining.maxStrength(count2, strength2, trainDays2));
  }

}
