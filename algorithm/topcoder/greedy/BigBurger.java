package topcoder.greedy;

/*
BigBurger 

  BigBurger Inc. wants to see if having a single person at the counter both to take orders and to serve them is feasible. At each BigBurger, customers will arrive and get in line. When they get to the head of the line they will place their order, which will be assembled and served to them. Then they will leave the BigBurger and the next person in line will be able to order.
  We need to know how long a customer may be forced to wait before he or she can place an order. Given a script that lists each customer for a typical day, we want to calculate the maximum customer waiting time. Each customer in the script is characterized by an arrival time (measured in minutes after the store opened) and a service duration (the number of minutes between ordering and getting the food).

  Create a class BigBurger that contains method maxWait that is given a int[] arrival and a int[] service describing all the customers and returns the maximum time spent by a customer between arriving and placing the order. Corresponding elements of arrival and service refer to the same customer, and they are given in the order in which they arrive at the store (arrival is in non-descending order).

  If multiple customers arrive at the same time they will all join the line at the same time, with the ones listed earlier ahead of ones appearing later in the list.
 */
public class BigBurger {

  public int maxWait(int[] arrival, int[] service) {
    // int maxWaitTime = Integer.MIN_VALUE;
    int maxWaitTime = 0;
    int serviceTime = 0;
    for (int i = 0; i < service.length; i++) {
      // arrival time before service time, need wait
      if (arrival[i] <= serviceTime) {
        int curWaitTime = serviceTime - arrival[i];
        maxWaitTime = Math.max(maxWaitTime, curWaitTime);
      } else {
        serviceTime = arrival[i];
      }
      serviceTime += service[i];
    }

    return maxWaitTime;
  }

  public static void main(String[] args) {
    BigBurger bigBurger = new BigBurger();
    int[] arrival = { 182 };
    int[] service = { 11 };
    System.out.println(bigBurger.maxWait(arrival, service));
  }

}
