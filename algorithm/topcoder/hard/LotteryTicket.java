package topcoder.hard;

/*
LotteryTicket

  Nick likes to play the lottery. The cost of a single lottery ticket is price. Nick has exactly four banknotes with values b1, b2, b3 and b4 (some of the values may be equal). He wants to know if it's possible to buy a single lottery ticket without getting any change back. In other words, he wants to pay the exact price of a ticket using any subset of his banknotes. Return "POSSIBLE" if it is possible or "IMPOSSIBLE" if it is not (all quotes for clarity).
 */
public class LotteryTicket {

  public String buy(int price, int b1, int b2, int b3, int b4) {
    int[] bills = { b1, b2, b3, b4 };
    for (int mask = 1; mask < 16; mask++) {
      int sum = 0;
      for (int i = 0; i < 4; i++) {
        if ((mask & (1 << i)) != 0) {
          sum += bills[i];
        }
      }
      if (sum == price)
        return "POSSIBLE";

    }
    return "IMPOSSIBLE";
  }

  public String isPossible(int price, int b1, int b2, int b3, int b4) {
    int[] bills = { b1, b2, b3, b4 };

    int n = bills.length;

    for (int i = 0; i < n; i++) {
      if (bills[i] == price)
        return "POSSIBLE";
      for (int j = i + 1; j < n; j++) {
        if (bills[i] + bills[j] == price)
          return "POSSIBLE";
        for (int k = j + 1; k < n; k++) {
          if (bills[i] + bills[j] + bills[k] == price)
            return "POSSIBLE";
          for (int l = k + 1; l < n; l++) {
            if (bills[i] + bills[j] + bills[k] + bills[l] == price)
              return "POSSIBLE";
          }
        }
      }
    }
    return "IMPOSSIBLE";
  }

}
