package topcoder.graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Model {

  int bfsIndexAdj(List<List<Integer>> g, int start, int target) {
    int n = g.size();
    boolean[] seen = new boolean[n];
    int[] dist = new int[n];
    Arrays.fill(dist, -1);

    Queue<Integer> q = new ArrayDeque<>();
    q.offer(start);
    seen[start] = true;
    dist[start] = 0;

    while (!q.isEmpty()) {
      int cur = q.poll();
      if (cur == target)
        return dist[cur];

      for (int nxt : g.get(cur)) {
        if (!seen[nxt]) {
          seen[nxt] = true;
          dist[nxt] = dist[cur] + 1;
          q.offer(nxt);
        }
      }
    }
    return -1;
  }

  int dijkstraIndexAdj(List<List<int[]>> g, int start, int target) {
    // g.get(u) contains int[]{v, weight}
    int n = g.size();
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start] = 0;

    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    pq.offer(new int[] { start, 0 });

    while (!pq.isEmpty()) {
      int[] cur = pq.poll();
      int u = cur[0], d = cur[1];
      if (u == target)
        return d;
      if (d > dist[u])
        continue;

      for (int[] edge : g.get(u)) {
        int v = edge[0], w = edge[1];
        if (d + w < dist[v]) {
          dist[v] = d + w;
          pq.offer(new int[] { v, dist[v] });
        }
      }
    }
    return -1;
  }

  int bfsNodeAdj(Map<String, List<String>> g, String start, String target) {
    Set<String> seen = new HashSet<>();
    Map<String, Integer> dist = new HashMap<>();

    Queue<String> q = new ArrayDeque<>();
    q.offer(start);
    seen.add(start);
    dist.put(start, 0);

    while (!q.isEmpty()) {
      String cur = q.poll();
      if (cur.equals(target))
        return dist.get(cur);

      for (String nxt : g.getOrDefault(cur, List.of())) {
        if (seen.add(nxt)) {
          dist.put(nxt, dist.get(cur) + 1);
          q.offer(nxt);
        }
      }
    }
    return -1;
  }

  int dijkstraNodeAdj(Map<String, List<int[]>> g, String start, String target) {
    // g.get("A") contains int[]{ "B".hashCode(), weight }, 用字符串简化处理
    Map<String, Integer> dist = new HashMap<>();
    dist.put(start, 0);

    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
    pq.offer(new int[] { start.hashCode(), 0 });
    Map<Integer, String> idToNode = new HashMap<>();
    idToNode.put(start.hashCode(), start);

    while (!pq.isEmpty()) {
      int[] cur = pq.poll();
      int nodeHash = cur[0], cost = cur[1];
      String u = idToNode.get(nodeHash);
      if (u.equals(target))
        return cost;
      if (cost > dist.getOrDefault(u, Integer.MAX_VALUE))
        continue;

      for (int[] edge : g.getOrDefault(u, List.of())) {
        int vHash = edge[0], w = edge[1];
        String v = idToNode.getOrDefault(vHash, String.valueOf(vHash));
        int newDist = cost + w;
        if (newDist < dist.getOrDefault(v, Integer.MAX_VALUE)) {
          dist.put(v, newDist);
          idToNode.put(vHash, v); // optional: track node name
          pq.offer(new int[] { vHash, newDist });
        }
      }
    }
    return -1;
  }

  int bfsGrid(char[][] grid, int sx, int sy, int tx, int ty) {
    int m = grid.length, n = grid[0].length;
    int[][] dist = new int[m][n];
    for (int[] row : dist)
      Arrays.fill(row, -1);

    int[] dx = { -1, 1, 0, 0 };
    int[] dy = { 0, 0, -1, 1 };

    Queue<int[]> q = new ArrayDeque<>();
    q.offer(new int[] { sx, sy });
    dist[sx][sy] = 0;

    while (!q.isEmpty()) {
      int[] cur = q.poll();
      int x = cur[0], y = cur[1];
      if (x == tx && y == ty)
        return dist[x][y];

      for (int k = 0; k < 4; k++) {
        int nx = x + dx[k], ny = y + dy[k];
        if (isValid(grid, nx, ny) && dist[nx][ny] == -1) {
          dist[nx][ny] = dist[x][y] + 1;
          q.offer(new int[] { nx, ny });
        }
      }
    }
    return -1;
  }

  boolean isValid(char[][] g, int x, int y) {
    int m = g.length, n = g[0].length;
    return 0 <= x && x < m && 0 <= y && y < n && g[x][y] != '#';
  }

  int dijkstraGrid(int[][] cost, int sx, int sy, int tx, int ty) {
    int m = cost.length, n = cost[0].length;
    int[][] dist = new int[m][n];
    for (int[] row : dist)
      Arrays.fill(row, Integer.MAX_VALUE);
    dist[sx][sy] = cost[sx][sy];

    int[] dx = { -1, 1, 0, 0 };
    int[] dy = { 0, 0, -1, 1 };

    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
    pq.offer(new int[] { sx, sy, cost[sx][sy] });

    while (!pq.isEmpty()) {
      int[] cur = pq.poll();
      int x = cur[0], y = cur[1], d = cur[2];
      if (x == tx && y == ty)
        return d;
      if (d > dist[x][y])
        continue;

      for (int k = 0; k < 4; k++) {
        int nx = x + dx[k], ny = y + dy[k];
        if (isValid(cost, nx, ny)) {
          int newCost = d + cost[nx][ny];
          if (newCost < dist[nx][ny]) {
            dist[nx][ny] = newCost;
            pq.offer(new int[] { nx, ny, newCost });
          }
        }
      }
    }
    return -1;
  }

  boolean isValid(int[][] grid, int x, int y) {
    int m = grid.length, n = grid[0].length;
    return 0 <= x && x < m && 0 <= y && y < n && grid[x][y] >= 0;
  }

}
