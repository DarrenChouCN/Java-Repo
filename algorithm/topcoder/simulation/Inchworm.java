package topcoder.simulation;

/*
Inchworm

Problem Statement
    	
    The inchworm is a creature of regular habits. She inches forward some distance along the branch of a tree, then stops to rest. If she has stopped at a leaf, she makes a meal of it. Then she inches forward the same distance as before, and repeats this routine until she has reached or passed the end of the branch.

    Consider an inchworm traveling the length of a branch whose leaves are spaced at uniform intervals. Depending on the distance between her resting points, the inchworm may or may not be able to eat all of the leaves. There is always a leaf at the beginning of the branch, which is where the inchworm rests before setting out on her journey.

    You are given three int values that specify, in inches: the length of the branch; the distance traveled by the inchworm between rests; and the distance between each consecutive pair of leaves. Given that the inchworm only eats at rest, calculate the number of leaves she will consume.

Analysis

    1. The length of the branch is 'branch', the worm crawls a distance of 'rest' every time, and there is a leaf at every 'leaf' distance on the branch.

    2. A leaf is at the beginning position of the branch, and the worm will have a rest at the beginning, so the worm will eat the first leaf.

    3. If the position where the worm crawls is the same as the position of a leaf, then the worm will eat the leaf, and the result increases by one.

    4. The worm continues crawling the same distance and calculates the number of leaves eaten during the crawling process. When it reaches or exceeds the branch's endpoint, the process finishes.

Thought process: use the two-pointer approach

    1. Setting two pointers, one pointer (wormPosition) points to the position reached by the worm after each crawl, another pointer (leafPosition) points to the position of the next leaf closest to where the worm crawled, both of them are initialized to 0.

    2. Setting the loop condition, the values of wormPosition and leafPosition must be less than 'branch'.

    3. Loop:
        a. If wormPosition equals leafPosition, then the number of leaves eaten by the worm increases by 1, and both pointers move to the next appropriate position.
        b. If wormPosition does not equal leafPosition, then compare the two positions, and the smaller one needs to move forward.

    4. Loop over, return the value of eaten leaves.

Complexity

    Time complexity: the maximum number of loops is 'branch', so the time complexity is O(n).

    Space complexity: only constant pointers are used, so the space complexity is O(1).

 */
public class Inchworm {

    public int lunchtime(int branch, int rest, int leaf) {
        if (branch < 1 || branch > 1000000 || rest < 1 || rest > 1000 || leaf < 1 || leaf > 1000) {
            return 0;
        }

        int leaves = 0;

        int wormPosition = 0;
        int leafPosition = 0;

        while (wormPosition <= branch && leafPosition <= branch) {
            if (wormPosition == leafPosition) {
                leaves++;
                wormPosition += rest;
                leafPosition += leaf;
            } else if (wormPosition < leafPosition) {
                wormPosition += rest;
            } else {
                leafPosition += leaf;
            }
        }

        return leaves;
    }

    public static void main(String[] args) {
        Inchworm inchworm = new Inchworm();

        int lunchtime0 = inchworm.lunchtime(11, 2, 4);
        int lunchtime1 = inchworm.lunchtime(12, 6, 4);
        int lunchtime2 = inchworm.lunchtime(20, 3, 7);
        int lunchtime3 = inchworm.lunchtime(21, 7, 3);
        int lunchtime4 = inchworm.lunchtime(15, 16, 5);
        int lunchtime5 = inchworm.lunchtime(1000, 3, 7);
        int lunchtime6 = inchworm.lunchtime(1000, 7, 3);

        String lunchtime = "lunchtime(11, 2, 4): " + lunchtime0 + "\n" +
                "lunchtime(12, 6, 4): " + lunchtime1 + "\n" +
                "lunchtime(20, 3, 7): " + lunchtime2 + "\n" +
                "lunchtime(20, 7, 3): " + lunchtime3 + "\n" +
                "lunchtime(15, 16, 5): " + lunchtime4 + "\n" +
                "lunchtime(1000, 3, 7): " + lunchtime5 + "\n" +
                "lunchtime(1000, 7, 3): " + lunchtime6;
        System.out.println(lunchtime);
    }
}
