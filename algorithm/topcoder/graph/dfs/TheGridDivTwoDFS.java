package topcoder.graph.dfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
TheGridDivTwo
  John is standing at the origin of an infinite two-dimensional grid. He is going to move along this grid. During each second he can either stay where he is or he can move by one unit in one of the four cardinal directions (north, south, east, or west). Some of the grid points are blocked. John is not allowed to move to a blocked grid point.

  You are given the coordinates of the blocked grid points as int[]s x and y. For each valid i, the grid point that is x[i] units east and y[i] units north of the origin is blocked. You are also given an int k. Compute and return the maximal possible x-coordinate of a point John can reach in k seconds.
 */
public class TheGridDivTwoDFS {

  // East, West, North, South, Stay
  int[] dx = { 1, -1, 0, 0, 0 };
  int[] dy = { 0, 0, 1, -1, 0 };

  // ------------------------------------------------
  int limit;
  int maxX;
  Set<String> blockedDFS;
  Map<String, Integer> best;

  public int findDFS(int[] x, int[] y, int k) {
    maxX = 0;
    limit = k;
    blockedDFS = new HashSet<>();
    best = new HashMap<>();
    for (int i = 0; i < x.length; i++) {
      blockedDFS.add(key(x[i], y[i]));
    }

    dfs(0, 0, 0);
    return maxX;
  }

  private void dfs(int x, int y, int step) {
    maxX = Math.max(x, maxX);
    if (limit == step)
      return;

    for (int dir = 0; dir < 5; dir++) {
      int nx = x + dx[dir];
      int ny = y + dy[dir];
      String k = key(nx, ny);

      if (blockedDFS.contains(k))
        continue;

      Integer prev = best.get(k);
      if (prev != null && prev <= step + 1)
        continue;

      best.put(k, step + 1);
      dfs(nx, ny, step + 1);
    }

  }

  public String key(int x, int y) {
    return x + "," + y;
  }

  public static void main(String[] args) {
    TheGridDivTwoDFS grid = new TheGridDivTwoDFS();
    int[] x1 = { 1, 1, 1, 1 };
    int[] y1 = { -2, -1, 0, 1 };
    System.out.println(grid.findDFS(x1, y1, 4));// 2

    int[] x2 = { -1, 0, 0, 1 };
    int[] y2 = { 0, -1, 1, 0 };
    System.out.println(grid.findDFS(x2, y2, 9));// 0

    int[] x3 = { -8, -8, 5, 6, -6, -1, -3, -3, -1 };
    int[] y3 = { 8, 2, -6, 3, -3, -10, 5, -5, 9 };
    System.out.println(grid.findDFS(x3, y3, 19));// 19

    int[] x4 = { 0, 0, -2, -2, 4, 4, 2, -1, 3, -1, 2, 1, 3, 4, 0, 1, -2, -1, 4, -1, 4, 5, 3, -1, 1, 2, 0, 3, -1, 1, 0,
        -2, -2, 2, 5, 2, -2, -1, 3, 1, 4, 3, 0, 5 };
    int[] y4 = { 3, 1, -2, 1, 5, -1, 0, 5, -2, 4, -1, -1, 1, 0, -1, 2, 2, 3, 3, 0, 1, 1, 3, -2, 1, 4, 5, 5, -1, -2, -2,
        0, 4, -2, 5, 5, 3, 1, 2, 4, -2, -1, 2, -2 };
    System.out.println(grid.findDFS(x4, y4, 11));// 1

    int[] x5 = { -1, -1, 3, -1, 1, -1, 2 };
    int[] y5 = { 6, -1, -9, 2, -3, -9, -6 };
    System.out.println(grid.findDFS(x5, y5, 17));// 17
  }
}
