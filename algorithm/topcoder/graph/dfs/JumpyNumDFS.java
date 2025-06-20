package topcoder.graph.dfs;

/*
 JumpyNum

  A jumpy number is a positive integer, all of whose adjacent digits differ by at least 2. For example,
          
          NOT JUMPY: 28459, 28549, 1091919, 97753, 111111
              JUMPY: 290464, 13131313, 97539753, 5
  Create a class JumpyNum that contains a method howMany that is given low and high and returns the number of jumpy numbers that are between low and high, inclusive.
 */
public class JumpyNumDFS {

  private int count = 0;

  public int howManyDFS(int low, int high) {
    count = 0;

    for (int i = 1; i <= 9; i++) {
      dfs(i, low, high);
    }

    return count;
  }

  private void dfs(long num, int low, int high) {
    if (num >= low) {
      count++;
    }

    int lastDigit = (int) num % 10;
    for (int d = 0; d <= 9; d++) {
      if (Math.abs(d - lastDigit) >= 2) {
        long next = num * 10 + d;
        if (next <= high) {
          dfs(next, low, high);
        }
      }
    }
  }

  public static void main(String[] args) {
    JumpyNumDFS jumpyNum = new JumpyNumDFS();
    System.out.println(jumpyNum.howManyDFS(8000, 20934));
  }

}
