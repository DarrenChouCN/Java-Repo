package topcoder.graph.bfs;

import java.util.LinkedList;
import java.util.Queue;

/*
JungleCell 

  You are given the String[] jungle that describes a rectangular jungle. The jungle is divided into R rows by C columns of cells. Both rows and columns are numbered starting from 0. The character '.' represents an empty cell, the character '#' represents a green hedge (a fence or boundary formed by closely growing bushes). Implicitly, the jungle is surrounded by a green hedge which is  not given in the input.

  You start in the cell (0, 0) and your goal is to reach the cell (R-1, C-1). In each step, you can move from your current cell to one of the horizontally or vertically adjacent cells. Of course, you cannot walk into a hedge.

  Reaching your goal is currently impossible. Luckily for you, you are carrying a blade (cutter). The blade (cutter) can be used exactly once to cut into a hedge that is horizontally or vertically adjacent to your current cell.

  Find all the green hedges such that if you use the blade (cutter) to chop that hedge, it will be possible to reach the cell (R-1, C-1). Return the number of such hedges.
 */
public class JungleCell {

  int[] dr = { -1, 1, 0, 0 };
  int[] dc = { 0, 0, -1, 1 };

  public int countHedges(String[] jungle) {
    int R = jungle.length;
    int C = jungle[0].length();
    char[][] g = new char[R][C];
    for (int i = 0; i < R; i++) {
      g[i] = jungle[i].toCharArray();
    }

    if (g[0][0] == '#' || g[R - 1][C - 1] == '#')
      return 0;

    // 从起点（不砍墙）可以到达哪些格子
    boolean[][] reachS = bfs(g, 0, 0);
    // 从终点（不砍墙）可以到达哪些格子
    boolean[][] reachT = bfs(g, R - 1, C - 1);

    // 判断跨过某个'#'，两片区域是否相通
    int ans = 0;
    for (int r = 0; r < R; r++) {
      for (int c = 0; c < C; c++) {
        if (g[r][c] != '#')
          continue;

        // 计算每一个'#'的四个方向，判断两片区域同一个位置是否连通，如果连通即代表打通这个'#'可以通路
        boolean adjS = false, adjT = false;
        for (int k = 0; k < 4; k++) {
          int nr = r + dr[k], nc = c + dc[k];
          if (nr < 0 || nr >= R || nc < 0 || nc >= C)
            continue;

          if (reachS[nr][nc])
            adjS = true;
          if (reachT[nr][nc])
            adjT = true;
          if (adjS && adjT)
            break;
        }
        if (adjS && adjT)
          ans++;
      }
    }

    return ans;
  }

  private boolean[][] bfs(char[][] g, int sr, int sc) {
    int R = g.length, C = g[0].length;
    boolean[][] vis = new boolean[R][C];

    Queue<int[]> q = new LinkedList<>();
    if (g[sr][sc] == '.') {
      vis[sr][sc] = true;
      q.offer(new int[] { sr, sc });
    }

    while (!q.isEmpty()) {
      int[] cur = q.poll();
      for (int k = 0; k < 4; k++) {
        int nr = cur[0] + dr[k], nc = cur[1] + dc[k];
        if (nr < 0 || nr >= R || nc < 0 || nc >= C)
          continue;

        if (g[nr][nc] == '.' && !vis[nr][nc]) {
          vis[nr][nc] = true;
          q.offer(new int[] { nr, nc });
        }
      }
    }

    return vis;
  }

  public static void main(String[] args) {
    String[] jungle1 = {
        "..#",
        ".#.",
        "#.." };// 3
    String[] jungle2 = {
        "..##..",
        "..##..",
        "...#..",
        "..##.." };// 1

    String[] jungle3 = {
        "..##..",
        "..##..",
        "..##..",
        "..##.." };// 0

    String[] jungle4 = {
        ".....#.",
        ".###.#.",
        ".#.#...",
        ".###.##",
        ".#.#.#.",
        ".#.###.",
        "##....." };// 6

    JungleCell cell = new JungleCell();
    System.out.println(cell.countHedges(jungle1));
    System.out.println(cell.countHedges(jungle2));
    System.out.println(cell.countHedges(jungle3));
    System.out.println(cell.countHedges(jungle4));
  }

}
