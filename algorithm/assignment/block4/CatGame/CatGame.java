package block4.CatGame;

/*
CatGame

  Cat Polka likes to play with her cat friends. Each of her friends currently sits on some coordinate on a straight line that goes from the left to the right. When Polka gives a signal, each of her friends must move exactly X units to the left or to the right.

  You are given an int[] coordinates and the int X. For each i, the element coordinates[i] represents the coordinate of the i-th cat before the movement. Return the smallest possible difference between the positions of the leftmost cat and the rightmost cat after the movement.
 */
public class CatGame {

  public int getNumber(int[] coordinates, int X) {
    return 0;
  }

  public static void main(String[] args) {
    CatGame catGame = new CatGame();
    int[] coordinates = { 3, 7, 4, 6, -10, 7, 10, 9, -5 };
    int X = 7;
    System.out.println(catGame.getNumber(coordinates, X));
  }

}
