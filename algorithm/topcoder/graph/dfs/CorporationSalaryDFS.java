package topcoder.graph.dfs;

import java.util.ArrayList;
import java.util.List;

/*
CorporationSalary 

  You are working in the HR department of a huge corporation. Each employee may have several direct managers and/or several direct subordinates. Of course, his subordinates may also have their own subordinates, and his direct managers may have their own managers. We say employee X is a boss of employee Y if there exists a sequence of employees A, B, ..., D, such that X is the manager of A, A is the manager of B, and so on, and D is the manager of Y (of course, if X is a direct manager of employee Y, X will be a boss of employee Y). If A is a boss of B, then B can not be a boss of A. According to the new company policy, the salary of an employee with no subordinates is 1. If an employee has any subordinates, then his salary is equal to the sum of the salaries of his direct subordinates.

  You will be given a String[] relations, where the j-th character of the i-th element is 'Y' if employee i is a direct manager of employee j, and 'N' otherwise. Return the sum of the salaries of all the employees.
 */
public class CorporationSalaryDFS {

  // DFS
  public long totalSalary(String[] relations) {
    int n = relations.length;
    // Build graph
    // int type uses a List<List<Integer>> adjacency list to represent the graph
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (relations[i].charAt(j) == 'Y') {
          graph.get(i).add(j);
        }
      }
    }

    // **DFS must use memory search, otherwise it will be time out**
    long[] memo = new long[n];
    long totalSalary = 0;
    for (int i = 0; i < n; i++) {
      totalSalary += dfs(i, graph, memo);
    }

    return totalSalary;
  }

  private long dfs(int i, List<List<Integer>> g, long[] memo) {
    if (memo[i] > 0)
      return memo[i];
    if (g.get(i).isEmpty())
      return memo[i] = 1;
    long sum = 0;
    for (int sub : g.get(i)) {
      sum += dfs(sub, g, memo);
    }
    return memo[i] = sum;
  }

  public static void main(String[] args) {
    CorporationSalaryDFS corporationSalary = new CorporationSalaryDFS();
    String[] relations = {
        "NNNNNN",
        "YNYNNY",
        "YNNNNY",
        "NNNNNN",
        "YNYNNN",
        "YNNYNN"
    };
    System.out.println(corporationSalary.totalSalary(relations));
  }
}
