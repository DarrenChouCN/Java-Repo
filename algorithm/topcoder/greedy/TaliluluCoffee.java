package topcoder.greedy;

import java.util.Arrays;

/*
TaliluluCoffee

  A new coffee place opened on the campus of Talilulu University. Things work differently at this coffee place.

  Every customer arrives at time 0, and the owner decides the order in which to serve them. A single customer is served each minute, until all the customers have been served. The first customer is served at minute 0. If customer i is served at time t minute, he will pay tips[i] - t. If this number is negative, he will not pay a tip at all. Given a int[] tips return the maximum amount of money the owner can make in tips.
 */
public class TaliluluCoffee {

  public int maxTip(int[] tips) {
    int n = tips.length;
    Arrays.sort(tips);
    int total = 0;

    for (int i = 0; i < n; i++) {
      int tip = tips[n - 1 - i] - i;
      if (tip > 0) {
        total += tip;
      }
    }
    return total;
  }

  public static void main(String[] args) {
    TaliluluCoffee coffee = new TaliluluCoffee();
    int[] tips1 = { 3, 3, 3, 3 };
    int[] tips2 = { 3, 2, 3 };
    int[] tips3 = { 7, 8, 6, 9, 10 };
    int[] tips4 = { 1, 1, 1, 1, 2 };
    int[] tips5 = { 1, 2, 3 };
    System.out.println(coffee.maxTip(tips1));
    System.out.println(coffee.maxTip(tips2));
    System.out.println(coffee.maxTip(tips3));
    System.out.println(coffee.maxTip(tips4));
    System.out.println(coffee.maxTip(tips5));
  }

}
