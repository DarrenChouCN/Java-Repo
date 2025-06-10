package topcoder.hard;

/*
KebabDispatch

  Peter Parker's Kebab delivery place is located in a hilly terrain. The terrain is represented as a rectangular grid of squares, where each square either contains a building or is empty. Each empty square has an integer height between 0 and 9, inclusive.

  From each square in the grid, you can only move to adjacent squares. Two squares are adjacent if they share an edge. You can only move between two empty squares if the absolute difference of their heights is less than or equal to 1.

    if the height difference is 0, it takes 1 minute to make the move
    if the absolute height difference is 1, it takes 3 minutes.

  You can always move to a building from any of its adjacent squares and vice versa, regardless of height. This is because all buildings are taller than the highest terrain, and each building has entrances and exits for all its adjacent squares at the correct heights. Moving to or from a square containing a building takes 2 minutes.  The delivery men are allowed to enter buildings even if they are not their final destinations. Note that the kebab place itself is also a building.

  Today, each building in the area has ordered one kebab, and Peter must use his two delivery men to fulfil all the orders in the shortest total amount of time possible.

  Each delivery man can only carry one kebab at a time. This means that after each delivery, the delivery man must return to the kebab place to pick up another kebab if there are more deliveries left to do.

  You are given a String[] terrain, where the j-th character of the i-th element represents the square at row i, column j of the terrain.

    '$' represents a building from which a kebab was ordered
    'X' represents the location of the restaurant,
    the digits '0'-'9' represent the heights of empty squares.
  

  The initial time is 0. Return the minimum time in minutes at which the last delivery can be made. If it is not possible to deliver all the kebabs, return -1 instead.
 */
public class KebabDispatch {
  public int calculateMinimumTime(String[] terrain) {
    return 0;
  }

}
