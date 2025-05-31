package leetcode.class26;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/*
Word Search II

https://leetcode.com/problems/word-search-ii/description/

  Given an m x n board of characters and a list of strings words, return all words on the board.

  Each word must be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once in a word.

  在一个字符网格中，找出所有可以通过相邻字母拼成的单词（每个格子只能用一次），目标单词来自给定列表。
 */
public class WordSearchII {

  public static class TrieNode {
    public TrieNode[] nexts;
    public int pass;
    public boolean end;

    public TrieNode() {
      nexts = new TrieNode[26];
      pass = 0;
      end = false;
    }
  }

  // 构建字符前缀树
  public static void fillWord(TrieNode head, String word) {
    head.pass++;
    char[] chs = word.toCharArray();
    int index = 0;
    TrieNode node = head;
    for (int i = 0; i < chs.length; i++) {
      index = chs[i] - 'a';
      if (node.nexts[index] == null) {
        node.nexts[index] = new TrieNode();
      }
      node = node.nexts[index];
      node.pass++;
    }
    node.end = true;
  }

  public static List<String> findWords(char[][] board, String[] words) {
    TrieNode head = new TrieNode();
    // HashSet 无序、查找快、不允许重复元素
    HashSet<String> set = new HashSet<>();
    // 把所有word构建到一棵前缀树中
    for (String word : words) {
      if (!set.contains(word)) {
        fillWord(head, word);
        set.add(word);
      }
    }

    List<String> ans = new ArrayList<>();
    LinkedList<Character> path = new LinkedList<>();

    for (int row = 0; row < board.length; row++) {
      for (int col = 0; col < board[0].length; col++) {
        // 枚举board中的所有位置
        process(board, row, col, path, head, ans);
      }
    }

    return ans;
  }

  public static String generatePath(LinkedList<Character> path) {
    char[] str = new char[path.size()];
    int index = 0;
    for (Character cha : path) {
      str[index++] = cha;
    }
    return String.valueOf(str);
  }

  public static int process(char[][] board, int row, int col, LinkedList<Character> path, TrieNode cur,
      List<String> res) {
    char cha = board[row][col];
    if (cha == 0) {
      return 0;// 之前走过的位置
    }

    int index = cha - 'a';
    // 如果没路 或者这条路最终字符串之前加入过结果里
    if (cur.nexts[index] == null || cur.nexts[index].pass == 0) {
      return 0;
    }

    // 没有走回头路且能登上去
    cur = cur.nexts[index];
    path.addLast(cha);// 当前位置的字符加到路径里去
    int fix = 0;// 从row和col位置出发，后续一共搞定了多少答案
    if (cur.end) {
      res.add(generatePath(path));
      cur.end = false;
      fix++;
    }

    // 往上、下、左、右，四个方向尝试
    board[row][col] = 0;
    if (row > 0) {
      fix += process(board, row - 1, col, path, cur, res);
    }

    if (row < board.length - 1) {
      fix += process(board, row + 1, col, path, cur, res);
    }

    if (col > 0) {
      fix += process(board, row, col - 1, path, cur, res);
    }

    if (col < board[0].length - 1) {
      fix += process(board, row, col + 1, path, cur, res);
    }

    board[row][col] = cha;
    path.pollLast();
    cur.pass -= fix;
    return fix;
  }

}
