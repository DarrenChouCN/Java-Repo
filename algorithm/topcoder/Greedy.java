package topcoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Greedy {

  /*
   * PermutationSignature
   * 
   * The signature of a permutation is a string that is computed as follows: for
   * each pair of consecutive elements of the permutation, write down the letter
   * 'I' (increasing) if the second element is greater than the first one,
   * otherwise write down the letter 'D' (decreasing).
   * 
   * For example, the signature of the permutation {3,1,2,7,4,6,5} is "DIIDID".
   * 
   * Your task is to reverse this computation: You are given a String signature
   * containing the signature of a permutation. Find and return the
   * lexicographically smallest permutation with the given signature. If no such
   * permutation exists, return an empty int[] instead.
   */

  // Local Segment Adjustment
  public int[] reconstruct(String signature) {
    int n = signature.length();
    int[] result = new int[n + 1];

    for (int i = 0; i <= n; i++) {
      result[i] = i + 1;
    }

    int i = 0;
    while (i < n) {
      if (signature.charAt(i) == 'D') {
        int start = i;
        while (i < n && signature.charAt(i) == 'D') {
          i++;
        }
        int end = i;
        reverse(result, start, end);
      } else {
        i++;
      }
    }
    return result;
  }

  private void reverse(int[] arr, int start, int end) {
    while (start < end) {
      int temp = arr[start];
      arr[start] = arr[end];
      arr[end] = temp;
      start++;
      end--;
    }
  }

  /*
   * OlympicCandles
   * 
   * To celebrate the upcoming Thought Challenge Olympics, you are going to follow
   * tradition and light candles. On the first night of the event, you will light
   * one candle. At the end of the night, you will extinguish the candle. On each
   * subsequent night, you will light one more candle than you did on the previous
   * night, so that on the n-th night (indexed from 1) you will light n candles
   * (and extinguish them all in the morning). Each night that you light a candle,
   * its height will decrease by 1 inch; once its height reaches 0 inches, you
   * cannot use it anymore.
   * 
   * You are given a int[] candles, the i-th element of which is the height of the
   * i-th candle that you own. Return the maximum number of nights you can
   * celebrate the event without going to the store to get more candles. For
   * example, if you have three candles of height 2, you can light one the first
   * night, the other two on the second night, and then all three candles on the
   * third night.
   */

  // Resource Allocation Greedy
  public int numberOfNights(int[] candles) {
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    for (int candle : candles) {
      if (candle > 0) {
        maxHeap.offer(candle);
      }
    }

    int nights = 0;
    while (true) {
      nights++;
      List<Integer> used = new ArrayList<>();
      if (maxHeap.size() < nights) {
        break;
      }

      for (int i = 0; i < nights; i++) {
        int height = maxHeap.poll();
        if (height > 1) {
          used.add(height - 1);
        }
      }
      for (int h : used) {
        maxHeap.offer(h);
      }
    }

    return nights - 1;
  }

}
