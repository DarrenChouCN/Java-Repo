package topcoder.recursion;

/*
MonstersValley2
 
Problem Statement

    Manao is traversing a valley inhabited by monsters. During his journey, he will encounter several monsters one by one. The scariness of each monster is a positive integer. Some monsters may be scarier than others. The i-th (0-based index) monster Manao will meet has scariness equal to dread[i].

    Manao is not going to fight the monsters. Instead, he will bribe some of them and make them join him. To bribe the i-th monster, Manao needs price[i] gold coins. The monsters are not too greedy, therefore each value in price will be either 1 or 2.

    At the beginning, Manao travels alone. Each time he meets a monster, he first has the option to bribe it, and then the monster may decide to attack him. A monster will attack Manao if and only if he did not bribe it and its scariness is strictly greater than the total scariness of all monsters in Manao's party. In other words, whenever Manao encounters a monster that would attack him, he has to bribe it. If he encounters a monster that would not attack him, he may either bribe it, or simply walk past the monster.

    Consider this example: Manao is traversing the valley inhabited by the Dragon, the Hydra and the Killer Rabbit. When he encounters the Dragon, he has no choice but to bribe him, spending 1 gold coin (in each test case, Manao has to bribe the first monster he meets, because when he travels alone, the total scariness of monsters in his party is zero). When they come by the Hydra, Manao can either pass or bribe her. In the end, he needs to get past the Killer Rabbit. If Manao bribed the Hydra, the total scariness of his party exceeds the Rabbit's, so they will pass. Otherwise, the Rabbit has to be bribed for two gold coins. Therefore, the optimal choice is to bribe the Hydra and then to walk past the Killer Rabbit. The total cost of getting through the valley this way is 2 gold coins.
  */
public class MonstersValley2 {

  public int passOrBribe(int[] dread, int[] price, int curPosition, long curDread) {
    if (dread.length == curPosition)
      return 0;

    long bribeDread = curDread + dread[curPosition];

    if (curDread < dread[curPosition]) {
      return price[curPosition] + passOrBribe(dread, price, curPosition + 1, bribeDread);
    }

    int notBribePrice = passOrBribe(dread, price, curPosition + 1, curDread) + 0;

    int bribePrice = passOrBribe(dread, price, curPosition + 1, bribeDread) + price[curPosition];

    return Math.min(bribePrice, notBribePrice);
  }

  public int minimumPrice(int[] dread, int[] price) {
    return passOrBribe(dread, price, 0, 0);
  }

  public static void main(String[] args) {
    int[] dread1 = {
        1999999991, 1999999992, 1999999993, 1999999994,
        1999999995, 1999999996, 1999999997, 1999999998,
        1999999999, 2000000000 };
    int[] price1 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

    MonstersValley2 m = new MonstersValley2();
    int minPrice1 = m.minimumPrice(dread1, price1);

    System.out.println("Minimum price for dread1: " + minPrice1);

  }

}
