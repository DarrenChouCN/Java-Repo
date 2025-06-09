package topcoder.dp.knapsack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Knapsack

  Given n objects, each object having a positive integer value and a positive integer weight, find a subset of these objects so the total value of the objects in the set is maximized while the total weight of the same objects is not greater than a given maximum total weight.

  If there are several subsets yielding the same best total value, pick the subset among these with the lowest total weight. If there is still a tie, pick the subset with the fewest objects. If there's a tie again, pick the subset that comes first lexicographically. If A and B are two sorted (in ascending order) int[] containing the indexes of the objects in the subsets, then A is lexicographically before B if there exist some j so that A[i]=B[i] (0<=i<j) and A[j]<B[j]. For instance, {0,1,5,8,9} is lexicographically before {0,2,3,4,5}.

  Create a class Knapsack which contains the method whichObjects which takes a int[] weight and a int[] value and returns a int[] containing the index of the objects (0-based) in the best possible subset according to the priorities above. The elements in the return value should be in increasing order. Element 0 in value and weight correspond to object 0, etc.
 */
public class Knapsack {

  private static class Node {
    int value;
    int weight;
    List<Integer> pick;

    Node(int val, int weight, List<Integer> pick) {
      this.value = val;
      this.weight = weight;
      this.pick = pick;
    }
  }

  public int[] whichObjects(int[] weight, int[] value, int maxWeight) {
    int n = weight.length;
    Node[] dp = new Node[maxWeight + 1];
    dp[0] = new Node(0, 0, new ArrayList<>());

    for (int i = 0; i < n; i++) {
      int w = weight[i], v = value[i];
      for (int cap = maxWeight; cap >= w; cap--) {
        if (dp[cap - w] == null)
          continue;
        List<Integer> newPick = new ArrayList<>(dp[cap - w].pick);
        newPick.add(i);
        Node cand = new Node(dp[cap - w].value + v, dp[cap - w].weight + w, newPick);
        dp[cap] = better(dp[cap], cand);
      }
    }
    Node best = null;
    for (Node node : dp)
      best = better(node, best);
    if (best == null)
      return new int[0];

    return best.pick.stream().mapToInt(Integer::intValue).toArray();
  }

  private Node better(Node a, Node b) {
    if (a == null)
      return b;
    if (b == null)
      return a;

    if (a.value != b.value)
      return (a.value > b.value) ? a : b;

    if (a.weight != a.weight)
      return (a.weight < b.weight) ? a : b;

    if (a.pick.size() != b.pick.size())
      return (a.pick.size() < b.pick.size()) ? a : b;

    for (int i = 0; i < a.pick.size(); i++) {
      if (!a.pick.get(i).equals(b.pick.get(i)))
        return a.pick.get(i) < b.pick.get(i) ? a : b;
    }
    return a;
  }

  public static void main(String[] args) {
    Knapsack knapsack = new Knapsack();
    int[] weight = { 415, 528, 744, 555, 526, 530, 274, 154, 769 };
    int[] value = { 428, 200, 627, 470, 891, 167, 974, 101, 770 };
    int maxWeight = 2205;
    System.out.println(Arrays.toString(knapsack.whichObjects(weight, value,
        maxWeight)));

    int[] weight1 = { 2, 4, 8, 16, 16, 4, 8, 32, 16, 16, 24, 24, 8, 16, 12 };
    int[] value1 = { 1, 2, 4, 8, 8, 2, 4, 16, 8, 8, 12, 12, 4, 8, 6 };
    int maxWeight1 = 100;
    System.out.println(Arrays.toString(knapsack.whichObjects(weight1, value1,
        maxWeight1)));

    // out of memory
    // int[] weight2 = { 1, 3, 7, 14, 22, 46, 80, 215, 359, 942, 1522, 2990, 7090,
    // 12351, 26743, 48893, 85629, 235492,
    // 362968, 770528, 1800660, 3381064, 6988830, 13036376, 33340767, 51545964,
    // 104661498, 71929479, 103512379,
    // 95429577, 106459433, 125206458, 120525428, 72406746 };
    // int[] value2 = { 1, 4, 5, 14, 20, 48, 80, 219, 359, 937, 1522, 2990, 7090,
    // 12349, 26748, 48896, 85634, 235491,
    // 362969, 770531, 1800656, 3381067, 6988833, 13036375, 33340766, 51545969,
    // 104661499, 71929477, 103512383,
    // 95429573, 106459436, 125206463, 120525431, 72406742 };
    // int maxWeight2 = 338585106;
    // System.out.println(Arrays.toString(knapsack.whichObjects(weight2, value2,
    // maxWeight2)));
  }

}
