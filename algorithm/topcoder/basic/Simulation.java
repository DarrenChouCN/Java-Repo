package topcoder.basic;

import java.util.PriorityQueue;

public class Simulation {

  /*
   * CarrotBoxesEasy
   * 
   * Rabbit Hanako has N boxes of carrots numbered 0 through N-1. The i-th box
   * contains carrots[i] carrots.
   * 
   * She decides to eat K carrots from these boxes. She will eat the carrots one
   * at a time, each time choosing a carrot from the box with the greatest number
   * of carrots. If there are multiple such boxes, she will choose the lowest
   * numbered box among them.
   * 
   * Return the number of the box from which she will eat her last carrot.
   */
  public static int theIndex(int[] carrots, int K) {
    // Priority queue to store the boxes with a custom comparator
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
      if (b[0] != a[0]) {
        return b[0] - a[0]; // Compare by number of carrots (descending)
      } else {
        return a[1] - b[1]; // Compare by box number (ascending)
      }
    });

    // Initialize the priority queue with the boxes
    for (int i = 0; i < carrots.length; i++) {
      pq.add(new int[] { carrots[i], i });
    }

    int lastBox = -1;
    // Simulate eating K carrots
    for (int i = 0; i < K; i++) {
      int[] box = pq.poll();
      lastBox = box[1];
      box[0]--;
      pq.add(box);
    }

    return lastBox;
  }
}
