package topcoder.graph.dfs;

import java.util.ArrayList;
import java.util.List;

/*
FiringEmployees

  BottomCoder has a CEO and N employees. The CEO has number 0, the employees are numbered 1 through N. Each employee can be described by three numbers: their salary, their productivity, and the number of their direct manager. For each employee E, employee E's manager has a number smaller than E. Hence, the company has a tree-like hierarchy.

  You are given the description of BottomCoder in the int[]s manager, salary, and productivity. For each i between 1 and N, inclusive, employee i is described by the values manager[i-1], salary[i-1], and productivity[i-1].

  The profit from an employee can be computed as the employee's productivity minus their salary. Note that the profit from an employee can be negative. The total profit of the company is the sum of profits of its employees.

  The CEO would like to increase the total profit of the company by firing some of its employees. However, if he fires an employee E, he must also fire all employees who reported to E. For example, if he fires your manager, he must fire you as well. As another example, if he fires the manager of your manager, he must fire your manager and therefore he must also fire you.

  Find a valid way of firing some (possibly none, possibly all) employees that maximizes the profit of the resulting company. Return the total profit after the selected employees are fired.
 */
public class FiringEmployees {

  public int fire(int[] manager, int[] salary, int[] productivity) {
    int N = manager.length;
    int totalNodes = N + 1;

    List<List<Integer>> children = new ArrayList<>();
    for (int i = 0; i < totalNodes; i++) {
      children.add(new ArrayList<>());
    }
    for (int i = 1; i <= N; i++) {
      int p = manager[i - 1];
      children.get(p).add(i);
    }

    int[] profit = new int[totalNodes];
    for (int i = 1; i <= N; i++) {
      profit[i] = productivity[i - 1] - salary[i - 1];
    }

    long best = dfs(0, children, profit);
    return (int) best;
  }

  private long dfs(int node, List<List<Integer>> children, int[] profit) {
    long keepSum = profit[node];
    for (int child : children.get(node)) {
      keepSum += dfs(child, children, profit);
    }
    return Math.max(0L, keepSum);
  }

  public static void main(String[] args) {
    FiringEmployees employees = new FiringEmployees();
    int[] manager1 = { 0, 0, 0 };
    int[] salary1 = { 1, 2, 3 };
    int[] productivit1 = { 3, 2, 1 };
    System.out.println(employees.fire(manager1, salary1, productivit1));

    int[] manager2 = { 0, 1, 2, 1, 2, 3, 4, 2, 3 };
    int[] salary2 = { 5, 3, 6, 8, 4, 2, 4, 6, 7 };
    int[] productivit2 = { 2, 5, 7, 8, 5, 3, 5, 7, 9 };
    System.out.println(employees.fire(manager2, salary2, productivit2));

    int[] manager3 = { 0, 1, 0, 2, 4, 0, 2, 7, 8, 7, 7, 5, 5, 1, 14, 14, 9, 1, 15, 16, 6, 6, 10, 12, 20, 22, 14, 12, 21,
        14, 0, 27, 9, 9, 28, 5, 0, 33, 19, 4, 40, 3, 19, 43, 3, 2, 10, 20, 39, 8, 48, 46, 14, 47, 8, 49, 25, 57, 45, 39,
        50, 49, 43, 61, 23, 15, 59, 59, 50, 4, 4, 29, 20, 10, 50, 41, 49, 72, 68, 28, 21, 50, 20, 21, 70, 48, 45, 28,
        63, 42, 7, 27, 2, 15, 64, 79, 1, 7, 61, 84, 16, 30, 43 };
    int[] salary3 = { 2778, 8336, 6650, 28, 7764, 3427, 5212, 6430, 2863, 3136, 4023, 8168, 5012, 7374, 3785, 4325,
        6414, 8981, 6863, 7282, 7085, 6506, 1314, 3896, 8815, 365, 1088, 7179, 5404, 2400, 9677, 13, 8095, 571, 7468,
        2903, 6653, 281, 3866, 6620, 8032, 5772, 710, 7857, 4587, 4684, 1529, 8830, 8271, 6716, 7797, 2246, 2922, 7489,
        9842, 1501, 125, 5857, 2228, 1937, 6438, 5408, 8859, 1238, 5819, 1012, 8777, 5764, 8607, 4819, 7370, 6997, 9471,
        5500, 5645, 8140, 7670, 8465, 9356, 8612, 9300, 5569, 3312, 1802, 4879, 8737, 8523, 3417, 2925, 5625, 3453,
        5551, 974, 4931, 8661, 7982, 2960, 9669, 2927, 1341, 3377, 9108, 9180 };
    int[] productivit3 = { 6916, 5387, 1422, 8691, 3927, 9173, 5369, 5783, 5124, 3930, 3059, 1394, 8043, 4422, 8538,
        8316, 3527, 9957, 9171, 2306, 6328, 847, 5858, 9583, 3368, 4044, 6809, 5789, 2652, 9933, 3369, 6227, 7540, 1435,
        6602, 3318, 757, 4287, 9690, 8441, 8118, 4482, 8928, 9498, 6966, 6220, 2872, 9504, 3369, 6341, 724, 2847, 3556,
        7765, 2351, 7035, 4915, 3744, 8366, 1433, 9229, 1475, 4396, 8236, 4429, 5929, 2405, 4614, 6841, 5129, 7918,
        3325, 2184, 9773, 5591, 2955, 8083, 198, 8805, 3623, 7344, 4341, 3811, 5662, 1306, 9445, 3466, 8283, 7638, 2601,
        1900, 7469, 7132, 8934, 164, 8900, 3774, 7191, 6467, 2091, 5543, 7446, 8419 };
    System.out.println(employees.fire(manager3, salary3, productivit3));
  }

}
