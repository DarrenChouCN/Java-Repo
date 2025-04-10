package block3.ForestGarbage;

import java.util.*;

public class ForestPath {

  static class State implements Comparable<State> {
    int row, col, gCount, uCount;

    public State(int r, int c, int g, int u) {
      row = r;
      col = c;
      gCount = g;
      uCount = u;
    }

    @Override
    public int compareTo(State other) {
      if (this.gCount != other.gCount) {
        return Integer.compare(this.gCount, other.gCount);
      } else {
        return Integer.compare(this.uCount, other.uCount);
      }
    }
  }

  public int[] bestWay(String[] forest) {
    int rows = forest.length;
    int cols = forest[0].length();
    int[] start = new int[2], end = new int[2];
    boolean[][] unsafe = new boolean[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        char c = forest[i].charAt(j);
        if (c == 'S') {
          start = new int[] { i, j };
        } else if (c == 'F') {
          end = new int[] { i, j };
        } else if (c == '.') {
          for (int[] dir : new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }) {
            int x = i + dir[0];
            int y = j + dir[1];
            if (x >= 0 && x < rows && y >= 0 && y < cols && forest[x].charAt(y) == 'g') {
              unsafe[i][j] = true;
              break;
            }
          }
        }
      }
    }

    int[][][] dist = new int[rows][cols][2];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Arrays.fill(dist[i][j], Integer.MAX_VALUE);
      }
    }
    dist[start[0]][start[1]][0] = 0;
    dist[start[0]][start[1]][1] = 0;

    PriorityQueue<State> pq = new PriorityQueue<>();
    pq.offer(new State(start[0], start[1], 0, 0));

    int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    while (!pq.isEmpty()) {
      State curr = pq.poll();
      int r = curr.row;
      int c = curr.col;

      if (r == end[0] && c == end[1]) {
        return new int[] { curr.gCount, curr.uCount };
      }

      if (curr.gCount > dist[r][c][0] ||
          (curr.gCount == dist[r][c][0] && curr.uCount > dist[r][c][1])) {
        continue;
      }

      for (int[] d : dirs) {
        int nr = r + d[0];
        int nc = c + d[1];
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols)
          continue;

        char cell = forest[nr].charAt(nc);
        int newG = curr.gCount;
        int newU = curr.uCount;

        if (cell == 'g') {
          newG++;
        } else if (cell == '.' || cell == 'S' || cell == 'F') {
          if (cell == '.' && unsafe[nr][nc]) {
            newU++;
          }
        }

        if (newG < dist[nr][nc][0] ||
            (newG == dist[nr][nc][0] && newU < dist[nr][nc][1])) {
          dist[nr][nc][0] = newG;
          dist[nr][nc][1] = newU;
          pq.offer(new State(nr, nc, newG, newU));
        }
      }
    }

    return new int[] { -1, -1 };
  }

  public static void main(String[] args) {
    ForestPath fPath = new ForestPath();
    String[] test1 = { "S............",
        "gggggggggggg.",
        ".............",
        ".gggggggggggg",
        ".............",
        "gggggggggggg.",
        ".F.g...g...g.",
        ".....g...g..." };
    int[] result = fPath.bestWay(test1);
    System.out.println(Arrays.toString(result));
  }
}