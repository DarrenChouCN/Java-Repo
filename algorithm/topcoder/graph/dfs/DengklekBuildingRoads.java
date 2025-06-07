package topcoder.graph.dfs;

/*
DengklekBuildingRoads

Mr. Dengklek lives in the Kingdom of Ducks, where humans and ducks live together in peace and harmony.

  There are N duck houses in the kingdom, conveniently numbered 1 through N. Currently, there are no roads between the houses. The king assigned Mr. Dengklek to build exactly M bidirectional roads, each connecting a pair of houses.

  The king imposed two rules on Mr. Dengklek:

    Let A and B be two different houses. Mr. Dengklek may build roads connecting these two houses if and only if 0 < |A-B| <= K. After the road is built, both house number A and house number B are said to be incident to the road. For each such pair of houses Mr. Dengklek may build multiple roads, each connecting the two houses.
    Each house must be incident to an even number of roads. (Zero is also an even number.)

  You are given the ints N, M, and K. Return the number of different ways Mr. Dengklek can build the roads, modulo 1,000,000,007. Two ways are different if there are two houses that are connected by a different number of roads. 
 */

// Build M roads between N houses, such that each house has an even number of incident roads (including 0). A road can only be built between two houses A and B if their house numbers satisfy: 0<|A-B|<=K. Also, multiple roads can be built between the same pair of houses.
public class DengklekBuildingRoads {

  static final int MOD = 1_000_000_007;

  static long[] fact, invFact;

  static long modPow(long a, long e) {
    long r = 1;
    while (e > 0) {
      if ((e & 1) == 1)
        r = r * a % MOD;
      a = a * a % MOD;
      e >>= 1;
    }
    return r;
  }

  static void buildFact(int up) {
    fact = new long[up + 1];
    invFact = new long[up + 1];
    fact[0] = 1;
    for (int i = 1; i <= up; i++)
      fact[i] = fact[i - 1] * i % MOD;
    invFact[up] = modPow(fact[up], MOD - 2);
    for (int i = up; i > 0; i--)
      invFact[i - 1] = invFact[i] * i % MOD;
  }

  static long C(int n, int k) {
    if (k < 0 || k > n)
      return 0;
    return fact[n] * invFact[k] % MOD * invFact[n - k] % MOD;
  }

  public int numWays(int N, int M, int K) {
    if (K == 0)
      return M == 0 ? 1 : 0;

    long E = 0;
    for (int d = 1; d <= K; d++)
      E += Math.max(0, N - d);

    int maxCombN = (M / 2) + (int) E;
    buildFact(maxCombN);

    int maxMask = 1 << K;
    int[][] cur = new int[maxMask][M + 1];
    cur[0][0] = 1;

    for (int pos = 0; pos < N; pos++) {
      int avail = Math.min(K, N - pos - 1);
      int subAll = 1 << avail;

      int[][] next = new int[maxMask][M + 1];

      for (int mask = 0; mask < maxMask; mask++) {
        for (int s = 0; s <= M; s++) {
          int ways = cur[mask][s];
          if (ways == 0)
            continue;
          int bit0 = mask & 1;

          for (int sub = 0; sub < subAll; sub++) {
            int pc = Integer.bitCount(sub);
            if (((bit0 ^ (pc & 1)) != 0))
              continue;
            int s2 = s + pc;
            if (s2 > M)
              continue;

            int newMask = mask >> 1;
            newMask ^= sub;

            long add = (next[newMask][s2] + ways) % MOD;
            next[newMask][s2] = (int) add;
          }
        }
      }
      cur = next;
    }

    int[] c = cur[0];

    long ans = 0;
    for (int s = 0; s <= M; s++) {
      if (((M - s) & 1) != 0)
        continue;
      int T = (M - s) >> 1;
      long comb = C((int) (T + E - 1), (int) (E - 1));
      ans = (ans + (long) c[s] * comb) % MOD;
    }
    return (int) ans;
  }

  public static void main(String[] args) {
    DengklekBuildingRoads roads = new DengklekBuildingRoads();
    System.out.println(roads.numWays(3, 4, 1));
    System.out.println(roads.numWays(4, 3, 3));
    System.out.println(roads.numWays(2, 1, 1));
    System.out.println(roads.numWays(5, 0, 3));
    System.out.println(roads.numWays(10, 20, 5));
  }

}
