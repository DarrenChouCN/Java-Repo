package topcoder;

public class WarehouseRenovation {
  public int countDark(String[] roomPlan) {
    int R = roomPlan.length;
    int C = roomPlan[0].length();
    boolean[][] lit = new boolean[R][C];

    for (int i = 0; i < R; i++) {
      boolean lampOn = false;
      for (int j = 0; j < C; j++) {
        char ch = roomPlan[i].charAt(j);
        if (ch == '#')
          lampOn = false;
        else {
          if (ch == 'O')
            lampOn = true;
          if (lampOn)
            lit[i][j] = true;
        }
      }
    }
    for (int i = 0; i < R; i++) {
      boolean lampOn = false;
      for (int j = C - 1; j >= 0; j--) {
        char ch = roomPlan[i].charAt(j);
        if (ch == '#')
          lampOn = false;
        else {
          if (ch == 'O')
            lampOn = true;
          if (lampOn)
            lit[i][j] = true;
        }
      }
    }
    for (int j = 0; j < C; j++) {
      boolean lampOn = false;
      for (int i = 0; i < R; i++) {
        char ch = roomPlan[i].charAt(j);
        if (ch == '#')
          lampOn = false;
        else {
          if (ch == 'O')
            lampOn = true;
          if (lampOn)
            lit[i][j] = true;
        }
      }
    }
    for (int j = 0; j < C; j++) {
      boolean lampOn = false;
      for (int i = R - 1; i >= 0; i--) {
        char ch = roomPlan[i].charAt(j);
        if (ch == '#')
          lampOn = false;
        else {
          if (ch == 'O')
            lampOn = true;
          if (lampOn)
            lit[i][j] = true;
        }
      }
    }

    int dark = 0;
    for (int i = 0; i < R; i++) {
      for (int j = 0; j < C; j++) {
        if (roomPlan[i].charAt(j) == '.' && !lit[i][j]) {
          dark++;
        }
      }
    }
    return dark;
  }
}
