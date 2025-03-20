package block1.PartySeats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
PartySeats

Problem Statement

	It is time to arrange the seating around a circular table for a party. We want to alternate boys and girls around the table. We have a list of all the attendees and their genders. Each attendee is specified by a String that consists of the name, followed by one space, followed by either "boy" or "girl".

  In addition to the attendees, we need to seat the HOST (a boy) and the HOSTESS (a girl) with the HOSTESS directly across from the HOST. That means that half the attendees should be on the HOST's left, and half on his right.

  Create a class PartySeats that contains a method seating that is given a String[] attendees that lists all the attendees and their genders. The method returns a String[] that gives the seating plan, starting with "HOST" and proceeding clockwise around the table, including all the attendees and the HOSTESS.

  If there is more than one possible seating plan, return the one that comes first lexicographically. "First lexicographically" means that each successive element in the return should be chosen to be the earliest alphabetically that is consistent with a legal seating plan. If there is no legal seating plan, the return should contain 0 elements.

Analysis

  According to the problem, the output is expected to have boys and girls alternating and sitting clockwise around the table, with the first position and the middle position fixed as HOST and HOSTESS respectively. So we can derive the core requirements of this question:

    1. The number of boys and girls must be equal; otherwise, the requirement of "half boys and half girls" cannot be met.

    2. Because the HOST is fixed as a boy and the seating must alternate between boys and girls, the odd positions must be for girls and the even positions for boys, as shown in the diagram below:

              HOST
          girl    girl
        boy        boy
      girl           girl
          boy      boy
            HOSTESS

    3. Both boys and girls come in pairs, so the total number of boys and girls must be even.

Thought Process

  1. Firstly, we should iterate through the original array and parse it, classifying the names of boys and girls into separate arrays.

  2. Determine if there are an equal number of boys and girls, and if the number is even.

  3. Determine the size of the result array, which should be the sum of the given array size and the two hosts, i.e., attendees.length + 2.

  4. Use the Java API Collections.sort to sort the arrays of names according to alphabetical (lexicographical) order.

  5. Fill the result array: 

    a. The starting position (i = 0) and the middle position (i = result.length/2) are fixed with HOST and HOSTESS. 

    b. Even positions (i % 2 == 0) are filled with boys' names, and odd positions (i % 2 != 0) are filled with girls' names.

Complexity

  Time Complexity: The parsing and filling of the array are both O(n), and the sorting step is O(n log n), so the final time complexity is O(n log n).

  Space Complexity: Two temporary arrays are used to store the names, so the space complexity is O(n).
  
 */
public class PartySeats {

  private static final String BOY = "boy";
  private static final String GIRL = "girl";
  private static final String HOST = "HOST";
  private static final String HOSTESS = "HOSTESS";

  public String[] seating(String[] attendees) {

    String[] result = {};

    if (attendees == null || attendees.length == 0) {
      return result;
    }

    List<String> boyAttendees = new ArrayList<>();
    List<String> girlAttendees = new ArrayList<>();

    for (String attendee : attendees) {
      String[] attendeeDetail = attendee.split(" ");
      if (attendeeDetail.length != 2) {
        continue;
      }

      if (attendeeDetail[1].equals(BOY)) {
        boyAttendees.add(attendeeDetail[0]);
      }

      if (attendeeDetail[1].equals(GIRL)) {
        girlAttendees.add(attendeeDetail[0]);
      }
    }

    if (boyAttendees.size() != girlAttendees.size() || boyAttendees.size() % 2 != 0) {
      return result;
    }

    int resultArraySize = attendees.length + 2;
    result = new String[resultArraySize];

    Collections.sort(boyAttendees);
    Collections.sort(girlAttendees);

    int boyAttendeePos = 0;
    int girlAttendeePos = 0;
    for (int i = 0; i < resultArraySize; i++) {
      if (i == 0) {
        result[i] = HOST;
      } else if (i == resultArraySize / 2) {
        result[i] = HOSTESS;
      } else if (i % 2 == 0) {
        result[i] = boyAttendees.get(boyAttendeePos++);
      } else {
        result[i] = girlAttendees.get(girlAttendeePos++);
      }
    }

    return result;
  }

  public static void main(String[] args) {

    String[] test0 = { "BOB boy", "SAM girl", "DAVE boy", "JO girl" };
    String[] test1 = { "JOHN boy" };
    String[] test2 = { "JOHN boy", "CARLA girl" };
    String[] test3 = { "BOB boy", "SUZIE girl", "DAVE boy", "JO girl", "AL boy", "BOB boy", "CARLA girl",
        "DEBBIE girl" };

    PartySeats seats = new PartySeats();
    System.out.println(Arrays.toString(seats.seating(test0)));
    System.out.println(Arrays.toString(seats.seating(test1)));
    System.out.println(Arrays.toString(seats.seating(test2)));
    System.out.println(Arrays.toString(seats.seating(test3)));
  }

}
