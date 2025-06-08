package topcoder.simulation;

/*
SquareOfDigits

Problem Statement

    You are given a String[] data representing a rectangular grid where each cell contains a digit. Find the largest square in this grid that contains the same digit in all of its corner cells. The sides of the square must be parallel to the sides of the grid. If there is more than one such largest square, pick any one of them.

    Return the number of cells in the square. Note that a single cell is also considered a square, so there will always be an answer.

Analysis

    Given a one-dimensional array where each string consists of digits of equal length, find the largest square with four identical corner values and compute its area.

Thought process: use the two-pointer approach

    This problem can be solved using a natural intelligence approach: consider the array as a 2D grid, iterate over each element, and treat it as the top-left corner of a square. Then, expand to the right and downward to determine the positions of the bottom-left, top-right, and bottom-right corners for a fixed side length. The core of the problem is to compute the positions of these four corners and check if their values are identical:
        a. If they are the same, update the maximum side length found.
        b. If they are different, continue iterating to explore larger side lengths or move to a new starting point.
    The process continues until all possible squares have been evaluated, and the maximum side length is determined to compute the final area.

Complexity

    Time Complexity:  O(row × col × sideLength)
    Space Complexity: O(1)

 */
public class SquareOfDigits {

    public int getMax(String[] data) {
        int rows = data.length;
        int cols = data[0].length();

        int maxSide = 1;

        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                for (int side = 1; row + side < rows && col + side < cols; side++)
                    if (checkAvailable(data, row, col, side)) {
                        int availableSide = side + 1;
                        if (availableSide > maxSide) {
                            maxSide = availableSide;
                        }
                    }

        return maxSide * maxSide;
    }

    private boolean checkAvailable(String[] data, int row, int col, int sideLength) {
        char topLeft = data[row].charAt(col);
        char topRight = data[row].charAt(col + sideLength);
        char bottomLeft = data[row + sideLength].charAt(col);
        char bottomRight = data[row + sideLength].charAt(col + sideLength);

        return topLeft == topRight && topLeft == bottomLeft && topLeft == bottomRight;
    }

    public static void main(String[] args) {
        SquareOfDigits digits = new SquareOfDigits();

        String[] data1 = { "12", "34" };
        String[] data2 = { "1255", "3455" };
        String[] data3 = { "42101", "22100", "22101" };
        String[] data4 = { "1234567890" };
        String[] data5 = { "9785409507", "2055103694", "0861396761", "3073207669", "1233049493",
                "2300248968", "9769239548", "7984130001", "1670020095", "8894239889", "4053971072" };

        int result1 = digits.getMax(data1);
        int result2 = digits.getMax(data2);
        int result3 = digits.getMax(data3);
        int result4 = digits.getMax(data4);
        int result5 = digits.getMax(data5);

        String result = "result 1: " + result1 + ", \n"
                + "result 2: " + result2 + ", \n"
                + "result 3: " + result3 + ", \n"
                + "result 4: " + result4 + ", \n"
                + "result 5: " + result5 + ", \n";

        System.out.println(result);
    }
}
