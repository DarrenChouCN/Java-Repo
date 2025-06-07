package topcoder.graph.bfs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/*
TopographicalImage

  Suppose you are standing at the highest point, called M0, of a mountainous landscape. As you look around, you wonder how many different points you could walk to starting from your lofty position without ever going uphill. The set of these points is called peak P0, and the entire landscape can be divided into peaks according to a similar definition. For i > 0, let Mi be the highest point of the landscape not contained in peaks P0 through Pi - 1, and let peak Pi be the set of points to which there is a path from Mi that never goes uphill (but may remain level) and never touches points already contained in P0 through Pi - 1. The number of peaks in the landscape is the smallest value of n for which all points of the landscape are contained in P0 through Pn - 1.

  You have a topographical map of a rectangular landscape, and you are interested in the area of its peaks. Write a class TopographicalImage with a method calcPeakAreas that takes a String[] topoData containing the height of the landscape at each x and y position and returns a int[] with the areas of each peak. The ASCII value of character x of element y of topoData is the height of the landscape at point (x,y). You can walk from a point to each of its vertical, horizontal, and diagonal neighbors. The return value should have a number of elements equal to the number of peaks in the landscape, and element i should be the number of points in Pi. If there is a tie between multiple points for maximum height when choosing Mi, choose the point with the smallest y-coordinate. If there is still a tie between points with the same y-coordinate, choose the point with the smallest x-coordinate.
 */
public class TopographicalImage {

  int[] dx = { -1, 1, 0, 0, -1, -1, 1, 1 };
  int[] dy = { 0, 0, -1, 1, -1, 1, -1, 1 };

  public int[] calcPeakAreas(String[] topoData) {
    int rows = topoData.length;
    int cols = topoData[0].length();

    int[][] height = new int[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        height[i][j] = topoData[i].charAt(j);// ASCII value
      }
    }

    boolean[][] visited = new boolean[rows][cols];

    // int[]{x,y,h}
    PriorityQueue<int[]> pq = new PriorityQueue<>(
        (a, b) -> {
          if (a[2] != b[2])
            return b[2] - a[2];// high -> low
          if (a[1] != b[1])
            return a[1] - b[1];// y is at the front
          return a[0] - b[0]; // x is at the front
        });

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        pq.offer(new int[] { i, j, height[i][j] });
      }
    }

    List<Integer> areas = new ArrayList<>();
    Queue<int[]> queue = new LinkedList<>();
    while (!pq.isEmpty()) {
      int[] start = pq.poll();
      int sx = start[0];
      int sy = start[1];

      if (visited[sx][sy])
        continue;

      visited[sx][sy] = true;
      queue.offer(start);

      int area = 0;
      while (!queue.isEmpty()) {
        int[] cur = queue.poll();
        int cx = cur[0];
        int cy = cur[1];
        int ch = cur[2];

        area++;
        for (int dir = 0; dir < 8; dir++) {
          int nx = cx + dx[dir];
          int ny = cy + dy[dir];

          if (nx < 0 || nx >= rows || ny < 0 || ny >= cols)
            continue;

          if (visited[nx][ny])
            continue;

          if (height[nx][ny] <= ch) {
            visited[nx][ny] = true;
            queue.offer(new int[] { nx, ny, height[nx][ny] });
          }
        }
      }
      areas.add(area);
    }

    int[] result = new int[areas.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = areas.get(i);
    }
    return result;
  }

  public static void main(String[] args) {
    TopographicalImage image = new TopographicalImage();

    String[] topoData = { "cL8W@*/.jyu0_XN{5ba7{.aag}%5!LR*B&2sZkIK{/HzhgnZOs" };

    System.err.println(Arrays.toString(image.calcPeakAreas(topoData)));
  }
}
