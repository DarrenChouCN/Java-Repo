package topcoder.enumeration;

/*
RunLengthEncoding

  Run-length encoding is a simple compression technique which compresses strings of letters by replacing repeated consecutive letters (called runs) by the number of occurrences of the letter, followed by that letter. For example, AAAABBBCDDE compresses to 4A3BC2DE. The number 1 may be omitted in runs consisting of a single letter, as with letters 'C' and 'E' in the previous example.

  Any string consisting of uppercase letters where each letter is optionally preceded by a positive integer is called a properly encoded string. Given a properly encoded string text, return the decoded string. If the decoded string would be more than 50 characters long, return "TOO LONG" (without the quotes).
 */
public class RunLengthEncoding {

  public String decode(String text) {
    int n = text.length();
    StringBuilder builder = new StringBuilder();
    int i = 0;
    while (i < n) {
      long count = 0;
      while (i < n && Character.isDigit(text.charAt(i))) {
        count = count * 10 + (text.charAt(i) - '0');
        if (count > 50) {
          return "TOO LONG";
        }
        i++;
      }

      if (count == 0)
        count = 1;

      char ch = text.charAt(i++);
      if (builder.length() + count > 50) {
        return "TOO LONG";
      }

      for (int j = 0; j < count; j++) {
        builder.append(ch);
      }
    }

    return builder.toString();
  }

  public static void main(String[] args) {
    RunLengthEncoding encoding = new RunLengthEncoding();
    System.out.println(encoding.decode("123456789012345678901234567890B"));
  }

}
