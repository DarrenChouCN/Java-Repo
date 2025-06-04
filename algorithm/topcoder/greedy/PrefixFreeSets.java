package topcoder.greedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
PrefixFreeSets 
  A prefix-free set is a set of words such that no element in the set is a prefix of another element of the set. For example {"hello"} , {"hello", "goodbye", "giant", "hi"} and the empty set are examples of prefix-free sets. On the other hand, {"hello","hell"} and {"great","gig","g"} are not prefix-free.

  You will be given a String[] words containing a set of words, and you must return the number of elements in the largest prefix-free subset of words.
 */
public class PrefixFreeSets {

  public int maxElements(String[] words) {
    int count = 0;
    Arrays.sort(words);

    for (int i = 0; i < words.length; i++) {
      if (i + 1 < words.length && words[i + 1].startsWith(words[i])) {
        continue;
      }
      count++;
    }
    return count;
  }

  public static void main(String[] args) {
    PrefixFreeSets sets = new PrefixFreeSets();
    String[] words1 = { "hello", "hi", "h", "run", "rerun", "running" };
    System.out.println(sets.maxElements(words1));
    String[] words2 = { "a", "b", "cba", "cbc", "cbb", "ccc" };
    String[] words3 = { "a", "ab", "abc", "abcd", "abcde", "abcdef" };
    String[] words4 = { "topcoder", "topcoder", "topcoding" };
    System.out.println(sets.maxElementsDFS(words2));
    System.out.println(sets.maxElementsDFS(words3));
    System.out.println(sets.maxElementsDFS(words4));
  }

  public int maxElementsDFS(String[] words) {
    int n = words.length;

    // Build graph
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      graph.add(new ArrayList<>());
    }
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (i != j && words[j].startsWith(words[i])) {
          graph.get(i).add(j);
        }
      }
    }

    boolean[] visited = new boolean[n];
    int count = 0;
    for (int i = 0; i < n; i++) {
      if (!visited[i]) {
        dfs(i, graph, visited);
        count++;
      }
    }
    return count;
  }

  private void dfs(int node, List<List<Integer>> graph, boolean[] visited) {
    visited[node] = true;
    for (int neighbor : graph.get(node)) {
      if (!visited[neighbor]) {
        dfs(neighbor, graph, visited);
      }
    }
  }
}
