package topcoder.graph.bfs;

import java.util.LinkedList;
import java.util.Queue;

/*
  EmoticonsDiv2
  
  You are very happy because you advanced to the next round of a very important
  programming contest. You want your best friend to know how happy you are.
  Therefore, you are going to send him a lot of smile emoticons. You are given
  an int smiles: the exact number of emoticons you want to send.
  
  You have already typed one emoticon into the chat. Then, you realized that
  typing is slow. Instead, you will produce the remaining emoticons using copy
  and paste.
  
  You can only do two different operations:
  Copy all the emoticons you currently have into the clipboard.
  Paste all emoticons from the clipboard.
  Each operation takes precisely one second. Copying replaces the old content of the clipboard. Pasting does not empty the clipboard. Note that you are not allowed to copy just a part of the emoticons you already have.
  
  Return the smallest number of seconds in which you can turn the one initial emoticon into smiles emoticons.
 */
public class EmoticonsDiv2 {

  public int printSmiles(int smiles) {
    Queue<int[]> queue = new LinkedList<>();
    boolean[][] visted = new boolean[smiles + 1][smiles + 1];

    // emoticons, clips, time
    queue.offer(new int[] { 1, 0, 0 });
    visted[1][0] = true;

    while (!queue.isEmpty()) {
      int[] state = queue.poll();
      int cur = state[0];
      int clip = state[1];
      int time = state[2];

      if (cur == smiles)
        return time;

      // copy
      if (!visted[cur][cur]) {
        visted[cur][cur] = true;
        queue.offer(new int[] { cur, cur, time + 1 });
      }

      // paste
      if (clip > 0 && cur + clip <= smiles && !visted[cur + clip][clip]) {
        visted[cur + clip][clip] = true;
        queue.offer(new int[] { cur + clip, clip, time + 1 });
      }
    }

    return -1;
  }
}
