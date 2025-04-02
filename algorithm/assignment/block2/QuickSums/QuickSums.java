package block2.QuickSums;

/*
QuickSums

Problem Statement

    Given a string of digits, find the minimum number of additions required for the string to equal some target number. Each addition is the equivalent of inserting a plus sign somewhere into the string of digits. After all plus signs are inserted, evaluate the sum as usual. For example, consider the string "12" (quotes for clarity). With zero additions, we can achieve the number 12. If we insert one plus sign into the string, we get "1+2", which evaluates to 3. So, in that case, given "12", a minimum of 1 addition is required to get the number 3. As another example, consider "303" and a target sum of 6. The best strategy is not "3+0+3", but "3+03". You can do this because leading zeros do not change the result.

    Write a class QuickSums that contains the method minSums, which takes a String numbers and an int sum. The method should calculate and return the minimum number of additions required to create an expression from numbers that evaluates to sum. If this is impossible, return -1.

 */
public class QuickSums {
  private String mNumbers;
  private int mSum;
  private int mMinPlusCount;

  public int minSums(String numbers, int sum) {
    this.mNumbers = numbers;
    this.mSum = sum;
    this.mMinPlusCount = Integer.MAX_VALUE;
    recursion(0, 0, 0);
    return mMinPlusCount == Integer.MAX_VALUE ? -1 : mMinPlusCount;
  }

  private void recursion(int index, int currentSum, int plusCount) {
    if (index == mNumbers.length()) {
      if (currentSum == mSum) {
        mMinPlusCount = Math.min(mMinPlusCount, plusCount);
      }
      return;
    }

    for (int i = index; i < mNumbers.length(); i++) {
      String numStr = mNumbers.substring(index, i + 1);
      int num = Integer.parseInt(numStr);
      if (currentSum + num > mSum) {
        break;
      }
      if (index == 0) {
        recursion(i + 1, num, plusCount);
      } else {
        recursion(i + 1, currentSum + num, plusCount + 1);
      }
    }
  }

  public static void main(String[] args) {
    QuickSums quickSums = new QuickSums();
    System.out.println(quickSums.minSums("99999", 45));
    System.out.println(quickSums.minSums("1110", 3));
    System.out.println(quickSums.minSums("0123456789", 45));
    System.out.println(quickSums.minSums("99999", 100));
    System.out.println(quickSums.minSums("382834", 100));
    System.out.println(quickSums.minSums("9230560001", 71));
  }
}
