package topcoder.graph.bfs;

import java.util.LinkedList;
import java.util.Queue;

/*
ArcadeManao 

  You might remember the old computer arcade games. Here is one about Manao.

  The game level is an NxM grid of equal cells. The bottom of some cells has a platform at which Manao can stand. All the cells in the bottommost row contain a platform, thus covering the whole ground of the level. The rows of the grid are numbered from 1 to N starting from the top and the columns are numbered from 1 to M starting from the left. Exactly one cell contains a coin and Manao needs to obtain it.

  Initially, Manao is standing on the ground, i.e., in the bottommost row. He can move between two horizontally adjacent cells if both contain a platform. Also, Manao has a ladder which he can use to climb. He can use the ladder to climb both up and down. If the ladder is L units long, Manao can climb between two cells (i1, j) and (i2, j) if both contain a platform and |i1-i2| &lt;= L. Note that Manao carries the ladder along, so he can use it multiple times. You need to determine the minimum ladder length L which is sufficient to acquire the coin.

  Take a look at the following picture. On this level, Manao will manage to get the coin with a ladder of length 2.

  You are given a int[] level containing N elements. The j-th character in the i-th row of level is 'X' if cell (i+1, j+1) contains a platform and '.' otherwise. You are also given ints coinRow and coinColumn. The coin which Manao seeks is located in cell (coinRow, coinColumn) and it is guaranteed that this cell contains a platform.

  Return the minimum L such that ladder of length L is enough to get the coin. If Manao can perform the task without using the ladder, return 0.
 */
public class ArcadeManao {

  int[] dx = { -1, 1, 0, 0 };
  int[] dy = { 0, 0, -1, 1 };

  public int shortestLadder(String[] level, int coinRow, int coinColumn) {
    int n = level.length;

    coinRow--;
    coinColumn--;

    for (int L = 0; L <= n; L++) {
      if (canReach(level, coinRow, coinColumn, L)) {
        return L;
      }
    }

    return -1;
  }

  private boolean canReach(String[] level, int targetRow, int targetCol, int L) {
    int n = level.length;
    int m = level[0].length();
    boolean[][] visited = new boolean[n][m];
    Queue<int[]> queue = new LinkedList<>();

    for (int j = 0; j < m; j++) {
      if (level[n - 1].charAt(j) == 'X') {
        visited[n - 1][j] = true;
        queue.offer(new int[] { n - 1, j });
      }
    }

    while (!queue.isEmpty()) {
      int[] curr = queue.poll();
      int x = curr[0];
      int y = curr[1];

      if (x == targetRow && y == targetCol) {
        return true;
      }

      for (int dir = 0; dir < 4; dir++) {
        if (dir >= 2) {
          int nx = x + dx[dir];
          int ny = y + dy[dir];

          if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
            if (level[nx].charAt(ny) == 'X' && !visited[nx][ny]) {
              visited[nx][ny] = true;
              queue.offer(new int[] { nx, ny });
            }
          }
        } else {
          for (int d = 1; d <= L; d++) {
            int nx = x + dx[dir] * d;
            int ny = y;
            if (nx < 0 || nx >= n)
              break;

            if (level[nx].charAt(ny) == 'X' && !visited[nx][ny]) {
              visited[nx][ny] = true;
              queue.offer(new int[] { nx, ny });
            }
          }
        }
      }
    }

    return false;
  }

  int targetRow;
  int targetCol;
  String[] level;

  public int shortestLadderDFS(String[] level, int coinRow, int coinColumn) {
    int n = level.length;
    int m = level[0].length();
    this.level = level;
    targetRow = --coinRow;
    targetCol = --coinColumn;
    for (int L = 0; L <= n; L++) {
      boolean[][] vis = new boolean[n][m];
      for (int j = 0; j < m; j++) {
        if (level[n - 1].charAt(j) == 'X' && !vis[n - 1][j]) {
          if (dfs(n - 1, j, L, vis)) {
            return L;
          }
        }
      }
    }

    return -1;
  }

  private boolean dfs(int x, int y, int L, boolean[][] vis) {
    if (x == targetRow && y == targetCol) {
      return true;
    }
    vis[x][y] = true;

    int n = level.length;
    int m = level[0].length();

    // left and right: one step
    for (int dir = 2; dir < 4; dir++) {
      int nx = x + dx[dir];
      int ny = y + dy[dir];

      if (nx >= 0 && ny < n && ny >= 0 && ny < m) {
        if (level[nx].charAt(ny) == 'X' && !vis[nx][ny]) {
          if (dfs(nx, ny, L, vis))
            return true;
        }
      }
    }

    for (int dir = 0; dir < 2; dir++) {
      for (int d = 1; d <= L; d++) {
        int nx = x + dx[dir] * d;
        int ny = y;
        if (nx < 0 || nx >= n)
          break;

        if (level[nx].charAt(ny) != 'X')
          continue;

        if (!vis[nx][ny] && dfs(nx, ny, L, vis)) {
          return true;
        }
      }
    }

    return false;
  }

  public static void main(String[] args) {
    ArcadeManao manao = new ArcadeManao();

    String[] level = { "X...X.X.X.XX..X..X.......", "X..X...............X.....", "XXX....X.XXX...XXX...XX..",
        "..X....X........X.......X", ".......X.................", "..X.....X..XX..X..X...X..",
        ".X...X.....X...X..X.X..X.", "..X....X.X.X......X....X.", "..X..........X.....X.XX..",
        "X.X...X....XXX.....XXX...", ".X.......XX..X.XX..X.....", "......X.XX....X..........",
        ".X..X..........X.X.......", ".......X.X.X.X....X......", "......X.X.X...X..........",
        ".XX......X..X.....X....XX", "X..........X..X..........", "......X....X..........X..",
        ".XX......X....X..X...X...", "XXXXXXXXXXXXXXXXXXXXXXXXX" };
    System.out.println(manao.shortestLadderDFS(level, 10, 20));
  }

}
