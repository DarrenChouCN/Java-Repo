package topcoder.graph.bfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/*
FillTheBoar

  There is a hungry wild boar blocking your access to the forest where a treasure is buried.  A local shepherd has told you a secret:  the wild boar loves eating muffins and when it gets full it falls sleep. 

  Thus, you have made a plan to fill the boar until it falls sleep and you have bought a collection of muffins for that purpose. Their sizes in grams are the elements of the int[] muffins. As long as the wild boar feels hungry, you will select one of the muffins and feed it to the wild boar. Once the wild boar is fed a muffin, it will eat the whole muffin, even if doing so exceeds its stomach capacity - it will somehow stuff all the muffin into its digestive tract.

  Most animals get full when their stomach reach capacity. However, some wild boars do not feel immediately full and keep eating more muffins. The variable delay describes such variability. 
  •	If delay = 0, the boar stops feeling hungry as soon as the total amount of muffin eaten reaches or exceeds its stomach capacity. 
  •	if delay is positive, the boar stops feeling hungry only after  eating delay additional muffins. 

  As you want the boar to sleep for a long time you want to feed him as much quantity as possible.  Return the highest muffin amount that you will feed to the wild boar.
 */
public class FillTheBoar {

  static class State {
    int mask, sum;

    State(int mask, int sum) {
      this.mask = mask;
      this.sum = sum;
    }
  }

  public static int eaten(int stomach, int[] muffins, int delay) {
    int n = muffins.length;
    int max = 0;
    Queue<State> queue = new LinkedList<>();
    queue.offer(new State(0, 0));
    Set<String> visited = new HashSet<>();

    while (!queue.isEmpty()) {
      State s = queue.poll();
      if (s.sum >= stomach) {
        List<Integer> remain = new ArrayList<>();
        for (int i = 0; i < n; i++)
          if (((s.mask >> i) & 1) == 0)
            remain.add(muffins[i]);
        remain.sort(Collections.reverseOrder());
        int add = 0;
        for (int i = 0; i < Math.min(delay, remain.size()); i++)
          add += remain.get(i);
        max = Math.max(max, s.sum + add);
        continue;
      }
      for (int i = 0; i < n; i++) {
        if (((s.mask >> i) & 1) == 0) {
          int nextMask = s.mask | (1 << i);
          int nextSum = s.sum + muffins[i];
          String key = nextMask + "," + nextSum;
          if (!visited.contains(key)) {
            queue.offer(new State(nextMask, nextSum));
            visited.add(key);
          }
        }
      }
      max = Math.max(max, s.sum);
    }
    return max;
  }

  public static void main(String[] args) {
    System.out.println(eaten(4700, new int[] { 1000, 8000, 2000, 5000, 3000 }, 0));
  }

}
