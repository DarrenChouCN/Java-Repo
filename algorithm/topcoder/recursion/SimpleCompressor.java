package topcoder.recursion;

/*
SimpleCompressor 

  A simple way to compress a string is to encode repeated consecutive substrings as a counter followed by the substring. For example, if X represents a substring, and the string contains a sequence "XX...X", we can compress the sequence as "[DX]", where D is the number of times X is repeated (D is a single digit, i.e., 1 to 9), and X is the substring being repeated.
  topcoder.bgcoder.com

  The compression can be nested. For example, "[2[3AB]]" represents "ABABABABABAB".

  Given a compressed string, return the original uncompressed string.

  The compressed string will only contain uppercase letters ('A'-'Z'), digits ('1'-'9'), and square brackets ('[', ']').

  The compressed string is guaranteed to be valid and properly formatted.
 */
public class SimpleCompressor {
  public String uncompress(String data) {
    return repeatDeepest(data);
  }

  private String repeatDeepest(String s) {
    int open = -1, close = -1;

    // 找到最右边的 '[' 和它之后第一个 ']'
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '[') {
        open = i;
      }
      if (s.charAt(i) == ']' && open != -1) {
        close = i;
        break;
      }
    }
    if (open == -1 || close == -1) {
      return s;
    }

    // 次数
    int repeat = s.charAt(open + 1) - '0';
    // 重复字符串
    String toRepeat = s.substring(open + 2, close);

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(s, 0, open);
    for (int i = 0; i < repeat; i++) {
      stringBuilder.append(toRepeat);
    }
    stringBuilder.append(s.substring(close + 1));
    return repeatDeepest(stringBuilder.toString());
  }

  public static void main(String[] args) {
    SimpleCompressor sc = new SimpleCompressor();

    System.out.println(sc.uncompress("C[6AB]C")); // CABABABABABABC
    System.out.println(sc.uncompress("C[2[3AB]]C")); // CABABABABABABC
    System.out.println(sc.uncompress("CO[1N]TEST")); // CONTEST
    System.out.println(sc.uncompress("[2[2AB]]")); // ABABABAB
    System.out.println(sc.uncompress("AAAAAAAAAA")); // AAAAAAAAAA
  }
}
