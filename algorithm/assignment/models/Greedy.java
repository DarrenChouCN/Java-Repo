package models;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Greedy {

  /*
   * 1.1 Interval Scheduling / Activity Selection
   * Choose the maximum number of non-overlapping intervals
   * Applicable: meeting-room allocation, largest set of compatible jobs, etc.
   */
  int maxNonOverlapping(int[][] intervals) {
    // sort by earliest finishing time
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

    int count = 0;
    int lastEnd = Integer.MIN_VALUE;

    for (int[] iv : intervals) {
      if (iv[0] >= lastEnd) { // interval starts after previous ends
        count++;
        lastEnd = iv[1]; // update reference end time
      }
    }
    return count;
  }

  /*
   * 1.2 Coin / Bank-Note Change (Canonical Currency)
   * Minimum number of coins/notes for a given amount (assumes canonical system,
   * e.g., AU$)
   * Applicable: vending-machine change, cashier systems, when denominations are
   * canonical.
   * 
   * For non-canonical denominations, dynamic programming is required (lecturers
   * sometimes test this trap).
   */
  int minCoins(int amount, int[] denom) {
    Arrays.sort(denom); // ascending order
    int count = 0;
    for (int i = denom.length - 1; i >= 0 && amount > 0; i--) {
      if (denom[i] <= amount) {
        int use = amount / denom[i];
        count += use;
        amount -= use * denom[i];
      }
    }
    return amount == 0 ? count : -1; // -1 if impossible
  }

  /*
   * 1.3 Jump Game / Furthest Reach (1-D Array Greedy)
   * Determine if we can reach the last index (LeetCode "Jump Game")
   * Applicable: single-pass array reachability, resource reach, battery charging
   * hops, etc.
   */
  boolean canReachEnd(int[] nums) {
    int maxReach = 0;
    for (int i = 0; i < nums.length; i++) {
      if (i > maxReach)
        return false; // gap we cannot cross
      maxReach = Math.max(maxReach, i + nums[i]);
    }
    return true;
  }

  /*
   * 1.4 Minimum Number of Jumps (Jump Game II)
   * Returns minimum jumps to reach the last index
   */
  int minJumps(int[] nums) {
    int jumps = 0, curEnd = 0, curFarthest = 0;
    for (int i = 0; i < nums.length - 1; i++) {
      curFarthest = Math.max(curFarthest, i + nums[i]);
      if (i == curEnd) { // we must make a jump
        jumps++;
        curEnd = curFarthest;
      }
    }
    return jumps;
  }

  /*
   * 1.5 Gas Station / Circular Tour
   * Find starting station to complete circuit, -1 if impossible
   * Applicable: circular resource replenishment (classic Go8 exam favourite)
   */
  int canCompleteCircuit(int[] gas, int[] cost) {
    int total = 0, tank = 0, start = 0;
    for (int i = 0; i < gas.length; i++) {
      int diff = gas[i] - cost[i];
      total += diff;
      tank += diff;
      if (tank < 0) { // cannot reach i+1 from start
        start = i + 1; // choose next station as new start
        tank = 0;
      }
    }
    return total >= 0 ? start : -1;
  }

  /*
   * 1.6 Huffman-Style Minimum Merge Cost (Priority Queue Pattern)
   * Minimum cost to merge files/ropes/frequencies (Huffman coding pattern)
   * Applicable: optimal merging, compression tasks, “combine ropes” problems
   */
  int minMergeCost(int[] sizes) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int s : sizes)
      pq.offer(s);

    int cost = 0;
    while (pq.size() > 1) {
      int a = pq.poll();
      int b = pq.poll();
      int merged = a + b;
      cost += merged; // accumulate cost
      pq.offer(merged);
    }
    return cost;
  }
}
