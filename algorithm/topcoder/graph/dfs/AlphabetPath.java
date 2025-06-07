package topcoder.graph.dfs;

/*
AlphabetPath

  You are given a 2-dimensional matrix of characters represented by the String[] letterMaze. The i-th character of the j-th element of letterMaze represents the character at row i and column j. Each of the 26 letters from 'A' to 'Z' appears exactly once in letterMaze, the remaining characters are periods ('.').

  An alphabet path is a sequence of 26 elements of the matrix such that:
    The first element contains the letter 'A'.
    The first element and the second element are (horizontally or vertically) adjacent.
    The second element contains the letter 'B'.
    The second element and the third element are (horizontally or vertically) adjacent.
    ...
    The 25-th element and the 26-th element are (horizontally or vertically) adjacent.
    The last, 26-th element contains the letter 'Z'.
 */
public class AlphabetPath {

  int[] dx = { -1, 1, 0, 0 };
  int[] dy = { 0, 0, -1, 1 };

  int H, W;
  char[][] g;
  Boolean[][] memo;

  public String doesItExist(String[] letterMaze) {
    H = letterMaze.length;
    W = letterMaze[0].length();
    g = new char[H][W];
    memo = new Boolean[H][W];

    int sr = -1, sc = -1;
    for (int r = 0; r < H; r++) {
      g[r] = letterMaze[r].toCharArray();
      for (int c = 0; c < W; c++) {
        if (g[r][c] == 'A') {
          sr = r;
          sc = c;
        }
      }
    }
    return dfs(sr, sc) ? "YES" : "NO";
  }

  private boolean dfs(int r, int c) {
    if (memo[r][c] != null)
      return memo[r][c];

    char cur = g[r][c];
    if (cur == 'Z')
      return memo[r][c] = true;

    char next = (char) (cur + 1);
    for (int dir = 0; dir < 4; dir++) {
      int nr = r + dx[dir];
      int nc = c + dy[dir];
      if (nr < 0 || nr >= H || nc < 0 || nc >= W)
        continue;
      if (g[nr][nc] == next && dfs(nr, nc)) {
        return memo[nr][nc] = true;
      }
    }
    return memo[r][c] = false;
  }

  public static void main(String[] args) {
    AlphabetPath path = new AlphabetPath();
    String[] letterMaze = { "..............", "..............", "..............", "...DEFGHIJK...", "...C......L...",
        "...B......M...", "...A......N...", "..........O...", "..ZY..TSRQP...", "...XWVU.......", ".............." };
    System.err.println(path.doesItExist(letterMaze));
  }

}
