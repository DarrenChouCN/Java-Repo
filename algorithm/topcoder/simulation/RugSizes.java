package topcoder.simulation;

/*
RugSizes

Problem Statement

  Rugs come in various sizes. In fact, we can find a rug with any integer width and length, except that no rugs have a distinct width and length that are both even integers. For example, we can find a 4x4 rug, but not a 2x4 rug. We want to know how many different choices we have for a given area.

  Create a class RugSizes the contains a method rugCount that is given the desired area and returns the number of different ways in which we can choose a rug size that will cover that exact area. Do not count the same size twice -- a 6 x 9 rug and a 9 x 6 rug should be counted as one choice.

Analysis

  According to the question, there are two restricted conditions we need to consider:

    1. The situation that can not meet the requirement, which is the width and length are both even and distinct. The pseudo code is :

      width != length && (width % 2 == 0 && length % 2 == 0)

    2. Consider the repetition, for example , 6 x 9 and 9 x 6 belong to the same case. So we just need to pick one edge and only consider the cases where this edge is smaller or equal to the other.

Thought process

  Enumerates all values of an edge starting from 1. The modulus of area and width is used to determine whether the width is a valid rug size:

  1. If the current width is valid, calculate the other edge's length using "area/width", and only consider cases where "width <= length" to remove repetitions. In this case, check whether width and length meet the constraint, which is that width and length cannot both be even and distinct:

    a. If they meet the constraint, then ignore the width.
    b. If they do not meet the constraint, then this width is an available result and increment the result count by 1.

  2. If the current width is invalid, ignore it.


Complexity

  Time and space complexity : O(n) and O(1)

 */
public class RugSizes {

  public int rugCount(int area) {

    int rugCount = 0;

    if (area <= 0 || area > 100000)
      return rugCount;

    for (int width = 1; width <= area; width++) {
      if (area % width == 0) {
        int length = area / width;
        if (width <= length) {
          if (width != length && (width % 2 == 0 && length % 2 == 0)) {
            continue;
          }
          rugCount++;
        }
      }
    }

    return rugCount;
  }

  public static void main(String[] args) {
    RugSizes sizes = new RugSizes();
    int area4 = sizes.rugCount(4);
    int area8 = sizes.rugCount(8);

    System.out.println("area4: " + area4 + ", area8: " + area8);
  }

}
