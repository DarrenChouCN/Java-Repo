package topcoder.greedy.FoxAndMountainEasy;

/*
BearPasswordAny
  A substring of a string is any non-empty contiguous subsequence of its characters. For example, both "abc" and "bcd" are substrings of "abcde", but "ace" is not a substring of "abcde".

  A string is called constant if all of its characters are the same. For example, "a" and "bbbbb" are constant strings, but "aba" is not a constant string.

  Two substrings of the same string are considered distinct if they start or end at a different position. For example, the string "ababab" contains three distinct copies of the substring "ab", and the string "aaaa" contains two distinct copies of the substring "aaa".

  Bear Limak is creating a new account and he needs to choose a password. His password should satisfy the following security requirements:

    The password must be a string of length N.
    Each character of the password must be either 'a' or 'b'.
    For each i between 1 and N, inclusive, the password must contain exactly x[i-1] constant substrings of length i.

  You are given the int[] x with N elements. Help Limak: find and return a valid password! If there are many valid passwords, you may return any of them. If there are no valid passwords, return "" (i.e., an empty string).
 */
public class BearPasswordAny {

  public String findPassword(int[] x) {
    return "";
  }

  public static void main(String[] args) {
    BearPasswordAny bearPasswordAny = new BearPasswordAny();
    int[] x1 = { 5, 0, 0, 0, 0 };// "ababa"
    System.err.println(bearPasswordAny.findPassword(x1));

    int[] x2 = { 4, 2, 1, 0 };// "baaa"
    System.err.println(bearPasswordAny.findPassword(x2));

    int[] x3 = { 3, 1, 1 };// ""
    System.err.println(bearPasswordAny.findPassword(x3));

    int[] x4 = { 4, 3, 2, 1 };// "aaaa"
    System.err.println(bearPasswordAny.findPassword(x4));

    int[] x5 = { 0 };// ""
    System.err.println(bearPasswordAny.findPassword(x5));

    int[] x6 = { 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// "ababababababababababababababababababababababababab"
    System.err.println(bearPasswordAny.findPassword(x6));

    int[] x7 = { 10, 5, 2, 1, 0, 0, 0, 0, 0, 0 };// "bbbbaababb"
    System.err.println(bearPasswordAny.findPassword(x7));

    int[] x8 = { 5, 0, 0, 0, 0, 0 };// ""
    System.err.println(bearPasswordAny.findPassword(x8));

    int[] x9 = { 2, 0, 0, 0, 0 };// ""
    System.err.println(bearPasswordAny.findPassword(x9));

    int[] x10 = { 4, 2, 1, 0 };// "baaa"
    System.err.println(bearPasswordAny.findPassword(x10));

    int[] x11 = { 4, 2, 0, 0 };// "aabb"
    System.err.println(bearPasswordAny.findPassword(x11));
  }
}
