package topcoder.graph.bfs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TheGraph {

  /*
   * Markus is working in the Tree Research Center. It may come as a surprise that
   * the trees they research are not the ones you can see in a park. Instead, they
   * are researching special graphs. (See Notes for definitions of terms related
   * to these trees.)
   * 
   * Markus's daily job is reconstructing trees, given some partial information
   * about them. Today Markus faced a difficult task. He needed to find the
   * maximum possible diameter of a tree, given the following information:
   * 
   * Some vertex in the tree is called V.
   * The distance between V and the farthest vertex from V is D.
   * For each x between 1 and D, inclusive, Markus knows the number of vertices
   * such that their distance from V is x.
   * You are given a int[] cnt containing D strictly positive integers. For each
   * i, the i-th element of cnt is equal to the number of vertices which have
   * distance i+1 from V. Please help Markus with his task. Return the maximum
   * possible diameter of a tree that matches the given information.
   */
  public int maxD(int[] cnt) {
    List<List<Integer>> tree = buildGraph(cnt);
    return bfsDiameter(tree);
  }

  private List<List<Integer>> buildGraph(int[] cnt) {
    List<List<Integer>> adj = new ArrayList<>();
    int nodeId = 0;
    adj.add(new ArrayList<>());
    nodeId++;

    List<Integer> prevLayer = new ArrayList<>();
    prevLayer.add(0);

    for (int i = 0; i < cnt.length; i++) {
      int numNodes = cnt[i];
      List<Integer> currLayer = new ArrayList<>();

      for (int j = 0; j < numNodes; j++) {
        adj.add(new ArrayList<>());
        currLayer.add(nodeId);
        nodeId++;
      }

      int parentIndex = 0;
      for (int child : currLayer) {
        if (parentIndex >= prevLayer.size())
          parentIndex = 0;
        int parent = prevLayer.get(parentIndex++);
        adj.get(parent).add(child);
        adj.get(child).add(parent);
      }

      prevLayer = currLayer;
    }

    return adj;
  }

  private int bfsDiameter(List<List<Integer>> adj) {
    int[] farthest1 = bfs(adj, 0);
    int[] farthest2 = bfs(adj, farthest1[0]);

    return farthest2[1];
  }

  private int[] bfs(List<List<Integer>> adj, int start) {
    int n = adj.size();
    boolean[] visited = new boolean[n];
    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[] { start, 0 });
    visited[start] = true;

    int farthestNode = start;
    int maxDist = 0;

    while (!queue.isEmpty()) {
      int[] cur = queue.poll();
      int node = cur[0], dist = cur[1];

      if (dist > maxDist) {
        maxDist = dist;
        farthestNode = node;
      }

      for (int neighbor : adj.get(node)) {
        if (!visited[neighbor]) {
          visited[neighbor] = true;
          queue.offer(new int[] { neighbor, dist + 1 });
        }
      }
    }
    return new int[] { farthestNode, maxDist };
  }

  public static void main(String[] args) {
    TheGraph graph = new TheGraph();

    int[] cnt = { 4, 2, 1, 3, 2, 5, 7, 2, 4, 5, 2, 3, 1, 13, 6 };
    System.out.println(graph.maxD(cnt));
  }

}
