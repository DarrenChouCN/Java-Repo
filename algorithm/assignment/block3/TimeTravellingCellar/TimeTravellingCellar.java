package block3.TimeTravellingCellar;

import java.util.PriorityQueue;

/*
TimeTravellingCellar
 
  Gogo owns N wine cellars, numbered 0 through N-1. He possesses a time machine and will use it to advance time in one of the cellars, maturing all the wine inside. However, as a side effect, he must also choose one other cellar and turn back time there, making the wine inside younger.

  You are given two int[]s, profit and decay. Advancing time in cellar i will gain Gogo a profit of profit[i]. Turning back time in cellar i will lose him decay[i] in profit. Return the maximum profit that Gogo can gain by advancing time in one cellar and turning time back in another cellar. It is guaranteed that this profit will be positive.
 */
public class TimeTravellingCellar {

  public int determineProfit(int[] profit, int[] decay) {
    int n = profit.length;

    PriorityQueue<int[]> maxHeapProfit = new PriorityQueue<>((a, b) -> b[0] - a[0]);
    PriorityQueue<int[]> minHeapDecay = new PriorityQueue<>((a, b) -> a[0] - b[0]);

    for (int i = 0; i < n; i++) {
      maxHeapProfit.offer(new int[] { profit[i], i });
      minHeapDecay.offer(new int[] { decay[i], i });
    }

    int[] bestProfit = maxHeapProfit.poll();
    int[] secondBestProfit = maxHeapProfit.poll();

    int[] bestDecay = minHeapDecay.poll();
    int[] secondBestDecay = minHeapDecay.poll();

    int maxProfit = Integer.MIN_VALUE;
    if (bestProfit[1] != bestDecay[1]) {
      maxProfit = bestProfit[0] - bestDecay[0];
    } else {
      maxProfit = Math.max(secondBestProfit[0] - bestDecay[0], bestProfit[0] - secondBestDecay[0]);
    }

    return maxProfit;
  }

  public static void main(String[] args) {
    TimeTravellingCellar cRabbits = new TimeTravellingCellar();
    int[] profit = {};
    int[] decay = {};
    System.out.println(cRabbits.determineProfit(profit, decay));
  }

}
