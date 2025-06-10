package topcoder.hard;

/*
FillBox 

  You have a length x width x height box, and you want to fill it with cubes. The cubes have sides that are powers of 2 (1x1x1, 2x2x2, 4x4x4, 8x8x8, etc.). You are given a int[] cubes, the i-th element of which is the number of 2^i x 2^i x 2^i cubes you have (i is a 0-based index). Return the minimum number of cubes necessary to fill the box, or -1 if it is impossible to do so.

  你有一个长 × 宽 × 高的盒子，需要用立方体来填满它。这些立方体的边长是2的幂次方（如1×1×1、2×2×2、4×4×4、8×8×8等）。给定一个整型数组 cubes，其中第i个元素表示你拥有的边长为2^i的立方体的数量（i从0开始索引）。返回填满盒子所需的最少立方体数量，如果无法完成则返回-1。
 */
public class FillBox {
  public int minCubes(int length, int width, int height, int[] cubes) {
    int kinds = cubes.length;
    int[] used = new int[kinds];
    long totalUsed = 0;

    for (int i = kinds - 1; i >= 0; i--) {
      // 从大到小遍历边长
      int cubeSize = (int) Math.pow(2, i);

      // 不是算体积，而是算当前维度最多放多少个
      long fitLength = length / cubeSize;
      long fitWidth = width / cubeSize;
      long fitHeight = height / cubeSize;
      long totalPositions = fitLength * fitWidth * fitHeight;

      // 减去之前更大立方体占用的数量
      for (int j = i + 1; j < kinds; j++) {
        int largerSize = (int) Math.pow(2, j);
        int ratio = (largerSize / cubeSize); // 每边缩放倍数
        long occupied = (long) used[j] * ratio * ratio * ratio;
        totalPositions -= occupied;
      }

      if (totalPositions <= 0)
        continue;

      long take = Math.min(totalPositions, cubes[i]);
      used[i] = (int) take;
      totalUsed += take;

      if (take < totalPositions)
        return -1;
    }

    return (int) totalUsed;
  }

  public static void main(String[] args) {
    FillBox box = new FillBox();
    int[] cubes1 = { 10, 10, 10 };
    System.out.println(box.minCubes(4, 4, 8, cubes1));

    int[] cubes2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };
    System.out.println(box.minCubes(524288, 524288, 524288, cubes2));

    int[] cubes3 = { 146071, 777733, 599818, 266038, 12509, 478351, 201640, 618984, 143988, 87783, 837107, 349651,
        99683, 484992, 553337, 438088, 68198, 972282, 890781, 586124 };
    System.out.println(box.minCubes(163840, 698368, 179200, cubes3));
  }
}
