package topcoder;

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

  public int numWays(int N, int M, int K) {
    return 0;
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
