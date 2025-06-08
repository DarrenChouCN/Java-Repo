package topcoder.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
CatGame

  Cat Polka likes to play with her cat friends. Each of her friends currently sits on some coordinate on a straight line that goes from the left to the right. When Polka gives a signal, each of her friends must move exactly X units to the left or to the right.

  You are given an int[] coordinates and the int X. For each i, the element coordinates[i] represents the coordinate of the i-th cat before the movement. Return the smallest possible difference between the positions of the leftmost cat and the rightmost cat after the movement.
 */
public class CatGame {

  public int getNumber(int[] coordinates, int X) {
    int n = coordinates.length;
    List<Integer> list = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      list.add(coordinates[i] - X);
      list.add(coordinates[i] + X);
    }

    Collections.sort(list);

    int res = Integer.MAX_VALUE;
    for (int i = 0; i < list.size(); i++) {
      for (int j = i; j < list.size(); j++) {
        int leftMost = list.get(i);
        int rightMost = list.get(j);

        boolean valid = true;
        for (int k = 0; k < n; k++) {
          int left = coordinates[k] - X;
          int right = coordinates[k] + X;

          if ((left < leftMost || left > rightMost) &&
              (right < leftMost || right > rightMost)) {
            valid = false;
            break;
          }
        }

        if (valid)
          res = Math.min(res, rightMost - leftMost);
      }
    }

    return res;
  }

  public static void main(String[] args) {
    CatGame catGame = new CatGame();
    int[] coordinates = { 3, 7, 4, 6, -10, 7, 10, 9, -5 };
    int X = 7;
    System.out.println(catGame.getNumber(coordinates, X));
  }

}
