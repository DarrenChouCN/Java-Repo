package topcoder.dp.sequenceDP;

/*
ColorfulRoad

  There is a one-dimensional road. The road is separated into N consecutive parts. The parts are numbered 0 through N-1, in order. Ciel is going to walk from part 0 to part N-1.

  Ciel also noticed that each part of the road has a color: either red, green, or blue. Part 0 is red.

  Ciel is going to perform a sequence of steps. Each step must lead in the positive direction. That is, if her current part is i, the next step will take her to one of the parts i+1 through N-1, inclusive. Her steps can be arbitrarily long. However, longer steps are harder: a step of length j costs j*j energy.

  Additionally, Ciel wants to step on colors in a specific order: red, green, blue, red, green, blue, ... That is, she starts on the red part 0, makes a step to a green part, from there to a blue part, and so on, always repeating red, green, and blue in a cycle. Note that the final part N-1 also has some color and thus Ciel must reach it in a corresponding step.

  You are given a String road containing N elements. For each i, element i of road is the color of part i: 'R' represents red, 'G' green, and 'B' blue. If Ciel can reach part N-1 in the way described above, return the smallest possible total cost of doing so. Otherwise, return -1.
 */
public class ColorfulRoad {

  public int getMinColor(String road) {
    int n = road.length();
    int[] dp = new int[n];
    final int INF = Integer.MAX_VALUE;

    for (int i = 0; i < dp.length; i++) {
      dp[i] = INF;
    }
    dp[0] = 0;

    for (int i = 0; i < n; i++) {
      if (dp[i] == INF)
        continue;

      char currentColor = road.charAt(i);
      for (int j = i + 1; j < n; j++) {
        char nextColor = road.charAt(j);
        if (isNextColor(currentColor, nextColor)) {
          int cost = (j - i) * (j - i) + dp[i];
          dp[j] = Math.min(dp[j], cost);
        }
      }
    }
    return dp[n - 1] == INF ? -1 : dp[n - 1];
  }

  private boolean isNextColor(char current, char next) {
    if (current == 'R' && next == 'G')
      return true;
    if (current == 'G' && next == 'B')
      return true;
    if (current == 'B' && next == 'R')
      return true;
    return false;
  }

  public static void main(String[] args) {
    ColorfulRoad colorfulRoad = new ColorfulRoad();
    System.out.println(colorfulRoad.getMinColor("RGGGB"));// 8
    System.out.println(colorfulRoad.getMinColor("RGBRGBRGB"));// 8
    System.out.println(colorfulRoad.getMinColor("RBBGGGRR"));// -1
    System.out.println(colorfulRoad.getMinColor("RBRRBGGGBBBBR"));// 50
    System.out.println(colorfulRoad.getMinColor("RG"));// 1
    System.out.println(colorfulRoad.getMinColor("RBRGBGBGGBGRGGG"));// 52
  }
}
