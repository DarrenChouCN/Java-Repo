package model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class Graph {

  // Recursive DFS for Graph Traversal
  // Applicable: Connected components, cycle detection, topological sort, islands
  // count, etc.
  void dfs1(int u, List<List<Integer>> graph, boolean[] visited) {
    if (visited[u])
      return;
    visited[u] = true;

    // === Pre-order logic ===
    // e.g. collect answer, mark discovery time

    for (int v : graph.get(u)) {
      dfs1(v, graph, visited);
    }

    // === Post-order logic ===
    // e.g. push to stack for topological sort

  }

  // Iterative DFS using explicit stack
  // Applicable: When recursion is not allowed or you need to control visiting
  // order
  void dfsIterative(int start, List<List<Integer>> graph) {
    boolean[] visited = new boolean[graph.size()];
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(start);

    while (!stack.isEmpty()) {
      int u = stack.pop();
      if (visited[u])
        continue;
      visited[u] = true;
      // Process u...

      // Push children, order matters if traversal order is important
      for (int v : graph.get(u))
        stack.push(v);
    }
  }

  // BFS from a single source node in a graph
  // Applicable: Shortest path, level order traversal, min steps to reach target,
  // etc.
  int bfs(int src, List<List<Integer>> graph) {
    int n = graph.size();
    int[] dist = new int[n];
    Arrays.fill(dist, -1);

    Queue<Integer> queue = new ArrayDeque<>();
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
    return 0;
  }

  //// BFS from multiple source nodes
  // Applicable: Spread from multiple starting points, shortest distance to any
  // source
  int multiBfs(List<Integer> sources, List<List<Integer>> graph) {
    int n = graph.size();
    int[] dist = new int[n];
    Arrays.fill(dist, -1);

    Queue<Integer> queue = new ArrayDeque<>();
    for (int s : sources) {
      queue.offer(s);
      dist[s] = 0;
    }

    while (!queue.isEmpty()) {
      int u = queue.poll();
      for (int v : graph.get(u)) {
        if (dist[v] == -1) {
          dist[v] = dist[u] + 1;
          queue.offer(v);
        }
      }
    }

    return 0;
  }

}
