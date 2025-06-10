package topcoder.hard;

/*
 LightSwitches

  In a ballroom, there are some light bulbs and some switches that control those bulbs. However, the switches and the bulbs are not related in a logical way. Each switch is connected to a subset of the bulbs, and when a switch is flipped, all the bulbs connected to that switch change state (the ones that were off are turned on, and the ones that were on are turned off). Bulbs that are not connected to the flipped switch remain as they were.

  The states of all the bulbs in the room can be described as a configuration of lights. If there are N bulbs, there are 2N possible configurations. Two configurations are distinct if a bulb in one configuration has a different state than that same bulb in the other configuration.

  You will be given the connections between the switches and bulbs as a String[] switches. The jth character of the ith element of switches is 'Y' if the ith switch is connected to the jth bulb, and 'N' otherwise. All the bulbs are initially off. Return the number of distinct configurations of lights that can be achieved using this setup.
 */
public class LightSwitches {

  public long countPossibleConfigurations(String[] switches) {
    int m = switches.length;
    int n = switches[0].length();

    long[] vec = new long[m];
    for (int i = 0; i < m; i++) {
      long mask = 0L;
      for (int j = 0; j < n; j++) {
        if (switches[i].charAt(j) == 'Y')
          mask |= 1L << j;
      }
      vec[i] = mask;
    }

    int rank = 0;
    for (int col = 0; col < n; col++) {
      int pivot = -1;
      for (int row = rank; row < m; row++) {
        if (((vec[row] >>> col) & 1L) == 1L) {
          pivot = row;
          break;
        }
      }
      if (pivot == -1)
        continue;

      long tmp = vec[pivot];
      vec[pivot] = vec[rank];
      vec[rank] = tmp;

      for (int row = 0; row < m; row++) {
        if (row != rank && (((vec[row] >>> col) & 1L) == 1L)) {
          vec[row] ^= vec[rank];
        }
      }
      rank++;
    }

    return 1L << rank;
  }

  public static void main(String[] args) {
    LightSwitches lightSwitches = new LightSwitches();
    String[] switches = { "NYYNNNNYNNYNNNYYYNY", "YYYNNYNYYYNNNYYNNYY", "YNNNNYNYYNNNYNYNNNN", "NYNYYYYNNNNNYNYNNYY",
        "NNNYNYYYYYYYNNYYYNY", "NNNNNNYYYNNYNYNNYYN", "NYNYYYNNNNYYYYNYNYY", "NYNNNYNYYYYYNYNYNNY",
        "NNYNYNYNYNNYNNYNYNN", "NYYYYYNNYNNYYYYYNNN", "YYYNYYNYNYYYYNYNNYY", "YYNYYYNYNNNNYNYNNYY",
        "YYNYNNNYYYNNYNYNNYN", "NYNNYNYYYYYYNYYNNYN", "YNNNNNNNNNYYYNYNNNN", "NYYNYYYYNYYNNYNNYYY",
        "NYYYNYYYYNNNYNYNYYN", "YYYNYYYNNYNNNYNYNNN", "YYNNYNNNNNYYNNYNYYY", "YNYYNNYYYNYYYYNYYYY",
        "NNNNNYYNNYYYYYNYYYY" };
    System.out.println(lightSwitches.countPossibleConfigurations(switches));
  }

}
