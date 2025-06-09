package topcoder.dp;

/*
UnsealtheSafe

Problem Statement

A door of a safe is locked by a password. Josh witnessed an employee opening the safe. Here is the information Josh spied.

  1. The password is a sequence containing exactly N digits..
  2. The password is entered using the keypad shown in the picture below.
  3. Every pair of neighboring digits in the password is adjacent on the keypad. Two digits are adjacent on the keypad if they are distinct and have a common edge.

    1 2 3
    4 5 6
    7 8 9
    0 

 Josh has evil intentions of unsealing the safe, but before he can realize his plan, he wants to know how many different passwords exist. Given the value for N, return the number of possible passwords that satisfy all the constraints given above.


This problem I initially tried to use a recursive approach, but when I input 25, it cannot output the result, which means the recursive stack is too massive and cannot calculate the result. So, I decided to use dynamic programming. (The recursive implementation still remains in the Java code.)

Thought process:

  1. Create a dynamic programming table. The row represents the digit, which spans from 0 to 9; the column represents the step count, which means calculating current digit, how many steps are needed.

  2. Initialize the dp table. The first column is filled with 1, which means there is only 1 step needed when n = 1.

  3. Fill the dp table, starting with the first step and digit 0:
    a. Calculate the current digit's neighbors and examine the validity.
    b. If the neighbor is a valid digit, update the dp table: the row that represents the next digit, and the column that represents step + 1, should be incremented by the number of ways to reach the current digit at the current step.

  4. After finishing filling the dp table, calculate the sum of the N steps for all starting digits.

 */
public class UnsealTheSafe {

  private final int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

  private final int[][] keypad = {
      { 1, 2, 3 },
      { 4, 5, 6 },
      { 7, 8, 9 },
      { 0, -1, -1 }
  };

  public long countPasswords(int N) {
    if (N < 1)
      return 0;

    // row represents digit(0-9), column represents step(1-N)
    long[][] dp = new long[10][N + 1];

    for (int i = 0; i < 10; i++) {
      dp[i][1] = 1;
    }

    for (int step = 1; step < N; step++) {
      for (int digit = 0; digit <= 9; digit++) {
        int row = digit == 0 ? 3 : (digit - 1) / 3;
        int col = digit == 0 ? 0 : (digit - 1) % 3;

        for (int[] dir : directions) {
          int newRow = row + dir[0];
          int newCol = col + dir[1];

          if (newRow < 0 || newRow >= keypad.length)
            continue;
          if (newCol < 0 || newCol >= keypad[newRow].length)
            continue;

          int nextDigit = keypad[newRow][newCol];
          if (nextDigit == -1)
            continue;
          dp[nextDigit][step + 1] += dp[digit][step];
        }
      }
    }

    long total = 0;
    for (int i = 0; i <= 9; i++) {
      total += dp[i][N];
    }

    return total;
  }

  public static void main(String[] args) {
    UnsealTheSafe safe = new UnsealTheSafe();
    System.out.println(safe.countPasswords(3));
  }

  // private long recursion(int[][] keypad, int N, int row, int col, int curDigit)
  // {
  // if (curDigit == N) {
  // return 1;
  // }
  // long curCount = 0;
  // for (int i = 0; i < directions.length; i++) {
  // int dx = row + directions[i][0];
  // int dy = col + directions[i][1];
  // if (dx < 0 || dx >= keypad.length)
  // continue;
  // if (dy < 0 || dy >= keypad[dx].length)
  // continue;
  // if (keypad[dx][dy] == -1)
  // continue;
  // curCount += recursion(keypad, N, dx, dy, curDigit + 1);
  // }
  // return curCount;
  // }

  // private long countPasswordsRecursion(int N) {
  // if (N < 2 || N > 30) {
  // return 0;
  // }

  // int count = 0;
  // for (int i = 0; i <= 9; i++) {
  // int row = i / 3;
  // int col = i % 3;
  // count += recursion(keypad, N, row, col, 1);
  // }
  // return count;
  // }

}
