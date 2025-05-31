package leetcode.class26;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

/*  
MinRange

https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/

  You have k lists of sorted integers in non-decreasing order. Find the smallest range that includes at least one number from each of the k lists.
  We define the range [a, b] is smaller than range [c, d] if b - a < d - c or a < c if b - a == d - c.

从每个数组中挑一个数，使得这组数中最大值和最小值之间的区间 [min, max] 最小。
*/
public class MinRange {

  public static class Node {
    public int val;
    public int arr;
    public int idx;

    public Node(int value, int arrIndex, int index) {
      val = value;
      arr = arrIndex;// 数组位置
      idx = index;// 元素在所属数组的位置
    }
  }

  public static class NodeComparator implements Comparator<Node> {

    // 值不等则从小到大排序，相等则以节点在所属数组的位置排序
    @Override
    public int compare(Node a, Node b) {
      return a.val != b.val ? (a.val - b.val) : (a.arr - b.arr);
    }
  }

  // TreeSet: 不允许有重复元素的有序集合
  public int[] smallestRange(List<List<Integer>> nums) {
    int N = nums.size();
    TreeSet<Node> set = new TreeSet<>(new NodeComparator());

    // 每个数组的第一个元素加入TreeSet
    for (int i = 0; i < N; i++) {
      set.add(new Node(nums.get(i).get(0), i, 0));
    }

    int range = Integer.MAX_VALUE;
    int start = -1;
    int end = -1;

    // 防止出现数组长度不等的情况
    while (set.size() == N) {
      Node max = set.last();
      Node min = set.pollFirst();

      // 有区间，立即更新
      if (max.val - min.val < range) {
        range = max.val - min.val;
        start = min.val;
        end = max.val;
      }

      // 找到刚刚被移除的最小元素所在数组的下一个元素，并将它加入 TreeSet
      if (min.idx < nums.get(min.arr).size() - 1) {
        int nextIdx = min.idx + 1;
        int nextValue = nums.get(min.arr).get(min.idx + 1);
        set.add(new Node(nextValue, min.arr, nextIdx));
      }
    }

    return new int[] { start, end };
  }

  // PriorityQueue
  public static int[] smallestRangePQ(List<List<Integer>> nums) {
    PriorityQueue<Node> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
    int maxValue = Integer.MIN_VALUE;

    for (int i = 0; i < nums.size(); i++) {
      int val = nums.get(i).get(0);
      minHeap.offer(new Node(val, i, 0));
      maxValue = Math.max(maxValue, val);
    }

    int range = Integer.MAX_VALUE;
    int start = -1;
    int end = -1;

    while (minHeap.size() == nums.size()) {
      Node miNode = minHeap.poll();
      int minVal = miNode.val;

      if (maxValue - minVal < range) {
        range = maxValue - minVal;
        start = minVal;
        end = maxValue;
      }

      if (miNode.idx + 1 < nums.get(miNode.arr).size()) {
        int nextVal = nums.get(miNode.arr).get(miNode.idx + 1);
        minHeap.offer(new Node(nextVal, miNode.arr, miNode.idx + 1));
        maxValue = Math.max(maxValue, nextVal);
      } else {
        break;
      }

    }

    return new int[] { start, end };
  }

}
