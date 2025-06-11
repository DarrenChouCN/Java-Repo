package topcoder.divide;

/*
FairWorkload 

  Fabian is in charge of a law firm working on an important case. For a case coming up, he needs a specific folder which is stored in one of the filing cabinets arranged in a line against the wall of the records room. He has assigned a number of workers to find the folder from the filing cabinets. He doesn't want the workers to get in each other's way, nor does he want folders from different filing cabinets getting mixed up, so he has decided to partition the cabinets, and assign a specific section to each worker. Each worker will have at least 1 cabinet to search through.

  More specifically, Fabian wants to divide the line of filing cabinets into N sections (where N is the number of workers) so that every cabinet that the ith worker looks through is earlier in the line than every cabinet that the jth worker has to look through, for i < j.

  His initial thought was to make all the sections equal, giving each worker the same number of filing cabinets to look through, but then he realized that the filing cabinets differed in the number of folders they contained. He now has decided to partition the filing cabinets so as to minimize the maximum number of folders that a worker would have to look through. For example, suppose there were three workers and nine filing cabinets with the following number of folders:
  1. 10 20 30 40 50 60 70 80 90

  He would divide up the filing cabinets into the following sections:
  2. 10 20 30 40 50 | 60 70 | 80 90

  The worker assigned to the first section would have to look through 150 folders. The worker assigned to the second section would have to search through 130 folders, and the last worker would filter through 170 folders. In this partitioning, the maximum number of folders that a worker looks through is 170. No other partitioning has less than 170 folders in the largest partition.

  Write a class FairWorkload with a method getMostWork which takes a int[] folders (the number of folders for each filing cabinet) and an int workers (the number of workers). The method should return an int which is the maximum amount of folders that a worker would have to look through in an optimal partitioning of the filing cabinets. For the above example, the method would have returned 170.
 */
public class FairWorkload {

  public int getMostWork(int[] folders, int workers) {
    int low = 0, high = 0;
    for (int f : folders) {
      low = Math.max(low, f);
      high += f;
    }

    while (low < high) {
      int mid = (low + high) / 2;
      if (canSplit(folders, workers, mid)) {
        high = mid;
      } else {
        low = mid + 1;
      }
    }

    return low;
  }

  boolean canSplit(int[] folders, int workers, int maxWork) {
    int required = 1; // 至少需要一个工人（第一段）
    int current = 0; // 当前工人累积工作量
    for (int f : folders) {
      if (current + f > maxWork) {
        required++; // 超出限制，只能交给下一个工人
        current = f; // 新工人重新开始干活
      } else {
        current += f; // 当前工人继续接任务
      }
    }
    return required <= workers; // 看是否在限定人数内
  }

  public static void main(String[] args) {
    int[] folders1 = { 10, 20, 30, 40, 50, 60, 70, 80, 90 };
    int[] folders2 = { 568, 712, 412, 231, 241, 393, 865, 287, 128, 457, 238, 98, 980, 23, 782 };
    int[] folders3 = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1000 };
    int[] folders4 = { 50, 50, 50, 50, 50, 50, 50 };
    int[] folders5 = { 1, 1, 1, 1, 100 };

    FairWorkload lWorkload = new FairWorkload();
    System.out.println(lWorkload.getMostWork(folders1, 3));// 170
    System.out.println(lWorkload.getMostWork(folders1, 5));// 110
    System.out.println(lWorkload.getMostWork(folders2, 4));// 1785
    System.out.println(lWorkload.getMostWork(folders3, 2));// 1000
    System.out.println(lWorkload.getMostWork(folders4, 2));// 200
    System.out.println(lWorkload.getMostWork(folders5, 5));// 100
  }
}
