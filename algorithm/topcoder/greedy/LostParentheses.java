package topcoder.greedy;

/*
LostParentheses

  We have an arithmetic expression made up of positive integers, the + and - operators and parentheses. All the parentheses, however, have been erased by the cleaning staff and we want to calculate the minimum value the original expression may have had.

  You will be given a String e containing the expression without the parentheses. Return the minimum value the original expression could have had before the parentheses were erased.
 */
public class LostParentheses {

  // 一旦出现减号 -，后面的所有东西你都希望加括号一起减掉，这样总值才会最小
  public int minResult(String e) {
    String[] parts = e.split("-");
    int result = sum(parts[0]);

    for (int i = 1; i < parts.length; i++) {
      result -= sum(parts[i]);
    }

    return result;
  }

  private int sum(String expr) {
    String[] tokens = expr.split("\\+");
    int total = 0;
    for (String token : tokens) {
      total += Integer.parseInt(token);
    }
    return total;
  }

  public static void main(String[] args) {
    LostParentheses lostParentheses = new LostParentheses();
    System.out.println(lostParentheses.minResult("1-1+1-1+1-1+1-1+1-1+1-1+1-1+1-1+1-1+1-1+1-1+1-1+10"));
    System.out.println(lostParentheses.minResult("003-23-453+34-2324+23-142+232-008"));
    System.out.println(lostParentheses.minResult("10+20+30+40-20+40-30+20-40+50+30-20-10+60-10"));
    System.out.println(lostParentheses.minResult("00005+123-231+213+23-237"));
    System.out.println(lostParentheses.minResult("1+2-100"));
  }
}
