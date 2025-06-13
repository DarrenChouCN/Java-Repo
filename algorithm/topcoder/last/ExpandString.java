package topcoder.last;

import java.util.ArrayDeque;
import java.util.Deque;

/*
ExpandString 

  Uncompress the given string s in the following manner: Find a section that matches the pattern "k(q)" (quotes for clarity only), where k is a single digit, the two parentheses are a matching set, and q is zero or more characters. Replace the entire section with k consecutive occurrences of q. Repeat this process until there are no more such patterns. Return the length of the uncompressed string.
 */
public class ExpandString {

  public static int howLong(String s) {
    Deque<Long> lenStack = new ArrayDeque<>();
    Deque<Integer> kStack = new ArrayDeque<>();

    long curLen = 0;
    int pendingK = -1;

    for (char ch : s.toCharArray()) {
      if (Character.isDigit(ch)) {
        pendingK = ch - '0';
      } else if (ch == '(') {
        lenStack.push(curLen);
        kStack.push(pendingK);
        curLen = 0;
        pendingK = -1;
      } else if (ch == ')') {
        long inner = curLen;
        int k = kStack.pop();
        curLen = lenStack.pop() + k * inner;
      } else {
        curLen++;
      }
    }

    return (int) curLen;
  }

}
