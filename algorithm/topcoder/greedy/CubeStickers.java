package topcoder.greedy;

import java.util.HashMap;
import java.util.Map;

/*
CubeStickers

  Fox Ciel has a cube and some colored stickers. You are given a String[] sticker, where the i-th element is the color of the i-th sticker. Ciel wants to choose six of these stickers and apply one on each of the cube's faces. Each pair of adjacent faces must have different colors. Two faces are considered adjacent if they share an edge.

  If this is possible, return "YES", otherwise, return "NO" (all quotes for clarity).
 */
public class CubeStickers {

  public String isPossible(String[] sticker) {
    Map<String, Integer> freq = new HashMap<>();
    for (String s : sticker) {
      freq.put(s, freq.getOrDefault(s, 0) + 1);
    }

    int usable = 0;
    for (int cnt : freq.values()) {
      usable += Math.min(cnt, 2);
      if (usable >= 6) {
        return "YES";
      }
    }
    return "NO";
  }

  public static void main(String[] args) {
    CubeStickers stickers = new CubeStickers();
    String[] sticker1 = { "red", "yellow", "blue", "red", "yellow", "blue", "red", "yellow", "blue" };
    System.out.println(stickers.isPossible(sticker1));

  }

}
