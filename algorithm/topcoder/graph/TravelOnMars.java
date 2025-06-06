package topcoder.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
TravelOnMars

  Bob recently went to Mars.

  There are N cities on Mars. The cities all lie on a circular railroad and they are numbered 0 through N-1 along the railroad. More precisely, there is a railroad segment that connects cities N-1 and 0, and for each i (0 <= i <= N - 2) there is a railroad segment that connects cities i and i+1. Trains travel along the railroad in both directions.

  You are given a int[] range with N elements. For each i: the set of cities that are reachable from city i by a direct train is precisely the set of cities that are within the distance range[i] of city i. (The distance between two cities is the smallest number of railroad segments one needs to travel in order to get from one city to the other. For example, if N=17 and range[2]=3, the cities directly reachable from city 2 are the cities {16,0,1,2,3,4,5}.)

  You are also given ints startCity and endCity. Bob starts his tour in the city startCity and wants to end it in the city endCity. Calculate and return the minimum number of succesive direct trains he needs to take.
 */
public class TravelOnMars {

  public int minTimes(int[] range, int startCity, int endCity) {
    int n = range.length;
    if (startCity == endCity)
      return 0;

    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < n; i++) {
      for (int d = -range[i]; d <= range[i]; d++) {
        int j = (i + d + n) % n;
        graph.get(i).add(j);
      }
    }

    int[] dist = new int[n];
    Arrays.fill(dist, -1);

    Queue<Integer> queue = new LinkedList<>();
    dist[startCity] = 0;
    queue.offer(startCity);

    while (!queue.isEmpty()) {
      int cur = queue.poll();
      for (int next : graph.get(cur)) {
        if (dist[next] != -1)
          continue;
        dist[next] = dist[cur] + 1;
        if (next == endCity) {
          return dist[next];
        }
        queue.offer(next);
      }
    }

    return -1;
  }

  public static void main(String[] args) {
    TravelOnMars travelOnMars = new TravelOnMars();
    int[] range1 = { 2, 1, 1, 1, 1, 1 };
    int[] range2 = { 2, 1, 1, 1, 1, 1 };
    int[] range3 = { 2, 1, 1, 2, 1, 2, 1, 1 };
    int[] range4 = { 3, 2, 1, 1, 3, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3 };
    int[] range5 = { 2, 4, 2, 3, 4, 1, 4, 2, 5, 4, 3, 3, 5, 4, 5, 2, 2, 4, 4, 3, 3, 4, 2, 3, 5, 4, 2, 4, 1, 3, 2, 3, 4,
        1, 1, 4, 4, 3, 5, 3, 2, 1, 4, 1, 4 };
    System.out.println(travelOnMars.minTimes(range1, 1, 4));
    System.out.println(travelOnMars.minTimes(range2, 4, 1));
    System.out.println(travelOnMars.minTimes(range3, 2, 6));
    System.out.println(travelOnMars.minTimes(range4, 6, 13));
    System.out.println(travelOnMars.minTimes(range5, 24, 8));
  }

}
