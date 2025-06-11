package topcoder.enumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
SimpleDuplicateRemover 

  We have a sequence of integers. We want to remove duplicate elements from it.

  You will be given a int[] sequence. For each element that occurs more than once leave only its rightmost occurrence. All unique elements must be copied without changes.
 */
public class SimpleDuplicateRemover {

  // 对于每个数字，只保留最后一次出现的位置的那一个，其它重复项删除。
  public int[] process(int[] sequence) {
    Set<Integer> seen = new HashSet<>();
    List<Integer> result = new ArrayList<>();

    // 从后往前遍历，先保留最右侧的元素
    for (int i = sequence.length - 1; i >= 0; i--) {
      int num = sequence[i];
      if (!seen.contains(num)) {
        seen.add(num);
        result.add(num);
      }
    }

    // 翻转回正序
    Collections.reverse(result);

    // 转换为 int[]
    int[] finalResult = new int[result.size()];
    for (int i = 0; i < result.size(); i++) {
      finalResult[i] = result.get(i);
    }

    return finalResult;
  }

  public static void main(String[] args) {
    SimpleDuplicateRemover remover = new SimpleDuplicateRemover();
    int[] sequence = { 1, 5, 5, 1, 6, 1 };
    System.out.println(Arrays.toString(remover.process(sequence)));
  }
}
