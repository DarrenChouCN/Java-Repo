package block5.SentenceDecomposition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
SentenceDecomposition

  Little Bonnie and her friends were dismayed to learn that their parents were reading all of their private communications. They decided to invent a new language that would allow them to talk freely. What they finally came up with was a language where sentences are built using a special method.

  All the valid words that can be used in the new language are given in the String[] validWords. A sentence is a concatenation (with no spaces) of a sequence of valid words. Each valid word can appear 0 or more times in the sentence. What makes the language special is that each word can be transformed by rearranging its letters before being used. The cost to transform a word is defined as the number of character positions where the original word and the transformed word differ. For example, "abc" can be transformed to "abc" with a cost of 0, to "acb", "cba" or "bac" with a cost of 2, and to "bca" or "cab" with a cost of 3.

  Although several different sequences of valid words can produce the same sentence in this language, only the sequence with the least total transformation cost carries the meaning of the sentence. The advantage of the new language is that the parents can no longer understand what the kids are saying. The disadvantage is that the kids themselves also do not understand. They need your help.

  Given a String sentence, return the total cost of transformation of the sequence of valid words which carries the meaning of the sentence, or -1 if no such sequence exists.
 */
public class SentenceDecomposition {

  public int decompose(String sentence, String[] validWords) {
    int n = sentence.length();
    Map<String, Set<String>> graph = new HashMap<>();

    for (String word : validWords) {
      char[] arr = word.toCharArray();
      Arrays.sort(arr);
      String key = new String(arr);

      if (!graph.containsKey(key)) {
        graph.put(key, new HashSet<>());
      }
      graph.get(key).add(word);
    }

    Integer[] memo = new Integer[n + 1];

    return dfs(0, sentence, graph, memo);
  }

  private int dfs(int index, String sentence, Map<String, Set<String>> graph, Integer[] memo) {
    if (index == sentence.length())
      return 0;
    if (memo[index] != null)
      return memo[index];

    int minCost = Integer.MAX_VALUE;

    for (int end = index + 1; end <= sentence.length(); end++) {
      String sub = sentence.substring(index, end);
      char[] chars = sub.toCharArray();
      Arrays.sort(chars);
      String key = new String(chars);

      if (graph.containsKey(key)) {
        for (String word : graph.get(key)) {
          if (word.length() != sub.length())
            continue;
          int cost = calcCost(sub, word);
          int next = dfs(end, sentence, graph, memo);
          if (next != -1) {
            minCost = Math.min(minCost, cost + next);
          }
        }
      }
    }

    memo[index] = (minCost == Integer.MAX_VALUE) ? -1 : minCost;
    return memo[index];
  }

  private int calcCost(String a, String b) {
    int cost = 0;
    for (int i = 0; i < a.length(); i++) {
      if (a.charAt(i) != b.charAt(i))
        cost++;
    }
    return cost;
  }

  public static void main(String[] args) {
    SentenceDecomposition decomposition = new SentenceDecomposition();
    String sentence = "ommwreehisymkiml";
    String[] validWords = { "we", "were", "here", "my", "is", "mom", "here", "si", "milk", "where", "si" };
    System.out.println(decomposition.decompose(sentence, validWords));
  }

}
