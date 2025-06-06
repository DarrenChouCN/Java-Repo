package topcoder.graph;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
GameOnABoard

  Alice and Bob are playing a game on a rectangular board. We use (i, j) to denote the j-th cell in the i-th row (0-based index). Each cell has a cost of either 0 or 1 and they are given by the String[] cost. The j-th character of i-th element in cost (0-based index) denotes the cost of cell (i, j). A path between two distinct cells (x1, y1) and (x2, y2) is a sequence of cells (c0, c1, ..., ck) such that c0=(x1, y1), ck=(x2, y2) and for each i from 0 to k-1, cells ci and ci+1 have a common side. Cost of a path is the total cost of cells on this path.

  The game is played as follows: First Alice chooses a cell (x1,y1), then Bob chooses a cell (x2,y2) which is different from (x1, y1). Finally, they compute the value L: the minimum cost of a path between (x1,y1) and (x2,y2). Alice's goal is to minimize L, and Bob's goal is to maximize L. Compute and return the value L that will be achieved if both players play optimally.
 */
public class GameOnABoard {

  int[] dx = { -1, 1, 0, 0 };
  int[] dy = { 0, 0, -1, 1 };

  public int optimalChoice(String[] cost) {
    int rows = cost.length;
    int cols = cost[0].length();

    int answer = Integer.MAX_VALUE;

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        int[][] dist = new int[rows][cols];
        for (int[] row : dist)
          Arrays.fill(row, Integer.MAX_VALUE);

        Deque<int[]> queue = new ArrayDeque<>();
        dist[i][j] = cost[i].charAt(j) - '0';
        queue.addFirst(new int[] { i, j });

        while (!queue.isEmpty()) {
          int[] curr = queue.poll();
          int x = curr[0], y = curr[1];
          int d = dist[x][y];

          for (int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if (nx < 0 || nx >= rows || ny < 0 || ny >= cols)
              continue;

            int w = cost[nx].charAt(ny) - '0';
            if (d + w < dist[nx][ny]) {
              dist[nx][ny] = d + w;
              int[] point = { nx, ny };
              if (w == 0)
                queue.addFirst(point);
              else
                queue.addLast(point);
            }
          }
        }

        int worst = 0;
        for (int x = 0; x < rows; x++) {
          for (int y = 0; y < cols; y++) {
            if (x == i && y == j)
              continue;
            worst = Math.max(worst, dist[x][y]);
          }
        }
        answer = Math.min(answer, worst);
      }
    }

    return answer;
  }

  /* --- 简单测试 --- */
  public static void main(String[] args) {
    GameOnABoard g = new GameOnABoard();
    System.out.println(
        g.optimalChoice(
            new String[] { "0110011000", "1000101110", "1100111110", "1110111101", "0001111111", "1100001101",
                "1111011100", "1010111111", "1000001000", "1010010110" }));
  }

}
