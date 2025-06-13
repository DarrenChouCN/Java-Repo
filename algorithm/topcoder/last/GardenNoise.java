package topcoder.last;

/*
GardenNoise

  All ducks make the same sound: "quack". Each duck makes the sound one or more times, one after another. For example, valid sounds for a single duck are "quack", "quackquackquackquack", "quackquack", etc.

  You have lost count of how many ducks are in your garden.  You have recorded the sounds, obtaining a String of characters. When you later listened to the recording, you realised that the quacking of each individual duck forms a (not necessarily contiguous) subsequence of the recording.  You also noticed that each letter in the recording belongs to exactly one duck. For example, if there were two ducks, you may have recorded "quqacukqauackck".

  You are given a String s that contains an arbitrary recording. Find and return the smallest number of ducks that could have produced the recording. Note that it is possible that the given recording is not a valid recording of quacking ducks. In such case, return -1.
 */
public class GardenNoise {

  public int countDucks(String s) {
    if (s == null || s.length() % 5 != 0)
      return -1;

    int[] stage = new int[4];
    int active = 0, maxActive = 0;

    for (char ch : s.toCharArray()) {
      switch (ch) {
        case 'q':// 新鸭子/新一轮 quack 的开始
          stage[0]++;
          active++;
          maxActive = Math.max(active, maxActive);
          break;
        case 'u':
          if (stage[0] == 0)
            return -1;
          stage[0]--;
          stage[1]++;
          break;
        case 'a':
          if (stage[1] == 0)
            return -1;
          stage[1]--;
          stage[2]++;
          break;
        case 'c':
          if (stage[2] == 0)
            return -1;
          stage[2]--;
          stage[3]++;
          break;
        case 'k':
          if (stage[3] == 0)
            return -1;
          stage[3]--;
          active--;
          break;
        default:
          return -1;
      }
    }

    for (int cnt : stage)
      if (cnt != 0)
        return -1;

    return active == 0 ? maxActive : -1;
  }

}
