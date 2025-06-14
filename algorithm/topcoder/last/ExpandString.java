package topcoder.last;

import java.util.ArrayDeque;
import java.util.Deque;

/*
ExpandString 

  Uncompress the given string s in the following manner: Find a section that matches the pattern "k(q)" (quotes for clarity only), where k is a single digit, the two parentheses are a matching set, and q is zero or more characters. Replace the entire section with k consecutive occurrences of q. Repeat this process until there are no more such patterns. Return the length of the uncompressed string.
 */
public class ExpandString {

  public int howLong(String s) {
    Deque<long[]> stack = new ArrayDeque<>(); // {prevLen, k}
    long currLen = 0;
    int n = s.length();

    for (int i = 0; i < n;) {
      char ch = s.charAt(i);

      // (1) 发现数字且后面紧跟 '(' → 这是 "k("
      if (Character.isDigit(ch) && i + 1 < n && s.charAt(i + 1) == '(') {
        int k = ch - '0';
        stack.push(new long[] { currLen, k });
        currLen = 0; // 进入新的括号层
        i += 2; // 跳过 "k("
      }
      // (2) 右括号，结束当前层
      else if (ch == ')') {
        long[] top = stack.pop();
        long prevLen = top[0], mult = top[1];
        currLen = prevLen + mult * currLen;
        i++; // 跳过 ')'
      }
      // (3) 其它情况：普通字符（包括裸数字）
      else {
        currLen++;
        i++;
      }
    }
    return (int) currLen; // 题目保证 ≤ 2 147 483 647
  }

  public static void main(String[] args) {
    ExpandString expandString = new ExpandString();
    System.out.println(expandString.howLong("7(55(1652)85(1)899)128193(689)8586685"));
  }

}
