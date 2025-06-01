package leetcode.class26;

import java.util.ArrayList;
import java.util.List;

/*
Expression Add Operators

https://leetcode.com/problems/expression-add-operators/

  Given a string num that contains only digits and an integer target, return all possibilities to insert the binary operators '+', '-', and/or '*' between the digits of num so that the resultant expression evaluates to the target value.

  Note that operands in the returned expressions should not contain leading zeros.

  在一个数字字符串中插入 +、-、*，列出所有能算出目标值的表达式（不允许前导0）。
 */
public class ExpressionAddOperators {

  List<String> res = new ArrayList<>();
  String num;
  int target;

  public List<String> addOperators(String num, int target) {
    this.num = num;
    this.target = target;
    // index, left, cur, expr
    dfs(0, 0, 0, new StringBuilder(), true);
    return res;
  }

  public void dfs(int index, long left, long cur, StringBuilder expression, boolean isFirst) {
    if (index == num.length()) {
      // 模拟运算优先级，
      // left和cur为各自计算完成的部分，也就是乘法完成的部分；如果是全乘法，那么其中一个必为0
      if (left + cur == target) {
        res.add(expression.toString());
      }
      return;
    }

    long right = 0;
    int len = expression.length();
    for (int i = index; i < num.length(); i++) {
      if (i > index && num.charAt(index) == '0')
        break;
      // 作为前缀推进一位
      right = right * 10 + (num.charAt(i) - '0');

      if (isFirst) {
        expression.append(right);
        dfs(i + 1, 0, right, expression, false);
        // 通过setLength截取字符串，达到回溯dfs的效果
        // 比如 "1+2" 尝试"*3"，失败后通过setLength直接可以回溯成"1+2"，有裁剪字符串的效果
        expression.setLength(len);
      } else {
        expression.append('+').append(right);
        dfs(i + 1, left + cur, right, expression, false);
        expression.setLength(len);

        expression.append('-').append(right);
        dfs(i + 1, left + cur, -right, expression, false);
        expression.setLength(len);

        // 乘法在此处注意优先级
        expression.append('*').append(right);
        dfs(i + 1, left, cur * right, expression, false);
        expression.setLength(len);
      }
    }
  }

}
