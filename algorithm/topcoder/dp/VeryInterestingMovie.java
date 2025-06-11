package topcoder.dp;

/*
VeryInterestingMovie

  A teacher wants to take his students to see a contemporary play at the theatre. The only problem is that the students talk a lot, so the teacher doesn't want any two students in the same row to sit in adjacent seats.

  You are given a String[] seats which contains information about which seats are available for purchase. Character j of element i of seats is 'Y' if the j-th seat in row i is available, or 'N' otherwise. Return the maximum number of students who can watch the play.
 */
public class VeryInterestingMovie {
  public int maximumPupils(String[] seats) {
    int cols = seats[0].length();
    int total = 0;

    for (String row : seats) {
      int[] dp = new int[cols + 1];
      dp[0] = 0;

      for (int j = 1; j <= cols; j++) {
        // 不在第 j-1 个座位坐人，延续前一状态
        dp[j] = dp[j - 1];
        // 尝试坐在第 j-1 个座位
        if (row.charAt(j - 1) == 'Y') {
          // 如果选当前这个位置，需要 j >= 2 以避开相邻限制
          int take = 1 + (j >= 2 ? dp[j - 2] : 0);
          dp[j] = Math.max(dp[j], take);
        }
      }
      total += dp[cols];
    }

    return total;
  }

  public static void main(String[] args) {
    VeryInterestingMovie movie = new VeryInterestingMovie();
    String[] seats1 = { "YY", "YY", "YY" };
    String[] seats2 = { "NNNNN", "NNNNN", "NNNNN", "NNNNN", "NNNNN" };
    String[] seats3 = { "YYYYYYN", "YYYYNYY", "NYYYNYY", "NYYYYYN", "YYYYYYN", "NYYNYNY", "YYYYYYY" };
    System.out.println(movie.maximumPupils(seats1));
    System.out.println(movie.maximumPupils(seats2));
    System.out.println(movie.maximumPupils(seats3));
  }
}
