package topcoder.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/*
Egalitarianism
  A kingdom has n citizens. Each one has some amount of money, a number of dollars denoted by a non-negative integer.

  Citizens are numbered from 0 to n-1. Some citizens have friends. Their friendship network is described by a String[] called isFriend, such that if isFriend[i][j] == 'Y', the citizens numbered i and j are friends, and if isFriend[i][j] == 'N', these citizens are not friends.

  The king decrees the following:

  Each citizen's amount of money must be within d dollars of the amount of money belonging to any of his friends. In other words, a citizen with x dollars must not have any friends with less than x-d dollars or more than x+d dollars.

  Given the number of citizens and their friendship network, what is the greatest possible money difference between any two people (not necessarily friends) in this kingdom? If there is a finite answer, return it. Otherwise, return -1.
 */
public class Egalitarianism {

  public int maxDifference(String[] isFriend, int d) {
    int n = isFriend.length;
    // Build graph
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        if (isFriend[i].charAt(j) == 'Y') {
          graph.get(i).add(j);
          graph.get(j).add(i);
        }
      }
    }

    boolean[] visited = new boolean[n];
    dfs(0, graph, visited);
    for (boolean v : visited) {
      if (!v)
        return -1;
    }

    int diameter = 0;
    for (int s = 0; s < n; s++) {
      int[] dist = bfs(s, graph, n);
      for (int x : dist)
        diameter = Math.max(diameter, x);
    }
    return diameter * d;
  }

  private void dfs(int u, List<List<Integer>> graph, boolean[] visited) {
    visited[u] = true;
    for (int v : graph.get(u)) {
      if (!visited[v]) {
        dfs(v, graph, visited);
      }
    }
  }

  private int[] bfs(int src, List<List<Integer>> graph, int n) {
    int[] dist = new int[n];
    Arrays.fill(dist, -1);

    Queue<Integer> queue = new LinkedList<>();
    queue.offer(src);
    dist[src] = 0;

    while (!queue.isEmpty()) {
      int u = queue.poll();
      for (int v : graph.get(u)) {
        if (dist[v] == -1) {
          dist[v] = dist[u] + 1;
          queue.offer(v);
        }
      }
    }

    return dist;
  }

  public static void main(String[] args) {
    Egalitarianism egalitarianism = new Egalitarianism();
    String[] isFriend1 = { "NYNYY", "YNYYY", "NYNNY", "YYNNY", "YYYYN" };
    String[] isFriend2 = { "NNYN", "NNNY", "YNNN", "NYNN" };
    System.out.println(egalitarianism.maxDifference(isFriend1, 921));
    System.out.println(egalitarianism.maxDifference(isFriend2, 584));

    String[] isFriend3 = { "NNYYYYNYYNNNNNYNYYNNNYYYYYNYYYYNYYNYYYNYYYYNNNYYN",
        "NNNYYNYYYYYYNNNYYYNYNYYYYYNYNYYYYYYYYYYNNYNYYYYYY", "YNNNYYYNNNYYNYYYYYYYNNNYYYNNNNYYYNYNYNYNYNYNNYYYN",
        "YYNNYYYNYNYYNNYYNYNYYNYNYNYNYYYYYNYYNYYNYYYYNNNNY", "YYYYNNYNNNYYYYNYYNYYNYYYYYNYYYNNNYNYYYYYYYYNNNNYN",
        "YNYYNNYNYNNYNNNNNYYYYYYYYYYYNNYYYYYNYNYYNNYNYNYYN", "NYYYYYNYYYNYNNNNYYYYNNYYYNYYNNYYYNNYYYYNNYNYYYNYY",
        "YYNNNNYNNNYYYNNYYYNYYYYYNYNYYYNNYYYNYYNNNNNYYNYYY", "YYNYNYYNNYNYYYNNYYNYNYYNNNYYYYYNYNYNNNYNYYYNYYNYY",
        "NYNNNNYNYNYYYYYYNNYNNYNYNNYYNYNYYYNYYYYYYYYYYYYYY", "NYYYYNNYNYNNYNYNYNNNNYYNYYYYYNYYNYNYYYYYYYYYNYYYY",
        "NYYYYYYYYYNNNNNYYNYYYNYNYYYYYNNYYYNYYYNYYYYYNYYYY", "NNNNYNNYYYYNNNYNYYYNYNYNYYNNNYYYYYYYYYYNNNYNNYNYN",
        "NNYNYNNNYYNNNNYNNNYYYYYYYYYYNYYNNYNNYYNYYYYNYYNYN", "YNYYNNNNNYYNYYNYNNYNYYNNYYNYYNNYNNYNYYYYNYYNYNYNY",
        "NYYYYNNYNYNYNNYNYYYYNYYNYYNNYNNYNYYYNNNNYYYYYNNYY", "YYYNYNYYYNYYYNNYNYYNNNYYNYYNYYYNNYYYYYYNNYYNYYNNN",
        "YYYYNYYYYNNNYNNYYNNNNNYNYNNNNYNYYYNYYYNNYNYYYNYNN", "NNYNYYYNNYNYYYYYYNNYYYNYYYYYYNYNNNNNNYYYYYYYYNYYY",
        "NYYYYYYYYNNYNYNYNNYNYYNYYYYNYNYYNNYYNNYYYYYYNNYNN", "NNNYNYNYNNNYYYYNNNYYNYNYYYYYYNYYYNYNYNYYNNYYYNYNN",
        "YYNNYYNYYYYNNYYYNNYYYNYYYNYYNNNYYNNNYYNNNNYNNYYYY", "YYNYYYYYYNYYYYNYYYNNNYNYNYYYYNYYYYYNNYNNYYNYNNNYY",
        "YYYNYYYYNYNNNYNNYNYYYYYNYYYNYNNYNNNYYYYYNNYYNNYYY", "YYYYYYYNNNYYYYYYNYYYYYNYNYYYNYYNNNYNNYYNYYYYNNYYN",
        "YYYNYYNYNNYYYYYYYNYYYNYYYNYYYYYYYYYYYNNYYYNYNNYYN", "NNNYNYYNYYYYNYNNYNYYYYYYYYNNYYYYNNYNYYYNNNYYYYNNY",
        "YYNNYYYYYYYYNYYNNNYNYYYNYYNNYYYYYYYNNYYYYNNYYYNYY", "YNNYYNNYYNYYNNYYYNYYYNYYNYYYNYYNYYYNYYNYNYYYYNYYY",
        "YYNYYNNYYYNNYYNNYYNNNNNNYYYYYNNNNYYYNNYYYYNNYNNNY", "YYYYNYYNYNYNYYNNYNYYYNYNYYYYYNNYNYNYYNYNYYYNNYYNY",
        "NYYYNYYNNYYYYNYYNYNYYYYYNYYYNNYNYNYYYYYNYNYYYYYNY", "YYYYNYYYYYNYYNNNNYNNYYYNNYNYYNNYNNYNYYNYYYYYYNNNY",
        "YYNNYYNYNYYYYYNYYYNNNNYNNYNYYYYNNNYYYNYNYNYYNYYNY", "NYYYNYNYYNNNYNYYYNNYYNYNYYYYYYNYYYNNYNYYYYYYYYYNN",
        "YYNYYNYNNYYYYNNYYYNYNNNYNYNNNYYYNYNNYYYNYNYYYNNYY", "YYYNYYYYNYYYYYYNYYNNYYNYNYYNYNYYYYYYNYYYYYNYNYYYN",
        "YYNYYNYYNYYYYYYNYYYNNYYYYNYYYNNYYNNYYNYYNNNYYYYNY", "NYYYYYYNYYYNYNYNYNYYYNNYYNYYNYYYNYYYYYNYNNYNYYNYY",
        "YNNNYYNNNYYYNYYNNNYYYNNYNYNYYYNNYNYNYYYNYYNYNNNNN", "YNYYYNNNYYYYNYNYNYYYNNYNYYNYNYYYYYYYYNNYNYYYYYYYN",
        "YYNYYNYNYYYYNYYYYNYYNNYNYYNNYYYNYNYNYNNYYNNYNYYNY", "YNYYYYNNYYYYYYYYYYYYYYNYYNYNYNYYYYYYNNYNYNNNYYYNY",
        "NYNYNNYYNYYYNNNYNYYYYNYYYYYYYNNYYYYYYYNYYYNNYYNNY", "NYNNNYYYYYNNNYYYYYYNYNNNNNYYYYNYYNYYNYYNYNYYNYYYY",
        "NYYNNNYNYYYYYYNNYNNNNYNNNNYYNNYYNYYNYYYNYYYYYNNYN", "YYYNNYNYNYYYNNYNNYYYYYNYYYNNYNYYNYYNYYNNYYYNYNNYY",
        "YYYNYYYYYYYYYYNYNNYNNYYYYYNYYNNNNNNYYNYNYNNNYYYNY", "NYNYNNYYYYYYNNYYNNYNNYYYNNYYYYYYYYNYNYYNNYYYYNYYN" };
    System.out.println(egalitarianism.maxDifference(isFriend3, 783));// 1566?

  }

}
