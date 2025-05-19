package models;

import java.util.Arrays;
import java.util.PriorityQueue;

public class Greedy {

  /*
   * 1.1 区间调度 / 活动选择问题
   * 从多个区间中选择最多个互不重叠的区间
   * 会议安排:给出一堆会议的起止时间，问最多能安排几场不冲突的会议
   * 
   * 最大兼容工作集: 多个工作安排（时间段），选最多个不冲突的工作。
   * 最多电影场次: 一天中有很多电影，选尽可能多场不重叠的电影来看。
   * 非重叠广告投放时段: 给出多个投放区间，问最多能安排多少条广告不冲突。
   * 最大能量吸收（比如电池充电、太阳能板对接）
   * 最大非冲突子集 – 通用模型
   */
  int maxNonOverlapping(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> Integer.compare(a[1], b[1]));

    int count = 0;
    int lastEnd = Integer.MIN_VALUE;

    for (int[] iv : intervals) {
      // 如果当前区间的开始时间 iv[0] ≥ 上一个选中区间的结束时间 lastEnd
      if (iv[0] >= lastEnd) {
        count++;
        lastEnd = iv[1];
      }
    }
    return count;
  }

  /*
   * 1.2 硬币 / 纸币找零问题（标准货币系统）
   * 给定金额所需的最少硬币或纸币数（假设为标准面额体系，如澳币）
   * 应用场景：自动贩卖机找零、收银系统等
   * 注意：若面额不标准，需用动态规划，考试常考陷阱！
   * 
   * 自动贩卖机找零：顾客付款后，用最少枚数找零。
   * 超市收银系统：用最少纸币凑出找零金额。
   * 停车场缴费系统：找出应退金额的最少币种组合。
   * 银行 ATM 分币：机器分发纸币，用最少张数满足取款。
   * 货币兑换最小张数：兑换目标金额时使用最少面额张数。
   * 硬币组合检测题：判断是否可用贪心解决最少枚数组合问题。
   */
  int minCoins(int amount, int[] denom) {
    Arrays.sort(denom); // ascending order
    int count = 0;
    for (int i = denom.length - 1; i >= 0 && amount > 0; i--) {
      if (denom[i] <= amount) {
        int use = amount / denom[i];
        count += use;
        amount -= use * denom[i];
      }
    }
    return amount == 0 ? count : -1; // -1 if impossible
  }

  /*
   * 1.3 跳跃游戏 / 最远可达位置（一维贪心）
   * 判断是否能从起点跳到最后一个位置（如 LeetCode Jump Game）
   * 应用场景：数组跳跃可达性、资源跳跃、电池续航等
   * 
   * 一维跳跃路径验证：是否能跳到终点。
   * 电动车电量跳跃：电量每段递减，能否撑到终点。
   * 通信信号中继跳点验证：信号跳跃链是否能覆盖目标。
   * 游戏人物跳跃平台：是否能跳到终点平台。
   * 连锁资源访问路径可达性。
   * 单次遍历判断 reachability 的任何问题。
   */
  boolean canReachEnd(int[] nums) {
    int maxReach = 0;
    for (int i = 0; i < nums.length; i++) {
      // 只有当前跳跃的能力 maxReach 覆盖到了遍历点 i，才有资格继续跳跃。
      if (i > maxReach)
        return false;
      maxReach = Math.max(maxReach, i + nums[i]);
    }
    return true;
  }

  /*
   * 1.4 最少跳跃次数（Jump Game II）
   * 返回跳到最后一个位置所需的最小跳跃数
   * 
   * 游戏跳跃最短路径（如 Jump Game II）
   * 电池补给点最少中转次数
   * 无需记录路径，只问最小跳跃数
   * 每步能量耗尽前跳最远
   * 可等价转化为 BFS 最短步数模型的一维贪心写法
   */
  int minJumps(int[] nums) {
    int jumps = 0, curEnd = 0, curFarthest = 0;
    for (int i = 0; i < nums.length - 1; i++) {
      curFarthest = Math.max(curFarthest, i + nums[i]);
      if (i == curEnd) {
        jumps++;
        curEnd = curFarthest;
      }
    }
    return jumps;
  }

  /*
   * 1.5 加油站问题 / 环形路径补给
   * 找出能完成环路的起始加油站编号，若无法完成返回 -1
   * 应用场景：环形资源补给，Go8 考试经典题型之一
   * 
   * 环形电量补给：电动车沿环形路线行驶，各站点电量供给与耗电。
   * 网络环形节点续电：通信节点是否可维持环形联通链。
   * 游戏中资源补充环路：是否存在一个起点，能补给整圈资源。
   * 地图巡逻路径判断：是否能从某个点出发巡逻完整圈。
   * 任务点体力消耗与补给：判断一圈体力是否能支撑。
   * 所有环形路径 + 差值累加判断 + 唯一解贪心问题。
   */
  int canCompleteCircuit(int[] gas, int[] cost) {
    int total = 0, tank = 0, start = 0;
    for (int i = 0; i < gas.length; i++) {
      int diff = gas[i] - cost[i];
      total += diff;
      tank += diff;
      if (tank < 0) {
        start = i + 1;
        tank = 0;
      }
    }
    return total >= 0 ? start : -1;
  }

  /*
   * 1.6 Huffman 风格的最小合并代价（优先队列）
   * 最小代价合并文件 / 绳索 / 频率（即 Huffman 编码模式）
   * 应用场景：最优合并、压缩、合并绳索类问题
   * 
   * 文件合并压缩：多个小文件合并成一个，总耗时最少。
   * 绳子拼接成本：不同长度的绳子拼成一根，成本按长度计算。
   * 哈夫曼编码权重：构建最优前缀树时的编码总代价最小。
   * 任意“合并两个元素会产生代价，求总代价最小”的问题。
   * 任务合并最省时、区块压缩最省空间等贪心型累积代价场景。
   */
  int minMergeCost(int[] sizes) {
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int s : sizes)
      pq.offer(s);

    int cost = 0;
    while (pq.size() > 1) {
      int a = pq.poll();
      int b = pq.poll();
      int merged = a + b;
      cost += merged; // accumulate cost
      pq.offer(merged);
    }
    return cost;
  }
}
