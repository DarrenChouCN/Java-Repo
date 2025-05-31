package block5.FamilyTravel;

import java.util.Comparator;
import java.util.PriorityQueue;

/*
FamilyTravel 

  You just qualified for a programming contest which is held in a very far city. Since your family loves you too much, they decided to come along with you. Of course, your little brother will come also. You will drive from your home city to the city where the contest is being held, possibly stopping at several other cities along the way. The time it takes to get from one city to the next is called an interval. If an interval of your trip is ever longer than the previous interval, your brother will start crying and your parents will have to turn the car around and go back home, forcing you to miss the contest. Therefore, you must plan the trip so that doesn't happen. If there are multiple ways to achieve this, you must choose the one that minimizes the total length of the trip.

  You will be given a String[] graph containing the distances between the cities where you may stop. The jth character of the ith element of graph is the distance from the ith city to the jth city. If it's '0' (zero), it means that you cannot go directly from the ith city to the jth city. Characters 'a' to 'z' represent distances of 1 to 26, and characters 'A' to 'Z' represent distances of 27 to 52. You live in city 0 and the contest is being held in city 1. Return the length of the shortest route from city 0 to city 1 that will not make your brother cry. If no such route exists, return -1.
 */
public class FamilyTravel {

  static class State {
    int city;
    int distance;
    int lastDistance;

    State(int city, int distance, int lastDistance) {
      this.city = city;
      this.distance = distance;
      this.lastDistance = lastDistance;
    }

  }

  public int shortest(String[] edges) {
    int n = edges.length;

    // Build graph
    int[][] graph = new int[n][n];
    for (int i = 0; i < n; i++) {
      char[] current = edges[i].toCharArray();
      for (int j = 0; j < n; j++) {
        char c = current[j];
        if (c == '0') {
          graph[i][j] = 0;
        } else if (Character.isLowerCase(c)) {
          graph[i][j] = c - 'a' + 1;
        } else if (Character.isUpperCase(c)) {
          graph[i][j] = c - 'A' + 27;
        }
      }
    }

    PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.distance));

    boolean[][] visited = new boolean[n][54];

    // state { 0, 0, 53 }: total distance so far; current city; previous distance
    pq.offer(new State(0, 0, 53));

    while (!pq.isEmpty()) {
      State state = pq.poll();
      int distance = state.distance;
      int city = state.city;
      int lastDistance = state.lastDistance;

      // end city
      if (city == 1)
        return distance;

      if (visited[city][lastDistance])
        continue;

      visited[city][lastDistance] = true;

      for (int neighbor = 0; neighbor < n; neighbor++) {
        int value = graph[city][neighbor];
        // value = 0 : cannot pass
        // value > lastMaxDistance: parents will go back home
        if (value > 0 && value <= lastDistance) {
          pq.offer(new State(neighbor, distance + value, value));
        }
      }

    }

    return -1;
  }

  public static void main(String[] args) {
    FamilyTravel travel = new FamilyTravel();
    String[] edges = { "000a", "00a0", "0a0a", "a0a0" };
    System.out.println(travel.shortest(edges));
  }

}
