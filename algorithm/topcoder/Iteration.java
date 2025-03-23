package topcoder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Iteration {

  /*
   * AmoebaDivTwo
   * 
   * Little Romeo likes cosmic amoebas a lot. Recently he received one as a gift
   * from his mother. He decided to place his amoeba on a rectangular table. The
   * table is a grid of square 1x1 cells, and each cell is occupied by either
   * matter or antimatter. The amoeba is a rectangle of size 1xK. Romeo can place
   * it on the table in any orientation as long as every cell of the table is
   * either completely covered by part of the amoeba or completely uncovered, and
   * no part of the amoeba lies outside of the table. It is a well-known fact that
   * cosmic amoebas cannot lie on top of matter, so every cell of the table
   * covered by the amoeba must only contain antimatter.
   * 
   * 
   * You are given a String[] table, where the j-th character of the i-th element
   * is 'A' if the cell in row i, column j of the table contains antimatter or 'M'
   * if it contains matter. Return the number of different ways that Romeo can
   * place the cosmic amoeba on the table. Two ways are considered different if
   * and only if there is a table cell that is covered in one but not the other.
   */
  public int count(String[] table, int K) {
    return 0;
  }

  /*
   * InterestingParty
   * 
   * Mr. White is a very versatile person - absolutely everything is interesting
   * to him. Perhaps this is why he has many friends. Quite unfortunately,
   * however, none of his friends are versatile at all. Each of them is interested
   * only in two topics and refuses to talk about anything else. Therefore, each
   * time Mr. White organizes a party, it's a big problem for him to decide whom
   * to invite so that the party is interesting to everybody. Now that Mr. White
   * has a lot of experience in organizing parties, he knows for sure that a party
   * will be interesting if and only if there's a topic interesting to each of the
   * invited friends.
   * 
   * You will be given String[]s first and second. The i-th friend of Mr. White is
   * interested in topics first[i] and second[i]. Return the largest number of
   * friends that Mr. White can invite to his party so that the party will be
   * interesting.
   */
  public static int bestInvitation(String[] first, String[] second) {
    Map<String, Integer> topicCount = new HashMap<>();

    for (int i = 0; i < first.length; i++) {
      topicCount.put(first[i], topicCount.getOrDefault(first[i], 0) + 1);
      topicCount.put(second[i], topicCount.getOrDefault(second[i], 0) + 1);
    }

    int maxFriends = 0;
    for (String topic : topicCount.keySet()) {
      maxFriends = Math.max(maxFriends, topicCount.get(topic));
    }

    return maxFriends;
  }

  /*
   * AnagramFree
   * A string X is an anagram of string Y if X can be obtained by arranging all
   * characters of Y in some order, without removing any characters and without
   * adding new characters. For example, each of the strings "baba", "abab",
   * "aabb" and "abba" is an anagram of "aabb", and strings "aaab", "aab" and
   * "aabc" are not anagrams of "aabb".
   * 
   * A set of strings is anagram-free if it contains no pair of strings which are
   * anagrams of each other. Given a set of strings S, return the size of its
   * largest anagram-free subset. Note that the entire set is considered a subset
   * of itself.
   */
  public static int getMaximumSubset(String[] S) {
    Set<String> uniqueAnagrams = new HashSet<>();

    for (String s : S) {
      char[] charArray = s.toCharArray();
      Arrays.sort(charArray);
      String sortedArray = charArray.toString();
      uniqueAnagrams.add(sortedArray);
    }

    return uniqueAnagrams.size();
  }

  /*
   * Filtering
   * 
   * You recently got a job at a company that designs various kinds of filters,
   * and today, you've been given your first task. A client needs a filter that
   * accepts some objects and rejects some other objects based on their size. The
   * requirements are described in the int[] sizes and the String outcome. If
   * character i in outcome is 'A', then all objects of size sizes[i] must be
   * accepted, and if character i is 'R', then all objects of size sizes[i] must
   * be rejected. If an object's size does not appear in sizes, then it doesn't
   * matter if it is accepted or rejected.
   * 
   * Unfortunately, your knowledge of filters is very limited, and you can only
   * design filters of one specific kind called (A, B)-filters. Each such filter
   * is characterized by two integers A and B. It accepts an object if and only if
   * its size is between A and B, inclusive. You have excellent (A, B)-filter
   * construction skills, so you can construct any such filter where 1 <= A
   * <= B.
   * 
   * If it is possible to construct an (A, B)-filter that fulfills all the
   * requirements described in sizes and outcome, return a int[] containing the
   * filter's parameters, where element 0 is A and element 1 is B. If there are
   * several appropriate filters, choose the one that minimizes B - A. If there
   * are no suitable filters, return an empty int[].
   * 
   * 
   * Use a pointer to iterate through each element of sizes and outcome. If
   * outcome is valid, update the minimum and maximum values accordingly. Finally,
   * return an array containing the minimum and maximum values.
   */

  public static int[] designFilter(int[] sizes, String outcome) {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;

    int pos = 0;
    while (pos < sizes.length) {

      char flag = outcome.charAt(pos);
      if (flag == 'A') {
        min = Math.min(min, sizes[pos]);
        max = Math.max(max, sizes[pos]);
      }
      pos++;
    }

    return new int[] { min, max };
  }

  /*
   * AdditionGame
   * 
   * Fox Ciel is playing a game called Addition Game.
   * 
   * Three numbers A, B and C are written on a blackboard, and Ciel initially has
   * 0 points. She repeats the following operation exactly N times: She chooses
   * one of the three numbers on the blackboard. Let X be the chosen number. She
   * gains X points, and if X >= 1, the number X on the blackboard becomes X-1.
   * Otherwise, the number does not change.
   * 
   * Return the maximum number of points she can gain if she plays optimally.
   * 
   * **Use Java API PriorityQueue**
   */
  public int getMaximumPoints(int A, int B, int C, int N) {
    int points = 0;

    // Using PriorityQueue to create a max heap, notice that default PriorityQueue
    // is min Heap, so we need use Comparator
    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
    maxHeap.add(A);
    maxHeap.add(B);
    maxHeap.add(C);

    for (int i = 0; i < N; i++) {
      int max = maxHeap.poll();
      points += max;

      if (max > 0) {
        maxHeap.add(--max);
      } else {
        maxHeap.add(0);
      }
    }

    return points;
  }

  /*
   * SimpleGuess
   * 
   * A cat and a rabbit are playing a simple number guessing game. The cat chose
   * two different positive integers X and Y. He then told the rabbit several
   * numbers. One of those numbers was X + Y and another was X - Y. The others
   * were simply made up.
   * 
   * The rabbit thinks the cat prefers large numbers. Given a int[] hints
   * containing the numbers the cat told the rabbit, return the largest possible
   * value of X * Y.
   * 
   * **
   * 1. Nested Loop Traversal: Use nested loops to traverse all possible
   * combinations in the array.
   * 2. Condition Checking: Add condition checks inside the loops to ensure the
   * combinations meet the problem's requirements.
   * 3. Result Calculation: Calcultate the result based on the problem's
   * requirements and update the optimal solution during traversal.
   * **
   */
  public int getMaximum(int[] hints) {
    int maxProduct = -1;

    for (int i = 0; i < hints.length; i++) {
      for (int j = 0; j < hints.length; j++) {
        if (i != j) {
          int sum = hints[i];
          int diff = hints[j];

          if (sum > diff && (sum + diff) % 2 == 0 && (sum - diff) % 2 == 0) {
            int X = (sum + diff) / 2;
            int Y = (sum - diff) / 2;

            if (X > 0 && Y > 0 && X != Y) {
              maxProduct = Math.max(maxProduct, X * Y);
            }
          }
        }
      }
    }

    return maxProduct;
  }

  /*
   * FoxProgression
   * 
   * Fox Ciel likes sequences of integers. She especially likes sequences that are
   * either an arithmetic progression of integers or a geometric progression of
   * integers with an integer common ratio. She calls these beautiful sequences.
   * An arithmetic progression is a sequence of numbers such that the difference
   * of any two consecutive numbers of the sequence is a constant. A geometric
   * progression is a sequence of numbers where each number after the first is
   * found by multiplying the previous one by a constant non-zero number which is
   * called the common ratio.
   * 
   * Ciel has a sequence of integers. She says that an integer is good if she can
   * obtain a beautiful sequence by appending the integer to the end of the
   * sequence. You are given a int[] seq. Calculate the number of good integers
   * for the given sequence. If there are infinitely many good integers, return
   * -1.
   */
  public int theCount(int[] seq) {
    int n = seq.length;
    if (n < 2)
      return -1;

    boolean isArithmetic = true;
    boolean isGeometric = true;

    int diff = seq[1] - seq[0];
    boolean hasZero = false;
    double ratio = 0;
    if (seq[0] != 0) {
      ratio = seq[1] / seq[0];
    } else {
      hasZero = true;
    }

    for (int i = 1; i < n; i++) {
      if (seq[i] - seq[i - 1] != diff) {
        isArithmetic = false;
      }
      if (seq[i - 1] == 0) {
        hasZero = true;
      } else if (seq[i] / seq[i - 1] != ratio) {
        isGeometric = false;
      }
    }

    if (isArithmetic && isGeometric)
      if (hasZero)
        return -1;
      else
        return 1;

    if (isArithmetic)
      return 1;

    if (isGeometric)
      if (seq[n - 1] == 0)
        return -1;
      else
        return 1;
    return 0;
  }

  /*
   * TheProgrammingContestDivTwo
   * 
   * Farmer John and Fox Brus are participating in a programming contest as a
   * team.
   * 
   * The duration of the contest is T minutes and they are given N tasks.
   * Solutions can be submitted at any time during the contest, including exactly
   * T minutes after the start of the contest. It takes them requiredTime[i]
   * minutes to solve the i-th task.
   * 
   * The score in this contest is represented by two numbers, solved and penalty.
   * Initially both numbers are 0. If they solve a task t minutes after the start
   * of the contest, solved increases by 1 and penalty increases by t. Two scores
   * are compared by solved first. If two scores have different solved, the score
   * with bigger solved is better. If two scores have the same solved, the score
   * with smaller penalty is better.
   * 
   * Return a int[] containing exactly two integers that describes the best score
   * they can get. The first integer of the return value must represent solved and
   * the second integer must represent penalty.
   */
  public int[] find2(int T, int[] requiredTime) {
    Arrays.sort(requiredTime);
    int solvedCount = 0;
    int penalty = 0;

    int currentTime = 0;

    for (int time : requiredTime) {
      if (currentTime + time <= T) {
        currentTime += time;
        solvedCount++;
        penalty += currentTime;
      } else {
        break;
      }
    }

    return new int[] { solvedCount, penalty };
  }

  /*
   * ToastXRaspberry
   * 
   * You have some raspberry jam and a slice of plain toast. You're going to
   * create a slice of breathtaking raspberry toast by applying some of the jam to
   * the toast. Each application will apply L layers of raspberry jam, where L is
   * a positive integer (chosen before the application) and L is not greater than
   * upper_limit. A slice of breathtaking raspberry toast is a slice of plain
   * toast that has had exactly layer_count layers of raspberry jam applied.
   * Return the minimum number of applications required to turn a slice of plain
   * toast into a slice of breathtaking raspberry toast.
   * 
   * **
   * upper_limit：The maximum number of layers of raspberry jam that can be applied
   * in one application.
   * layer_count：The number of layers of raspberry jam required to achieve a
   * breathtaking raspberry toast.
   * **
   */
  public int apply(int upper_limit, int layer_count) {

    int count = layer_count / upper_limit;

    if (layer_count % upper_limit != 0) {
      count++;
    }

    return count;
  }

  /*
   * SlimeXSlimeRancher2
   * 
   * You are playing a game titled Slime Rancher 2. You will be training slimes in
   * this game.
   * 
   * You have a slime-in-training. Associated with the slime are N attributes,
   * numbered 0 through N-1, each represented by a positive integer. You are given
   * int[] attributes containing N integers : the i-th integer is the initial
   * value of the i-th attribute for the slime. After the training is complete,
   * each of the slime's attributes will either stay the same or increase to some
   * positive integer less than or equal to 999. None of the attributes will
   * decrease in value. The weight of the training is defined as the sum of the
   * differences between the final and initial values of all the attributes for
   * the slime.
   * 
   * You are a master slime breeder, and you're able to obtain any possible final
   * values for a slime's attributes. This time, you would like to create a
   * well-balanced slime. A slime is well-balanced if all of its attributes have
   * equal values. What is the minimum possible weight of the training?
   * 
   * **
   * 1. All attributes have equal values.
   * 2. The final value of each attribute is a positive integer less than or equal
   * to 999.
   * 3. The weight of the training is the sum of the differences between the final
   * and initial values of all the attributes for the slime.
   * **
   */
  public int train(int[] attributes) {
    int maxAttribute = 0;
    for (int attr : attributes) {
      maxAttribute = Math.max(attr, maxAttribute);
    }

    int weight = 0;
    for (int attr : attributes) {
      weight += maxAttribute - attr;
    }

    return weight;
  }

  /*
   * CandyShop
   * 
   * Manao has been to a tremendous candy shop several times. He has forgotten its
   * exact placement, but remembers some information about each time he visited
   * the shop.
   * 
   * The city Manao lives in can be represented as an infinite two-dimensional
   * plane with a Cartesian coordinate system. From point (x, y), Manao can move
   * to any of the points (x - 1, y), (x, y - 1), (x + 1, y), or (x, y + 1). In
   * order to perform each of these 4 moves, he needs to walk 1 unit of length.
   * Manao recalls that the candy shop was located at a point with integer
   * coordinates. Also, he remembers that the i-th time he visited the candy shop,
   * he went there from point (X[i], Y[i]) and walked at most R[i] units of
   * length.
   * 
   * Since Manao's visits to the shop span a long period of time, he may have
   * misremembered some details. If no intersection complies with his
   * reminiscence, return 0. Otherwise return the number of different
   * intersections where Manao's candy shop could be, assuming Manao remembers
   * everything correctly.
   * 
   * ** Enumerate and calculate the possible range (diamond-shaped region) for
   * each
   * i, then compute the intersection of these ranges. Be sure to verify the
   * intersection points using the Manhattan distance formula. **
   */
  public static int countProbablePlaces(int[] X, int[] Y, int[] R) {
    if (X.length != Y.length || X.length != R.length) {
      return 0;
    }

    int globalLeft = Integer.MIN_VALUE;
    int globalTop = Integer.MAX_VALUE;
    int globalRight = Integer.MAX_VALUE;
    int globalBottom = Integer.MIN_VALUE;

    for (int i = 0; i < X.length; i++) {
      int iLeft = X[i] - R[i];
      int iTop = Y[i] + R[i];
      int iRight = X[i] + R[i];
      int iBottom = Y[i] - R[i];

      globalLeft = Math.max(globalLeft, iLeft);
      globalTop = Math.min(globalTop, iTop);
      globalRight = Math.min(globalRight, iRight);
      globalBottom = Math.max(globalBottom, iBottom);
    }

    if (globalLeft > globalRight || globalBottom > globalTop) {
      return 0;
    }

    int count = 0;
    for (int x = globalLeft; x <= globalRight; x++) {
      for (int y = globalBottom; y <= globalTop; y++) {
        boolean valid = true;
        for (int i = 0; i < X.length; i++) {
          if (Math.abs(x - X[i]) + Math.abs(y - Y[i]) > R[i]) {
            valid = false;
            break;
          }
        }
        if (valid)
          count++;
      }
    }
    return count;
  }

  /*
   * TheAlmostLuckyNumbersDivTwo
   * 
   * John and Brus believe that the digits 4 and 7 are lucky and all others are
   * not. According to them, an almost lucky number is a number that contains at
   * most one non-lucky digit in its decimal representation. Return the total
   * number of almost lucky numbers between a and b, inclusive.
   * 
   * **A number can have either 0 or 1 non-lucky digit.**
   */
  public int find1(int a, int b) {
    int count = 0;
    for (int i = a; i <= b; i++) {
      char[] curNumberArr = String.valueOf(i).toCharArray();
      if (isLuckNumber(curNumberArr)) {
        count++;
      }
    }
    return count;
  }

  private boolean isLuckNumber(char[] curNumberArr) {
    int nonLuckCount = 0;
    for (char c : curNumberArr) {
      if (c != '4' && c != '7') {
        nonLuckCount++;
        if (nonLuckCount > 1) {
          return false;
        }
      }
    }
    return true;
  }

  /*
   * Palindromization Div 2
   * 
   * Little Arthur loves numbers, especially palindromic ones. A palindromic
   * string is a string that reads the same both forwards and backwards. A
   * palindromic number is a non-negative integer such that its decimal
   * representation (without insignificant leading zeros) is a palindromic string.
   * For example, 12321, 101, 9, and 0 are palindromic numbers but 2011, 509, and
   * 40 are not.
   * 
   * Arthur has a number X and he would like to palindromize it. Palindromization
   * of a number means adding or subtracting some value to obtain a palindromic
   * number. For example, one possible way to palindromize number 25 is adding 8
   * resulting in number 33, which is palindromic.
   * 
   * Unfortunately Arthur cannot palindromize numbers for free. The cost of
   * palindromization in dollars is equal to the value added or subtracted. In the
   * previous example Arthur would have to pay 8 dollars.
   * 
   * Of course Arthur would like to palindromize X spending the least amount of
   * money. Given X return the minimum amount of money Arthur needs.
   */
  public int getMinimumCost(int X) {
    int addingX = X;
    int subtractingX = X;

    while (addingX <= 100000) {
      if (!isPalindrome(addingX))
        addingX++;
      else
        break;

    }
    while (subtractingX >= 0) {
      if (!isPalindrome(subtractingX))
        subtractingX--;
      else
        break;
    }

    return Math.min(addingX - X, X - subtractingX);
  }

  private boolean isPalindrome(int x) {
    String xStr = String.valueOf(x);
    char[] xCharArr = xStr.toCharArray();
    int startPos = 0;
    int endPos = xCharArr.length - 1;

    while (startPos <= endPos) {
      if (xCharArr[startPos++] != xCharArr[endPos--]) {
        return false;
      }
    }

    return true;
  }

}