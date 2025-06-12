package topcoder.graph.dijkstra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/*
KebabDispatch

  Peter Parker's Kebab delivery place is located in a hilly terrain. The terrain is represented as a rectangular grid of squares, where each square either contains a building or is empty. Each empty square has an integer height between 0 and 9, inclusive.

  From each square in the grid, you can only move to adjacent squares. Two squares are adjacent if they share an edge. You can only move between two empty squares if the absolute difference of their heights is less than or equal to 1.

    if the height difference is 0, it takes 1 minute to make the move
    if the absolute height difference is 1, it takes 3 minutes.

  You can always move to a building from any of its adjacent squares and vice versa, regardless of height. This is because all buildings are taller than the highest terrain, and each building has entrances and exits for all its adjacent squares at the correct heights. Moving to or from a square containing a building takes 2 minutes.  The delivery men are allowed to enter buildings even if they are not their final destinations. Note that the kebab place itself is also a building.

  Today, each building in the area has ordered one kebab, and Peter must use his two delivery men to fulfil all the orders in the shortest total amount of time possible.

  Each delivery man can only carry one kebab at a time. This means that after each delivery, the delivery man must return to the kebab place to pick up another kebab if there are more deliveries left to do.

  You are given a String[] terrain, where the j-th character of the i-th element represents the square at row i, column j of the terrain.

    '$' represents a building from which a kebab was ordered
    'X' represents the location of the restaurant,
    the digits '0'-'9' represent the heights of empty squares.
  

  The initial time is 0. Return the minimum time in minutes at which the last delivery can be made. If it is not possible to deliver all the kebabs, return -1 instead.
 */
public class KebabDispatch {

  private static final int INF = Integer.MAX_VALUE;

  public int calculateMinimumTime(String[] terrain) {
    int R = terrain.length, C = terrain[0].length();
    int[][] distGrid = new int[R][C];
    for (int[] row : distGrid)
      Arrays.fill(row, INF);

    int sx = -1, sy = -1;
    List<int[]> orders = new ArrayList<>();
    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        char ch = terrain[i].charAt(j);
        if (ch == 'X') {
          sx = i;
          sy = j;
        } else if (ch == '$') {
          orders.add(new int[] { i, j });
        }
      }
    }

    dijkstra(terrain, sx, sy, distGrid);

    int n = orders.size();
    if (n == 0)
      return 0;
    int[] d = new int[n];
    for (int k = 0; k < n; k++) {
      int[] p = orders.get(k);
      d[k] = distGrid[p[0]][p[1]];
      if (d[k] >= INF)
        return -1;// 无法到达
    }
    // only one order
    if (n == 1)
      return d[0];

    long best = Long.MAX_VALUE;
    // 以下代码可以理解为二进制搜索，如果是3位则需要bfs/dfs
    // 把1左移n位，表示2个配送员共有2^n种分配方式，以这种方式展开按位枚举
    int total = 1 << n;
    for (int mask = 0; mask < total; mask++) {
      long sumA = 0, sumB = 0, maxA = 0, maxB = 0;
      for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
          // mask 的第 i 位是 1 → 属于骑手 A
          sumA += d[i];// 骑手A距离总和
          if (d[i] > maxA)
            maxA = d[i];// 更新骑手A最远距离
        } else {
          // 否则属于骑手B
          sumB += d[i];// 骑手B距离总和
          if (d[i] > maxB)
            maxB = d[i];// 更新骑手B最远距离
        }
      }
      // 当前遍历到 d[i]，但这个订单可能属于 B 配送员，而不是 A，所以它不能作为 A 的最后一单。
      // 最短路径最后一个d[i]，也就是最后一次配送只需要送，无需返回，所以需要- maxA或maxB
      long timeA = (sumA == 0) ? 0 : 2 * sumA - maxA;
      long timeB = (sumB == 0) ? 0 : 2 * sumB - maxB;
      best = Math.min(best, Math.max(timeA, timeB));
    }

    return (int) best;
  }

  private void dijkstra(String[] terrain, int sx, int sy, int[][] dist) {
    int R = terrain.length, C = terrain[0].length();
    int[] dx = { -1, 1, 0, 0 };
    int[] dy = { 0, 0, -1, 1 };

    PriorityQueue<Node> pq = new PriorityQueue<>();
    dist[sx][sy] = 0;
    pq.offer(new Node(sx, sy, 0));

    while (!pq.isEmpty()) {
      Node cur = pq.poll();
      if (cur.d != dist[cur.x][cur.y])
        continue;

      for (int dir = 0; dir < 4; dir++) {
        int nx = cur.x + dx[dir], ny = cur.y + dy[dir];
        if (nx < 0 || nx >= R || ny < 0 || ny >= C)
          continue;

        int w = edgeCost(terrain, cur.x, cur.y, nx, ny);
        if (w < 0)
          continue;
        int nd = cur.d + w;
        if (nd < dist[nx][ny]) {
          dist[nx][ny] = nd;
          pq.offer(new Node(nx, ny, nd));
        }
      }
    }

  }

  private int edgeCost(String[] t, int x1, int y1, int x2, int y2) {
    char a = t[x1].charAt(y1);
    char b = t[x2].charAt(y2);

    // 任意建筑
    if (a == 'X' || a == '$' || b == 'X' || b == '$') {
      return 2;
    }

    int h1 = a - '0', h2 = b - '0';
    int diff = Math.abs(h1 - h2);
    if (diff > 1)
      return -1;

    return (diff == 0) ? 1 : 3;
  }

  class Node implements Comparable<Node> {
    int x, y, d;

    Node(int x, int y, int d) {
      this.x = x;
      this.y = y;
      this.d = d;
    }

    @Override
    public int compareTo(Node o) {
      return Integer.compare(d, o.d);
    }

  }

  public static void main(String[] args) {
    KebabDispatch kd = new KebabDispatch();
    String[] terrain1 = {
        "3442211",
        "34$221X",
        "3442211" };
    System.out.println(kd.calculateMinimumTime(terrain1));

    String[] terrain2 = {
        "001000$",
        "$010X0$",
        "0010000" };
    System.out.println(kd.calculateMinimumTime(terrain2));

    String[] terrain3 = {
        "001000$",
        "$010X0$",
        "0010000",
        "2232222",
        "2222222",
        "111$111" };
    System.out.println(kd.calculateMinimumTime(terrain3));

    String[] terrain4 = {
        "001000$",
        "$010X0$",
        "0010000",
        "1232222",
        "2222222",
        "111$111" };
    System.out.println(kd.calculateMinimumTime(terrain4));

    String[] terrain5 = {
        "X$$",
        "$$$" };
    System.out.println(kd.calculateMinimumTime(terrain5));
  }
}
