package block3.ForestGarbage;

import java.util.Arrays;
import java.util.PriorityQueue;

/*
Problem Statement

 	It's Sunday morning and you decide to take your girlfriend for a romantic trip. You don't have time to plan anything fancy, so your only option is a trip through a small rectangular forest surrounded by skyscrapers. You know that somewhere, deep inside the forest, there is a flower. You are eager to venture with your girlfriend into the heart of the forest and show her your discovery. Unfortunately, people have left their garbage all over the forest, and you suspect the following about your girlfriend:
  - She really hates walking through garbage.
  - She doesn't feel very comfortable walking along the garbage either.

  You know where all the garbage is, so you want to plan out your path ahead of time. The String[] forest contains the map of the forest, where each character represents a cell. 'S' represents your starting point, 'F' represents the flower's location, 'g' represents a cell filled with garbage, and '.' is a clean empty cell. Your goal is to find a path from 'S' to 'F' that contains the fewest possible garbage-filled cells. You may only move one cell at a time, horizontally or vertically. If there are multiple such paths, you want to choose one from among them with the fewest empty cells that have at least one garbage-filled neighbor. Two cells are neighbors if they are horizontally or vertically adjacent. Note that the 'S' and 'F' cells do not count as empty cells.

  Return a int[] containing exactly two elements. The first element is the number of garbage-filled cells in the path. The second element is the number of empty cells in the path that have at least one garbage-filled neighbor.
*/

/*
Problem

  1. Initially, I was unsure whether this problem should be solved using dynamic programming, greedy strategy, or graph algorithms. I was concerned that using dynamic programming might require a high-dimensional DP table and result in complex implementation, and I couldn’t find a proper greedy strategy to apply.

  2. I attempted to solve the problem using BFS, but it led to incorrect results.

  3. I suspected that this was a graph search problem and that it might require a modified version of Dijkstra’s algorithm. However, this problem involves two cost metrics, whereas the classic Dijkstra algorithm handles only a single weight. I was unsure how to modify the algorithm and how to compare dual weights effectively.

  4. I wasn’t sure what kind of data structure to use—specifically, whether I needed to define a custom class to store the state of each node (such as coordinates, number of garbage count, number of unsafe count, etc.).

  5. I was unclear whether Java’s PriorityQueue could handle “composite state priority”—i.e., selecting priority based on two different criteria (garbage count first, then unsafe count).

  6. I was confused about how to calculate the number of unsafe count—whether it should be determined at the moment a node is visited by checking if any of its four neighbors are garbage count, and then incrementing the count accordingly.
 */

/*
Process:
  1. parseForest(forest): 
    Parses the original array and extracts key information: the start and end point, and the table of unsafe areas.

  2. initDpTable(): 
    Initializes the 3D dynamic programming table, defines the meaning of each dimension and the meaning of values at each position. The x and y represent the row and column coordinates of a certain node, and z = 0 represents garbage cells, z = 1 represents unsafe cells. dp[x][y][0] and dp[x][y][1] represent the minimum number of garbage cells and the minimum number of unsafe areas on the shortest path from the starting point to the current node, respectively.

  3. Override Comparable method: 
    Define the rule: when garbageCount is not equal, choose the one with a smaller garbageCount; when they are equal, choose the one with a smaller unsafeCount.

  4. dijkstra(): 
    Rewrite the Dijkstra algorithm to fill the dp table, using a PriorityQueue to select the optimal path each time according to the comparison rules defined above: 
      a) Determine whether the current dequeued node is the destination; if yes, return the result immediately.
      b) Rewrite the classic Dijkstra weight comparison to: only when "the garbage count of the current node's path is smaller than the value stored at the corresponding position in the dp table", or "when the garbage count is equal, and the unsafe area count is smaller", it is considered a potentially shortest-path node; otherwise, skip it.
      c) For the neighbors (up, down, left, right) of the current node, determine whether the neighbor is garbage or an unsafe area, recalculate the two key values of the current node, and compare them with the values stored in the dp table. Only when both values are strictly smaller, the current node is considered a potential shortest-path node. In this case, update the dp table and add the node to the priority queue.
      d) Loop through the priority queue until it's empty. If no result is returned by the end of the loop, it means no path was found.
 */
public class ForestGarbage {

  private static final int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

  private String[] forest;

  private int rows, cols;
  private Node start, end;

  private boolean[][] unsafe;
  private int[][][] dp;

  public int[] bestWay(String[] forest) {

    parseForest(forest);

    initDpTable();

    return dijkstra();
  }

  private void parseForest(String[] forest) {
    this.forest = forest;
    rows = forest.length;
    cols = forest[0].length();
    unsafe = new boolean[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        char c = forest[i].charAt(j);
        if (c == 'S') {
          start = new Node(i, j, 0, 0);
        }
        if (c == 'F') {
          end = new Node(i, j, 0, 0);
        }
        if (c == '.') {
          for (int[] dir : directions) {
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
  }

  private void initDpTable() {
    dp = new int[rows][cols][2];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Arrays.fill(dp[i][j], Integer.MAX_VALUE);
      }
    }

    dp[start.row][start.col][0] = 0;
    dp[start.row][start.col][1] = 0;
  }

  private int[] dijkstra() {
    PriorityQueue<Node> pq = new PriorityQueue<>();
    pq.offer(start);

    while (!pq.isEmpty()) {
      Node cur = pq.poll();
      if (cur.row == end.row && cur.col == end.col) {
        return new int[] { cur.garbageCount, cur.unsafeCount };
      }

      if (cur.garbageCount > dp[cur.row][cur.col][0] ||
          (cur.garbageCount == dp[cur.row][cur.col][0] && cur.unsafeCount > dp[cur.row][cur.col][1])) {
        continue;
      }

      for (int[] d : directions) {
        int neighborRow = cur.row + d[0];
        int neighborCol = cur.col + d[1];

        if (neighborRow < 0 || neighborRow >= rows || neighborCol < 0 || neighborCol >= cols)
          continue;

        char cell = forest[neighborRow].charAt(neighborCol);
        int newG = cur.garbageCount;
        int newU = cur.unsafeCount;

        if (cell == 'g') {
          newG++;
        } else if (cell == '.' || cell == 'S' || cell == 'F') {
          if (cell == '.' && unsafe[neighborRow][neighborCol]) {
            newU++;
          }
        }

        if (newG < dp[neighborRow][neighborCol][0] ||
            (newG == dp[neighborRow][neighborCol][0] && newU < dp[neighborRow][neighborCol][1])) {
          dp[neighborRow][neighborCol][0] = newG;
          dp[neighborRow][neighborCol][1] = newU;
          pq.offer(new Node(neighborRow, neighborCol, newG, newU));
        }
      }
    }
    return new int[] { -1, -1 };
  }

  static class Node implements Comparable<Node> {
    int row, col, garbageCount, unsafeCount;

    public Node(int row, int col, int garbageCount, int unsafeCount) {
      this.row = row;
      this.col = col;
      this.garbageCount = garbageCount;
      this.unsafeCount = unsafeCount;
    }

    @Override
    public int compareTo(Node other) {
      if (this.garbageCount != other.garbageCount) {
        return Integer.compare(this.garbageCount, other.garbageCount);
      } else {
        return Integer.compare(this.unsafeCount, other.unsafeCount);
      }
    }
  }

  public static void main(String[] args) {
    ForestGarbage fPath = new ForestGarbage();
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