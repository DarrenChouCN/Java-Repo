package block3.CastleGuards;

import java.util.HashMap;
import java.util.Map;

/*
CastleGuards

  We have a rectangular castle. The first floor is protected by some number of guards. We want to have at least one guard in each row and in each column. You are given a String[] castle. The j-th character of the i-th element of castle is either '.' (free cell) or uppercase 'X' (guard). Return the smallest number of additional guards we have to place in the castle to achieve our goal.
 */
public class CastleGuards {

  public int missing(String[] castle) {
    int rowCount = 0;
    Map<Integer, Boolean> map = new HashMap<>();
    for (String str : castle) {
      if (!str.contains("X")) {
        rowCount++;
      }

      for (int i = 0; i < str.length(); i++) {
        if (str.charAt(i) == 'X') {
          map.put(i, true);
        }
      }
    }

    int colCount = castle[0].length() - map.size();

    return Math.max(rowCount, colCount);
  }

  public static void main(String[] args) {
    CastleGuards guards = new CastleGuards();
    String[] castle = {
        "........X..",
        "...X.......",
        "...........",
        "..X...X....",
        "...........",
        "...........",
        "........X..",
        "...........",
        "...........",
        "........X..",
        ".....X....." };
    System.out.println(guards.missing(castle));
  }

}
