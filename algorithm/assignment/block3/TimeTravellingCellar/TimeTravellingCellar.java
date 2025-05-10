package block3.TimeTravellingCellar;

import java.util.PriorityQueue;

/*
TimeTravellingCellar
 
  Gogo owns N wine cellars, numbered 0 through N-1. He possesses a time machine and will use it to advance time in one of the cellars, maturing all the wine inside. However, as a side effect, he must also choose one other cellar and turn back time there, making the wine inside younger.

  You are given two int[]s, profit and decay. Advancing time in cellar i will gain Gogo a profit of profit[i]. Turning back time in cellar i will lose him decay[i] in profit. Return the maximum profit that Gogo can gain by advancing time in one cellar and turning time back in another cellar. It is guaranteed that this profit will be positive.

I solve this problem using a greedy approach with priority queues:

  1. Use a max-heap to keep track of the top two cellars with the highest profit.

  2. Use a min-heap to keep track of the two cellars with the lowest decay.

  3. To maximize total profit, ideally I choose: the highest profit cellar for advancing time; the lowest decay cellar for turning back time.

  4. If these two cellars are different, return the profit from the first minus the decay from the second.

  5. If they are the same cellar, instead try both: using the second-best profit with the best decay; using the best profit with the second-best decay.

  6. Return the better of the two results.
 */
public class TimeTravellingCellar {

  public int determineProfit(int[] profit, int[] decay) {
    PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);

    for (int i = 0; i < profit.length; i++) {
      maxHeap.offer(new int[] { profit[i], i });
      minHeap.offer(new int[] { decay[i], i });
    }

    int[] firstProfit = maxHeap.poll();
    int[] secondProfit = maxHeap.poll();

    int[] firstDecay = minHeap.poll();
    int[] secondDecay = minHeap.poll();

    int maxProfit = Integer.MIN_VALUE;
    if (firstProfit[1] != firstDecay[1])
      maxProfit = firstProfit[0] - firstDecay[0];
    else
      maxProfit = Math.max(secondProfit[0] - firstDecay[0], firstProfit[0] - secondDecay[0]);

    return maxProfit;
  }

  public static void main(String[] args) {
    TimeTravellingCellar cRabbits = new TimeTravellingCellar();
    int[] profit = { 1000, 500, 250, 125 };
    int[] decay = { 64, 32, 16, 8 };
    System.out.println(cRabbits.determineProfit(profit, decay));
  }

}
