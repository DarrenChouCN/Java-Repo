package block2.FibonacciDiv2;

/*
FibonacciDiv2

Problem Statement

    The Fibonacci sequence is defined as follows:
    F[0] = 0
    F[1] = 1
    for each i >= 2: F[i] = F[i-1] + F[i-2]

    Thus, the Fibonacci sequence starts as follows: 0, 1, 1, 2, 3, 5, 8, 13, ... The elements of the Fibonacci sequence are called Fibonacci numbers.

    You're given an int N. You want to change N into a Fibonacci number. This change will consist of zero or more steps. In each step, you can either increment or decrement the number you currently have. That is, in each step you can change your current number X either to X+1 or to X-1.
    Return the smallest number of steps needed to change N into a Fibonacci number.
 */

public class FibonacciDiv2 {

  public int find(int N) {
    int preNum = 0;
    int followNum = 1;
    int fibonacciNum = 0;

    while (fibonacciNum < N) {
      fibonacciNum = preNum + followNum;
      preNum = followNum;
      followNum = fibonacciNum;
    }

    int count = 0;
    if (fibonacciNum > N) {
      count = Math.min(fibonacciNum - N, N - preNum);
    }

    return count;
  }

  public static void main(String[] args) {
    FibonacciDiv2 fDiv2 = new FibonacciDiv2();
    System.out.println(fDiv2.find(1000000));
  }

}
